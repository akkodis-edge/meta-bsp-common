DESCRIPTION = "NVRAM base program"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=14e5f6d6fc625ef5ece406e9c85a768a"

NVRAM_INTERFACE ??= "file"

SRCREV ?= "a44eb31d96c8ed33b49717a5292c8509dc02f6c0"
BRANCH ?= "master"
SRC_URI = "gitsm://git@github.com/data-respons-solutions/nvram.git;protocol=ssh;branch=${BRANCH}"

PV = "2.0+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = " \
	${@oe.utils.conditional('NVRAM_INTERFACE','efi','e2fsprogs','',d)} \
	${@oe.utils.conditional('NVRAM_INTERFACE','mtd','mtd-utils','',d)} \
"

RDEPENDS_${PN} += "\
	${@oe.utils.conditional('NVRAM_INTERFACE','efi','e2fsprogs','',d)} \
"

def get_interface_cflags(d):
    if d.getVar('NVRAM_INTERFACE') =='mtd':
        gpio = d.getVar('NVRAM_MTD_WP')
        if gpio:
            return f'-DNVRAM_WP_GPIO={gpio}'
    return ''

EXTRA_OEMAKE += "\
	NVRAM_INTERFACE_TYPE=${NVRAM_INTERFACE} \
"

TARGET_CFLAGS += "${@get_interface_cflags(d)}"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/nvram ${D}${bindir}
}

FILES_${PN} = "${bindir}/nvram"

PACKAGE_ARCH = "${MACHINE_ARCH}"
