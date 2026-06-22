DEPENDS += "libp11-native p11-kit-native sbsigntool-native gnutls-native"

do_sbsign[vardeps] += "P11_KIT_SERVER_ADDRESS UEFI_SIGNING_PKCS11_URI"

do_sbsign() {
	# Instruct openssl pkcs11 engine libp11 to use p11-kit client module
	export PKCS11_MODULE_PATH="${STAGING_LIBDIR_NATIVE}/pkcs11/p11-kit-client.so"

	# Set p11-kit server address for client p11-kit client module
	export P11_KIT_SERVER_ADDRESS="${P11_KIT_SERVER_ADDRESS}"

	export OPENSSL_MODULES="${STAGING_LIBDIR_NATIVE}/ossl-modules"
	export OPENSSL_ENGINES="${STAGING_LIBDIR_NATIVE}/engines-3"
	export OPENSSL_CONF="${STAGING_LIBDIR_NATIVE}/ssl-3/openssl.cnf"
	export SSL_CERT_DIR="${STAGING_LIBDIR_NATIVE}/ssl-3/certs"
	export SSL_CERT_FILE="${STAGING_LIBDIR_NATIVE}/ssl-3/cert.pem"

	# Retrieve certificate, sbsign can't fetch from pkcs11 engine
	p11tool --provider "${PKCS11_MODULE_PATH}" --export --outfile "${B}/sbsign.cert" "${UEFI_SIGNING_PKCS11_URI};type=cert"

	# Sign file
	sbsign --engine pkcs11 --key "${UEFI_SIGNING_PKCS11_URI}" --cert "${B}/sbsign.cert" --output "${SECURE_BOOT_SIGNING_FILE}.signed" "${SECURE_BOOT_SIGNING_FILE}"
}

EXPORT_FUNCTIONS do_sbsign
