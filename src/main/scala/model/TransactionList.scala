package model

import java.security.MessageDigest
import java.util._

import cryptography.{ECDSA, Key}

class TransactionList extends ArrayList[Transaction] {
  def add(privateKey: Key, transaction: Transaction): Boolean = {
    transaction.publicKey = privateKey.pub.compress.toString
    transaction.previousHash = getPreviousHash
    transaction.hash = generateHash(transaction)
    transaction.signedHash = ECDSA.sign(privateKey, transaction.hash, "SHA-256").toString

    this.add(transaction)
  }

  private def generateHash(transaction: Transaction): String = {
    return MessageDigest.getInstance("SHA-256").digest(transaction.toString.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }

  private def getPreviousHash: String = {
    if (this.isEmpty) {
      return ""
    } else {
      val previousTransaction = this.get(this.size() - 1);

      return previousTransaction.hash
    }
  }
}
