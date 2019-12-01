package model

import java.util.ArrayList

import org.mongodb.scala.bson.ObjectId

case class Block(_id: ObjectId, size: Int, hash: String, transactionList: ArrayList[Transaction] = new ArrayList[Transaction]())
