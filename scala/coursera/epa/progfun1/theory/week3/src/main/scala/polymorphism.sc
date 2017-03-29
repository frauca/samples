trait List[T]{
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
  def nth(n:Int): T
}

class Cons[T](val head:T,val tail : List[T]) extends List[T]{
  def isEmpty: Boolean = false;

  def nth(n: Int): T = {
    if (n == 0) head
    else tail.nth(n - 1)
  }
}

class Nil[T] extends List[T]{
  def isEmpty= true;
  def head: Nothing = throw  new NoSuchElementException("Theres no head on Nil")
  def tail: Nothing = throw new NoSuchElementException("Theres no tail on Nil")

  def nth(n: Int): T = throw new IndexOutOfBoundsException("No such element")
}