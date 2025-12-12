DESCRIPTION = "Quectel modem firmware update utility"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit  cmake

SRCREV ?= "bf11e24951cd41f7d7627e5f2c8f6688b2bab86f"
SRC_URI = "git://git@github.com/akkodis-edge/qfirehose-mirror.git;protocol=ssh;branch=v1.4.17"
