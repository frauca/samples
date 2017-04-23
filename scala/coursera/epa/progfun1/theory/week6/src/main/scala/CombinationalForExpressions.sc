object CombinationalForExpressions{

  def isPrime(n:Int) = (2 until n) forall (x => n%x!=0)

  isPrime(4)
  isPrime(5)

  val n=7;

  ((1 until n) map (i =>
    (1 until i) map (j => (i,j)))).flatten

  (1 until n) flatMap(i =>
    (1 until i) map (j => (i,j))) filter( pair => isPrime(pair._1+pair._2))

  for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i + j)
  }
    yield (i,j)

  def scalarProduct (xs: List[Double], ys: List[Double]):Double = {
    (for{
      x<- xs
      y<- xs
    } yield (x*y)).sum
  }

  def scalarProduct2 (xs: List[Double], ys: List[Double]):Double =
    (for((x,y)<- xs zip ys) yield (x*y)).sum

}