DESCRIPTION = "Utility to set hostname from serial number in nvram"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = 	" \
    file://nvram-hostname.sh \
    file://nvram-hostname.service.in \
"

NVRAM_HOSTNAME ?= "akkodis"
NVRAM_HOSTNAME_VARIABLE ?= "SYS_SERIALNUMBER"

inherit systemd

S = "${WORKDIR}"
RDEPENDS:${PN} = "nvram"

do_install () {
    install -d ${D}${bindir}
    install -d ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/nvram-hostname.sh ${D}${bindir}/nvram-hostname
    sed -e "s:@NVRAM_HOSTNAME@:${NVRAM_HOSTNAME}:g" \
    	-e "s:@NVRAM_HOSTNAME_VARIABLE@:${NVRAM_HOSTNAME_VARIABLE}:g" \
    	${WORKDIR}/nvram-hostname.service.in > ${WORKDIR}/nvram-hostname.service
	install -m 0644 ${WORKDIR}/nvram-hostname.service ${D}${systemd_unitdir}/system/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "nvram-hostname.service"
