DESCRIPTION = "Utility to flash boot loader"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

inherit python3-dir

SRCREV ?= "4cea11021c567840cfc0d53f4940d80d603bb4f5"
SRC_URI = "git://git@github.com/data-respons-solutions/flash-uboot.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

RDEPENDS_${PN} = "python3-core python3-crypt mtd-utils"

S = "${WORKDIR}/git"

do_compile() {
	oe_runmake
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/flash-uboot ${D}${bindir}
}
