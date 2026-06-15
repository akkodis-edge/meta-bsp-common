DESCRIPTION = "Service for delaying units which depend on nvram"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://nvram-check.service.in"
S = "${UNPACKDIR}"

inherit systemd

RDEPENDS:${PN} = "nvram"

do_compile() {
	sed -e "s:@BINDIR@:${bindir}:g" \
		${UNPACKDIR}/nvram-check.service.in > ${B}/nvram-check.service
}

do_install () {
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${B}/nvram-check.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "nvram-check.service"
