object monad{
  //Left unit with option
  def funct(x:Int):Some[Int]=Some(x*x)

  def funct2(x:Int):Some[Int]=Some(x-4)

  Some(7).flatMap(funct)
  //f(x)
  funct(7)

  //Righ unit with option
  Some(7).flatMap(x=>Some(x))

  "Associative"
  Some(7) flatMap funct flatMap funct2

  Some(7).flatMap(x => funct(x).flatMap(funct2))
}