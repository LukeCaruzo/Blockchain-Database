package database

import _root_.model.Block
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, _}
import _root_.model.TransactionList
import _root_.model.Transaction

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object MongoDb {
  val codecRegistry = fromRegistries(fromProviders(classOf[Block]), DEFAULT_CODEC_REGISTRY)

  val client = MongoClient() // ("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + role)
  val database = client.getDatabase("blockchain").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Block] = database.getCollection("blocks")

  val waitDuration = Duration(5, "seconds")

  implicit class FutureAwait[T](future: Future[T]) {
    def execute: T = Await.result(future, waitDuration)
  }

  def insert(block: Block): Completed = {
    collection.insertOne(block).toFuture().execute
  }

  def count: Long = collection.countDocuments().toFuture().execute

  def read(_id: Long): Option[Block] = {
    for (block <- this.show) {
      if (block._id == _id) {
        return Some(block)
      }
    }

    None
  }

  def show: Seq[Block] = collection.find().toFuture().execute
}
