DESCRIPTION = "Quectel modem log utility"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit  cmake

SRCREV ?= "647a07cfce643d1bb100ba4a0d4c2e65502564c9"
SRC_URI = "git://git@github.com/akkodis-edge/qlog-mirror.git;protocol=ssh;branch=v1.5.22"
