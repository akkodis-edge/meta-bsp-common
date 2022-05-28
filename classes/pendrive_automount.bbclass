ROOTFS_POSTPROCESS_COMMAND:append = " pendrive_automount;"

PENDRIVE_LABEL ??= "TESTDRIVE"
PENDRIVE_FS ??= "vfat,ext4"
PENDRIVE_PATH ??= "/mnt/pendrive"

pendrive_automount() {
	echo "LABEL=${PENDRIVE_LABEL}      ${PENDRIVE_PATH}        ${PENDRIVE_FS}       defaults,ro,nofail           0  0" >> ${IMAGE_ROOTFS}/etc/fstab
}
