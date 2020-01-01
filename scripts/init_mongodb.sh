#!/bin/bash

rm -rf ../data
mkdir -p ../data/replicaset0 ../data/replicaset1 ../data/replicaset2

openssl rand -base64 756 >../data/keyfile
chmod 400 ../data/keyfile

mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27017 --dbpath ../data/replicaset0 --bind_ip localhost,server0 --keyFile ../data/keyfile
mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27018 --dbpath ../data/replicaset1 --bind_ip localhost,server1 --keyFile ../data/keyfile
mongod --logpath /usr/local/var/log/mongodb/mongo.log --logappend --fork --replSet rs --port 27019 --dbpath ../data/replicaset2 --bind_ip localhost,server2 --keyFile ../data/keyfile

sleep 1

mongo --port 27017 <<EOF
rsconf = { _id: "rs", members: [ { _id: 0, host: "localhost:27017" },
  { _id: 1, host: "localhost:27018" },
  { _id: 2, host: "localhost:27019" } ] }
rs.initiate(rsconf)
EOF

echo "sleeping for 30s to initate primary"
sleep 30

array=(27017 27018 27019)
for i in "${array[@]}"; do
  mongo --port "$i" <<EOF
admin = db.getSiblingDB("admin")
admin.createUser( { user: "admin", pwd: "test", roles: [ { role: "root", db: "admin" } ] } )
EOF
  sleep 1
done

# TODO: SCHEMA VALIDIERUNG https://docs.mongodb.com/manual/core/schema-validation/

mongo --port 27017 <<EOF
use "blockchain"
db.createCollection("blocks", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: [ "_id", "timestamp", "previousHash", "hash", "data" ],
         properties: {
            _id: {
               bsonType: "int",
               minimum: 0,
               description: "is int and is required"
            },
            timestamp: {
               bsonType: "string",
               description: "is a string and is required"
            },
            previousHash: {
               bsonType: "string",
               minLength: 0,
               maxLength: 64,
               description: "is a string and is required"
            },
            hash: {
               bsonType: "string",
               minLength: 64,
               maxLength: 64,
               description: "is a string and is required"
            },
            data: {
               bsonType: "string",
               description: "is a string and is required"
            }
         }
      }
   }
})
EOF
