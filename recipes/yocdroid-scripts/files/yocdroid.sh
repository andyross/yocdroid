
# Clone environment variables from the zygote process except ones that
# can interfere with the local environment.  This is probably too
# broad, but at least it won't miss anything.
eval $(tr '\000' '\n' < /proc/$(pidof zygote)/environ | egrep -v '^(LD_LIBRARY_)?PATH=' | sed 's/^\([^=]*\)=\(.*\)/export \1="\2"/')

# Add the Android directories to the PATH
PATH=$PATH:/system/bin:/system/xbin

# Deep, deep voodoo.  The android properties mapping is a tmpfs file
# that gets deleted (!) by init and passed to child processes as an
# open file descriptor.  We can't recover the existing file, but we
# can dup the file descriptor from an existing process...
# This hack to the bash startup only works for root logins who can read
# the zygote descriptors, though something similar could be done in the
# daemon and initscript setups too...                  
PFD=$(echo $ANDROID_PROPERTY_WORKSPACE | sed 's/,.*//')
eval "exec $PFD</proc/$(pidof zygote)/fd/$PFD"

