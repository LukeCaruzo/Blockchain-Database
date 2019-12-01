package database.mongodb

import database.DaoTrait
import org.mongodb.scala.MongoClient

class MongoDbDao(user: String, password: String, auth: String) extends DaoTrait {
  val client = MongoClient("mongodb://" + user + ":" + password + "@localhost:27017/?authSource=" + auth)
  val database = client.getDatabase("blockchain")
  val collection = database.getCollection("Transactions")

  override def update() {
    /*
    val docCount = collection.countDocuments().execute()
    val privateKey = getPrivateKeyFromLocation(locationPrivate)
    val pubKeyReceiver = getPublicKeyFromLocation(locationPublic, index)
    if (checkForPubSigPair(privateKey, collection)) {
      val sig = ECDSA.sign(privateKey, value.toString, "SHA-256")
      val pubKey = privateKey.pub.compress
      val hash = getAndHashBefore(sig, value, pubKey, collection: MongoCollection[Document])

      collection.insertOne(Document("_id" -> (docCount + 1).toInt, "sig" -> sig.toString(16), "hash" -> hash, "value" -> value, "pub" -> pubKeyReceiver.toString(16)))
        .subscribe((res: Completed) => println(res))
    } else {
      throw new Exception("This user is not allowed to insert a Transaction")
    }
    */
  }

  override def read() {

  }

  override def create() {
    throw new Exception("Unsupported action")
  }

  override def delete() {
    throw new Exception("Unsupported action")
  }
}
