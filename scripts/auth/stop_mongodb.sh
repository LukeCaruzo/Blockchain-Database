#!/bin/bash

mongo -u "admin" -p "test" <<EOF
db.adminCommand( { shutdown: 1 } )
EOF