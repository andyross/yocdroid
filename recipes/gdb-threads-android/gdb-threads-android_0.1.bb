SUMMARY = "gdb-threads-android"
DESCRIPTION = "Android-compatible libthread_db.so.1 for gdb thread debugging"
HOMEPAGE = "http://android.google.com"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

PR = "r1"

SRC_URI = "file://libthread_db.c \
           file://thread_db.h"

inherit local-license

# The automatic -dbg package generation only works for the default
# build directory layout, here we need to put the contents of the
# .debug directory and the source files into the -dbg package
# explicitly.
FILES_${PN} = "${libdir}/android ${libdir}/android/libpthread_db.so.1"
FILES_${PN}-dbg = "${libdir}/android/.debug /usr/src/debug/${PN}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -I.. -shared -fPIC -Wl,-soname,libthread_db.so.1 -o libthread_db.so.1 ../libthread_db.c
}

do_install() {
    install -D -m 755 libthread_db.so.1 ${D}${libdir}/android/libthread_db.so.1
}
