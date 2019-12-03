package database

import _root_.model.Block
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{MongoClient, _}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class MongoDbDao(user: String, password: String, role: String) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient() // ("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  val waitDuration = Duration(5, "seconds")

  private def execute(future: Future): Unit = {
    return Await.result(future, waitDuration)
  }

  def insert(block: Block): Boolean = {
    var inserted = false

    block.index = count()
    //Await.result(, waitDuration)
    collection.insertOne(block).toFuture().onComplete {
      case Success(value) => inserted = true
      case Failure(e) => e.printStackTrace
    }

    while (!inserted) {
      Thread.sleep(10)
    }

    inserted
  }

  def count(): Long = {
    var count: Long = -1

    collection.countDocuments().toFuture().onComplete {
      case Success(value) => count = value
      case Failure(e) => e.printStackTrace
    }

    while (count == -1) {
      Thread.sleep(10)
    }

    count
  }

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

    val blocks = show

    for(block <- blocks) {
      if(block.index == index) {
        return Some(block)
      }
    }

    None
  }

  def show(): Seq[Block] = {
    var blocks = None: Option[Seq[Block]]

    collection.find().toFuture().onComplete {
      case Success(value) => {
        blocks = Some(value)
      }
      case Failure(e) => e.printStackTrace
    }

    while (blocks == None) {
      Thread.sleep(10)
    }

    blocks.get
  }
}
