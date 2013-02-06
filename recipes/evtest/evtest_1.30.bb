SUMMARY = "Event device test program"
DESCRIPTION = " \
evtest is a simple utility to query information about input devices \
and watch the event stream generated by input devices."
HOMEPAGE = "http://cgit.freedesktop.org/evtest"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

PR = "r1"

SRC_URI = "http://cgit.freedesktop.org/${PN}/snapshot/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "e6ae4022158e87714677c46e45a2628b"
SRC_URI[sha256sum] = "517ecf3507959bd5c9eefbb2caeb0c17bc48b571e2b24ba3cb9beb2bbd3d621f"

inherit autotools