#!/bin/bash

function read_property() {
    # file path
    file_name=$1
    # replace "." to "\."
    property_name=`echo $2 | sed 's/\./\\\./g'`
    cat $file_name | sed -n -e "s/^[ ]*//g;/^#/d;s/^$property_name=//p" | tail -1
}

# check the port of rest server is occupied
function check_port() {
    local port=$1
    lsof -i :$port >/dev/null

    if [ $? -eq 0 ]; then
        echo "The port "$port" has already used"
        exit 1
    fi
}
