DESCRIPTION = "Utility to write calibration to nvram"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = 	"file://nvram-calibration-helper.sh file://nvram-calibration-export.sh"
S = "${UNPACKDIR}"

# Currently only wayland/weston supported
# Note weston.ini [libinput] section needs:
# touchscreen_calibrator=true
# calibration_helper=/usb/bin/nvram-calibration-helper.sh
# The calibration should be set on boot by udev rule, for example:
# ACTION=="add|change", KERNEL=="event[0-9]*", ATTRS{name}=="ILI210x Touchscreen", IMPORT{program}="/usr/bin/nvram-calibration-export.sh"

inherit features_check
REQUIRED_DISTRO_FEATURES = "wayland"

RDEPENDS:${PN} = "nvram"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/nvram-calibration-helper.sh ${D}${bindir}/nvram-calibration-helper
    install -m 0755 ${S}/nvram-calibration-export.sh ${D}${bindir}/nvram-calibration-export
}
