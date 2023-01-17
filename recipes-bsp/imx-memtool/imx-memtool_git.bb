# Copyright (C) 2012-2016 O.S. Systems Software LTDA.
# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright (C) 2017-2021 NXP

SUMMARY = "program for direct register access on imx"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

PE = "1"
PV = "7.0+${SRCPV}"

SRCBRANCH = "lf-5.15.5_1.0.0"
SRCREV = "b364c1aacc72a63290106ae065bb2f6a8c365ec6"
SRC_URI = " \
	git://source.codeaurora.org/external/imx/imx-test.git;protocol=https;branch=${SRCBRANCH} \
	file://0001-memtool-build-stand-alone-and-disable-named-register.patch \
"

S = "${WORKDIR}/git"

do_compile() {
	make -C ${S}/test/memtool clean
	make -C ${S}/test/memtool
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/test/memtool/memtool ${D}${sbindir}/imx-memtool
}
