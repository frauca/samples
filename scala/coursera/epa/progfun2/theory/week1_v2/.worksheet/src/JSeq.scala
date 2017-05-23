
sealed trait JSON
case class JSeq(elems: List[JSON]) extends  JSON
case class JObj(bindings: Map[String,JSON]) extends  JSON
case class JNum(num: Double) extends  JSON
case class JStr(str: String) extends  JSON
case class JBool(b: Boolean) extends  JSON
case class JNull() extends  JSON

object mydummy {
	def jtest = JStr("test")
  println("Welcome to the Scala worksheet")
  
  def hello(name:String):String = s"Hello $name"
  hello("Roger")
  
  
}
