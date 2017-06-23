package calculator

sealed abstract class Expr
final case class Literal(v: Double) extends Expr
final case class Ref(name: String) extends Expr
final case class Plus(a: Expr, b: Expr) extends Expr
final case class Minus(a: Expr, b: Expr) extends Expr
final case class Times(a: Expr, b: Expr) extends Expr
final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator {
  def computeValues(
      namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {
    namedExpressions.map{case (key,value)=>(key,Var(eval(value(),namedExpressions)))}
  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = {
    def evalRememvering(ex1: Expr,history:List[Expr]): Double = {
      def addAndReeval(ex2:Expr): Double ={
        if(history.contains(ex2)){
          Double.NaN
        }else{
          evalRememvering(ex2,ex2::history)
        }
      }
      ex1 match {
        case Literal(value)=> value
        case Ref(key) => addAndReeval(getReferenceExpr(key,references))
        case Plus(a,b) => addAndReeval(a) + addAndReeval(b)
        case Minus(a,b) => addAndReeval(a) - addAndReeval(b)
        case Times(a,b) => addAndReeval(a) * addAndReeval(b)
        case Divide(a,b) => addAndReeval(a) / addAndReeval(b)
      }
    }
    evalRememvering(expr,List(expr))
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String,
      references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }
}
