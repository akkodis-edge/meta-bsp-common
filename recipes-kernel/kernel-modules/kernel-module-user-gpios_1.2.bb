DESCRIPTION = "Auto exports gpios from of_node user-gpios"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRCREV ?= "eeb954ed5a06016afe7a3409ee5e79b367719965"
SRC_URI = "git://git@github.com/data-respons-solutions/kernel-module-user-gpios.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

KERNEL_MODULE_AUTOLOAD += "user-gpios"
