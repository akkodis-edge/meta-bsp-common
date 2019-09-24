DESCRIPTION = "Utility to flash boot loader"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit python3-dir

SRCREV ?= "077e68531e4bf32ac36c8808e08b0ee10308daa3"
SRC_URI = "git://git@bitbucket.datarespons.com:7999/oe-bsp/flash-uboot.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

RDEPENDS_${PN} = "python3 python3-core mtd-utils"

S = "${WORKDIR}/git"

do_compile() {
	oe_runmake
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/flash-uboot ${D}${bindir}
}
