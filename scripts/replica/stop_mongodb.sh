#!/bin/bash

mongo --port 27017 <<EOF
db.adminCommand( { shutdown: 1 } )
EOF

mongo --port 27018 <<EOF
db.adminCommand( { shutdown: 1 } )
EOF

mongo --port 27019 <<EOF
db.adminCommand( { shutdown: 1 } )
EOF