package database

import _root_.model.Transaction
import org.mongodb.scala.{Completed, Document, MongoClient, Observer, _}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MongoDbDao(user: String, password: String, role: String) {
  val client = MongoClient() //"mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain")
  val collection = database.getCollection("transactions")

  def update(transaction: Transaction) {
    val count = Await.result(collection.countDocuments().toFuture(), Duration(5, "seconds"))

    val document = Document("_id" -> (count + 1).toInt, "signature" -> transaction.signature, "hash" -> transaction.hash, "value" -> transaction.value, "publicKey" -> transaction.publicKey)
    collection.insertOne(document).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })
  }

  def read(_id: Int) {
    collection.find(Document("_id" -> _id)).subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })

  }

  def create() {
    throw new Exception("Unsupported action")
  }

  def delete() {
    throw new Exception("Unsupported action")
  }
}
