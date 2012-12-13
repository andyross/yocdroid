SUMMARY = "tcpdump"
DESCRIPTION = "The tcpdump network monitoring tool"
HOMEPAGE = "http://www.tcpdump.org/"
LICENSE = "BSD"

DEPENDS = "libpcap"

PR = "r1"

SRC_URI = "http://www.tcpdump.org/release/tcpdump-${PV}.tar.gz"

inherit autotools



