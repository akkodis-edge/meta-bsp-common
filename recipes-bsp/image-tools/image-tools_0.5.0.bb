DESCRIPTION = "Data Respons image tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "6ff37043c071a127b3df759c3edc43c1a594f07c"
SRC_URI = "git://git@github.com/data-respons-solutions/image-tools.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

inherit systemd

RDEPENDS:${PN} = " \
	util-linux-wipefs util-linux-mount util-linux-umount \
	util-linux-blkid parted e2fsprogs-mke2fs tar coreutils \
	bzip2 aosp-utils python3-pyyaml squashfs-tools openssl-bin \
	cryptsetup nvram bash \
"

PACKAGES += "${PN}-autocommit ${PN}-swap-root"

do_compile[noexec] = "1"

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/image-install.py ${D}${sbindir}/image-install
	install -m 0755 ${S}/install-image-container.sh ${D}${sbindir}/install-image-container
	install -m 0755 ${S}/swap-root.sh ${D}${sbindir}/swap-root
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${S}/swap-root.service.in ${D}${systemd_system_unitdir}/swap-root.service
}

RDEPENDS:${PN}-swap-root = "${PN}"
FILES:${PN}-swap-root = "${sbindir}/swap-root"

RDEPENDS:${PN}-autocommit = "${PN}-swap-root"
FILES:${PN}-autocommit = "${systemd_system_unitdir}/swap-root.service"
SYSTEMD_SERVICE:${PN}-autocommit += "swap-root.service"
SYSTEMD_PACKAGES += "${PN}-autocommit"
