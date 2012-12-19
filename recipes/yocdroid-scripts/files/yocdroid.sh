# Bash environment setup for processes running under the Yocdroid
# environment.  This is sourced for login shells, but utilities like
# system initscripts often read it also to set up their environments.
# Please don't pollute with interactive-only features.

# Clone environment variables from the zygote process except ones that
# can interfere with the local environment (and the ANDROID_SOCKET_xxx
# variables which specify file descriptors we obviously don't have).
# This is probably too broad, but at least it won't miss anything.
ZPID=$(pidof zygote)
eval $(tr '\000' '\n' < /proc/$ZPID/environ | egrep -v '^(LD_LIBRARY_)?PATH=' | grep -v '^ANDROID_SOCKET_' | sed 's/^\([^=]*\)=\(.*\)/export \1="\2"/')


# Add the Android directories to the PATH
PATH=$PATH:/system/bin:/system/xbin

# Deep, deep voodoo.  The android properties mapping is a tmpfs file
# that gets deleted (!) by init and passed to child processes as an
# open file descriptor which they then mmap().  This is insane -- I
# can only guess that the reason might be "security", but that's
# clearly wrong as we're about to defeat it right here: We can't
# recover the existing file, but we can dup the file descriptor from
# an existing process.  This hack to the bash startup only works for
# root logins who can read the zygote descriptors, though something
# similar could be done in the daemon and initscript setups too...
PFD=$(echo $ANDROID_PROPERTY_WORKSPACE | sed 's/,.*//')
if [ -e /proc/$ZPID/fd/$PFD ]; then
    eval "exec $PFD<>/proc/$ZPID/fd/$PFD"
fi

unset ZPID PFD
