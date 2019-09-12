# Generates a Manufacturing Tool Initramfs image
#
# This generates the initramfs used for the installation process. The
# image provides the utilities which are used, in the target, during
# the process and receive the commands from the MfgTool application.
#
# Copyright 2014 (C) O.S. Systems Software LTDA.

FEATURE_PACKAGES_mtd = "packagegroup-factory-mtd"
FEATURE_PACKAGES_extfs = "packagegroup-factory-extfs"

# Filesystems enabled by default
DEFAULT_FS_SUPPORT = " \
    mtd \
    extfs \
"

IMAGE_FEATURES = " \
    ${DEFAULT_FS_SUPPORT} \
    read-only-rootfs \
"

inherit core-image

CORE_IMAGE_BASE_INSTALL = " \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    udev \
    parted \
"