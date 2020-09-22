#!/bin/sh

WORKDIR="NONE"

cleanup() {
	if [ "${WORKDIR}" != "NONE" ]; then
		rm -rf "${WORKDIR}"
	fi
	WORKDIR="NONE"
}

die() {
	>&2 echo "${1}"
	cleanup
	exit 1
}

print_usage() {
    echo "Usage: ${1} INVM MAC"
    echo
    echo "INVM    iNVM flash file in hex format"
    echo "MAC     MAC address on capital letters and format XXXXXXXXXXXX"
	echo
    exit 0
}

if [ "${#}" -ne "2" ]; then
	print_usage "$(basename ${0})"
fi
invm_path="${1}"
invm="$(basename ${invm_path})"
mac="${2}"

WORKDIR="$(mktemp -d)"

echo "mac length ${#mac}: ${mac}"
if [ ${#mac} -ne 12 ]; then
	die "MAC invalid"
fi

mac1="$(echo ${mac} | cut -c 1-2)" || die "Failed extracting mac"
mac2="$(echo ${mac} | cut -c 3-4)" || die "Failed extracting mac"
mac3="$(echo ${mac} | cut -c 5-6)" || die "Failed extracting mac"
mac4="$(echo ${mac} | cut -c 7-8)" || die "Failed extracting mac"
mac5="$(echo ${mac} | cut -c 9-10)" || die "Failed extracting mac"
mac6="$(echo ${mac} | cut -c 11-12)" || die "Failed extracting mac"

cp -v "${invm_path}" "${WORKDIR}"/ || die "Failed copying iNVM to WORKDIR"
sed -i "s/XXXX/${mac2}${mac1}/" "${WORKDIR}/${invm}" || die "Failed setting MAC"
sed -i "s/YYYY/${mac4}${mac3}/" "${WORKDIR}/${invm}" || die "Failed setting MAC"
sed -i "s/ZZZZ/${mac6}${mac5}/" "${WORKDIR}/${invm}" || die "Failed setting MAC"

echo "Programming iNVM:"
cat "${WORKDIR}/${invm}" || die "Failed printing flash file"
echo

cd "${WORKDIR}" || die "Failed changing to workdir"
EepromAccessTool -nic=1 -dump || die "Failed reading iNVM"
zerocount="$(grep -o 00000000 I210NIC1.otp | wc -l)"
# Empty iNVM should have 62 zero DWORDS
if [ "${zerocount}" -ne 62 ]; then
	die "iNVM not empty"
fi

EepromAccessTool -nic=1 -f="${WORKDIR}/${invm}" || die "Failed flashing iNVM"

cleanup
exit 0
