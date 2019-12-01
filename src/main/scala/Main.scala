import database.mongodb.MongoDbDao
import model.Transaction

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "test123", "admin")

    dao.update(Transaction.apply("test", "resr", 1, "resr"))
  }
}
