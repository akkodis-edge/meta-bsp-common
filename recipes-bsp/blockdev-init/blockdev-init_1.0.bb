DESCRIPTION = "Block device partitioner and formatter"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://blockdev-init.sh"

RDEPENDS:${PN} += "e2fsprogs-mke2fs parted util-linux-wipefs bash"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/blockdev-init.sh ${D}${sbindir}/blockdev-init
}
