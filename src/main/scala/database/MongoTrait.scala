package database

import model.Block
import org.mongodb.scala.Completed

trait MongoTrait {
  val connection: String

  def insert(block: Block): Completed

  def read(_id: Int): Option[Block]

  def count: Int

  def show: Seq[Block]
}
