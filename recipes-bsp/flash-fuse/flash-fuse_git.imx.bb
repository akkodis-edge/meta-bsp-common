DESCRIPTION = "Utility to flash IMX fuses"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

inherit python3-dir

SRCREV ?= "2c190b843b0fb0e558492f2299a7d565bbbd5265"
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