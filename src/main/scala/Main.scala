import database.MongoDbDao
import model.Transaction
import org.mongodb.scala.bson.ObjectId

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "abc123", "admin")

    dao.update(Transaction.apply("test", "test", 1, "test"))
    dao.read(2)

    Thread.sleep(1000)
  }
}
