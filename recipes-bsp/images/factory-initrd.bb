DESCRIPTION = "Factory minimal initrd"

# Empty root password did for some reason not work. Not debugged further.
# Set root password to "root"
inherit extrausers
EXTRA_USERS_PARAMS = "usermod -p tt9/O6kW840oY root;"

FEATURE_PACKAGES:factory = "packagegroup-factory"
IMAGE_FEATURES = "factory empty-root-password"

inherit core-image dr-image-info

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

CORE_IMAGE_BASE_INSTALL = "${CORE_IMAGE_EXTRA_INSTALL}"

ROOTFS_POSTPROCESS_COMMAND:append = "\
						make_dirs; \
						"

make_dirs () {
	mkdir -p ${IMAGE_ROOTFS}/sys
	mkdir -p ${IMAGE_ROOTFS}/proc
	mkdir -p ${IMAGE_ROOTFS}/run/udev
}
