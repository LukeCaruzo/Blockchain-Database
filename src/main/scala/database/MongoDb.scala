package database

import java.security.MessageDigest

import model.Block
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import util.Helpers._

class MongoDb(connection: String) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient(connection)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  def insert(block: Block): Completed = {
    block.id = this.count
    block.timestamp = System.currentTimeMillis.toString
    block.previousHash = getPreviousHash
    block.hash = generateHash(block)

    collection.insertOne(block).execute()
  }

  private def generateHash(block: Block): String = MessageDigest.getInstance("SHA-256").digest(block.toString.getBytes("UTF-8")).map("%02x".format(_)).mkString

  private def getPreviousHash: String = {
    val previousBlock = this.read(this.count - 1)

    if (previousBlock != None) {
      return previousBlock.get.hash
    }

    return ""
  }

  def count: Long = collection.countDocuments().execute()

  def read(_id: Long): Option[Block] = {
    for (block <- this.show) {
      if (block.id == _id) {
        return Some(block)
      }
    }

    None
  }

  def show: Seq[Block] = collection.find().execute()
}
