package database.mongodb

import _root_.model.Transaction
import database.DaoTrait
import org.mongodb.scala._
import util.ObservableHelpers._

class MongoDbDao(user: String, password: String, auth: String) extends DaoTrait {
  val client = MongoClient("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + auth)
  val database = client.getDatabase("blockchain")
  val collection = database.getCollection("Transactions")

  override def update(transaction: Transaction) {
    val count = collection.countDocuments().execute()

    val observable: Observable[Completed] = collection.insertOne(Document("_id" -> (count + 1).toInt,
      "sig" -> transaction.signature, "hash" -> transaction.hash, "value" -> transaction.value, "pub" -> transaction.publicKey))

    observable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })
  }

  override def read() {

  }

  override def create() {
    throw new Exception("Unsupported action")
  }

  override def delete() {
    throw new Exception("Unsupported action")
  }
}
