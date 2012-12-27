SUMMARY = "nc"
DESCRIPTION = "OpenBSD netcat"
HOMEPAGE = "http://www.openbsd.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=83e1ec458f1e8449bf05b3e4d9bad2d1"

PR = "r1"

# This is a port of the Fedora 17 nc package, with identical patches.
# The source isn't distributed as an upstream tarball (just as a CVS
# subdirectory) and is BSD-specific, so it's included here exactly as
# unpacked from the F17 source RPM.

SRC_URI = " \
    file://${PN}-${PV}.tar.bz2 \
    file://nc-1.107-linux-ify.patch \
    file://nc-1.107-pollhup.patch \
    file://nc-1.107-udp-stop.patch \
    file://nc-1.107-udp-portscan.patch \
    file://nc-1.107-crlf.patch \
    file://nc-1.107-comma.patch \
    file://nc-1.100-libbsd.patch \
    file://nc-1.107-initialize-range.patch \
    file://nc-1.107-iptos-class.patch"

inherit local-license

DEPENDS = "libbsd"
RDEPENDS_${PN} = "libbsd"

inherit update-alternatives
ALTERNATIVE_${PN} = "nc"
ALTERNATIVE_PRIORITY = "100"

do_unpack[postfuncs] += "finish_unpack"
finish_unpack() {
    # Tarball unpacks under "../nc", move to the proper versioned directory
    mv ../${PN}/* .
}

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -o nc netcat.c atomicio.c socks.c  `pkg-config --cflags --libs libbsd`  -lresolv
}

do_install() {
    install -D -m 755 nc   ${D}${bindir}/nc
    install -D -m 644 nc.1 ${D}${mandir}/man1/nc.1
}
