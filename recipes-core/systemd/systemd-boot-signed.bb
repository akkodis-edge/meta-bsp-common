DESCRIPTION = "Signed version of systemd-boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require conf/image-uefi.conf

inherit sbsign deploy

DEPENDS += "systemd-boot"

do_compile() {
	cp ${DEPLOY_DIR_IMAGE}/systemd-boot${EFI_ARCH}.efi ${WORKDIR}/
}

SECURE_BOOT_SIGNING_FILES += "${WORKDIR}/systemd-boot${EFI_ARCH}.efi"
addtask do_sbsign after do_compile before do_install

do_install() {
	install -d ${D}/EFI/BOOT
	install -m 0644 ${WORKDIR}/systemd-boot${EFI_ARCH}.efi.signed ${D}/EFI/BOOT/boot${EFI_ARCH}.efi
}

do_deploy() {
	install -d ${DEPLOYDIR}/EFI/BOOT
	install -m 0644 ${WORKDIR}/systemd-boot${EFI_ARCH}.efi.signed ${DEPLOYDIR}/EFI/BOOT/boot${EFI_ARCH}.efi
}
addtask do_deploy after do_install before do_build

FILES:${PN} = "/EFI/BOOT/"

PACKAGE_ARCH = "${MACHINE_ARCH}"
