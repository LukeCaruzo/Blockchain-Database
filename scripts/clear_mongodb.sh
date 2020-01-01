#!/bin/bash

array=(27017 27018 27019)
for i in "${array[@]}"; do
  mongo --port "$i" --authenticationDatabase "admin" -u "admin" -p "test" <<EOF
use blockchain
db.dropDatabase()
EOF
done
