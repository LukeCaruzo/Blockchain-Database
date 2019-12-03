import database.MongoDbDao
import model.Block
import org.mongodb.scala.bson.ObjectId

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "abc123", "admin")

    println(dao.count())

    println(dao.insert(Block("test", "test", "test")))

    Thread.sleep(1000)

    println(dao.read(2))

    Thread.sleep(1000)

    println(dao.show())

    Thread.sleep(1000)
  }
}
