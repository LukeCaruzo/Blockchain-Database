package database

import model.Transaction

trait DaoTrait {
  def create()

  def read()

  def update(transaction: Transaction)

  def delete()
}
