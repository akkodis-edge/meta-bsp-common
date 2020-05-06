DESCRIPTION = "Intel utility for flashing I210, I211 and X550 NVM"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

SRC_URI[sha256sum] = "3f8982b2e05854f2943e434577aa920e079709bc6edac337346754ab2524ad6d"
SRC_URI = "sftp://ftp.datarespons.com/intel_confidential/eepromaccesstool-0-7-9.zip"

S = "${WORKDIR}/eepromaccesstool-0-7-9"

do_configure() {
	sed -i 's/CC=.*/#/' Makefile
	sed -i 's/CFLAGS=.*/#/' Makefile 
	sed -i 's/LDFLAGS=.*/#/' Makefile 
}

do_install () {
	install -d ${D}/usr/sbin
	install -d ${D}/lib/firmware/intel
	install -m 0755 ${S}/EepromAccessTool ${D}/usr/sbin
	for f in  ${S}/hex_files/*.HEX; do
		install -m 0644 ${f} ${D}/lib/firmware/intel
	done
}

FILES_${PN} += "/usr/sbin/EepromAccessTool /lib/firmware/intel/*.HEX"