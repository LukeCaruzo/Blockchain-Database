package database.mongodb

import database.DaoTrait
import model.Transaction
import org.mongodb.scala.MongoClient


class MongoDbDao extends DaoTrait {
  val client = MongoClient("mongodb://myUserAdmin:abc123@localhost:27017/?authSource=admin")
  val database = client.getDatabase("database")
  val collection = database.getCollection("collection")

  override def create(block: Transaction) {

  }

  override def read(id: Int): Unit = {

  }
}
