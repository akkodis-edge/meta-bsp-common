DESCRIPTION = "Data Respons image tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "20775ae6b5e03abf3fe2c49357e50ee5f979b94a"
SRC_URI = "git://git@github.com/data-respons-solutions/image-tools.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
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

FILES:${PN}:append = " ${datadir}/image-tools/*"
