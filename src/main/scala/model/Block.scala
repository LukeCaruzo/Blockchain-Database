package model

object Block {
  def apply(data: String): Block = new Block(-1, "", "", "", data)
}

/** Block of the blockchain.
 *
 * @param _id          Identifier of the block
 * @param timestamp    Timestamp of the block
 * @param previousHash Hash of the previous block
 * @param hash         Hash of the block
 * @param data         Data stored in the block
 */
case class Block(var _id: Long, var timestamp: String, var previousHash: String, var hash: String, data: String) {
  override def toString: String = this.getClass.getSimpleName + "(" + _id + "," + timestamp + "," + previousHash + "," + data + ")"
}
