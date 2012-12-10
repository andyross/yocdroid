DESCRIPTION = "Simple chroot'd sysroot with tools for Android"

inherit core-image

PR = "r0"

# I'd like to turn this on, but we end up with a bunch of errors due
# to duplicated doc file installs (often on stuff that would otherwise
# be handled by update-alternatives, e.g. blkid.8 installed by both
# util-linux-doc and sysvinit-doc)
#
# IMAGE_FEATURES = "doc-pkgs"

# All we want is a tarball
IMAGE_FSTYPES = "tar.bz2"

# The core packagegroup recipes tend to pull in global stuff on the
# assumption we're building a true distro.  List packages explicitly.
IMAGE_INSTALL = " \
    acl \
    acpid \
    alsa-utils \
    at \
    attr \
    base-files \
    bash \
    bc \
    binutils \
    busybox \
    bzip2 \
    coreutils \
    cpio \
    cronie \
    curl \
    e2fsprogs \
    ed \
    elfutils \
    ethtool \
    file \
    findutils \
    gawk \
    gdb \
    grep \
    gzip \
    iproute2 \
    iptables \
    iputils \
    less \
    libacpi \
    man-pages \
    mingetty \
    mktemp \
    ncurses \
    net-tools \
    openssh \
    openssl \
    parted \
    pciutils \
    perl \
    perl-misc \
    perl-modules \
    procps \
    psmisc \
    python-core \
    python-misc \
    python-modules \
    rsync \
    screen \
    sed \
    strace \
    sysfsutils \
    sysklogd \
    sysstat \
    sysvinit \
    tar \
    tcp-wrappers \
    time \
    tzdata \
    unzip \
    update-alternatives-cworth \
    update-rc.d \
    usbutils \
    util-linux \
    valgrind \
    wget \
    zip \
"
