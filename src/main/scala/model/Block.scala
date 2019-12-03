package model

import org.mongodb.scala.bson.ObjectId

object Block {
  def apply(index: Int, previousHash: String, hash: String, data: String): Block = Block(new ObjectId(), index, System.currentTimeMillis.toString, previousHash, hash, data)
}

case class Block(_id: ObjectId, index: Int, timestamp: String, previousHash: String, hash: String, data: String)