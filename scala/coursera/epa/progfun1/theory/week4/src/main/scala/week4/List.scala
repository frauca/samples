package week4

import week3.{Empty, NonEmpty}

trait List[+T] {
  def isEmpty: Boolean

  def head: T

  def tail: List[T]

  def prepend[U >: T](elem: U): List[U] = new Cons(elem, this)
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty: Boolean = false

  override def toString = "(" + head + tail + ")"
}

object Nil extends List[Nothing] {
  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException("Nil.head")

  def tail: Nothing = throw new NoSuchElementException("Nil.tail")

  override def toString = "."
}

object List {
  // List(1, 2) = List.apply(1, 2)
  def apply[T](x1: T, x2: T): List[T] = new Cons(x1, new Cons(x2, Nil))

  def apply[T](x1: T): List[T] = new Cons(x1, Nil)

  def apply[T] = Nil

  def isort(xs: List[T]):List[T] = {
    def insert(x: Int, ys: List[Int]) = ys match{
      case Empty => new List(x)
      case y :: zs => if (x<=y) x:: ys
                      else  y :: insert(x,zs)
    }
    xs match {
      case List() => new List()
      case x :: ys => insert(x, isort(ys))
    }
  }
}


