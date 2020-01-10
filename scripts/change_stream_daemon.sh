#!/bin/bash

# https://docs.mongodb.com/manual/reference/method/db.collection.watch/

mongo --port 27019 -u "admin" -p "test" <<\EOF
blockchain = db.getSiblingDB("blockchain")
watchCursor = blockchain.blocks.watch()

while (!watchCursor.isExhausted()){
  if (watchCursor.hasNext()){
    next = watchCursor.next()
    result = next.fullDocument

    if(next.operationType == "insert" && 0 < result._id) {
      previousResult = blockchain.blocks.find({ "_id" : (result._id - 1) })[0]

      if(result.previousHash != previousResult.hash) {
        blockchain.blocks.deleteOne({ "_id" : (result._id) })
      }
    }
  }
}

watchCursor.close()
EOF