SUMMARY = "libbsd"
DESCRIPTION = "BSD compatibility library"
HOMEPAGE = "http://libbsd.freedesktop.org"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=b569331c794e16df30c160bc899a23a8"

PR = "r1"

SRC_URI = "http://libbsd.freedesktop.org/releases/libbsd-${PV}.tar.gz"
SRC_URI[md5sum] = "591fc9de4ca22f78cf87a94e648a92f4"
SRC_URI[sha256sum] = "922b4885e0ccfd64b92fcacdb3fba18024fcab1e0c1b073f5ec0fe76388cbfdc"

inherit autotools
