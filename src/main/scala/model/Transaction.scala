package model

import org.mongodb.scala.bson.ObjectId

object Transaction {
  def apply(signature: String, hash: String, value: Int, publicKey: String): Transaction = Transaction(new ObjectId(), signature, hash, value, publicKey)
}

case class Transaction(_id: ObjectId, signature: String, hash: String, value: Int, publicKey: String)