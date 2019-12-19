package model

object Block {
  def apply(data: String): Block = new Block(-1, "", "", "", data)
}

case class Block(var id: Long, var timestamp: String, var previousHash: String, var hash: String, data: String) {
  override def toString: String = this.getClass.getSimpleName + "(" + id + "," + timestamp + "," + previousHash + "," + data + ")"
}
