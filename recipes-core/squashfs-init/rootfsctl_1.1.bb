DESCRIPTION = "Enable persistent rootfs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://rootfsctl.sh file://getsectors.c"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

# Set rootfs upperdir as directory on root device without encryption
ROOTFS_PERSISTENT_TYPE ??= "directory"
# keyctl trusted key arguments used by type "plain"
ROOTFS_PERSISTENT_PLAIN ??= "new 32"

do_compile[vardeps] += "ROOTFS_PERSISTENT_TYPE"

do_compile () {
	# Small utility for getting sector size and count
	${CC} ${CFLAGS} -Wall -Wextra -Werror -pedantic ${LDFLAGS} ${S}/getsectors.c -o ${S}/getsectors

	sed -e 's,@ROOTFS_PERSISTENT_TYPE@,${ROOTFS_PERSISTENT_TYPE},g' \
		-e 's,@ROOTFS_PERSISTENT_PLAIN@,${ROOTFS_PERSISTENT_PLAIN},g' \
		${S}/rootfsctl.sh > ${S}/rootfsctl
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${S}/rootfsctl ${D}${bindir}
	install -m 0755 ${S}/getsectors ${D}${bindir}
}

RDEPENDS:${PN} = " \
	keyutils \
	util-linux-losetup \
	cryptsetup \
"

FILES:${PN} = "${bindir}/"
