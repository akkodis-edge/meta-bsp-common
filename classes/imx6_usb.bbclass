#
# Generate config files for imx_usb loader
# IMX_USB_RAW_VID may be space separated list of VID to support. For example "0x0061 0x0054".
#

do_imx6_usb[vardeps] += " \
                        IMX6_USB_RAW_VID \
                        IMX6_USB_RAW_PID \
                        IMX6_USB_ZIMAGE_LOADADDR \
                        IMX6_USB_DTB \
                        IMX6_USB_DTB_LOADADDR \
                        IMX6_USB_INITRD_LOADADDR \
                       "
                       
IMX6_USB_DIR ?= "${DEPLOYDIR}"
IMX6_USB_ZIMAGE ?= "${KERNEL_IMAGETYPE}"
IMX6_USB_INITRD ?= "factory-initrd-${MACHINE}.${INITRAMFS_FSTYPES}"
IMX6_USB_UBOOT ?= "${UBOOT_BINARY}-${UBOOT_CONFIG}"
IMX6_USB_SPL ?= "${SPL_BINARY}-${UBOOT_CONFIG}"
IMX6_USB_VID ?= "${IMX6_USB_RAW_VID}"
IMX6_USB_PID ?= "${IMX6_USB_RAW_PID}"

do_imx6_usb() {
	install -d ${IMX6_USB_DIR}
	install -m 0644 /dev/null ${IMX6_USB_DIR}/imx_usb.conf
	
	if [ "${SPL_BINARY}" != "" ]; then
		echo "#vid:pid, config_file_spl, vid:pid, config_file_uboot" >> ${IMX6_USB_DIR}/imx_usb.conf
		for i in ${IMX6_USB_RAW_PID}; do
			echo "${IMX6_USB_RAW_VID}:${i}, mx6_usb_rom.conf, ${IMX6_USB_VID}:${IMX6_USB_PID}, mx6_usb_sdp_spl.conf" >> ${IMX6_USB_DIR}/imx_usb.conf
		done

		install -m 0644 /dev/null ${IMX6_USB_DIR}/mx6_usb_rom.conf
		cat > ${IMX6_USB_DIR}/mx6_usb_rom.conf << "EOF"
mx6_qsb
hid,1024,0x910000,0x10000000,1G,0x00900000,0x40000
${IMX6_USB_SPL}:jump header
EOF
	
		install -m 0644 /dev/null ${IMX6_USB_DIR}/mx6_usb_sdp_spl.conf
		cat > ${IMX6_USB_DIR}/mx6_usb_sdp_spl.conf << "EOF"
mx6_spl_sdp
hid,uboot_header,1024,0x10000000,1G,0x00907000,0x31000
${IMX6_USB_ZIMAGE}:load ${IMX6_USB_ZIMAGE_LOADADDR}
${IMX6_USB_DTB}:load ${IMX6_USB_DTB_LOADADDR}
${IMX6_USB_INITRD}:load ${IMX6_USB_INITRD_LOADADDR}
${IMX6_USB_UBOOT}:load,jump header
EOF
	else
		echo "#vid:pid, config_file_uboot" >> ${IMX6_USB_DIR}/imx_usb.conf
		for i in ${IMX6_USB_RAW_PID}; do
			echo "${IMX6_USB_RAW_VID}:${i}, mx6_usb_work.conf" >> ${IMX6_USB_DIR}/imx_usb.conf
		done
		
		install -m 0644 /dev/null ${IMX6_USB_DIR}/mx6_usb_work.conf
		cat > ${IMX6_USB_DIR}/mx6_usb_work.conf << "EOF"
mx6_qsb
hid,1024,0x910000,0x10000000,1G,0x00900000,0x40000
${IMX6_USB_UBOOT}:dcd
${IMX6_USB_ZIMAGE}:load ${IMX6_USB_ZIMAGE_LOADADDR}
${IMX6_USB_DTB}:load ${IMX6_USB_DTB_LOADADDR}
${IMX6_USB_INITRD}:load ${IMX6_USB_INITRD_LOADADDR}
${IMX6_USB_UBOOT}:clear_dcd,load,jump header
EOF
	fi
}

EXPORT_FUNCTIONS do_imx6_usb
