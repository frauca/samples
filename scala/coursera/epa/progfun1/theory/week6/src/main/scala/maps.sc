object maps{
  val romanNumerals=Map("I"->1,"V"->5,"X"->10)
  val capitalOfCountries=Map("US"->"Whasinton","France"->"Paris")

  capitalOfCountries("US")
  //capitalOfCountries("Andorra") java.util.NoSuchElementException: key not found: Andorra

  capitalOfCountries.get("US")
  capitalOfCountries.get("Andorra")

  capitalOfCountries.getOrElse("Andorra",Nil)

  val fruit =List("apple","orange","pinapple","pear")

  fruit.sortWith(_.length<_.length)
  fruit.sorted

  fruit.groupBy(_.head)


  class Poly(val terms0: Map[Int, Double]) {
    def this(bindings: (Int, Double)*) = this(bindings.toMap)
    val terms = terms0.withDefaultValue(0.0)
    def +(other: Poly) = new Poly(terms ++ (other.terms map adjust))
    def adjust(term: (Int,Double)):(Int,Double) = {
      val (exp,coef)=term
      exp -> (terms(exp)+coef)
    }

    override def toString: String = (for((exp,coef)<-terms)yield coef+"x^"+exp +" ") mkString " + "
  }

  val p1 = new Poly(1 -> 2.0, 3 -> 4.0, 5 -> 6.2)
  val p2 = new Poly(Map(0 -> 3.0, 3 -> 7.0))

  p1+p2




}