require image-tools.inc

inherit systemd

PACKAGECONFIG ?= " \
	${@'systemd' if d.getVar('INIT_MANAGER') == 'systemd' else ''} \
"

PACKAGECONFIG[systemd] = "USE_SYSTEMD=1,USE_SYSTEMD=0"

PACKAGES += " \
	${@bb.utils.contains('PACKAGECONFIG', 'systemd', '${PN}-autocommit', '', d)} \
	${PN}-swap-root \
"

do_compile() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS}
}

do_install() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS} install
	rm ${D}${bindir}/container-util
}

RDEPENDS:${PN} = " \
	util-linux-wipefs util-linux-mount util-linux-umount \
	util-linux-blkid parted e2fsprogs-mke2fs dosfstools tar \
	coreutils bzip2 aosp-utils python3-pyyaml squashfs-tools  \
	nvram bash bmaptool container-util \
"
FILES:${PN} = " \
	${bindir}/image-install \
	${bindir}/install-image-container \
	${bindir}/make-image-container \
	${bindir}/install-usb-image \
"

RDEPENDS:${PN}-swap-root = "${PN} bash"
FILES:${PN}-swap-root = "${bindir}/swap-root"

RDEPENDS:${PN}-autocommit = "${PN}-swap-root"
FILES:${PN}-autocommit = "${systemd_system_unitdir}/swap-root.service"
SYSTEMD_SERVICE:${PN}-autocommit += "swap-root.service"
SYSTEMD_PACKAGES += "${PN}-autocommit"
