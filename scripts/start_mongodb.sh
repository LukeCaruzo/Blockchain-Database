#!/bin/bash

declare -ga mservers=([0]="27017" [1]="27018" [2]="27019")
for i in "${!mservers[@]}"; do
  mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port "${mservers[${i}]}" --dbpath ../data/replicaset${i} --bind_ip localhost,server${i} --keyFile ../data/keyfile
done

# Shows running MongoDB instances
ps aux | grep -v grep | grep mongod
