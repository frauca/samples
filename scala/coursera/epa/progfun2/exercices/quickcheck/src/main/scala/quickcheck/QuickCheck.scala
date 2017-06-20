package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] ={
    for {
      i <- arbitrary[Int]
      d <- oneOf(const(empty), genHeap)
    } yield insert(i, d)
  }
  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("singleElement") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("minOfTwo") = forAll { (a: Int,b:Int) =>
    val h = insert(a, insert(b,empty))
    findMin(h) == Math.min(a, b)
  }

  property("delMin") = forAll { a: Int =>
    val h = insert(a, empty)
    val h1 = deleteMin(h)
    isEmpty(h1)
  }

  property("alwaysMin") = forAll { (h: H) =>
    def isNextMinSmaller(h: H): Boolean =
      if (isEmpty(h)) true
      else {
        val th = deleteMin(h)
        isEmpty(th) || (findMin(h) <= findMin(th) && isNextMinSmaller(th))
      }
    isNextMinSmaller(h)
  }

  property("minOfTwoIsMinOfBoth") = forAll { (h1: H,h2: H) =>
    val minofeach=Math.min(findMin(h1),findMin(h2))
    minofeach == findMin(meld(h1,h2))
  }

  property("other try") = forAll { (h1: H, h2: H) =>
    def areEqual(h1: H, h2: H): Boolean =
      if (isEmpty(h1) && isEmpty(h2)) true
      else findMin(h1) == findMin(h2) && areEqual(deleteMin(h1), deleteMin(h2))
    areEqual(meld(h1, h2), meld(deleteMin(h1), insert(findMin(h1), h2)))
  }

}
