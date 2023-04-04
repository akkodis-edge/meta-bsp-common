DESCRIPTION = "Build tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV ?= "b470fe7ebb1dd439f35d3cf1d4de3daf7c53a162"
SRC_URI = "git://git@github.com/data-respons-solutions/build-tools.git;protocol=ssh;branch=${BRANCH}"
BRANCH ?= "master"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/drs-deploy.sh ${D}${bindir}/drs-deploy
}

PACKAGES = "${PN}-drs-deploy"
FILES_${PN}-drs-deploy = "${bindir}/drs-deploy"
RDEPENDS_${PN}-drs-deploy += "openssh-sftp ncftp"
