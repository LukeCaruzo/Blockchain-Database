#!/bin/bash

mongo --port 27017 --authenticationDatabase "admin" -u "admin" -p "test" <<EOF
db.adminCommand( { shutdown: 1 } )
EOF

mongo --port 27018 --authenticationDatabase "admin" -u "admin" -p "test" <<EOF
db.adminCommand( { shutdown: 1 } )
EOF

mongo --port 27019 --authenticationDatabase "admin" -u "admin" -p "test" <<EOF
db.adminCommand( { shutdown: 1 } )
EOF