#!/bin/sh

die() {
	echo "$1"
	exit 1
}	

system=${1}
nvram_var=${2}

serial="$(nvram --get ${nvram_var})"
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
	echo "${nvram_var} not in nvram"
	hostnamectl set-hostname ${system} || die "Failed setting hostname to ${system}"
	echo "hostname set to ${system}"
fi

exit 0
