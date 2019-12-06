#!/bin/bash

rm -rf ../../data
mkdir -p ../../data

mongod --dbpath ../../data --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork

mongo <<EOF
use admin
db.createUser(
  {
    user: "admin",
    pwd: "test",
    roles: [ { role: "root", db: "admin" } ]
  }
)
db.adminCommand( { shutdown: 1 } )
EOF

mongod --dbpath ../../data --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --auth

# Shows running MongoDB instances
ps aux | grep -v grep | grep mongod
