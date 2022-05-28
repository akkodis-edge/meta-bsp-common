DESCRIPTION = "Utility to set hostname from serial number in nvram"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = 	" \
    file://nvram-hostname.sh \
    file://nvram-hostname.service \
"

SYSTEM_NAME ?= "datarespons"

inherit systemd

S = "${WORKDIR}"
RDEPENDS:${PN} = "nvram"

do_install () {
    install -d ${D}${bindir}
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/default/
    install -m 0755 ${WORKDIR}/nvram-hostname.sh ${D}${bindir}/nvram-hostname
	install -m 0644 ${WORKDIR}/nvram-hostname.service ${D}${systemd_unitdir}/system/
    echo "SYSTEMNAME=${SYSTEM_NAME}" > ${WORKDIR}/systemname
    install -m 0644 ${WORKDIR}/systemname ${D}${sysconfdir}/default/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES:${PN} = "${bindir}/* ${sysconfdir}/default/systemname"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "nvram-hostname.service"
