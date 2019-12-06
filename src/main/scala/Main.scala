import database.MongoDb
import model.Block
import org.mongodb.scala.model.changestream.ChangeStreamDocument
import org.mongodb.scala.{ChangeStreamObservable, Document}
import util.Helpers.LatchedObserver

object Main {
  val user = "admin"
  val password = "test"
  val source = "admin"
  val address = "localhost:27018"
  val replica = "rs"
  val connectionAuth = "mongodb://" + user + ":" + password + "@" + address + "/?authSource=" + source
  val connectionReplica = "mongodb://" + address + "/?replicaSet=" + replica + "&authSource=" + source

  def main(args: Array[String]): Unit = {
    val dao = new MongoDb(connectionReplica)

    // testChangeStreams(dao)
    testDatabaseOperations(dao)
  }

  private def testDatabaseOperations(dao: MongoDb): Unit = {
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

  // TODO: doesn't work yet because no replica sets.
  private def testChangeStreams(dao: MongoDb): Unit = {
    var observable: ChangeStreamObservable[Document] = dao.collection.watch()

    // Create a observer
    var observer = new LatchedObserver[ChangeStreamDocument[Document]]()
    observable.subscribe(observer)

    // Insert a test document into the collection and request a result
    dao.insert(Block("test"))
    observer.waitForThenCancel()

    /*val observable = dao.collection.watch.fullDocument(FullDocument.UPDATE_LOOKUP)
    val observer = new LatchedObserver[ChangeStreamDocument[Document]]()
    observable.subscribe(observer)

    dao.insert(Block("test"))
    // val docOld = collection.find(Filters.eq("username", "alice123")).first().execute()
    // collection.updateOne(Document("{username: 'alice123'}"), Document("{$set : { email: 'NickTest2@example.com'}}")).subscribeAndAwait()

    observer.waitForThenCancel()

    val results = observer.results()

    println(results)*/
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
