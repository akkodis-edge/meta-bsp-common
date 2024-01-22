DESCRIPTION = "Utilities for working with Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCREV ?= "18d1874bb64aa2ad1f0978e1e48bab5cd7484d47"
SRC_URI = "gitsm://git@github.com/data-respons-solutions/aosp-utils.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "main"

DEPENDS = "zlib"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "BUILD=${WORKDIR}/build"

CLEANBROKEN = "1"

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/build/core/libsparse/simg2img ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/core/libsparse/img2simg ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/core/libsparse/append2simg ${D}${bindir}/
	install -m 0755 ${WORKDIR}/build/simg2stdout ${D}${bindir}/
}
