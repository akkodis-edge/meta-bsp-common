#!/bin/sh


die() {
	echo $1
	exit 1	
}

if [ "$#" -lt "2" ]; then
echo "Usage: $0 <disk drive> <rootfs tar file>"
	exit 1
fi

drive="${1}"
part="${drive}1"
fs="${2}"

echo "Disk ${drive} using ${part}"

parted --script ${drive} mklabel msdos || die "Unable to create partition table on ${drive}"
parted --script ${drive} mkpart primary 4 1000 || die "Unable to create partition on ${drive}"
#parted --script ${drive} mkpart primary 1000 2000 || die "Unable to create partition 2 on $1"
sleep 1
echo "Creating ext4 on ${part}"
mkfs.ext4 ${part} -L rootfs || die "Unable to create FS on ${part}"

d=$(mktemp -d)
mount ${part} ${d} || die "Unable to mount ${part} on tmp ${d}"
echo "extract ${fs} on ${part}"
tar -C ${d} -xf ${fs} || die "Tar failure"
umount ${d}

echo "Done"
