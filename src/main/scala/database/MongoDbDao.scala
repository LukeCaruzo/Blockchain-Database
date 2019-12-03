package database

import _root_.model.Block
import org.mongodb.scala.{Completed, Document, MongoClient, Observer, _}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class MongoDbDao(user: String, password: String, role: String) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient() // ("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  def insert(block: Block): Unit = {
    block.index = count()
    collection.insertOne(block).toFuture().onComplete {
      case Success(value) => println("Inserted " + block)
      case Failure(e) => e.printStackTrace
    }
  }

  def read(index: Long): Option[Block] = {
    var placeholder = None : Option[Block]
    var flag = true

    collection.find(equal("index", index)).toFuture().onComplete {
      case Success(value) =>  {
        if(value.isEmpty) {
          flag = false
        } else {
          placeholder = Some(value.head)
        }
      }
      case Failure(e) => e.printStackTrace
    }

    while (placeholder == None && flag) {
      Thread.sleep(10)
    }

    placeholder
  }

  def show(): Option[Seq[Block]] = {
    var placeholder = None : Option[Seq[Block]]
    var flag = true

    collection.find().toFuture().onComplete {
      case Success(value) =>  {
        if(value.isEmpty) {
          flag = false
        } else {
          placeholder = Some(value)
        }
      }
      case Failure(e) => e.printStackTrace
    }

    while (placeholder == None && flag) {
      Thread.sleep(10)
    }

    placeholder
  }

  def count(): Long = {
    var count: Long = -1

    collection.countDocuments().toFuture().onComplete {
      case Success(value) => count = value
      case Failure(e) => e.printStackTrace
    }

    while (count ==  -1) {
      Thread.sleep(10)
    }

    count
  }
}
