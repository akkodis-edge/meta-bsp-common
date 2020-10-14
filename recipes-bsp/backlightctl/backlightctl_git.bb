DESCRIPTION = "Example application for controlling backlight based on sensor input"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=35f2d75e7fd344ac6abf727587716595"

SRCREV ?= "152dfc3dd1a21844d9b12aeb32c8d139a826a674"
BRANCH ?= "main"
SRC_URI = "git://git@github.com/data-respons-solutions/backlightctl.git;protocol=ssh;branch=${BRANCH}"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/backlightctl ${D}${bindir}
}

FILES_${PN} = "${bindir}/backlightctl"
