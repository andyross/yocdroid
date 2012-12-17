SUMMARY = "tcpdump"
DESCRIPTION = "The tcpdump network monitoring tool"
HOMEPAGE = "http://www.tcpdump.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d4b0366557951c84a94fabe3529f867"

PR = "r1"

SRC_URI = "http://www.tcpdump.org/release/tcpdump-${PV}.tar.gz \
           file://remove-dlpi.patch"
SRC_URI[md5sum] = "a3fe4d30ac85ff5467c889ff46b7e1e8"
SRC_URI[sha256sum] = "efd08b610210d39977ec3175fa82dad9fbd33587930081be2a905a712dba4286"

RDEPENDS_${PN} = "libpcap"

inherit autotools

do_configure() {
    autoconf
    oe_runconf
}

do_install_append() {
    # Needlessly installs a /usr/sbin/tcpdump.4.3.0
    rm -f ${D}{sbindir}/${PN}.${PV}
}
