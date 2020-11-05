#!/bin/sh

set -e

MATRIX="$(/usr/bin/nvram get SYS_TOUCHSCREEN_CALIBRATION)"
echo "LIBINPUT_CALIBRATION_MATRIX=\"$MATRIX\""

exit
