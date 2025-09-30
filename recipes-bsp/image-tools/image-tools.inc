DESCRIPTION = "Akkodis Edge image tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "f1b7a7fa198432387e1296fec499dcab13c8a41b"
SRC_URI = "git://git@github.com/akkodis-edge/image-tools.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

EXTRA_OECONF = " \
	BUILD=${WORKDIR}/build \
	DESTDIR=${D} \
	bindir=${bindir} \
	systemd_system_unitdir=${systemd_system_unitdir} \
"
