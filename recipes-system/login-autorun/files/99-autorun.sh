#!/bin/sh

echo ""
if [ -x /usr/bin/autorun ]; then
	/usr/bin/autorun
else
	echo "/usr/bin/autorun: not available"
fi
echo ""
