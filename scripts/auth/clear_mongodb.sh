#!/bin/bash

mongo -u "admin" -p "test" <<EOF
use blockchain
db.dropDatabase()
EOF