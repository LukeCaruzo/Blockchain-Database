#!/bin/bash

array=( 27017 27018 27019 )
for i in "${array[@]}"; do
mongo --port "$i" --authenticationDatabase "admin" -u "admin" -p "test" <<EOF
db.adminCommand( { shutdown: 1 } )
EOF
done