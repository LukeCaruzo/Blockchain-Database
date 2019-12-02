import database.MongoDbDao
import model.Transaction

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "abc123", "admin")

    dao.update(Transaction.apply("test", "test", 1, "test"))
    //dao.read()

    Thread.sleep(100)
  }
}
