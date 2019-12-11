package database

import java.security.MessageDigest

import model.Block
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import util.Helpers._

/** MongoDB Instance
 *
 * @param connection String which contains the connection details
 */
case class MongoDb(connection: String) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient(connection)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  /** Inserts a block into the blockchain
   *
   * @param block Block to insert
   * @return Status of the insertion
   */
  def insert(block: Block): Completed = {
    block._id = this.count
    block.previousHash = getPreviousHash
    block.hash = generateHash(block)

    collection.insertOne(block).execute()
  }

  private def generateHash(block: Block): String = MessageDigest.getInstance("SHA-256").digest(block.toString.getBytes("UTF-8")).map("%02x".format(_)).mkString

  private def getPreviousHash: String = {
    val previousBlock = this.read(this.count - 1)

    if (previousBlock != None) {
      return previousBlock.get.hash
    } else {
      return ""
    }
  }

  /** Counts the blocks in the database
   *
   * @return Number of documents in the database
   */
  def count: Long = collection.countDocuments().execute()

  /** Reads from the database via id
   *
   * @param _id Identifier which should be read
   * @return Some block which was read or none
   */
  def read(_id: Long): Option[Block] = {
    for (block <- this.show) {
      if (block._id == _id) {
        return Some(block)
      }
    }

    None
  }

  /** Returns all blocks from the database
   *
   * @return Sequence of blocks which are stored in the database
   */
  def show: Seq[Block] = collection.find().execute()
}
