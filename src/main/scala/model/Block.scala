package model

import java.security.MessageDigest

import database.MongoDb

object Block {
  def apply(data: String): Block = {
    val block = Block(MongoDb.count, System.currentTimeMillis.toString, getPreviousHash, "", data)

    block.hash = generateHash(block)

    return block
  }

  def generateHash(block: Block): String = {
    return MessageDigest.getInstance("SHA-256").digest(block.toString.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }

  def getPreviousHash: String = {
    val previousBlock = MongoDb.read(MongoDb.count - 1)

    if (previousBlock != None) {
      return previousBlock.get.hash
    } else {
      return ""
    }
  }
}

case class Block(_id: Long, timestamp: String, previousHash: String, var hash: String, data: String) {
  override def toString: String = this.getClass.getSimpleName + "(" + _id + "," + timestamp + "," + previousHash + "," + data + ")"
}
