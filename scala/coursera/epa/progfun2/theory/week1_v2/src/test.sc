object test {
  val f:String => String = {case "ping" => "pong"}

  f("ping")
  //f("pong") //throws match error

  val q: PartialFunction[String,String] = {case "ping" => "pong"}
  q.isDefinedAt("ping")
  q.isDefinedAt("pong")
  q("ping")

  val x:PartialFunction[List[Int],String] =
  {case Nil => "one"
  case x::y::rest => "two"
  }

  x.isDefinedAt(List(1,2,3))

  val g:PartialFunction[List[Int],String] =
  {case Nil => "one"
    case x::rest =>
      rest match {
        case Nil => "two"
      }
  }

  g.isDefinedAt(List(1,2,3))

  val z:PartialFunction[List[Int],String] =
  {case Nil => "one"
  case x::Nil => "two"
  }

  z.isDefinedAt(List(1,2,3))
}
