require efitools.inc

DEPENDS += "gnu-efi openssl"

EXTRA_OEMAKE:append = " \
    INCDIR_PREFIX='${STAGING_DIR_TARGET}' \
    CRTPATH_PREFIX='${STAGING_DIR_TARGET}' \
"
