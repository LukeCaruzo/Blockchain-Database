#!/bin/bash

rm -rf ../data
mkdir -p ../data/replicaset0 ../data/replicaset1 ../data/replicaset2

openssl rand -base64 756 >../data/keyfile
chmod 400 ../data/keyfile

declare -ga mservers=([0]="27017" [1]="27018" [2]="27019")
for i in "${!mservers[@]}"; do
  mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port "${mservers[${i}]}" --dbpath ../data/replicaset${i} --bind_ip localhost,server${i} --keyFile ../data/keyfile
done

sleep 1

mongo --port 27017 <<\EOF
rsconf = { _id: "rs", members: [ { _id: 0, host: "localhost:27017" },
  { _id: 1, host: "localhost:27018" },
  { _id: 2, host: "localhost:27019" } ] }
rs.initiate(rsconf)
EOF

echo "sleeping for 30s to initate primary"
sleep 30

array=(27017 27018 27019)
for i in "${array[@]}"; do
  mongo --port "$i" <<\EOF
admin = db.getSiblingDB("admin")
admin.createUser( { user: "admin", pwd: "test", roles: [ { role: "root", db: "admin" } ] } )
EOF
  sleep 1
done

for i in "${array[@]}"; do
  mongo --port "$i" -u "admin" -p "test" <<\EOF
admin = db.getSiblingDB("admin")
admin.createRole( { role: "onlyReadInsert",
     privileges: [ { resource: { db: "blockchain", collection: "blocks" }, actions: [ "find", "insert" ] } ],
     roles: [ ]
})
admin.createUser( { user: "lucas", pwd: "schmidt", roles: [ { role: "onlyReadInsert", db: "admin" } ] } )
EOF
  sleep 1
done

for i in "${array[@]}"; do
mongo --port "$i" -u "admin" -p "test" <<\EOF
blockchain = db.getSiblingDB("blockchain")
blockchain.createCollection("blocks", { validator: {
      $jsonSchema: {
         bsonType: "object", required: [ "_id", "timestamp", "previousHash", "hash", "data" ],
         properties: { _id: {
               bsonType: "int",
               minimum: 0,
               description: "must be a int and is required"
            }, timestamp: {
               bsonType: "string",
               description: "must be a string and is required"
            }, previousHash: {
               bsonType: "string",
               minLength: 0,
               maxLength: 64,
               description: "must be a string and is required"
            }, hash: {
               bsonType: "string",
               minLength: 64,
               maxLength: 64,
               description: "must be a string and is required"
            }, data: {
               bsonType: "string",
               minLength: 0,
               description: "must be a string and is required"
            }
         }
      }
   }
})
EOF
  sleep 1
done

source change_stream_daemon.sh