import database.mongodb.MongoDbDao

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "test123", "admin")

    dao.delete()
  }
}
