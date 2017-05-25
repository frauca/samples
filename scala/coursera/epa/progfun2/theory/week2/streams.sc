object streams{
  def isPrime(n: Int): Boolean = ! ((2 until n-1) exists (n % _ == 0))
  ((1000 to 10000) filter isPrime)
  ((1000 to 10000) filter isPrime)(1)

  ((1000 to 10000).toStream filter isPrime)(1)
}