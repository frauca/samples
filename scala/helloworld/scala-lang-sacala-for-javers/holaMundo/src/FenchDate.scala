import java.util.{Date,Locale}
import java.text.DateFormat._


object FenchDate extends App {
  val ahora = new Date
  val df = getDateInstance(LONG, Locale.FRANCE)
  println(df format ahora)
}