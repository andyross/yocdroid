# Choose a Yocto machine here.  The "qemuarm" is a armv5te softfp
# target which will work on all ARM Android devices, while qemux86
# will produce generic -march=i586 binaries.  There is a "qemux86-64"
# machine as well, if you have a suitable kernel on your device.
MACHINE = qemuarm

# Bitbake environment
OEROOT := $(shell pwd)/poky
PATH := $(OEROOT)/scripts:$(OEROOT)/bitbake/bin:$(PATH)
BB_ENV_EXTRAWHITE = MACHINE DISTRO TCMODE TCLIBC http_proxy ftp_proxy	\
                    https_proxy all_proxy ALL_PROXY no_proxy		\
                    SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY	\
                    SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK		\
                    PARALLEL_MAKE GIT_PROXY_COMMAND GIT_PROXY_IGNORE	\
                    SOCKS5_PASSWD SOCKS5_USER SCREENDIR
export OEROOT MACHINE PATH BB_ENV_EXTRAWHITE

IMGFILE = tmp/deploy/images/yocdroid-image-$(MACHINE).tar.bz2 
D = /data/y

image: pokycheck
	bitbake yocdroid-image
	ls -lL $(IMGFILE)

.PHONY: bbwrap pokycheck distclean image install install-ssh

pokycheck: bbwrap
	@test -f poky/meta/conf/layer.conf || { \
	    echo Poky Linux tree not found under ./poky.  See INSTALL.; \
	    exit 1; }

bbwrap:
	@echo "#!/bin/sh" > $@
	@echo 'test -z "$$MACHINE" && MACHINE=$(MACHINE)' >> $@
	@echo "OEROOT=$(OEROOT)" >> $@
	@echo "PATH=$(PATH)" >> $@
	@echo "BB_ENV_EXTRAWHITE=\"$(BB_ENV_EXTRAWHITE)\"" >> $@
	@echo "export OEROOT BB_ENV_EXTRAWHITE" >> $@
	@echo 'exec bitbake "$$@"' >> $@
	@chmod 755 $@

distclean:
	rm -rf sstate-cache tmp bbwrap bitbake.lock pseudodone

BUSYBOX = /system/bin/sh /sdcard/busywrap
SU = /system/bin/sh /sdcard/suwrap
wrappers:
	adb push bootstrap/busywrap /sdcard/busywrap
	adb push bootstrap/suwrap /sdcard/suwrap

# Note that this relies on a busybox tar (with a find under xbin to
# handle variant locations).  Would be good to bootstrap from only
# AOSP tools...
install: $(IMGFILE) wrappers
	adb shell $(SU) mkdir $(D)
	adb push $(IMGFILE) /sdcard/imgtmp.tar.bz2
	adb shell $(SU) $(BUSYBOX) tar -x -j -C $(D) -f /sdcard/imgtmp.tar.bz2 
	adb shell $(SU) rm /sdcard/imgtmp.tar.bz2

# Installs the current user's ssh public keys over adb to bootstrap
# root authentication.
install-ssh: wrappers
	cat ~/.ssh/id*.pub > sshkeystmp
	adb shell $(SU) mkdir $(D)/home/root/.ssh
	adb shell $(SU) chmod 700 $(D)/home/root/.ssh
	adb push sshkeystmp /sdcard/
	adb shell $(SU) cp /sdcard/sshkeystmp $(D)/home/root/.ssh/authorized_keys
	adb shell $(SU) chmod 644 $(D)/home/root/.ssh/authorized_keys
	adb shell $(SU) rm /sdcard/sshkeystmp
	rm -f sshkeystmp

start: wrappers
	adb shell $(SU) $(D)/sbin/yocdroid-start
	adb shell $(SU) $(D)/sbin/yocdroid-run /etc/init.d/rc 3

