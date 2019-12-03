package database

import _root_.model.Block
import org.mongodb.scala.{Completed, Document, MongoClient, Observer, _}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class MongoDbDao(user: String, password: String, role: String) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient() // ("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  def update(block: Block) {
    println("update")

    collection.countDocuments().toFuture().onComplete {
      case Success(value) => {
        println(s"Count: Got the callback, meaning = $value")
        block.index = value
        collection.insertOne(block).toFuture().onComplete {
          case Success(value) => println(s"Insert: Got the callback, meaning = $value")
          case Failure(e) => e.printStackTrace
        }
      }
      case Failure(e) => e.printStackTrace
    }

    /*
    val count = Await.result(collection.countDocuments().toFuture(), Duration(5, "seconds"))

    val document = Document("_id" -> (count + 1).toInt, "signature" -> transaction.signature, "hash" -> transaction.hash, "value" -> transaction.value, "publicKey" -> transaction.publicKey)

    collection.insertOne(block).subscribe((res: Completed) => println(res))
      collection.insertOne(block).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })*/
  }

  def read(index: Long) {
    println("read")

    collection.find(Document("index" -> index)).subscribe(new Observer[Document] {
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
