
OEROOT := $(shell pwd)/poky
PATH := $(OEROOT)/scripts:$(OEROOT)/bitbake/bin:$(PATH)
BB_ENV_EXTRAWHITE = MACHINE DISTRO TCMODE TCLIBC http_proxy ftp_proxy	\
                    https_proxy all_proxy ALL_PROXY no_proxy		\
                    SSH_AGENT_PID SSH_AUTH_SOCK BB_SRCREV_POLICY	\
                    SDKMACHINE BB_NUMBER_THREADS BB_NO_NETWORK		\
                    PARALLEL_MAKE GIT_PROXY_COMMAND GIT_PROXY_IGNORE	\
                    SOCKS5_PASSWD SOCKS5_USER SCREENDIR

export OEROOT PATH BB_ENV_EXTRAWHITE

all: pokycheck
	bitbake yocdroid-image

pokycheck:
	@test -f poky/meta/conf/layer.conf || { \
	    echo Poky Linux tree not found under ./poky.  Please make a symlink.; \
	    exit 1; }

distclean:
	rm -rf sstate-cache tmp
