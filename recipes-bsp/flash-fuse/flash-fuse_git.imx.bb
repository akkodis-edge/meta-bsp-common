DESCRIPTION = "Utility to flash IMX fuses"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit python3-dir

SRCREV ?= "ddfcf459fd8fee66df64ca87dc3ab7bcbe148fa3"
SRC_URI = "git://git@github.com/data-respons-solutions/flash-fuse.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

RDEPENDS_${PN} = "python3-core"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/flash-fuse-imx.py ${D}${bindir}/flash-fuse-imx
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(imx)"