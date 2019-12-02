import database.MongoDbDao
import model.Transaction
import org.mongodb.scala.bson.ObjectId

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "abc123", "admin")

    dao.update(Transaction.apply("test", "test", 1, "test"))

    Thread.sleep(1000)

    dao.read(0)

    Thread.sleep(1000)
  }
}
