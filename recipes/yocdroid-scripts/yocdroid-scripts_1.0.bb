LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

PR = "r1"

SRC_URI = "file://yocdroid-start \
           file://yocdroid-mountall \
           file://yocdroid-stop \
           file://yocdroid-run \
           file://yocdroid-dnswatch \
           file://yocdroid-dnswatch.init \
           file://yocdroid-modules \
           file://syscall-asm.h \
           file://chroot-static.c \
           file://yocdroid-mountd.c \
           file://yocdroid.sh \
           file://yocdroid-shell.sh \
           file://app_process"

inherit update-rc.d local-license

# Needed because the strip/objcopy step breaks chroot-static somehow...
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

INITSCRIPT_NAME = "yocdroid-dnswatch"

do_compile() {
    ${CC} -o chroot-static -I.. ../chroot-static.c -nostdlib -lgcc
    ${CC} -o yocdroid-mountd -I.. ../yocdroid-mountd.c -nostdlib -lgcc
}

do_install() {
    install -D -m 0755 ../app_process            ${D}${bindir}/app_process
    install -D -m 0755 chroot-static             ${D}${base_sbindir}/chroot-static
    install -D -m 0755 yocdroid-mountd           ${D}${base_sbindir}/yocdroid-mountd
    install -D -m 0755 ../yocdroid-start         ${D}${base_sbindir}/yocdroid-start
    install -D -m 0755 ../yocdroid-mountall      ${D}${base_sbindir}/yocdroid-mountall
    install -D -m 0755 ../yocdroid-stop          ${D}${base_sbindir}/yocdroid-stop
    install -D -m 0755 ../yocdroid-run           ${D}${base_sbindir}/yocdroid-run
    install -D -m 0644 ../yocdroid.sh            ${D}${sysconfdir}/profile.d/yocdroid.sh
    install -D -m 0644 ../yocdroid-shell.sh      ${D}${sysconfdir}/profile.d/yocdroid-shell.sh
    install -D -m 0755 ../yocdroid-dnswatch      ${D}${sbindir}/yocdroid-dnswatch
    install -D -m 0755 ../yocdroid-dnswatch.init ${D}${sysconfdir}/init.d/yocdroid-dnswatch
    install -D -m 0755 ../yocdroid-modules       ${D}${sysconfdir}/init.d/yocdroid-modules
}
