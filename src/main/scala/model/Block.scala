package model

import java.security.MessageDigest

import cryptography.{ECDSA, Key}
import database.MongoDb

object Block {
  def apply(privateKey: Key, data: String): Block = {
    val block = Block(MongoDb.count, System.currentTimeMillis.toString, getPreviousHash, "", "", privateKey.pub.compress.toString, data)

    // TODO: Signing must be in Transactions and data should be Transactionslist
    block.hash = generateHash(block)
    block.signedHash = ECDSA.sign(privateKey, block.hash, "SHA-256").toString

    println(ECDSA.verify(BigInt(block.signedHash), Key.pub(BigInt(block.publicKey), ECDSA.p192), block.hash))

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

case class Block(_id: Long, timestamp: String, previousHash: String, var hash: String, var signedHash: String, publicKey: String, data: String) {
  override def toString: String = this.getClass.getSimpleName + "(" + _id + "," + timestamp + "," + previousHash + "," + publicKey + "," + data + ")"
}
