SUMMARY = "Userland logical volume management tools "

DESCRIPTION = " \
LVM2 includes all of the support for handling read/write operations on \
physical volumes (hard disks, RAID-Systems, magneto optical, etc., \
multiple devices (MD), see mdadd(8) or even loop devices, see \
losetup(8)), creating volume groups (kind of virtual disks) from one \
or more physical volumes and creating one or more logical volumes \
(kind of logical partitions) in volume groups."

HOMEPAGE = "http://sourceware.org/lvm2/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PR = "r1"

SRC_URI = "ftp://sources.redhat.com/pub/lvm2/LVM2.${PV}.tgz"
SRC_URI[md5sum] = "1ce5b7f9981e1d02dfd1d3857c8d9fbe"
SRC_URI[sha256sum] = "71030a58fef7e00d82ca4144334548e46aad24551a3cfbe7c3059b1bd137d864"

inherit autotools

PACKAGES =+ "lvm2-dm lvm2-dm-dev"

FILES_lvm2-dm = "${sbindir}/dmsetup ${libdir}/libdevmapper.so.*"
FILES_lvm2-dm-dev = "${libdir}/libdevmapper.so ${includedir}/libdevmapper.h"

do_unpack[postfuncs] += "fix_unpack_dir"
fix_unpack_dir() {
    mv ../LVM2.${PV}/* .
}
