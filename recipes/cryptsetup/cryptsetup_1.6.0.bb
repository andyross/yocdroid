SUMMARY = "A utility for setting up encrypted disks"
DESCRIPTION = "The cryptsetup package contains a utility for setting up \
               disk encryption using dm-crypt kernel module."
HOMEPAGE = "http://cryptsetup.googlecode.com/"
LICENSE = "GPLv2 & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=32107dd283b1dfeb66c9b3e6be312326"

PR = "r1"

SRC_URI = "http://cryptsetup.googlecode.com/files/cryptsetup-1.6.0.tar.bz2"
SRC_URI[md5sum] = "99002ac59a65ea371e7a98200943cb80"
SRC_URI[sha256sum] = "dd9686fce5d3276b2eb2ac40d513a9b64850af8fff881442f2cfe87257ba2406"

inherit autotools gettext

