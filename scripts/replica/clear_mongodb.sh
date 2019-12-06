#!/bin/bash

mongo --port 27017 <<EOF
use blockchain
db.dropDatabase()
EOF