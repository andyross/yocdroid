
OEROOT := $(shell pwd)/poky
PATH := $(OEROOT)/scripts:$(OEROOT)/bitbake/bin:$(PATH)
BB_ENV_EXTRAWHITE = MACHINE DISTRO TCMODE TCLIBC http_proxy ftp_proxy	\
                    https_proxy all_proxy ALL_PROXY no_proxy		\
                    SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY	\
                    SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK		\
                    PARALLEL_MAKE GIT_PROXY_COMMAND GIT_PROXY_IGNORE	\
                    SOCKS5_PASSWD SOCKS5_USER SCREENDIR

export OEROOT PATH BB_ENV_EXTRAWHITE

image: pokycheck
	bitbake yocdroid-image

.PHONY: bbwrap pokycheck

pokycheck: bbwrap
	@test -f poky/meta/conf/layer.conf || { \
	    echo Poky Linux tree not found under ./poky.  See INSTALL.; \
	    exit 1; }

bbwrap:
	@echo "#!/bin/sh" > $@
	@echo "OEROOT=$(OEROOT)" >> $@
	@echo "PATH=$(PATH)" >> $@
	@echo "BB_ENV_EXTRAWHITE=\"$(BB_ENV_EXTRAWHITE)\"" >> $@
	@echo "unset DISPLAY" >> $@
	@echo "export OEROOT BB_ENV_EXTRAWHITE" >> $@
	@echo 'exec bitbake "$$@"' >> $@
	@chmod 755 $@

distclean:
	rm -rf sstate-cache tmp bbwrap bitbake.lock pseudodone

# Note that this relies on a /system/xbin/busybox/tar binary.  Would
# be good to bootstrap from only AOSP tools...
D = /data/y
install: tmp/deploy/images/yocdroid-image-qemux86.tar.bz2
	adb shell rm -rf $(D)
	adb shell mkdir $(D)
	adb push tmp/deploy/images/yocdroid-image-qemux86.tar.bz2 /data/
	adb shell 'cd $(D); /system/xbin/busybox/tar xjf /data/yocdroid-image-qemux86.tar.bz2'
	adb shell rm /data/yocdroid-image-qemux86.tar.bz2

# Installs the current user's ssh public keys over adb to bootstrap
# root authentication.
install-ssh:
	cat ~/.ssh/id*.pub > sshkeystmp
	adb shell mkdir $(D)/home/root/.ssh
	adb shell chmod 700 $(D)/home/root/.ssh
	adb push sshkeystmp $(D)/home/root/.ssh/authorized_keys
