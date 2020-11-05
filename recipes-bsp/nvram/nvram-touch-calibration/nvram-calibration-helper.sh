#!/bin/sh

# The arguments Weston gives us:
SYSPATH="$1"
MATRIX="$2 $3 $4 $5 $6 $7"

# Set calibration to nvram
NVRAM_SYSTEM_UNLOCK=16440 /usr/bin/nvram --sys set SYS_TOUCHSCREEN_CALIBRATION "$MATRIX"
exit
