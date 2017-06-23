package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    Var(math.pow(b(),2)- 4 * a()*c())
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
    Var({
      val deltav = delta();
      val bv=b();
      val av=a();
      val delsq=math.sqrt(deltav)
      if(deltav<0){
        Set()
      }else{
        Set((-1*bv+delsq)/(2*av),(-1*bv-delsq)/(2*av))
      }
    })
  }
}
