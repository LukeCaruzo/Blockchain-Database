import cryptography.{ECDSA, Key}
import database.MongoDb
import model.{Block}

object Main {
  val user = "myUserAdmin"
  val password = "abc123"
  val role = "admin"

  def main(args: Array[String]): Unit = {
    val dao = new MongoDb("myUserAdmin", "abc123", "admin")

    /*//println(BigInt(212, new Random))
    val privateKey = Key.sec(BigInt("291136341796021072510895648022535616155435573451899175553147951"), ECDSA.p192)

    val transactionList = new TransactionList
    transactionList.add(privateKey, Transaction("test"))
    transactionList.add(privateKey, Transaction("test"))
    println(transactionList)*/

    dao.insert(Block("test"))

    Thread.sleep(1000)

    val document = dao.read(1)
    document match {
      case Some(value) => prettyPrintBlock(value)
      case None => println("No document found")
    }

    Thread.sleep(1000)

    println("Documents: " + dao.count)

    val documents = dao.show
    for (document <- documents) {
      prettyPrintBlock(document)
    }

    Thread.sleep(1000)
  }

  def prettyPrintBlock(block: Block): Unit = {
    println("_id: " + block._id)
    println("timestamp: " + block.timestamp)
    println("previousHash: " + block.previousHash)
    println("hash: " + block.hash)
    println("data: " + block.data)
    println("")
  }
}
