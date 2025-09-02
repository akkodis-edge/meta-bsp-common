LICENSE = "MIT"

IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""
IMAGE_INSTALL = "squashfs-init"
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
# Remove .rootfs suffix
IMAGE_NAME_SUFFIX = ""

inherit core-image
