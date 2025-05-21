require conf/image-uefi.conf

inherit deploy sbsign image-artifact-names python3native

do_compile[depends] += " \
	${INITRD_IMAGE}:do_image_complete \
"
do_compile[vardeps] +=  "KERNEL_IMAGETYPE INITRD_IMAGE INITRAMFS_FSTYPES APPEND" 

DEPENDS += "virtual/kernel systemd-boot-native python3-pefile-native"

EFI_IMAGE_NAME ?= "${PN}-${MACHINE}.efi"

do_compile() {
	echo "${APPEND}" > ${WORKDIR}/cmdline.txt
    ukify build \
        --linux="${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}" \
        --initrd="${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}${IMAGE_NAME_SUFFIX}.${INITRAMFS_FSTYPES}" \
        --cmdline="@${WORKDIR}/cmdline.txt" \
        --efi-arch="${EFI_ARCH}" \
        --stub="${DEPLOY_DIR_IMAGE}/linux${EFI_ARCH}.efi.stub" \
        --output="${WORKDIR}/${EFI_IMAGE_NAME}"
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
