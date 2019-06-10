## How To Use

### Factory image
Start off by building factory image
```
$ MACHINE=[YOUR-FACTORY-MACHINE] bitbake factory-image
```


Connet to your machine by USB OTG and run imx_usb tool.
```
$ cd deploy/images/[YOUR-FACTORY-MACHINE]/imx_usb/
$ sudo imx_usb
```

Wait for factory installation to complete, firmware is now installed.
