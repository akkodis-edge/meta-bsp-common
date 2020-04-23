DESCRIPTION = "Auto exports gpios from of_node user-gpios"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRCREV ?= "cceafd26818c5415707075eafc76a534f829c865"
SRC_URI = "git://git@github.com/data-respons-solutions/kernel-module-user-gpios.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

KERNEL_MODULE_AUTOLOAD += "user-gpios"
