DESCRIPTION = "Utility to flash nvmem fuses"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

SRCREV ?= "84331d9b46d42e3dc80e6792906f218f9d5fc894"
SRC_URI = "git://git@github.com/akkodis-edge/flash-fuse.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx6dl ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx8mm ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx8mn ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx8mp ${D}${bindir}
}

CLEANBROKEN = "1"

PACKAGES += "${PN}-imx8mm ${PN}-imx8mn ${PN}-imx6dl ${PN}-imx8mp"
FILES:${PN} = ""
FILES:${PN}-imx8mm = "${bindir}/flash-fuse-imx8mm"
FILES:${PN}-imx8mn = "${bindir}/flash-fuse-imx8mn"
FILES:${PN}-imx8mp = "${bindir}/flash-fuse-imx8mp"
FILES:${PN}-imx6dl = "${bindir}/flash-fuse-imx6dl"
