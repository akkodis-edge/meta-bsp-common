DESCRIPTION = "Quectel modem log utility"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit  cmake

SRCREV ?= "a17bc3ad94a7b253de62ce88e5efa66ebb40f52a"
SRC_URI = "git://git@github.com/data-respons-solutions/qlog-mirror.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "v1.5.22"

S = "${WORKDIR}/git"
