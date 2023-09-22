#!/bin/bash
# Require bash due to builtin read

die() {
	>&2 echo "${1}"
	exit 1
}

exit_success() {
	exit 0
}

print_usage() {
    echo "Usage: ${1} COMMAND ARGS..."
    echo
    echo "Commands:"
    echo " update IMAGE         Write IMAGE to not-used rootfs partition"
   	echo " state                Return current state"
	echo "                         NORMAL:    Normal boot"
	echo "                         SWAPPING:  Swap in progress"
	echo "                         ROLLBACK:  Bootloader performed rollback"
	echo "                         INIT:      swap initiated (this boot)"
   	echo " commit               Commit change from state \"SWAPPING\" or \"ROLLBACK\" to \"NORMAL\""
   	echo "                      Commit from state \"NORMAL\" returns success."
   	echo " rollback             Rollback from SWAPPING to NORMAL"
	echo " cancel               Cancel from INIT to NORMAL"
	echo " has-manual-counter   Check if counter is controlled by firmware"
	echo "                         0: controlled by firmware"
	echo "                         1: has to be manually incremented by caller"
	echo " counter [VALUE]      Read or write counter. Writing counter not allowed when has-manual-counter is false"
    echo
    exit 1
}

# Parse arguments
cmd_update=""
cmd_state=""
cmd_commit=""
cmd_cancel=""
cmd_rollback=""
cmd_counter=""
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
	rollback)
		if [ "${#}" -ne "1" ]; then
			print_usage "$(basename ${0})"
		fi
		cmd_rollback="true"
		;;
	has-manual-counter)
		if [ "${#}" -ne "1" ]; then
			print_usage "$(basename ${0})"
		fi
		echo "no"
		exit 0
		;;
	counter)
		if [ "${#}" -ne "1" ]; then
			print_usage "$(basename ${0})"
		fi
		cmd_counter="true"
		;;
	*)
		print_usage "$(basename ${0})"
		;;
esac

state="UNKNOWN"
part="$(nvram --sys --get SYS_BOOT_PART)" || die "Failed reading nvram variable SYS_BOOT_PART"
swap="$(nvram --sys --get SYS_BOOT_SWAP)" || die "Failed reading nvram variable SYS_BOOT_SWAP"
attempts="0"
if [ "$part" = "$swap" ]; then
	attempts="$(nvram --sys --get SYS_BOOT_ATTEMPTS)"
	if [ $? -ne 0 ]; then
		state="NORMAL"
	else
		state="ROLLBACK"
	fi
else
	attempts="$(nvram --sys --get SYS_BOOT_ATTEMPTS)"
	if [ $? -ne 0 ]; then
		state="INIT"
	else
		state="SWAPPING"
	fi
fi

if [ "$state" = "UNKNOWN" ]; then
	die "Could not determine state"
fi

# COMMAND state
if [ "$cmd_state" = "true" ]; then
	echo "$state"
	exit_success
fi

# COMMAND commit
if [ "$cmd_commit" = "true" ]; then
	if [ "$state" = "NORMAL" ]; then
		exit_success
	fi
	if [ "$state" != "SWAPPING" -a "$state" != "ROLLBACK" ]; then
		die "Invalid state \""$state"\" for commit"
	fi
	NVRAM_SYSTEM_UNLOCK=16440 nvram --sys \
		--del SYS_BOOT_ATTEMPTS \
		--set SYS_BOOT_PART ${swap} || die "Failed setting nvram variables"
	exit_success
fi

# COMMAND cancel
if [ "$cmd_cancel" = "true" ]; then
	if [ "$state" != "INIT" ]; then
		die "Invalid state \""$state"\" for cancel"
	fi
	NVRAM_SYSTEM_UNLOCK=16440 nvram --sys --set SYS_BOOT_SWAP "$part" || die "Failed setting nvram variable SYS_BOOT_SWAP"
	exit_success
fi

# COMMAND rollback
if [ "$cmd_rollback" = "true" ]; then
	if [ "$state" != "SWAPPING" ]; then
		die "Invalid state \""$state"\" for rollback"
	fi
	NVRAM_SYSTEM_UNLOCK=16440 nvram --sys --set SYS_BOOT_SWAP "$part" || die "Failed setting nvram variable SYS_BOOT_SWAP"
	exit_success
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
	# Create partitions and install image
	read -r -d '' config <<- EOM
partitions:
   - label: "${new_root_label}"
     type: ext4

images:
   - name: image
     type: tar.bz2
     target: "label:${new_root_label}"
EOM
	
	args="--force-unmount --config - image=${image}"
	echo "Partition device and install images.."
	printf '%s\n' "$config"
	if printf '%s\n' "$config" | image-install $args; then
		echo "Success!"
	else
		die "Failed installation"
	fi	
	NVRAM_SYSTEM_UNLOCK=16440 nvram --sys --set SYS_BOOT_SWAP "$new_root_label" || die "Failed setting nvram variable SYS_BOOT_SWAP"
	echo "Image written to new root partition."
	echo "Reboot to swap"
	exit_success
fi

# COMMAND counter
if [ "$cmd_counter" = "true" ]; then
	if [ "x$attempts" = "x" ]; then
		echo 0
	else
		echo "$attempts"
	fi
	exit_success
fi

die "Should not be here"
