DESCRIPTION = "Data partition automount"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

DATA_DEVICE_PATH ??= "/dev/disk/by-partlabel/data"

SRC_URI = "file://data.mount.in"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install () {
	install -d ${D}${systemd_unitdir}/system
	sed 's,@DATA_DEVICE@,${DATA_DEVICE_PATH},g' ${S}/data.mount.in > ${S}/data.mount
	install -m 0644 ${S}/data.mount ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "data.mount"
FILES:{PN} = "${systemd_unitdir}/system/data.mount"

PACKAGE_ARCH = "${MACHINE_ARCH}"
