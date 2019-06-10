DESCRIPTION = "Fuse ethernet MAC from NVRAM"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI_mx6 = "file://nvram-fuse-eth-imx.py"

RDEPENDS_${PN} += "nvram python3"

do_install_mx6 () {
    install -d ${D}${sbindir}
    install -m 0744 ${WORKDIR}/nvram-fuse-eth-imx.py ${D}${sbindir}/nvram-fuse-eth
}

COMPATIBLE_MACHINE = "mx6"
PACKAGE_ARCH = "${MACHINE_ARCH}"
