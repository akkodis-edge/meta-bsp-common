DESCRIPTION = "Run install script in factory ramdisk"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
	file://linuxrc \
"
inherit python3-dir
RDEPENDS_${PN} = "bash"

do_install () {
    install -m 0755 ${WORKDIR}/linuxrc ${D}
}

FILES_${PN} += "/linuxrc"