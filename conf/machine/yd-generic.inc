# This stuff mostly copied from the upstream qemu.conf:

# Used to enable package selection in upstream packagegroup recipes
# (though not by our image)
MACHINE_FEATURES = "alsa bluetooth usbgadget screen"

# Don't include kernel in the image
RDEPENDS_kernel-base = ""

# Use the default upstream kernel when one must be built
PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto"
