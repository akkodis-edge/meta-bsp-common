ROOTFS_POSTPROCESS_COMMAND_append += " pendrive_autorun;"

PENDRIVE_PATH ??= "/mnt/pendrive"

IMAGE_INSTALL_append += " login-autorun"

pendrive_autorun() {
	ln -s ${PENDRIVE_PATH}/autorun ${IMAGE_ROOTFS}/usr/bin/autorun
}