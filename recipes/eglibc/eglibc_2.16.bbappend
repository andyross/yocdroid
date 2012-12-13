PRINC = "1"
FILESEXTRAPATHS_prepend := "${THISDIR}"

# Change /dev/log to /dev/syslog to avoid collision with Android
# /dev/log directory.
SRC_URI += "file://syslog-socket-path.patch"
