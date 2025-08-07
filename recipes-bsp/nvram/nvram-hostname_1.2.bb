DESCRIPTION = "Utility to set hostname from serial number in nvram"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = 	" \
	file://nvram-hostname.sh \
	file://nvram-hostname.service.in \
"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

NVRAM_HOSTNAME ?= "akkodis"
NVRAM_HOSTNAME_VARIABLE ?= "SYS_SERIALNUMBER"

inherit systemd

RDEPENDS:${PN} = "nvram"

do_install () {
	install -d ${D}${bindir}
	install -d ${D}${systemd_unitdir}/system
	install -m 0755 ${S}/nvram-hostname.sh ${D}${bindir}/nvram-hostname
	sed -e "s:@NVRAM_HOSTNAME@:${NVRAM_HOSTNAME}:g" \
		-e "s:@NVRAM_HOSTNAME_VARIABLE@:${NVRAM_HOSTNAME_VARIABLE}:g" \
		${S}/nvram-hostname.service.in > ${S}/nvram-hostname.service
	install -m 0644 ${S}/nvram-hostname.service ${D}${systemd_unitdir}/system/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "nvram-hostname.service"
