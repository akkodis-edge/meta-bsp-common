DESCRIPTION = "Example application for controlling backlight based on sensor input"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=35f2d75e7fd344ac6abf727587716595"

DEPENDS += "libiio"

SRCREV ?= "7cfb1b7924b6eff29b986860855f7c45ae54dc6a"
SRC_URI = "git://git@github.com/akkodis-edge/backlightctl.git;protocol=https;branch=main"

CLEANBROKEN = "1"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/build/backlightctl ${D}${bindir}
}

FILES:${PN} = "${bindir}/backlightctl"
