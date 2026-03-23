DESCRIPTION = "Akkodis Edge image tools"
LICENSE = "MIT & GPL-2.0-with-OpenSSL-exception"
LIC_FILES_CHKSUM = "file://COPYING;md5=26ba640c983c054d7af59178266970cf \
					file://COPYING.cryptsetup;md5=32107dd283b1dfeb66c9b3e6be312326"

DEPENDS = "cryptsetup openssl"

SRCREV ?= "1a37d654313203e881a7d14c73543d37b9265f4f"
SRC_URI = "git://git@github.com/akkodis-edge/image-tools.git;protocol=https;branch=master"

EXTRA_OECONF = " \
	BUILD=${WORKDIR}/build \
	DESTDIR=${D} \
	bindir=${bindir} \
	systemd_system_unitdir=${systemd_system_unitdir} \
"

inherit systemd

PACKAGECONFIG ?= " \
	${@'systemd' if d.getVar('INIT_MANAGER') == 'systemd' else ''} \
"
PACKAGECONFIG[systemd] = "USE_SYSTEMD=1,USE_SYSTEMD=0"

PACKAGES += " \
	${@bb.utils.contains('PACKAGECONFIG', 'systemd', '${PN}-autocommit', '', d)} \
	${PN}-swap-root \
	${PN}-utils \
	${PN}-pkcs11 \
"

do_compile() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS}
}

do_install() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS} install
	rm ${D}${bindir}/gpt-insert
}

FILES:${PN} = "${bindir}/container-util"
# Allow running without pkcs11 by splitting RDEPENDS to separate package
FILES:${PN}-pkcs11 = ""
ALLOW_EMPTY:${PN}-pkcs11 = "1"
RDEPENDS:${PN}-pkcs11 = "pkcs11-provider"
# script are split in separate -utils package to reduce mandatory RDEPENDS for
# container-util in ${PN}.
RDEPENDS:${PN}-utils = "${PN} ${PN}-pkcs11 util-linux-umount util-linux-blkid coreutils nvram bash bmaptool squashfs-tools"
FILES:${PN}-utils = "${bindir}/install-image-container ${bindir}/make-image-container"
RDEPENDS:${PN}-swap-root = "${PN}-utils"
FILES:${PN}-swap-root = "${bindir}/swap-root"
RDEPENDS:${PN}-autocommit = "${PN}-swap-root"
FILES:${PN}-autocommit = "${systemd_system_unitdir}/swap-root.service"
SYSTEMD_SERVICE:${PN}-autocommit += "swap-root.service"
SYSTEMD_PACKAGES += "${PN}-autocommit"

BBCLASSEXTEND = "native nativesdk"
