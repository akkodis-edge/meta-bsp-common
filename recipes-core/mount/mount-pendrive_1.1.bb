DESCRIPTION = "Pendrive mount point"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

SRC_URI = " \
	file://mnt-pendrive.mount \
"

PENDRIVE_PARTLABEL ??= "TESTDRIVE"

do_install () {
    install -d ${D}${systemd_unitdir}/system
    sed -i 's,@PARTLABEL@,${PENDRIVE_PARTLABEL},g' ${WORKDIR}/mnt-pendrive.mount
    install -m 0644 ${WORKDIR}/mnt-pendrive.mount ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "mnt-pendrive.mount"
FILES_${PN} += "${systemd_unitdir}/system/mnt-pendrive.mount"

PACKAGE_ARCH = "${MACHINE_ARCH}"
