LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

PR = "r1"

SRC_URI = "file://yocdroid-start \
           file://yocdroid-run \
           file://chroot-static.c \
           file://yocdroid.sh \
           file://app_process \
           file://COPYING"

do_configure() {
    # Needed to get the license file check to work.  Not sure what the
    # right thing to do is with a license in local files...
    cp ../COPYING .
}

do_compile() {
    ${CC} -Os -static -o chroot-static ../chroot-static.c
}

do_install() {
    # FIXME: why is the ../ needed?  What's the proper way to find SRC_URI components?
    install -D -m 0755 ../app_process    ${D}${base_bindir}/app_process
    install -D -m 0755 chroot-static     ${D}${base_sbindir}/chroot-static
    install -D -m 0755 ../yocdroid-start ${D}${base_sbindir}/yocdroid-start
    install -D -m 0755 ../yocdroid-run   ${D}${base_sbindir}/yocdroid-run
    install -D -m 0644 ../yocdroid.sh    ${D}${sysconfdir}/profile.d/yocdroid.sh
}
