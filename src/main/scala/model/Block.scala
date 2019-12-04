package model

import java.security.MessageDigest

import database.MongoDb

object Block {
  def apply(data: String): Block = new Block(-1, System.currentTimeMillis.toString, "", "", data)
}

case class Block(var _id: Long, timestamp: String, var previousHash: String, var hash: String, data: String) {
  override def toString: String = this.getClass.getSimpleName + "(" + _id + "," + timestamp + "," + previousHash + "," + data + ")"
}
