DESCRIPTION = "Data Respons image tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "daa9e502560bddffd47d4a7477eea3f674ae335f"
SRC_URI = "git://git@github.com/data-respons-solutions/image-tools.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
	util-linux-wipefs util-linux-mount util-linux-umount \
	util-linux-blkid parted e2fsprogs-mke2fs tar coreutils \
	bzip2 aosp-utils python3-pyyaml \
"

do_compile[noexec] = "1"

do_install() {
	install -d ${D}${sbindir}
	install -d ${D}${datadir}/image-tools/samples
	install -m 0755 ${S}/image-install.py ${D}${sbindir}/image-install
	install -m 0644 ${S}/example-*.yaml ${D}${datadir}/image-tools/samples/
}

FILES_${PN}_append = " ${datadir}/image-tools/*"
