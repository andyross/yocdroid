SUMMARY = "FUSE"
DESCRIPTION = "Filesystems in Userspace"
HOMEPAGE = "http://fuse.sourceforge.net/"
LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"

PR = "r1"

SRC_URI = "http://downloads.sourceforge.net/${PN}/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "7d80d0dc9cc2b9199a0c53787c151205"
SRC_URI[sha256sum] = "81a728fb3f87da33063068735e2fb7e2cd89df207d32687d3d3278385279cefc"

inherit autotools

do_install_append() {
    rm -rf ${D}/dev
}
