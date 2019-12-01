package database.mongodb

import database.DaoTrait
import model.Block


class MongoDbDao extends DaoTrait {
  override def create(block: Block): Unit = ???

  override def read(id: Int): Unit = ???
}
