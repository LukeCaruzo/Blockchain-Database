package database

import _root_.model.Transaction
import org.mongodb.scala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class MongoDbDao(user: String, password: String, role: String) {
  val client = MongoClient() //"mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain")
  val collection = database.getCollection("transactions")

  def update(transaction: Transaction): Future[Int] = {
    return Future {

      val count = Await.result(collection.countDocuments().toFuture(), Duration(5, "seconds"))

      val document = Document("_id" -> (count + 1).toInt, "sig" -> transaction.signature, "hash" -> transaction.hash, "value" -> transaction.value, "pub" -> transaction.publicKey)
      val observable: Observable[Completed] = collection.insertOne(document)

      observable.subscribe(new Observer[Completed] {
        override def onNext(result: Completed): Unit = println(s"onNext: $result")

        override def onError(e: Throwable): Unit = println(s"onError: $e")

        override def onComplete(): Unit = println("onComplete")
      })

      0
    }
  }

  /*def read(): Future[Int] = {
    return Future {
      var waitOnResult = true
      val result
      collection.find().collect().subscribe((results: Seq[Document]) => println(s"Found: #${results.size}"))

      servable.subscribe(new Observer[Document] {
        override def onNext(nextResult: Document): Unit = {
          println(s"onNext: $result")

          result = new Transaction(((Json.parse(nextResult.toJson()) \ "config").get.as[String])
        }

        override def onError(e: Throwable): Unit = println(s"onError: $e")

        override def onComplete(): Unit = {
          waitOnResult = false
          println("onComplete")
        }
      })

      0
    }
  }*/

  def create() {
    throw new Exception("Unsupported action")
  }

  def delete() {
    throw new Exception("Unsupported action")
  }
}
