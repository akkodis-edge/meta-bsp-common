#!/bin/sh

die() {
	echo "$1"
	exit 1
}

timeout=60
start="$(date +%s.%2N)" || die "Failed getting time"
while ! nvram >/dev/null; do
	now="$(date +%s.%2N)" || die "Failed getting time"
	elapsed="$(echo "(${now}-${start})" | bc -l | sed  's/^\./0./')" || die "Failed calculating elapsed time"
	[ "$(echo "(${elapsed} < ${timeout})" | bc -l)" -eq 1 ] || die "Timeout"
	sleep 1
done

exit 0
