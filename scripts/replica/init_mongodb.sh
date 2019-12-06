#!/bin/bash

rm -rf ../../data
mkdir -p ../../data/replicaset0 ../../data/replicaset1 ../../data/replicaset2

mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27017 --dbpath ../../data/replicaset0 --bind_ip localhost,server0
mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27018 --dbpath ../../data/replicaset1 --bind_ip localhost,server1
mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27019 --dbpath ../../data/replicaset2 --bind_ip localhost,server2

mongo --port 27017 <<EOF
rsconf = {
  _id: "rs",
  members: [ {
    _id: 0,
    host: "localhost:27017"
  }, {
    _id: 1,
    host: "localhost:27018"
  }, {
    _id: 2,
    host: "localhost:27019"
  }
] }
rs.initiate(rsconf)
rs.conf()
EOF

# Shows running MongoDB instances
ps aux | grep -v grep | grep mongod
