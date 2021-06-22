#!/bin/sh

mountpoint="NONE"

cleanup() {
	if [ "${mountpoint}" != "NONE" ]; then
		umount "${mountpoint}"
		rmdir "${mountpoint}"
	fi
	mountpoint="NONE"
}

die() {
	>&2 echo "${1}"
	cleanup
	exit 1
}

print_usage() {
    echo "Usage: ${1} COMMAND ARGS..."
    echo
    echo "Commands:"
    echo " update IMAGE   Write IMAGE to not-used rootfs partition"
   	echo " state          Return current state"
	echo "                   NORMAL:    Normal boot"
	echo "                   SWAP_INIT: Swap will be initiated on next boot"
	echo "                   ROLLBACK:  Rollback has occured"
	echo "                   FAILED:    Swap failed. State should not be possible in userspace"
	echo "                   SWAPPING:  Swap in progress"
   	echo " commit         Commit change from state \"SWAPPING\" to \"NORMAL\""
	echo " cancel		  Cancel \"SWAP_INIT\""
    echo
    exit 0
}

# Parse arguments
cmd_update=""
cmd_state=""
cmd_commit=""
cmd_cancel=""
image=""

if [ "${#}" -lt "1" ]; then
	print_usage "$(basename ${0})"
fi

case "${1}" in 
	update)
		if [ "${#}" -ne "2" ]; then
			print_usage "$(basename ${0})"
		fi
		cmd_update="true"
		image="${2}"
		;;
	state)
		if [ "${#}" -ne "1" ]; then
			print_usage "$(basename ${0})"
		fi
		cmd_state="true"
		;;
	commit)
		if [ "${#}" -ne "1" ]; then
			print_usage "$(basename ${0})"
		fi
		cmd_commit="true"
		;;
	cancel)
		if [ "${#}" -ne "1" ]; then
			print_usage "$(basename ${0})"
		fi
		cmd_cancel="true"
		;;
	*)
		print_usage "$(basename ${0})"
		;;
esac

state="UNKNOWN"
part="$(nvram --sys get SYS_BOOT_PART)" || die "Failed reading nvram variable SYS_BOOT_PART"
swap="$(nvram --sys get SYS_BOOT_SWAP)" || die "Failed reading nvram variable SYS_BOOT_SWAP"
if [ "$part" = "$swap" ]; then
	attempts="$(nvram --sys get SYS_BOOT_ATTEMPTS)"
	if [ $? -ne 0 ]; then
		state="NORMAL"
	else
		state="ROLLBACK"
	fi
else
	attempts="$(nvram --sys get SYS_BOOT_ATTEMPTS)"
	if [ $? -ne 0 ]; then
		state="SWAP_INIT"
	elif [ "$attempts" -ge 3 ]; then
		state="FAILED"
	elif [ "$attempts" -lt 3 ]; then
		state="SWAPPING"
	fi
fi

if [ "$state" = "UNKNOWN" ]; then
	die "Could not determine state"
fi

# COMMAND state
if [ "$cmd_state" = "true" ]; then
	echo "$state"
	exit 0
fi

# COMMAND commit
if [ "$cmd_commit" = "true" ]; then
	if [ "$state" = "SWAPPING" -o "$state" = "ROLLBACK" ]; then
		NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_PART "$swap" || die "Failed setting nvram variable SYS_BOOT_PART"
		NVRAM_SYSTEM_UNLOCK=16440 nvram --sys delete SYS_BOOT_ATTEMPTS || die "Failed deleting nvram variable SYS_BOOT_ATTEMPTS"
		exit 0
	else
		die "Invalid state \""$state"\" for commit"
	fi
fi

# COMMAND cancel
if [ "$cmd_cancel" = "true" ]; then
	if [ "$state" = "SWAP_INIT" ]; then
		NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_SWAP "$part" || die "Failed setting nvram variable SYS_BOOT_SWAP"
		exit 0
	else
		die "Invalid state \""$state"\" for cancel"
	fi
fi

# COMMAND update
if [ "$cmd_update" = "true" ]; then
	if [ "$state" != "NORMAL" ]; then
		die "Invalid state \""$state"\" for update"
	fi
	current_root_label="$(findmnt -no PARTLABEL /)" || die "Failed finding current root partition label"
	case "${current_root_label}" in
		rootfs1)
			new_root_label="rootfs2"
			;;
		rootfs2)
			new_root_label="rootfs1"
			;;
		*)
			die "Unknown root partition label: ${current_root}"
			;;
	esac
	echo "Current root: ${current_root_label}"
	echo "New root: ${new_root_label}"
	
	new_root_partition="$(findfs PARTLABEL=${new_root_label})" || die "Failed getting root partition device"
	new_root_fstype="ext4"
	echo "Target partition: ${new_root_partition}: type: ${new_root_fstype}"
	
	case "${new_root_fstype}" in
		ext4)
			mkfs.ext4 "${new_root_partition}" -F -q -L "${new_root_label}" || die "Failed formatting target partition"
			;;
		*)
			die "Unsupported fstype"
			;;
	esac
	
	echo "Extracting image: ${image}"
	mountpoint="$(mktemp -d)" || die "Failed creating temporary mountpoint"
	mount "${new_root_partition}" "${mountpoint}" || die "Failed mounting target partition"
	tar -xf "${image}" -C "${mountpoint}" || die "Failed extracting image"
	sync || die "Failed syncing"
	cleanup
	
	echo "Instructing bootloader"
	NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_SWAP "$new_root_label" || die "Failed setting nvram variable SYS_BOOT_SWAP"
	
	echo "Image written to new root partition."
	echo "Reboot to swap"
	exit 0
fi

die "Should not be here"