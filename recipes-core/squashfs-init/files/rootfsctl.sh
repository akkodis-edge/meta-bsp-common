#!/bin/sh

ENOENT=2
EFAULT=14
EINVAL=22

LOOPDEV="NONE"
MAPPER="NONE"
TRUSTED="NONE"

cleanup() {
	if [ "$MAPPER" != "NONE" ]; then
		dmsetup remove --deferred "$MAPPER" || echo "Failed removing MAPPER: $MAPPER"
		MAPPER="NONE"
	fi
	if [ "$LOOPDEV" != "NONE" ]; then
		losetup -d "$LOOPDEV" || echo "Failed detacing LOOPDEV: $LOOPDEV"
		LOOPDEV="NONE"
	fi
	if [ "$TRUSTED" != "NONE" ]; then
		keyctl purge trusted "$TRUSTED" || echo "Failed removing TRUSTED: $TRUSTED"
		TRUSTED="NONE"
	fi
}

die () {
	echo "$2"
	cleanup
	exit $1
}

print_usage() {
	echo "Usage: rootfsctl [OPTIONS]"
	echo "control rootfs persistent state."
	echo ""
	echo "One of:"
	echo " --state         Return current state"
	echo "                  active:   enabled and in use"
	echo "                  enabled:  enabled but not in use"
	echo "                  inactive: disabled and not in use"
	echo "                  disabled: disabled but in use"
	echo " --type          Return current type"
	echo " --enable        Enable persistent mode of --type,"
	echo "                   requires --size"
	echo " --disable       Disable persistent mode"
	echo " --open          Open device, called only from initramfs"
	echo ""
	echo "Optional:"
	echo "  -d,--debug     Debug output"
	echo " --keep          Do not remove persistent data"
	echo " --size          Size in MiB"
	echo ""
	echo "Return value:"
	echo " 0 for success or error code"
	echo "Error codes:"
	echo " 2  (ENOENT): No such file (or no permission)"
	echo " 14 (EFAULT): Operation failed"
	echo " 22 (EINVAL): Invalid argument"
}

persist_ctrl="/rootfs.dev/boot/rootfs.persistent"
persist_data="/rootfs.dev/boot/rootfs.rw"
persist_target="/rootfs.rw"
persist_type="@ROOTFS_PERSISTENT_TYPE@"
persist_blockdev="/rootfs.dev"

arg_size=""
arg_cmd=""
arg_debug="no"
arg_keep="no"

while [ "$#" -gt 0 ]; do
	case $1 in
	--state)
		arg_cmd="state"
		shift # past argument
		;;
	--enable)
		arg_cmd="enable"
		shift # past argument
		;;
	--disable)
		arg_cmd="disable"
		shift # past argument
		;;
	--open)
		arg_cmd="open"
		shift # past argument
		;;
	--size)
		[ "$#" -gt 1 ] || die $EINVAL "Invalid argument --size"
		arg_size="$2"
		shift # past argument
		shift # past value
		;;
	--type)
		echo "$persist_type"
		exit 0
		;;
	--keep)
		arg_keep="yes"
		shift # past argument
		;;
	-d|--debug)
		arg_debug="yes"
		shift # past argument
		;;
	-*|--*)
		print_usage
		exit $EINVAL
		;;
	*)
		arg_file="$1"
		shift # past argument
		;;
  esac
done

[ "x$arg_cmd" != "x" ] ||  die $EINVAL "No operation specified"

# OPEN is expected to be called from initramfs before state probing is possible
if [ "$arg_cmd" = "open" ]; then
	if [ ! -f "$persist_ctrl" ]; then
		die $ENOENT "persistent rootfs not enabled"
	fi
	mount -o remount,rw "$persist_blockdev" || die $EFAULT "Failed remounting root blockdev as rw"
	if [ "$persist_type" = "directory" ]; then
		mkdir -p "$persist_data" || die $EFAULT "Failed creating data dir"
		mount --bind "$persist_data" "$persist_target" || die $EFAULT "Failed bind mounting directory"
	elif [ "$persist_type" = "plain" ]; then
		keyctl add trusted rootcrypt "load $(cat "$persist_ctrl")" @u || die $EFAULT "Failed loading crypt key"
		TRUSTED="rootcrypt"
		dev="$(losetup -f --show "$persist_data")" || die $ENOENT "Failed loop mounting data"
		LOOPDEV="$dev"
		sector_count="$(getsectors --count "$LOOPDEV")" || die $EFAULT "Failed getting loopmount sector count"
		sector_size="$(getsectors --size "$LOOPDEV")" || die $EFAULT "Failed getting loopmount sector size"
		dmsetup create rootfs.rw --table \
			"0 $sector_count crypt aes-xts-plain64 :32:trusted:rootcrypt 0 $LOOPDEV 0 1 sector_size:$sector_size" \
			|| die $EFAULT "Failed setting up dm-crypt"
		MAPPER="rootfs.rw"
		dmsetup mknodes "$MAPPER" || die $EFAULT "Failed creating /dev/mapper nodes"
		mount "/dev/mapper/$MAPPER" "$persist_target" || die $EFAULT "Failed mounting mapper"
		# Disable any cleanup
		LOOPDEV="NONE"
		MAPPER="NONE"
		TRUSTED="NONE"
	else
		die $EINVAL "Unknown persist type: $persist_type"
	fi
	exit 0
