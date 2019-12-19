package database

import model.Block
import org.mongodb.scala.Completed

trait MongoTrait {
  val connection: String

  def insert(block: Block): Completed

  def read(_id: Long): Option[Block]

  def count: Long

  def show: Seq[Block]
}
