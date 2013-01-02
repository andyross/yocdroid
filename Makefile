# Choose a Yocto architecture with the MACHINE variable.  Choices
# supported are:
#
# yd-arm    - ARMv6 code for older (e.g. ARM11) machines
# yd-armv7  - ARMv7-A code for newer (ARM Cortex, Krait) devices
# yd-x86    - 32 bit i586 code
# yd-x86-64 - 64 bit x86_64, use only if you have a suitable kernel!
#
# Note that Yocto allows a single build tree to span multiple
# architectures.  The last MACHINE choice is cached in ./machine.mak
# to prevent accidents like installing the wrong architecture.
-include machine.mak
MACHINE ?= yd-arm

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

.PHONY: bbwrap pokycheck distclean image install install-ssh machine.mak

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
	rm -rf sstate-cache tmp bbwrap bitbake.lock pseudodone machine.mak conf/sanity_info

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

machine.mak:
	@echo '# Auto-generated machine choice from last build' > $@
	@echo MACHINE=$(MACHINE) >> $@
