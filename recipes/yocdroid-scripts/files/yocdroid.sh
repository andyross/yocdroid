
# Clone environment variables from the zygote process except ones that
# can interfere with the local environment.  This is probably too
# broad, but at least it won't miss anything.
eval $(tr '\000' '\n' < /proc/$(pidof zygote)/environ | egrep -v '^(LD_LIBRARY_)?PATH=' | sed 's/^\([^=]*\)=\(.*\)/export \1="\2"/')

# Add the Android directories to the PATH
PATH=$PATH:/system/bin:/system/xbin

