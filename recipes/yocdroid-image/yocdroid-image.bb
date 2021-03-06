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
    alsa-utils \
    at \
    attr \
    avahi \
    avahi-utils \
    base-files \
    bash \
    bc \
    bind-utils \
    binutils \
    binutils-symlinks \
    bluez4 \
    busybox \
    bzip2 \
    coreutils \
    cpio \
    cronie \
    cryptsetup \
    curl \
    e2fsprogs \
    ed \
    elfutils \
    ethtool \
    evtest \
    file \
    findutils \
    gawk \
    gdb \
    grep \
    gzip \
    iproute2 \
    iptables \
    iputils \
    iw \
    kmod \
    ldd \
    less \
    lvm2-dm \
    man-pages \
    mingetty \
    mktemp \
    nc \
    ncurses \
    net-tools \
    openssh \
    openssl \
    parted \
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
    sshfs-fuse \
    strace \
    sysfsutils \
    sysklogd \
    sysstat \
    sysvinit \
    tar \
    tcpdump \
    tcp-wrappers \
    time \
    tmux \
    tzdata \
    unzip \
    update-alternatives-cworth \
    update-rc.d \
    usbutils \
    util-linux \
    util-linux-blkid \
    wget \
    wireless-tools \
    yocdroid-scripts \
    zip \
"

# Make & gcc are sometimes useful, adds ~50MB unpacked
#IMAGE_INSTALL += "make gcc g++ gcc-symlinks g++-symlinks cpp cpp-symlinks eglibc-dev libgcc-dev libstdc++-dev"

X86_EXTRA = " \
    acpid \
    libacpi \
    pciutils \
"
IMAGE_INSTALL_append_x86 := "${X86_EXTRA}"
IMAGE_INSTALL_append_x86-64 := "${X86_EXTRA}"
