DESCRIPTION = "Exec /usr/bin/autorun on login if available"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://99-autorun.sh"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install() {
    install -d ${D}${sysconfdir}/profile.d/
    install -m 0755 ${S}/99-autorun.sh ${D}${sysconfdir}/profile.d/
}
