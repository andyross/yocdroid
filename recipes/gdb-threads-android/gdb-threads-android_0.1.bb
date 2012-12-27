SUMMARY = "gdb-threads-android"
DESCRIPTION = "Android-compatible libthread_db.so.1 for gdb thread debugging"
HOMEPAGE = "http://android.google.com"

# See the note in files/COPYING.
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1dfe44ec4ff387f12e822d9bbb603f2a"

PR = "r1"

SRC_URI = "file://libthread_db.c \
           file://gdb_thread_db.h"

inherit local-license

# The automatic -dbg package generation only works the the default
# directories, here we need to put the contents of the .debug
# directory into the -dbg package explicitly.
FILES_${PN} = "${libdir}/android ${libdir}/android/libpthread_db.so.1"
FILES_${PN}-dbg = "${libdir}/android/.debug/*"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -I.. -shared -fPIC -Wl,-soname,libthread_db.so.1 -o libthread_db.so.1 ../libthread_db.c
}

do_install() {
    install -D -m 755 libthread_db.so.1 ${D}${libdir}/android/libthread_db.so.1
}
