DESCRIPTION = "Locate squashfs for chroot"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://init.in"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

# Set empty string to disable
FALLBACK_OPTIONS ??= "--match-token"
FALLBACK_MATCH ??= "PARTLABEL=SERVICEUSB"

do_compile[vardeps] += " FALLBACK_OPTIONS FALLBACK_MATCH"

do_compile () {
	sed -e 's,@FALLBACK_MATCH@,${FALLBACK_MATCH},g' \
		-e 's,@FALLBACK_OPTIONS@,${FALLBACK_OPTIONS},g' \
		${S}/init.in > ${S}/init
}

do_install () {
	install -m 0755 ${S}/init ${D}
}

RDEPENDS:${PN} = " \
	kernel-module-squashfs \
	kernel-module-overlay \
	busybox \
	squashfs-tools \
	util-linux-blkid \
"

FILES:${PN} = "/init"
