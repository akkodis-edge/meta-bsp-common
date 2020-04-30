inherit rootfs_${IMAGE_PKGTYPE}

ROOTFS_POSTPROCESS_COMMAND_append = " set_image_info;"
DR_BUILD_PLAN ?= "development-build"
DR_BUILD_NO ?= "none"
DR_CM_COMMIT ?= "HEAD"

IMAGE_VERSION_SUFFIX = "-${DR_CM_COMMIT}-${DATETIME}"

set_image_info () {
    echo "${DISTRO_NAME} ${DISTRO_VERSION}" > ${IMAGE_ROOTFS}${sysconfdir}/distro_info
    echo ${IMAGE_NAME} > ${IMAGE_ROOTFS}${sysconfdir}/image_info
    echo "BUILD_PLAN: ${DR_BUILD_PLAN}" >> ${IMAGE_ROOTFS}${sysconfdir}/image_info
    echo "BUILD_NO: ${DR_BUILD_NO}" >> ${IMAGE_ROOTFS}${sysconfdir}/image_info
    echo "CM_COMMIT_ID: ${DR_CM_COMMIT}" >> ${IMAGE_ROOTFS}${sysconfdir}/image_info
    chmod 0444 ${IMAGE_ROOTFS}${sysconfdir}/distro_info
    chmod 0444 ${IMAGE_ROOTFS}${sysconfdir}/image_info
}
