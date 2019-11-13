#!/bin/bash

# Deps -> wipefs, umount, parted, mkfs.ext4

die() {
	echo $1
	exit 1
}

if [ "$#" -lt "3" ]; then
	echo "Usage: $0 BLOCKDEVICE PARTITIONS[LABEL FSTYPE SIZE(MiB)]..."
	exit 1
fi

dev=${1}

# Parse arguments into array of "LABEL FSTYPE SIZE"
argc=${#}
argv=(${@})
for ((i = 1; i < argc; i++)); do
	partitions+=("${argv[i]} ${argv[++i]} ${argv[++i]}")
done

echo "Device ${dev}"
echo "Checking if partitions mounted..."
for part in ${dev}?*; do
	if grep -qs "${part} " /proc/mounts; then
		echo "Unmounting ${part}..."
		umount ${part} || die "Failed unmounting ${part}"
	fi
done

echo "Wiping partition table..." 
wipefs --all ${dev} || die "Failed wiping partition table"

echo "Creating partition table..."
parted -s ${dev} mklabel gpt
start=1
for ((i = 0; i < ${#partitions[@]}; i++)); do
	read -a data <<< ${partitions[$i]}
	label=${data[0]}
	fs=${data[1]}
	size=${data[2]}
	end=$((${start}+${size}))
	if [[ ${dev} == *"mmcblk"* ]]; then
		part=${dev}p$((${i}+1))
	else
		part=${dev}$((${i}+1))
	fi

	echo "Partition ${part}(${label}) of type ${fs} starting at ${start}MiB ending at ${end}MiB"
	parted -s ${dev} mkpart ${label} ${fs} ${start} ${end} || die "Failed creating partition ${label}"
	start=${end}	
	if [[ ${fs} == "ext4" ]]; then
		mkfs.ext4 ${part} -F -q -L ${label} || die "Failed formatting ${label}"
	else
		die "Filesystem ${fs} not included"
	fi
done

exit 0
