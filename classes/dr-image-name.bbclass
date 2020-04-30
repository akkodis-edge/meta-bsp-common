python () {
    commit = d.getVar('DR_CM_COMMIT', True)
    if commit:
        image_name = '${IMAGE_BASENAME}-${MACHINE}-' + commit + '${IMAGE_VERSION_SUFFIX}'
        d.setVar('IMAGE_NAME', image_name) 
}
