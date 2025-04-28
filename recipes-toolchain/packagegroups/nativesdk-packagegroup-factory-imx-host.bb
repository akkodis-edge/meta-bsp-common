DESCRIPTION = "Host factory tools"
LICENSE = "MIT"

inherit packagegroup
inherit_defer nativesdk

RDEPENDS:${PN} += " \
    nativesdk-imx-usb-loader \
"
