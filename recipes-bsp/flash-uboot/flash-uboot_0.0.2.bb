DESCRIPTION = "Utility to flash boot loader"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

inherit python3-dir

SRCREV ?= "3f82fba8910f401458c0ee149e5cc67885a737a6"
SRC_URI = "git://git@github.com/data-respons-solutions/flash-uboot.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

RDEPENDS:${PN} = "python3-core python3-crypt mtd-utils util-linux-blockdev"

S = "${WORKDIR}/git"

do_compile() {
	oe_runmake
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/flash-uboot ${D}${bindir}
}