fi

# Check current state of persistent rootfs
persist_state="UNKNOWN"
rootrw_dev="$(findmnt -no SOURCE "$persist_target")"
if [ "$rootrw_dev" = "tmpfs" ]; then
	if [ -f "$persist_ctrl" ]; then
		persist_state="enabled"
	else
		persist_state="inactive"
	fi
else
	if [ -f "$persist_ctrl" ]; then
		persist_state="active"
	else
		persist_state="disabled"
	fi
fi
[ "$persist_state" = "UNKNOWN" ] && die $EFAULT "Failed determining state"

[ "$arg_debug" = "yes" ] && echo "state: $persist_state type: $persist_type"

# STATE
if [ "$arg_cmd" = "state" ]; then
	echo "$persist_state"
# OPEN
elif [ "$arg_cmd" = "open" ]; then
	die $EFAULT "UNEXPECTED STATE"
# ENABLE
elif [ "$arg_cmd" = "enable" ]; then
	if [ "$persist_state" = "active" -o "$persist_state" = "disabled" ]; then
		die $EINVAL "--enable called from incompatible state $persist_state"
	fi
	if [ "$persist_state" != "enabled" ]; then
		mount -o remount,rw "$persist_blockdev" || die $EFAULT "Failed remounting root blockdev as rw"
		if [ -e "$persist_data" ]; then
			if [ "$arg_keep" = "yes" ]; then
				[ "$arg_debug" = "yes" ] && echo "Using previous data due to --keep"
			else
				[ "$arg_debug" = "yes" ] && echo "Deleteing previous data"
				rm -rf "$persist_data" || die $EFAULT "Failed deleting persistent data"
			fi
		fi
		if [ "$persist_type" = "directory" ]; then
			touch "$persist_ctrl" || die $EFAULT "Failed enabling"
		elif [ "$persist_type" = "plain" ]; then
			# $arg_keep is ignored here as encryption key is lost when disabling
			[ "x$arg_size" = "x" ] && die $EINVAL "Missing mandatory argument --size"
			truncate -s "${arg_size}M" "$persist_data" || die $EFAULT "Failed creating persistent data"
			key_id="$(keyctl add trusted rootcrypt "new 32" @s)" || die $EFAULT "Failed creating keyring key"
			TRUSTED="rootcrypt"
			dev="$(losetup -f --show "$persist_data")" || die $EFAULT "Failed loopmounting data"
			LOOPDEV="$dev"
			sector_count="$(getsectors --count "$LOOPDEV")" || die $EFAULT "Failed getting loopmount sector count"
			sector_size="$(getsectors --size "$LOOPDEV")" || die $EFAULT "Failed getting loopmount sector size"
			dmsetup -v create rootrwsetup --table \
				"0 $sector_count crypt aes-xts-plain64 :32:trusted:rootcrypt 0 $LOOPDEV 0 1 sector_size:$sector_size" \
				|| die $EFAULT "Failed setting up dm-crypt"
			MAPPER="rootrwsetup"
			mkfs.ext4 "/dev/mapper/$MAPPER" || die $EFAULT "Failed formatting data"
			keyctl pipe "$key_id" > "$persist_ctrl" || die $EFAULT "Failed writing persist enable"
		else
			die $EINVAL "Unknown persist type: $persist_type"
		fi
	fi
# DISABLE
elif [ "$arg_cmd" = "disable" ]; then
	mount -o remount,rw "$persist_blockdev" || die $EFAULT "Failed remounting root blockdev as rw"
	rm -rf "$persist_ctrl" || die $EFAULT "Failed disabling"
else
	die $EINVAL "Invalid argument"
fi

cleanup
exit 0
