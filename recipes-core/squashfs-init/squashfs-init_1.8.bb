DESCRIPTION = "Locate squashfs for chroot"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://init.in"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

# Set empty string to disable
FALLBACK_OPTIONS ??= "--match-token"
FALLBACK_MATCH ??= "PARTLABEL=SERVICEUSB"

# Set rootfs public key validation method
ROOTFS_PUBLIC_KEY ??= "--pubkey-any"
# Allow adding key recipes
ROOTFS_PUBLIC_KEY_RDEPENDS ??= ""

do_compile[vardeps] += " FALLBACK_OPTIONS FALLBACK_MATCH ROOTFS_PUBLIC_KEY ROOTFS_PUBLIC_KEY_RDEPENDS"

do_compile () {
	sed -e 's,@FALLBACK_MATCH@,${FALLBACK_MATCH},g' \
		-e 's,@FALLBACK_OPTIONS@,${FALLBACK_OPTIONS},g' \
		-e 's,@ROOTFS_PUBLIC_KEY@,${ROOTFS_PUBLIC_KEY},g' \
		${S}/init.in > ${S}/init
}

do_install () {
	install -m 0755 ${S}/init ${D}
	install -m 0755 -d ${D}/dev
	mknod -m 0600 ${D}/dev/console c 5 1
	install -m 0700 -d ${D}/root
}

RDEPENDS:${PN} = " \
	busybox \
	squashfs-tools \
	util-linux-blkid \
	container-util \
	rootfsctl \
	${ROOTFS_PUBLIC_KEY_RDEPENDS} \
	util-linux-fsck \
	e2fsprogs-e2fsck \
	dosfstools \
"
RRECOMMENDS:${PN} = " \
	kernel-module-squashfs \
	kernel-module-overlay \
	kernel-module-dm-verity \
	kernel-module-dm-mod \
	kernel-module-dm-crypt \
"

FILES:${PN} = "/init /dev/console /root"
