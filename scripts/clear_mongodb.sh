#!/bin/bash

mongo --port 27017 --authenticationDatabase "admin" -u "admin" -p "test" <<EOF
use blockchain
db.dropDatabase()
EOF