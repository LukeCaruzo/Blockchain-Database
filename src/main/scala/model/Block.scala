package model

import java.util.ArrayList

case class Block(id: Int, size: Int, hash: String, transactionList: ArrayList[Transaction] = new ArrayList[Transaction]())
