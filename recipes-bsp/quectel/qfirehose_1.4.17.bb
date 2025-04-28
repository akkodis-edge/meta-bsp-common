DESCRIPTION = "Quectel modem firmware update utility"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit  cmake

SRCREV ?= "01bf801e4c01435045f9d345ebd3afbb3d3d4b97"
SRC_URI = "git://git@github.com/akkodis-edge/qfirehose-mirror.git;protocol=ssh;branch=v1.4.17"

S = "${WORKDIR}/git"
