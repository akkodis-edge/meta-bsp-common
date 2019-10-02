DESCRIPTION = "Utility to flash IMX fuses"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit python3-dir

SRCREV ?= "c9d0b6add407fac9d8301402ac3a44801b55fd25"
SRC_URI = "git://git@bitbucket.datarespons.com:7999/oe-bsp/flash-fuse.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

RDEPENDS_${PN} = "python3 python3-core"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/flash-fuse-imx.py ${D}${bindir}/flash-fuse-imx
}

PACKAGE_ARCH = "${MACHINE}"
COMPATIBLE_MACHINE = "(imx)"