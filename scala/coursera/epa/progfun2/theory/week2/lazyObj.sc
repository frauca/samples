object lazyObj{
 val x = { println("x"); 1 }
 lazy val y = { println("y"); 2 }
 def z = { println("z"); 3 }
 z + y + x + z + y + x

 def from(n: Int): Stream[Int] = n #:: from(n+1)

 val nats = from(0)
 nats(2)
 val m4s = nats map (_ * 4)
 m4s(2)

 def sieve(s: Stream[Int]): Stream[Int] =
  s.head #:: sieve(s.tail filter (_ % s.head != 0))

 val primes = sieve(from(2))
 primes(1000)

 def sqrtStream(x: Double): Stream[Double] = {
  def improve(guess: Double) = (guess + x / guess) / 2
  lazy val guesses: Stream[Double] = 1 #:: (guesses map improve)
  guesses
 }

 sqrtStream(12)(4)

}