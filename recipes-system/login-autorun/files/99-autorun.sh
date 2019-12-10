#!/bin/sh

TIMEOUT=3

echo ""
if [ -x /usr/bin/autorun ]; then
	if read -r -s -n 1 -t ${TIMEOUT} -p "Press any key to abort autorun in the next ${TIMEOUT} seconds... " key; then
		echo "aborted"
	else
		/usr/bin/autorun
	fi
else
	echo "/usr/bin/autorun: not available"
fi
echo ""
