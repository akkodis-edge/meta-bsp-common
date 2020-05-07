#!/bin/sh

die() {
	echo "$1"
	exit 1
}	

system=${1}

serial="$(nvram get SYS_SERIALNUMBER)"
if [ $? -eq 0 ]; then
	current="$(hostname)" || die "Failed getting current hostname"
	name="${system}-${serial}"
	if  [ "$current" != "$name" ]; then
		hostnamectl set-hostname "${name}" || die "Failed setting hostname to ${name}"
		echo "hostname set to ${name}"
	else
		echo "hostname already set to ${name}"
	fi
else
	echo "SYS_SERIALNUMBER not in nvram"
	hostnamectl set-hostname ${system} || die "Failed setting hostname to ${system}"
	echo "hostname set to ${system}"
fi

exit 0
