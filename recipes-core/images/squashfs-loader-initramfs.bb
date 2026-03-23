LICENSE = "MIT"

IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""
PACKAGE_INSTALL = "squashfs-init udev"
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
# Remove .rootfs suffix
IMAGE_NAME_SUFFIX = ""

inherit core-image
