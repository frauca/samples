



object JSONExec{;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(43); 
	
	
  val desc="desc";System.out.println("""desc  : String = """ + $show(desc ));$skip(162); 
	
	val value=JObj(Map(
			"firstName" -> JStr("Jhon"),
			"address" -> JObj(Map(
				"street" -> JStr("21 snd street"),
				"postalCode" -> JNum(2100)
			))
		));System.out.println("""value  : JObj = """ + $show(value ));$skip(7); val res$0 = 
	value;System.out.println("""res0: JObj = """ + $show(res$0))}
}
