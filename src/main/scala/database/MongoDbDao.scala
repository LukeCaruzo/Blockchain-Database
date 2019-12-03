package database

import _root_.model.Block
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, _}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class MongoDbDao(user: String, password: String, role: String) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient() // ("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  val waitDuration = Duration(5, "seconds")

  implicit class FutureAwait[T](future: Future[T]) {
    def execute(): T = Await.result(future, waitDuration)
  }

  def insert(block: Block): Completed = {
    block.index = this.count
    collection.insertOne(block).toFuture().execute()
  }

  def count(): Long = collection.countDocuments().toFuture().execute()

  def read(index: Long): Option[Block] = {
    /*var block = None: Option[Block]
    var flag = true

    collection.find(equal("index", index)).toFuture().onComplete {
      case Success(value) => {
        if (value.isEmpty) {
          flag = false
        } else {
          block = Some(value.head)
        }
      }
      case Failure(e) => e.printStackTrace
    }

    while (block == None && flag) {
      Thread.sleep(10)
    }

    block*/

    //val x: Seq[Block] =collection.find(equal("index", index)).toFuture().execute()

    for (block <- this.show) {
      if (block.index == index) {
        return Some(block)
      }
    }

    None
  }

  def show(): Seq[Block] = collection.find().toFuture().execute()

}
