import database.mongodb.MongoClient
import model.Block

object Main {
  def main(args: Array[String]): Unit = {
    testDatabaseOperationsReplica()
  }

  private def testDatabaseOperationsReplica(): Unit = {
    val replica = "rs"
    val source = "admin"
    val user = "admin"
    val password = "test"
    val address1 = "localhost:27017"
    val address2 = "localhost:27018"
    val address3 = "localhost:27019"
    val connectionReplica1 = "mongodb://" + user + ":" + password + "@" + address1 + "/?replicaSet=" + replica + "&authSource=" + source + "&w=majority"
    val connectionReplica2 = "mongodb://" + user + ":" + password + "@" + address2 + "/?replicaSet=" + replica + "&authSource=" + source + "&w=majority"
    val connectionReplica3 = "mongodb://" + user + ":" + password + "@" + address3 + "/?replicaSet=" + replica + "&authSource=" + source + "&w=majority"

    val dao1 = new MongoClient(connectionReplica1)
    val dao2 = new MongoClient(connectionReplica2)
    val dao3 = new MongoClient(connectionReplica3)

    println(dao1.insert(Block("test")))

    val document = dao1.read(0)
    document match {
      case Some(value) => prettyPrintBlock(value)
      case None => println("No document found")
    }

    val documents = dao3.show
    for (document <- documents) {
      prettyPrintBlock(document)
    }

    println("Documents: " + dao1.count)
    println("Documents: " + dao2.count)
    println("Documents: " + dao3.count)
    println()
  }

  private def prettyPrintBlock(block: Block): Unit = {
    println("_id: " + block._id)
    println("timestamp: " + block.timestamp)
    println("previousHash: " + block.previousHash)
    println("hash: " + block.hash)
    println("data: " + block.data)
    println("")
  }
}
