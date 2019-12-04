#!/bin/sh

mkdir -p ./data

mongod --dbpath ./data --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork

mongo <<EOF
use admin
db.createUser(
  {
    user: "myUserAdmin",
    pwd: "abc123",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
  }
)
db.adminCommand( { shutdown: 1 } )
EOF

ps aux | grep -v grep | grep mongod
