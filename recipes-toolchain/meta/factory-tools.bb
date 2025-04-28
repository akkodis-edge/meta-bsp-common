SUMMARY = "Package for building an installable with factory tools"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit populate_sdk

SDK_NAME = "${MACHINE}-${PN}-${SDK_ARCH}"
SDK_TITLE = "Akkodis Edge Sweden AB ${MACHINE} factory tools"
SKD_VERSION = "1.0"

TOOLCHAIN_TARGET_TASK = ""

TOOLCHAIN_HOST_TASK:imx += " \
	meta-environment-${MACHINE} \
	nativesdk-packagegroup-factory-imx-host \
"
