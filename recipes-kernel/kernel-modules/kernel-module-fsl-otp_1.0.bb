DESCRIPTION = "nxp fsl otp fuse sysfs kernel module"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRCREV ?= "43420b806c0bccfca1fbbb836d6646d08ca88642"
SRC_URI = "git://git@github.com/data-respons-solutions/kernel-module-fsl-otp.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

KERNEL_MODULE_AUTOLOAD += "fsl_otp"

COMPATIBLE_MACHINE = "(imx|use-mainline-bsp)"
