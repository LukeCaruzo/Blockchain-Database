package model

import org.mongodb.scala.bson.ObjectId

object Block {
  def apply(previousHash: String, hash: String, data: String): Block = Block(new ObjectId(), -1, System.currentTimeMillis.toString, previousHash, hash, data)
}

case class Block(_id: ObjectId, var index: Long, timestamp: String, previousHash: String, hash: String, data: String)
