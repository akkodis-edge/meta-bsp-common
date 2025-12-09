DESCRIPTION = "Akkodis Edge utilities"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a51e1b434b80ddca042429bbbc5264d9"

SRCREV ?= "983fc17e1c9584e90f3e31aebd8e81cad652c44e"
SRC_URI = "git://git@github.com/akkodis-edge/akkodis-utils.git;protocol=https;branch=main"

RDEPENDS:${PN} = "python3-core python3-pyserial"

EXTRA_OECONF = " \
	BUILD=${WORKDIR}/build \
	DESTDIR=${D} \
	bindir=${bindir} \
	USE_CLANG_TIDY=0 \
"

PACKAGECONFIG = ""

PACKAGECONFIG[sanitizer] = "USE_SANITIZER=1,USE_SANITIZER=0,gcc-sanitizers"

do_compile() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS}
}

do_install() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS} install
}
