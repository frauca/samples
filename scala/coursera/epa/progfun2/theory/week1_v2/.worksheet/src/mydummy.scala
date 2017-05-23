

object mydummy {
	sealed trait JSON
	case class JSeq(elems: List[JSON]) extends  JSON
	case class JObj(bindings: Map[String,JSON]) extends  JSON
	case class JNum(num: Double) extends  JSON
	case class JStr(str: String) extends  JSON
	case class JBool(b: Boolean) extends  JSON
	case class JNull() extends  JSON;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(358); 
	
  println("Welcome to the Scala worksheet");$skip(52); 
  
  def hello(name:String):String = s"Hello $name";System.out.println("""hello: (name: String)String""");$skip(17); val res$0 = 
  hello("Roger");System.out.println("""res0: String = """ + $show(res$0));$skip(407); 
  
  val data = JObj(Map(
		"firstName" -> JStr("John"),
		"lastName" -> JStr("Smith"),
		"address" -> JObj(Map(
		"streetAddress" -> JStr("21 2nd Street"),
		"state" -> JStr("NY"),
		"postalCode" -> JNum(10021)
		)),
		"phoneNumbers" -> JSeq(List(
		JObj(Map(
		"type" -> JStr("home"), "number" -> JStr("212 555-1234")
		)),
		JObj(Map(
		"type" -> JStr("fax"), "number" -> JStr("646 555-4567")
		)) )) ));System.out.println("""data  : mydummy.JObj = """ + $show(data ));$skip(400); 
		

  def show(json:JSON):String = json match{
    case JSeq(elems) => "[" + (elems map show mkString ", ") + "]"
    case JObj(bindings) =>
			val assocs = bindings map {
			case (key, value) => "\"" + key + "\": " + show(value)
			}
			"{" + (assocs mkString ", ") + "}"
    case JNum(num) => num.toString()
    case JStr(str) => str
    case JBool(b) => b.toString()
  	case JNull() => "null"
  };System.out.println("""show: (json: mydummy.JSON)String""");$skip(19); val res$1 = 
	  
	   show(data);System.out.println("""res1: String = """ + $show(res$1))}
  
}
