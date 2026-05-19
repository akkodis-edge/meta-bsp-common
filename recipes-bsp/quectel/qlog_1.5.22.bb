DESCRIPTION = "Quectel modem log utility"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit  cmake

SRCREV ?= "983f4cff7386be273cc10cdc3d676ecfe26e433e"
SRC_URI = "git://git@github.com/akkodis-edge/qlog-mirror.git;protocol=ssh;branch=v1.5.22"
