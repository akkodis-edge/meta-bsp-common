DESCRIPTION = "Utility to flash iMX8MM fuses"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

SRCREV = "d281dfba3ef357db4684c3b6f860bced7660e735"
SRC_URI = "git://git@github.com/data-respons-solutions/flash-fuse.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/flash-fuse-imx8mm ${D}${sbindir}/
}
