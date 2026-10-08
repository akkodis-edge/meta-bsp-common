SUMMARY = "Vehicle system controller driver"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRCREV ?= "ebafaf2879c3ab3a2b16e8fcf748c222a960c3a9"
SRC_URI = "git://git@github.com/akkodis-edge/kernel-module-vmcu.git;protocol=https;branch=main"

do_install:append() {
	install -d ${D}${bindir}
	install -m 0755 ${S}/flash-vmcu.sh ${D}${bindir}/flash-vmcu
	install -m 0755 ${S}/status-led.sh ${D}${bindir}/status-led
}

KERNEL_MODULE_AUTOLOAD += "vmcu"

PACKAGES += "${PN}-tools"
FILES:${PN}-tools = "${bindir}/flash-vmcu ${bindir}/status-led"
RDEPENDS:${PN}-tools = "bash"
RRECOMMENDS:${PN} += "${PN}-tools"
