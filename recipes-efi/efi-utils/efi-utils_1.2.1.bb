DESCRIPTION = "Akkodis Edge efi executables (for example from efi shell)" 
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy

SRCREV ?= "43e6a2174de0b1cccebadcdb75086ceb2d0d3e30"
SRC_URI = "git://git@github.com/akkodis-edge/efi-utils.git;protocol=ssh;branch=main"

DEPENDS += "gnu-efi sbsigntool-native"

EXTRA_OEMAKE = " \
	SYSROOT=${STAGING_DIR_TARGET} \
	USE_SBSIGN=1 \
	SBSIGN_KEY=${SECURE_BOOT_SIGNING_KEY} \
	SBSIGN_CERT=${SECURE_BOOT_SIGNING_CERT} \
"

do_configure() {
	:
}

do_install() {
	oe_runmake INSTALLDIR=${D}/EFI/bin/ install
}

addtask deploy after do_install
do_deploy() {
	oe_runmake INSTALLDIR=${DEPLOYDIR}/EFI/bin/ install
}

FILES:${PN} = "/EFI/bin/ "

COMPATIBLE_HOST = '(i.86|x86_64)'
