require conf/image-uefi.conf

inherit deploy sbsign image-artifact-names

do_compile[depends] += " \
	${INITRD_IMAGE}:do_image_complete \
"
do_compile[vardeps] +=  "KERNEL_IMAGETYPE INITRD_IMAGE INITRAMFS_FSTYPES APPEND" 

DEPENDS += "virtual/kernel systemd-boot"

EFI_IMAGE_NAME ?= "${PN}-${MACHINE}.efi"

do_compile() {
	echo "${APPEND}" > ${WORKDIR}/cmdline.txt
	${OBJCOPY} \
    --add-section .cmdline="${WORKDIR}/cmdline.txt" --change-section-vma .cmdline=0x30000 \
    --add-section .linux="${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}" --change-section-vma .linux=0x40000 \
    --add-section .initrd="${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}${IMAGE_NAME_SUFFIX}.${INITRAMFS_FSTYPES}" --change-section-vma .initrd=0x3000000 \
    ${DEPLOY_DIR_IMAGE}/linux${EFI_ARCH}.efi.stub \
    ${WORKDIR}/${EFI_IMAGE_NAME}
}

SECURE_BOOT_SIGNING_FILES += "${WORKDIR}/${EFI_IMAGE_NAME}"
addtask do_sbsign after do_compile before do_install

do_install() {
	install -d ${D}/boot
	install -m 0644 ${WORKDIR}/${EFI_IMAGE_NAME}.signed ${D}/boot/${EFI_IMAGE_NAME}
}

addtask deploy after do_install before do_build
do_deploy() {
	install -m 0644 ${WORKDIR}/${EFI_IMAGE_NAME}.signed ${DEPLOYDIR}/${EFI_IMAGE_NAME}
}

FILES:${PN} += "/boot/*"
