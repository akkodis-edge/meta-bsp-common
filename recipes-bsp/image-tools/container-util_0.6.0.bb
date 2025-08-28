require image-tools.inc

do_compile() {
	oe_runmake ${EXTRA_OECONF} USE_SYSTEMD=0 container-util
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/build/container-util ${D}${bindir}/
}

RDEPENDS:${PN} = "coreutils openssl-bin libp11 cryptsetup vim-xxd"

BBCLASSEXTEND = "native nativesdk"
