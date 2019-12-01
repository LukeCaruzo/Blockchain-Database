package database

import model.Transaction

trait DaoTrait {
  def create(block: Transaction)

  def read(id: Int)

  // def update()

  // def delete()
}
