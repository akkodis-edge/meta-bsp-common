DESCRIPTION = "Utility to flash nvmem fuses"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

SRCREV ?= "2ccb37dc83c40b3f87c0875d752d3f29bcd6d6b5"
SRC_URI = "git://git@github.com/data-respons-solutions/flash-fuse.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx6dl ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx8mm ${D}${bindir}
    install -m 0755 ${S}/build/flash-fuse-imx8mn ${D}${bindir}
}

PACKAGES += "${PN}-imx8mm ${PN}-imx8mn ${PN}-imx6dl"
FILES:${PN} = ""
FILES:${PN}-imx8mm = "${bindir}/flash-fuse-imx8mm"
FILES:${PN}-imx8mn = "${bindir}/flash-fuse-imx8mn"
FILES:${PN}-imx6dl = "${bindir}/flash-fuse-imx6dl"
