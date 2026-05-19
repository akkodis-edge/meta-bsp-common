DESCRIPTION = "Quectel modem firmware update utility"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit  cmake

SRCREV ?= "d4a41863f2ec621b0fc73bcf7a2aa9428f746836"
SRC_URI = "git://git@github.com/akkodis-edge/qfirehose-mirror.git;protocol=ssh;branch=v1.4.17"
