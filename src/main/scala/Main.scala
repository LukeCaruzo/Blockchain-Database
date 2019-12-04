import database.MongoDb
import model.Block

object Main {
  val user: String = "myUserAdmin"
  val password: String = "abc123"
  val role: String = "admin"

  def main(args: Array[String]): Unit = {
    //val dao = new MongoDbDao("myUserAdmin", "abc123", "admin")

    println(MongoDb.count)

    println(MongoDb.insert(Block("test")))

    Thread.sleep(1000)

    println(MongoDb.read(2))

    Thread.sleep(1000)

    println(MongoDb.show)

    Thread.sleep(1000)
  }
}
