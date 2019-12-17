package model

import org.scalatest._
import org.scalatest.matchers.should.Matchers

class BlockSpec extends WordSpec with Matchers {
  "A Block" when {
    "new" should {
      val block = Block("test")
      "be initalized with" in {
        block._id should be(-1)
        block.timestamp should be("")
        block.previousHash should be("")
        block.hash should be("")
        block.data should be("test")
      }
      "have a toString method" in {
        block.toString should be("Block(-1,,,test)")
      }
    }
  }
}
