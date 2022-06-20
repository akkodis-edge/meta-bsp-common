DESCRIPTION = "Data Respons image tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "5920f66d0c631f605eaecf6d5f5c63c98f849a17"
SRC_URI = "git://git@github.com/data-respons-solutions/image-tools.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

RDEPENDS:{PN} = "\
	wipefs mount umount blkid parted \
	mke2fs tar sync dd bzcat \
"
# simg2img
RDEPENDS:{PN}:append = "aosp-utils"

do_compile[noexec] = "1"

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${datadir}/image-tools/samples
	install -m 0755 ${S}/image-install.py ${D}${bindir}/image-install
	install -m 0644 ${S}/example-*.yaml ${D}${datadir}/image-tools/samples/
}

FILES:${PN}:append = " ${datadir}/image-tools/*"
