package database

import model.Block

trait DaoTrait {
  def create(block: Block)

  def read(id: Int)

  // def update()

  // def delete()
}
