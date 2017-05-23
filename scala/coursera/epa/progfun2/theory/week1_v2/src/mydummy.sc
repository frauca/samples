

object mydummy {
	sealed trait JSON
	case class JSeq(elems: List[JSON]) extends  JSON
	case class JObj(bindings: Map[String,JSON]) extends  JSON
	case class JNum(num: Double) extends  JSON
	case class JStr(str: String) extends  JSON
	case class JBool(b: Boolean) extends  JSON
	case class JNull() extends  JSON
	
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  def hello(name:String):String = s"Hello $name"  //> hello: (name: String)String
  hello("Roger")                                  //> res0: String = Hello Roger
  
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
		)) )) ))                          //> data  : mydummy.JObj = JObj(Map(firstName -> JStr(John), lastName -> JStr(Sm
                                                  //| ith), address -> JObj(Map(streetAddress -> JStr(21 2nd Street), state -> JSt
                                                  //| r(NY), postalCode -> JNum(10021.0))), phoneNumbers -> JSeq(List(JObj(Map(typ
                                                  //| e -> JStr(home), number -> JStr(212 555-1234))), JObj(Map(type -> JStr(fax),
                                                  //|  number -> JStr(646 555-4567)))))))
		

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
  }                                               //> show: (json: mydummy.JSON)String
	  
	   show(data)                             //> res1: String = {"firstName": John, "lastName": Smith, "address": {"streetAd
                                                  //| dress": 21 2nd Street, "state": NY, "postalCode": 10021.0}, "phoneNumbers":
                                                  //|  [{"type": home, "number": 212 555-1234}, {"type": fax, "number": 646 555-4
                                                  //| 567}]}
  
}