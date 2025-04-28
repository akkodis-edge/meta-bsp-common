DESCRIPTION = "Akkodis Edge image tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "b9475fc01ba4daccdd1302413b1f6d4f4fdd0523"
SRC_URI = "git://git@github.com/akkodis-edge/image-tools.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

inherit systemd

RDEPENDS:${PN} = " \
	util-linux-wipefs util-linux-mount util-linux-umount \
	util-linux-blkid parted e2fsprogs-mke2fs tar coreutils \
	bzip2 aosp-utils python3-pyyaml squashfs-tools openssl-bin \
	cryptsetup nvram bash bmaptool \
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

FILES:${PN} = "${sbindir}/image-install ${sbindir}/install-image-container"

RDEPENDS:${PN}-swap-root = "${PN} bash"
FILES:${PN}-swap-root = "${sbindir}/swap-root"

RDEPENDS:${PN}-autocommit = "${PN}-swap-root"
FILES:${PN}-autocommit = "${systemd_system_unitdir}/swap-root.service"
SYSTEMD_SERVICE:${PN}-autocommit += "swap-root.service"
SYSTEMD_PACKAGES += "${PN}-autocommit"
