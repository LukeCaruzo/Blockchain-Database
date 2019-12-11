#!/bin/bash

mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27017 --dbpath ../data/replicaset0 --bind_ip localhost,server0 --keyFile ../data/keyfile
mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27018 --dbpath ../data/replicaset1 --bind_ip localhost,server1 --keyFile ../data/keyfile
mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27019 --dbpath ../data/replicaset2 --bind_ip localhost,server2 --keyFile ../data/keyfile
