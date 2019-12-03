import database.MongoDbDao
import model.Block
import org.mongodb.scala.bson.ObjectId

object Main {
  def main(args: Array[String]): Unit = {
    val dao = new MongoDbDao("myUserAdmin", "abc123", "admin")

    println(System.currentTimeMillis)

    dao.update(Block(0, "test", "test", "test"))

    Thread.sleep(1000)
    /*
        dao.read(0)

        Thread.sleep(1000)*/
  }
}
