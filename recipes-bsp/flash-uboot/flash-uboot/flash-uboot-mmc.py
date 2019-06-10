#!/usr/bin/env python3

import sys, errno, os, hashlib, string, argparse, time

is_spl = False;
emmc_part = '0'
offsets = [1024, 1024 * 256]

  
def get_mmc_hash(dev, offset, length):
    device = os.path.join("/dev", dev)
    with open(device, "rb") as f:
        f.seek(offset)
        bytes_read = f.read(length);
        h = hashlib.md5()
        h.update(bytes_read);
        return h.hexdigest()


def check_same(dev, fnames):
    same = 1;
    n = 0;
    for fname in fnames:
        statf = os.stat(fname)
        with open(fname, "rb") as f:
            file_bytes = f.read(statf.st_size)
            md5sum = hashlib.md5()
            md5sum.update(file_bytes)
            mmc_md5 = get_mmc_hash(dev, offsets[n], statf.st_size);
            if (md5sum.hexdigest() != mmc_md5):
                same = 0;
                print (fname.ljust(20), mmc_md5, " => ", md5sum.hexdigest())
            n = n + 1
    
    return same;


def enable_write(bp):
    with open(os.path.join(bp, "force_ro"), "r+") as f:
        f.write("0")
        time.sleep(0.5)
    if (args.debug):
        with open(os.path.join(bp, "force_ro"), "r+") as f:
            print("Readback force_ro as", f.readline())

def disable_write(bp):
    with open(os.path.join(bp, "force_ro"), "r+") as f:
        f.write("1")


def write_emmc(dev, fnames):
    device = os.path.join("/dev", dev)
    fdev = open(device, "r+b")
    n = 0
    for fname in fnames:
        statf = os.stat(fname)
        with open(fname, "rb") as f:
            emmc_bytes = f.read(statf.st_size)
            fdev.seek(offsets[n])
            fdev.write(emmc_bytes)
        n = n + 1
      
    fdev.flush()
    fdev.close()


def set_led(led, on):
    p = "/sys/class/leds/" + led
    if (os.path.exists(p)):
        with open(p + "/brightness", "r+") as f:
            if (on):
                f.write("1")
            else:
                f.write("0")
    else:
        print ("LED", led, "does not exist")


# Program starts here
result = 0

parser = argparse.ArgumentParser(description='Parse FLASH programmer', prog='lm-flash-uboot')
parser.add_argument('--spl')
parser.add_argument('uboot', help='The main u-boot file (u-boot.im[xg])')
parser.add_argument('--partition')
parser.add_argument('--debug', action='store_true')

args = parser.parse_args()

if (args.partition):
    emmc_part = args.partition

if (args.spl):
    is_spl = True
    file_names = [args.spl, args.uboot]
else:
    file_names = [args.uboot]
    
dev_name = "mmcblk0boot" + emmc_part

block_path = os.path.join("/sys/block", dev_name)
print ("Trying", block_path)
if (not os.path.exists(block_path)):
    dev_name = "mmcblk1boot" + emmc_part
    block_path = os.path.join("/sys/block", dev_name)
    print ("Trying", block_path)
    if (not os.path.exists(block_path)):
        print ("No eMMC found - exit")
        sys.exit(1)

print ("Prog", file_names, "to", dev_name)
  
try:
    set_led("status-red", False)
    set_led("status-green", False)
  
  # Check need for update
    t = check_same(dev_name, file_names)
    if (t == 0):
        print ("NEED TO FLASH")
        enable_write(block_path)
        write_emmc(dev_name, file_names)
        disable_write(block_path)
        t = check_same(dev_name, file_names)
        if (t == 0):
            print ("Programming of", dev_name, "failed")
            set_led("status-red", True)
            sys.exit(1)
        else:
            print ("Flash succeded")
            set_led("status-green", True)
    else:
        print ("Flash is up to date, no programming")
    set_led("status-green", True)
    
except IOError as e:
    print ("IO Error", os.strerror(e.errno))
    result = e.errno
    set_led("status-red", True)
except OSError as e:
    print ("OS Error", os.strerror(e.errno))
    result = e.errno
    set_led("status-red", True)

finally:
    disable_write(block_path)

sys.exit(result)

