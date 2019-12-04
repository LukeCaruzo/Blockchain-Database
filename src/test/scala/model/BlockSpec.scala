package model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BlockSpec extends WordSpec with Matchers {
  "A Block" when {
    "new" should {
      val block = new Block(0, "1575488199270", "test", "test", "test")
      "have a toString method" in {
        block.toString should be("Block(0,1575488199270,test,test)")
      }
    }
  }
}
