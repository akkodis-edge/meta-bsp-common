DESCRIPTION = "NVRAM base program"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

NVRAM_TARGET ??= "FILE"
NVRAM_WP_GPIO ??= ""

SRCREV ?= "a898835c3513c6017b7294c8b02ee5dbe660eb8e"
BRANCH ?= "master"
SRC_URI = "gitsm://git@bitbucket.datarespons.com:7999/oe-bsp/nvram.git;protocol=ssh;branch=${BRANCH}"

PV = "1.3+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = " \
		${@oe.utils.conditional('NVRAM_TARGET','EFI','e2fsprogs','',d)} \
		${@oe.utils.conditional('NVRAM_TARGET','MTD','mtd-utils','',d)} \
"

RDEPENDS_${PN} += "\
		libstdc++ \
		${@oe.utils.conditional('NVRAM_TARGET','EFI','e2fsprogs','',d)} \
"

EXTRA_OEMAKE += "\
		${@oe.utils.conditional('NVRAM_TARGET','EFI','nvram_efi','',d)} \
		${@oe.utils.conditional('NVRAM_TARGET','MTD','nvram_mtd VPD_MTD_WP=${NVRAM_WP_GPIO}','',d)} \
		${@oe.utils.conditional('NVRAM_TARGET','FILE','nvram_file','',d)} \
		${@oe.utils.conditional('NVRAM_TARGET','LEGACY','nvram_legacy','',d)} \
"

do_install () {
    install -d ${D}${sbindir}
    if [ ${NVRAM_TARGET} = "FILE" ]; then
    	install -m 0755 ${S}/nvram_file ${D}${sbindir}/nvram
    elif [ ${NVRAM_TARGET} = "EFI" ]; then
    	install -m 0755 ${S}/nvram_efi ${D}${sbindir}/nvram
    elif [ ${NVRAM_TARGET} = "LEGACY" ]; then
    	install -m 0755 ${S}/nvram_legacy ${D}${sbindir}/nvram
    elif [ ${NVRAM_TARGET} = "MTD" ]; then
    	install -m 0755 ${S}/nvram_mtd ${D}${sbindir}/nvram
    fi
}

FILES_${PN} = "${sbindir}/nvram"

PACKAGE_ARCH = "${MACHINE_ARCH}"
