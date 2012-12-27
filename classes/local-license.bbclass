# When a recipe contains its own source code, it makes sense to put
# the license file in the root of the recipe directory.  This class
# does a copy of that file (specify as "LICENSE_FILE", default
# "COPYING") to the build directory at unpack time to make the
# license.bbclass check work.

LICENSE_FILE ?= "COPYING"

LICENSE_PATH := "${THISDIR}/${LICENSE_FILE}"
do_unpack[postfuncs] += "unpack_license"
unpack_license() {
    cp ${LICENSE_PATH} .
}
