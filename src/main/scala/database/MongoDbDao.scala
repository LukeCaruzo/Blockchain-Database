package database

import _root_.model.Transaction
import org.mongodb.scala._
import util.ObservableHelpers._

class MongoDbDao(user: String, password: String, role: String) {
  val client = MongoClient("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain")
  val collection = database.getCollection("Transactions")

  def update(transaction: Transaction) {
    val count = collection.countDocuments().execute()

    val observable: Observable[Completed] = collection.insertOne(Document("_id" -> (count + 1).toInt,
      "sig" -> transaction.signature, "hash" -> transaction.hash, "value" -> transaction.value, "pub" -> transaction.publicKey))

    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })
  }

  def read() {

  }

  def create() {
    throw new Exception("Unsupported action")
  }

  def delete() {
    throw new Exception("Unsupported action")
  }
}
