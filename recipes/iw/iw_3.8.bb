SUMMARY = "A nl80211 based wireless configuration tool"
DESCRIPTION = " \
iw is a new nl80211 based CLI configuration utility for wireless devices. \
Currently you can only use this utility to configure devices which \
use a mac80211 driver as these are the new drivers being written - \
only because most new wireless devices being sold are now SoftMAC. \
"

HOMEPAGE = "http://www.linuxwireless.org/en/users/Documentation/iw/"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=878618a5c4af25e9b93ef0be1a93f774"

PR = "r1"

SRC_URI = "http://wireless.kernel.org/download/iw/${PN}-${PV}.tar.bz2 \
           file://fix-version-script.patch"
SRC_URI[md5sum] = "618ad1106a196fb1c3d827de96da437c"
SRC_URI[sha256sum] = "3dae92ca5989cbc21155941fa01907a5536da3c5f6898642440c61484fc7e0f9"

DEPENDS = "libnl"
RDEPENDS_${PN} = "libnl"

do_compile() {
    CFLAGS="$CFLAGS `pkg-config libnl-3.0 --cflags`"
    LDFLAGS="$LDFLAGS `pkg-config libnl-3.0 --libs`"
    # Not oe_runmake, parallel build issue...
    make
}

do_install() {
    oe_runmake install DESTDIR=${D}
}
