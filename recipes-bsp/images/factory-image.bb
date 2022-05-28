DESCRIPTION = "Container image with artifacts required for initializing system"
LICENSE = "MIT"

INITRD_IMAGE = "factory-initrd"

FACTORY_IMAGE_INSTALL ?= ""
IMAGE_INSTALL += "${FACTORY_IMAGE_INSTALL}"
IMAGE_CONTAINER_NO_DUMMY = "1"
IMAGE_FSTYPES = "container"
IMAGE_LINGUAS = ""
#IMAGE_PREPROCESS_COMMAND:remove = " prelink_setup; prelink_image; mklibs_optimize_image;"

do_initrd[depends] += " \
	${INITRD_IMAGE}:do_image_complete \
"

addtask do_initrd after do_rootfs before do_image

do_initrd () {
	install -d ${IMAGE_ROOTFS}/boot
	install -m 0644 ${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}.${INITRAMFS_FSTYPES} ${IMAGE_ROOTFS}/boot/
}

inherit core-image dr-image-info
