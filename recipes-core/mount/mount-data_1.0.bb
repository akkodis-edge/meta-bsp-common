DESCRIPTION = "Data partition automount"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

DATA_DEVICE_PATH ??= "/dev/disk/by-partlabel/data"

SRC_URI = "file://data.mount.in"

do_install () {
	install -d ${D}${systemd_unitdir}/system
	sed 's,@DATA_DEVICE@,${DATA_DEVICE_PATH},g' ${WORKDIR}/data.mount.in > ${WORKDIR}/data.mount
	install -m 0644 ${WORKDIR}/data.mount ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "data.mount"
FILES:{PN} = "${systemd_unitdir}/system/data.mount"

PACKAGE_ARCH = "${MACHINE_ARCH}"
