package week1




/**
  * Created by rofc on 27/06/2017.
  */
object SegmentSum {
  def sumSegment(a: Array[Int], p: Double, s: Int, t: Int): Int = {
    var i= s; var sum: Int = 0
    while (i < t) {
      sum= sum + power(a(i), p)
      i= i + 1
    }
    sum
  }

  def sumSegmentRofc(a: Array[Int], p: Double): Int = a.foldRight(0)((b,a) => power(b,p)+ a)

  def power(x: Int, p: Double): Int = math.exp(p * math.log(math.abs(x))).toInt

  def pNorm(a: Array[Int], p: Double): Int =
    power(sumSegment(a, p, 0, a.length), 1/p)

  def pNormRofc(a: Array[Int], p: Double): Int =
    power(sumSegmentRofc(a, p), 1/p)

  def pNormTwoPart(a: Array[Int], p: Double): Int = {
    val m = a.length / 2
    val (sum1, sum2) = (sumSegment(a, p, 0, m),
      sumSegment(a, p, m, a.length))
    power(sum1 + sum2, 1/p) }

  def pNormTwoPartRofc(a: Array[Int], p: Double): Int = {
    val m = a.length / 2
    val (sum1, sum2) =   (sumSegmentRofc(a.take(m),p),
      sumSegmentRofc(a.takeRight(m),p))
    power(sum1 + sum2, 1/p) }

  def pNormRec(a: Array[Int], p: Double): Int =
    power(segmentRec(a, p), 1/p)

  // like sumSegment but parallel
  def segmentRec(a: Array[Int], p: Double) = {
    if (a.length< 2)
      sumSegmentRofc(a, p) // small segment: do it sequentially
    else {
      val m = a.length/2
      val (sum1, sum2) =   (sumSegmentRofc(a.take(m),p),
        sumSegmentRofc(a.takeRight(m),p))
      sum1 + sum2 } }

  def main(args: Array[String]): Unit = {
    val p = 2d
    val a = 1 to 500 toArray;

    println(pNormRec(a,p)) //casi segur que aquesta sera mes rapida.
    println(pNormTwoPart(a,p)) //la meva fara servir molta memoria
  }
}
