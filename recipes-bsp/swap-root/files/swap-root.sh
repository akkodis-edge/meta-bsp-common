#!/bin/sh

# 
# Expects:
# - Two root partitions, labeled "rootfs1" and "rootfs2"
# - Bootloader understanding nvram values SYS_BOOT_PART, SYS_BOOT_SWAP and SYS_BOOT_VERIFIED
#

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
   	echo " state          Return current state. \"OK\" or \"UPDATING\""
   	echo " commit         Commit change from state \"UPDATING\" to \"OK\""
    echo
    exit 0
}

# Parse arguments
cmd_update=""
cmd_state=""
cmd_commit=""
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
	*)
		print_usage "$(basename ${0})"
		;;
esac

# Check if update already in progress
system_verified="$(nvram --sys get SYS_BOOT_VERIFIED)" || die "Failed reading nvram variable"

# COMMAND state
if [ "${cmd_state}" = "true" ]; then
	if [ "${system_verified}" = "true" ]; then
		echo "OK"
	else
		echo "UPDATING"
	fi
	exit 0
fi

# COMMAND commit
if [ "${cmd_commit}" = "true" ]; then
	if [ "${system_verified}" != "true" ]; then
		sys_boot_swap="$(nvram --sys get SYS_BOOT_SWAP)" || die "Failed getting nvram variable"
		NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_VERIFIED "true" || die "Failed setting nvram variable"
		NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_PART "${sys_boot_swap}" || die "Failed setting nvram variable"
		NVRAM_SYSTEM_UNLOCK=16440 nvram --sys delete SYS_BOOT_RETRIES || die "Failed setting nvram variable"
	fi
	exit 0
fi

# COMMAND update
if [ "${cmd_update}" = "true" ]; then
	if [ "${system_verified}" != "true" ]; then
		die "System already updating"
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
	case ${new_root_label} in
		rootfs1)
			NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_SWAP 1 || die "Failed setting nvram variable"
			;;
		rootfs2)
			NVRAM_SYSTEM_UNLOCK=16440 nvram --sys set SYS_BOOT_SWAP 2 || die "Failed setting nvram variable"
			;;
	esac
	
	echo "Image written to new root partition."
	echo "Reboot to swap"
	exit 0
fi

die "Should not be here"