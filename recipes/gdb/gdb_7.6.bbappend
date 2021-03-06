PRINC = "1"
FILESEXTRAPATHS_prepend := "${THISDIR}:"
SRC_URI += "file://gdbinit"

EXTRA_OECONF += "--with-system-gdbinit=/etc/gdbinit"

RDEPENDS_${PN} += "gdb-threads-android"

do_install_append() {
    install -D -m 0644 ../gdbinit ${D}${sysconfdir}/gdbinit
}
