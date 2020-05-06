DESCRIPTION = "Auto exports gpios from of_node user-gpios"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRCREV ?= "04201ed866c18c27d6bcdd9e8ae24cd8ee24577d"
SRC_URI = "git://git@github.com/data-respons-solutions/kernel-module-user-gpios.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

KERNEL_MODULE_AUTOLOAD += "user-gpios"
