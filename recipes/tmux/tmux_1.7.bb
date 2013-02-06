SUMMARY = "A terminal multiplexer"
DESCRIPTION = " \
tmux is a 'terminal multiplexer.'  It enables a number of terminals \
(or windows) to be accessed and controlled from a single terminal. \
tmux is intended to be a simple, modern, BSD-licensed alternative to \
programs such as GNU Screen. "
HOMEPAGE = "http://tmux.sourceforge.net"
LICENSE = "ISC & BSD"
LIC_FILES_CHKSUM = "file://NOTES;md5=78cc02844d8662c224e979faff6d8501"

PR = "r1"

SRC_URI = "http://downloads.sourceforge.net/${PN}/${PN}-${PV}.tar.gz \
           file://no-usr-local.patch"
SRC_URI[md5sum] = "2c48fb9beb22eedba7a5de3b78dd0c03"
SRC_URI[sha256sum] = "68346bda11cf7d86591e663b94b98576332ac88c2890df26acb080f4440f9e7b"

DEPENDS = "libevent"
RDEPENDS_${PN} = "libevent"

inherit autotools
