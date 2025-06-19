DESCRIPTION = "Automatically autorun file from pendrive on login "
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:{PN} += "login-autorun udev-pendrive"

PENDRIVE_PATH ??= "autorun"

do_install() {
	mkdir -p ${D}${bindir}
	ln -s /mnt/pendrive/${PENDRIVE_PATH} ${D}${bindir}/autorun
}
