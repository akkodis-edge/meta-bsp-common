#
# Generate config files for imx_usb loader
#

gen_imx_usb[vardeps] += " \
                        IMX_USB_RAW_VID \
                        IMX_USB_RAW_PID \
                        IMX_USB_VID \
                        IMX_USB_PID \
                        IMX_USB_ZIMAGE_LOADADDR \
                        IMX_USB_DTB \
                        IMX_USB_DTB_LOADADDR \
                        IMX_USB_INITRD_LOADADDR \
                       "
                       
IMX_USB_DIR ?= "${DEPLOYDIR}"
IMX_USB_INITRD ?= "factory-image-${MACHINE}.cpio.gz.u-boot"
IMX_USB_UBOOT ?= "u-boot-ivt.img-${UBOOT_CONFIG}"
IMX_USB_SPL ?= "SPL-${UBOOT_CONFIG}"

do_deploy_append() {
	install -d ${IMX_USB_DIR}
	
	install -m 0644 /dev/null ${IMX_USB_DIR}/imx_usb.conf
	cat > ${IMX_USB_DIR}/imx_usb.conf << "EOF"
#vid:pid, config_file_spl, vid:pid, config_file_uboot
${IMX_USB_RAW_VID}:${IMX_USB_RAW_PID}, mx6_usb_rom.conf, ${IMX_USB_VID}:${IMX_USB_PID}, mx6_usb_sdp_spl.conf
EOF

	install -m 0644 /dev/null ${IMX_USB_DIR}/mx6_usb_rom.conf
	cat > ${IMX_USB_DIR}/mx6_usb_rom.conf << "EOF"
mx6_qsb
hid,1024,0x910000,0x10000000,1G,0x00900000,0x40000
${IMX_USB_SPL}:jump header
EOF
	
	install -m 0644 /dev/null ${IMX_USB_DIR}/mx6_usb_sdp_spl.conf
	cat > ${IMX_USB_DIR}/mx6_usb_sdp_spl.conf << "EOF"
mx6_spl_sdp
hid,uboot_header,1024,0x10000000,1G,0x00907000,0x31000
zImage:load ${IMX_USB_ZIMAGE_LOADADDR}
${IMX_USB_DTB}:load ${IMX_USB_DTB_LOADADDR}
${IMX_USB_INITRD}:load ${IMX_USB_INITRD_LOADADDR}
${IMX_USB_UBOOT}:load,jump header
EOF
}
