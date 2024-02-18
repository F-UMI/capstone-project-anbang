#!/bin/bash

export PATH=$PATH:../bin/
export FABRIC_CFG_PATH=../config/


function allCAup() {
    ./ca-tls.sh&&
    ./rca-org0.sh&&
    ./rca-org1.sh&&
    ./rca-org2.sh
}
function enroll() {
    ./enrollOrg0.sh&&
    ./enrollOrg1.sh&&
    ./enrollOrg2.sh&&
    ./buildChannel.sh
}

# Check if a function name is provided as an argument
if [ -n "$1" ]; then
    # Call the function with the provided name
    "$1"
else
    echo "Usage: $0 <function_name>"
fi
