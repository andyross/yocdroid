LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

PR = "r1"

SRC_URI = "file://yocdroid-start \
           file://yocdroid-run \
           file://yocdroid-dnswatch \
           file://yocdroid-dnswatch.init \
           file://chroot-static.c \
           file://yocdroid.sh \
           file://app_process"

inherit update-rc.d local-license

# Needed because the strip/objcopy step breaks chroot-static somehow...
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

INITSCRIPT_NAME = "yocdroid-dnswatch"

do_compile() {
    ${CC} -o chroot-static ../chroot-static.c -nostdlib -lgcc
}

do_install() {
    install -D -m 0755 ../app_process            ${D}${bindir}/app_process
    install -D -m 0755 chroot-static             ${D}${base_sbindir}/chroot-static
    install -D -m 0755 ../yocdroid-start         ${D}${base_sbindir}/yocdroid-start
    install -D -m 0755 ../yocdroid-run           ${D}${base_sbindir}/yocdroid-run
    install -D -m 0644 ../yocdroid.sh            ${D}${sysconfdir}/profile.d/yocdroid.sh
    install -D -m 0755 ../yocdroid-dnswatch      ${D}${sbindir}/yocdroid-dnswatch
    install -D -m 0755 ../yocdroid-dnswatch.init ${D}${sysconfdir}/init.d/yocdroid-dnswatch
}
