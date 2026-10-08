DESCRIPTION = "Akkodis Edge utilities"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a51e1b434b80ddca042429bbbc5264d9"

SRCREV ?= "66715cf771215dfee46fd61b848cb65a71360d39"
SRC_URI = "git://git@github.com/akkodis-edge/akkodis-utils.git;protocol=https;branch=main"

inherit systemd python3-dir

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
	python3_sitepackages_dir=${PYTHON_SITEPACKAGES_DIR} \
	USE_CLANG_TIDY=0 \
"

PACKAGECONFIG ?= "libowl owld owl atcli"
PACKAGECONFIG[sanitizer] = "USE_SANITIZER=1,USE_SANITIZER=0,gcc-sanitizers"
PACKAGECONFIG[libowl] = "WITH_LIBOWL=1,WITH_LIBOWL=0,sqlite3"
PACKAGECONFIG[owld] = "WITH_OWLD=1,WITH_OWLD=0,cyaml libiio"
PACKAGECONFIG[owl] = "WITH_OWL=1,WITH_OWL=0,,python3-core python3-rich gnuplot"
PACKAGECONFIG[atcli] = "WITH_ATCLI=1,WITH_ATCLI=0,,python3-core python3-pyserial"

do_compile() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS}
}

do_install() {
	oe_runmake ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS} install
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}/*"

# cyaml parser includes buildpaths in generated code
ERROR_QA:remove = "buildpaths"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "${@bb.utils.contains('PACKAGECONFIG', 'owld', 'owld.service', '',d)}"
