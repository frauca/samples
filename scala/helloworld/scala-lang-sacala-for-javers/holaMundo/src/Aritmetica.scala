abstract class Arbol
case class Sum(l: Arbol, r: Arbol) extends Arbol
case class Var(n: String) extends Arbol
case class Const(v: Int) extends Arbol

object Aritmetica extends App {
  type Entorno = String => Int
  def eval(a: Arbol, ent: Entorno): Int = a match {
    case Sum(i, d) => eval(i, ent) + eval(d, ent)
    case Var(n)    => ent(n)
    case Const(v)  => v
  }
  def derivada(a: Arbol, v: String): Arbol = a match {
    case Sum(l, r)          => Sum(derivada(l, v), derivada(r, v))
    case Var(n) if (v == n) => Const(1)
    case _                  => Const(0)
  }

  val exp: Arbol = Sum(Sum(Var("x"), Var("x")), Sum(Const(7), Var("y")))
  val ent: Entorno = { case "x" => 5 case "y" => 7 }
  println("Expresión: " + exp)
  println("Evaluación con x=5, y=7: " + eval(exp, ent))
  println("Derivada con respecto a x (y=>1):\n " + derivada(exp, "x")+" ==  "+eval(derivada(exp, "x"),{case "y"=>1}))
  println("Derivada con respecto a y (x=>1):\n " + derivada(exp, "y")+" ==  "+eval(derivada(exp, "y"),{case "x"=>1}))
}