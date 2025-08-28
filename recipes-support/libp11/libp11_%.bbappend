# do_configure() can't autodetect openssl engines dir for native
# builds, set directory here.
EXTRA_OECONF:append:class-native = "--with-enginesdir=${libdir}/engines-3"
