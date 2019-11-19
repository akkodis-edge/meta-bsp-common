SUMMARY = "user-gpios kernel module"
DESCRIPTION = "Auto exports gpios from of_node user-gpios"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI = " \
	file://Makefile \
	file://user-gpios.c \
"

S = "${WORKDIR}"

KERNEL_MODULE_AUTOLOAD += "user-gpios"

#RPROVIDES_${PN} += "kernel-module-user-gpios"
