DESCRIPTION = "Factory minimal initrd"

FEATURE_PACKAGES_factory = "packagegroup-factory"
IMAGE_FEATURES = "factory read-only-rootfs"

inherit core-image

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

CORE_IMAGE_BASE_INSTALL = "${CORE_IMAGE_EXTRA_INSTALL}"
