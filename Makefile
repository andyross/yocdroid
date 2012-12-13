
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

pokycheck: bbwrap
	@test -f poky/meta/conf/layer.conf || { \
	    echo Poky Linux tree not found under ./poky.  See INSTALL.; \
	    exit 1; }

.PHONY: bbwrap

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
