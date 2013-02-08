PRINC = "1"

# The bluez4 package includes a udev rule which uses a binary that we
# don't want, because it's library dependencies pulls the whole udev
# subsystem into the build (yocdroid *really* can't be running udevd,
# that's init/ueventd's job on android).  Split it out into a separate
# package.

PACKAGES =+ "${PN}-udev-dbg ${PN}-udev"
FILES_${PN}-udev = "${base_libdir}/udev"
FILES_${PN}-udev-dbg = "${base_libdir}/udev/.debug"

