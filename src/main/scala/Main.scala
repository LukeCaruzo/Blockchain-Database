import database.MongoDb
import model.Block
import org.mongodb.scala.model.changestream.ChangeStreamDocument
import org.mongodb.scala.{ChangeStreamObservable, Document}
import util.Helpers.LatchedObserver

object Main {
  def main(args: Array[String]): Unit = {
    testDatabaseOperationsReplica()
    // testDatabaseOperationsAuth()
  }

  private def testDatabaseOperationsReplica(): Unit = {
    val replica = "rs"
    val source = "admin"
    val address1 = "localhost:27017"
    val address2 = "localhost:27018"
    val address3 = "localhost:27019"
    val connectionReplica1 = "mongodb://" + address1 + "/?replicaSet=" + replica + "&authSource=" + source + "w=majority"
    val connectionReplica2 = "mongodb://" + address2 + "/?replicaSet=" + replica + "&authSource=" + source + "w=majority"
    val connectionReplica3 = "mongodb://" + address3 + "/?replicaSet=" + replica + "&authSource=" + source + "w=majority"

    val dao1 = new MongoDb(connectionReplica1)
    val dao2 = new MongoDb(connectionReplica2)
    val dao3 = new MongoDb(connectionReplica3)

    println(dao3.insert(Block("test")))

    println("Documents: " + dao1.count)
    println("Documents: " + dao2.count)
    println("Documents: " + dao3.count)
    println()

    val documents = dao1.show
    for (document <- documents) {
      prettyPrintBlock(document)
    }

    testChangeStreams(dao1)
  }

  private def testChangeStreams(dao: MongoDb): Unit = {
    val observable: ChangeStreamObservable[Document] = dao.collection.watch()

    val observer = new LatchedObserver[ChangeStreamDocument[Document]]()
    observable.subscribe(observer)

    dao.insert(Block("test"))
    dao.insert(Block("test"))
    observer.waitForThenCancel()

    val res = observer.results()
    println("result: " + res)
  }

  private def testDatabaseOperationsAuth(): Unit = {
    val user = "admin"
    val password = "test"
    val source = "admin"
    val address = "localhost:27018"
    val connectionAuth = "mongodb://" + user + ":" + password + "@" + address + "/?authSource=" + source

    val dao = new MongoDb(connectionAuth)

    println(dao.insert(Block("test")))

    Thread.sleep(1000)

    val document = dao.read(0)
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

  private def prettyPrintBlock(block: Block): Unit = {
    println("_id: " + block._id)
    println("timestamp: " + block.timestamp)
    println("previousHash: " + block.previousHash)
    println("hash: " + block.hash)
    println("data: " + block.data)
    println("")
  }
}
