DESCRIPTION = "Akkodis Edge utilities"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a51e1b434b80ddca042429bbbc5264d9"

SRCREV ?= "ba3213162f18d2f1b9317a1a7dadf61eb7897c16"
SRC_URI = "git://git@github.com/akkodis-edge/akkodis-utils.git;protocol=https;branch=main"

RDEPENDS:${PN} = "python3-core python3-pyserial"

inherit systemd

# When building without static libs the --disable-static flag is passed to EXTRA_OECONF.
# Flag is not supported, disable here.
DISABLE_STATIC = ""

EXTRA_OECONF = " \
	BUILD=${WORKDIR}/build \
	DESTDIR=${D} \
	bindir=${bindir} \
	libdir=${libdir} \
	sysconfdir=${sysconfdir} \
	systemd_system_unitdir=${systemd_system_unitdir} \
	includedir=${includedir} \
	USE_CLANG_TIDY=0 \
"

PACKAGECONFIG ?= "owld owl"

PACKAGECONFIG[sanitizer] = "USE_SANITIZER=1,USE_SANITIZER=0,gcc-sanitizers"
PACKAGECONFIG[owld] = "WITH_OWLD=1,WITH_OWLD=0,sqlite3 cyaml libiio"
PACKAGECONFIG[owl] = "WITH_OWL=1,WITH_OWL=0,sqlite3,python3-core"

do_compile() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS}
}

do_install() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS} install
}

# cyaml parser includes buildpaths in codedp
ERROR_QA:remove = "buildpaths"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "${@bb.utils.contains('PACKAGECONFIG', 'owld', 'owld.service', '',d)}"
