
trait Ord {
  def <(that: Any): Boolean
  def <=(that: Any): Boolean = (this < that) || (this == that)
  def >(that: Any): Boolean = !(this <= that)
  def >=(that: Any): Boolean = !(this < that)
}

class Fecha(d: Int, m: Int, a: Int) extends Ord {
  def anno = a
  def mes = m
  def dia = d
  override def toString(): String = anno + "-" + mes + "-" + dia

  override def equals(that: Any): Boolean =
    that.isInstanceOf[Fecha] && {
      val o = that.asInstanceOf[Fecha]
      o.dia == dia && o.mes == mes && o.anno == anno
    }

  def <(that: Any): Boolean = {
    if (!that.isInstanceOf[Fecha])
      error("no se puede comparar" + that + " y una fecha")
    val o = that.asInstanceOf[Fecha]
    (anno < o.anno) ||
      (anno == o.anno && (mes < o.mes ||
        (mes == o.mes && dia < o.dia)))
  }

}
object Traits extends App {
    def f1 = new Fecha(4,15,2016)
    def f2 = new Fecha(5,15,2016)
    
    println("f1==f1"+(f1<f2))
    println("f1==f1"+(f1>f2))
}