package database.mongodb

import java.security.MessageDigest

import database.MongoTrait
import model.Block
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import util.Helpers._

class MongoClient(override val connection: String) extends MongoTrait {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient(connection)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  override def insert(block: Block): Completed = {
    block._id = this.count
    block.timestamp = System.currentTimeMillis.toString
    block.previousHash = getPreviousHash
    block.hash = generateHash(block)

    collection.insertOne(block).execute()
  }

  private def generateHash(block: Block): String = MessageDigest
    .getInstance("SHA-256")
    .digest(block.toString.getBytes("UTF-8"))
    .map("%02x".format(_)).mkString

  private def getPreviousHash: String = {
    val previousBlock = this.read(this.count - 1)

    if (previousBlock != None) {
      return previousBlock.get.hash
    }

    return ""
  }

  override def count: Int = collection.countDocuments().execute().toInt

  override def read(_id: Int): Option[Block] = {
    for (block <- this.show) {
      if (block._id == _id) {
        return Some(block)
      }
    }

    None
  }

  override def show: Seq[Block] = collection.find().execute()
}
