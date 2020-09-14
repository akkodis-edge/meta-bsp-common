DESCRIPTION = "Interface for updating and swapping root partition"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

SRC_URI += " \
	file://swap-root.sh \
	file://swap-root.service.in \
"

RDEPENDS_${PN} += "nvram"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/swap-root.sh ${D}${sbindir}/swap-root
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/swap-root.service.in ${D}${systemd_system_unitdir}/swap-root.service
}

SYSTEMD_SERVICE_${PN} += "swap-root.service"