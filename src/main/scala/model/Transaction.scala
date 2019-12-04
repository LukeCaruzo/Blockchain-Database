package model

@deprecated
object Transaction {
  def apply(data: String): Transaction = Transaction("", "", "", "", data)
}

@deprecated
case class Transaction(var previousHash: String, var hash: String, var signedHash: String, var publicKey: String, data: String) {
  override def toString: String = this.getClass.getSimpleName + "(" + previousHash + "," + publicKey + "," + data + ")"
}
