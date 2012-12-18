SUMMARY = "SSHFS"
DESCRIPTION = "FUSE filesystem over SSH"
HOMEPAGE = "http://fuse.sourceforge.net/sshfs.html"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PR = "r1"

SRC_URI = "http://downloads.sourceforge.net/project/fuse/${PN}/${PV}/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "3c7c3647c52ce84d09486f1da3a3ce24"
SRC_URI[sha256sum] = "3c93ba8522568093c94ff9c5a3763929380dd229365d905769ff82475d774dd1"

RDEPENDS_${PN} = "openssh-ssh"

inherit autotools
