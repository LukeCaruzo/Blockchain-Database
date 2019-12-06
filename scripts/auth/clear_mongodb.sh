#!/bin/bash
#
# Drops the table

mongo -u "admin" -p "test" <<EOF
use blockchain
db.dropDatabase()
EOF