package util

import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.mongodb.scala.{MongoTimeoutException, Observable, Observer, SingleObservable, Subscription, _}

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/** Observable Helpers
 *
 * @see https://github.com/mongodb/mongo-scala-driver/blob/master/examples/src/test/scala/tour/Helpers.scala
 * @see https://github.com/mongodb/mongo-scala-driver/blob/master/driver/src/it/scala/org/mongodb/scala/DocumentationChangeStreamExampleSpec.scala
 */
object Helpers {
  val waitDuration = Duration(5, "seconds")

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: (C) => String

    def printResults(initial: String = ""): Unit = {
      if (initial.length > 0) print(initial)
      results().foreach(res => println(converter(res)))
    }

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))

    def printHeadResult(initial: String = ""): Unit = println(s"${initial}${converter(headResult())}")

    def headResult() = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))
  }

  implicit class SingleObservableExecutor[T](observable: SingleObservable[T]) {
    def execute(): T = Await.result(observable.toFuture(), waitDuration)
  }

  implicit class ObservableExecutor[T](observable: Observable[T]) {
    def execute(): Seq[T] = Await.result(observable.toFuture(), waitDuration)

    def subscribeAndAwait(): Unit = {
      val observer: LatchedObserver[T] = new LatchedObserver[T](false)
      observable.subscribe(observer)
      observer.await()
    }
  }

  implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] {
    override val converter: (Document) => String = (doc) => doc.toJson
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: (C) => String = (doc) => doc.toString
  }

  class LatchedObserver[T](val printResults: Boolean = true, val minimumNumberOfResults: Int = 1) extends Observer[T] {
    private val latch: CountDownLatch = new CountDownLatch(1)
    private val resultsBuffer: mutable.ListBuffer[T] = new mutable.ListBuffer[T]
    private var subscription: Option[Subscription] = None
    private var error: Option[Throwable] = None

    override def onSubscribe(s: Subscription): Unit = {
      subscription = Some(s)
      s.request(Integer.MAX_VALUE)
    }

    override def onNext(t: T): Unit = {
      resultsBuffer.append(t)
      if (printResults) println(t)
      if (resultsBuffer.size >= minimumNumberOfResults) latch.countDown()
    }

    override def onError(t: Throwable): Unit = {
      error = Some(t)
      println(t.getMessage)
      onComplete()
    }

    override def onComplete(): Unit = {
      latch.countDown()
    }

    def results(): List[T] = resultsBuffer.toList

    def waitForThenCancel(): Unit = {
      if (minimumNumberOfResults > resultsBuffer.size) await()
      subscription.foreach(_.unsubscribe())
    }

    def await(): Unit = {
      if (!latch.await(60, SECONDS)) throw new MongoTimeoutException("observable timed out")
      if (error.isDefined) throw error.get
    }
  }

}
