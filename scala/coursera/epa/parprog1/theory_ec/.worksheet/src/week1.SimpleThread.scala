package week1

object SimpleThread {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(80); 
  println("Welcome to the Scala worksheet");$skip(26); 
  
  
  var uidCount = 0L;System.out.println("""uidCount  : Long = """ + $show(uidCount ));$skip(99); 
  def getUniqueId(): Long = {
    uidCount = uidCount + 1
    println (uidCount)
    uidCount

  }

  class Atomicon extends Thread{
	  override def run() {
	      println("Hello")
	      val uids = for(i <- 0 until 10) yield getUniqueId()
	      println(uids)
	    }
  };System.out.println("""getUniqueId: ()Long""");$skip(198); 
  
  val t1=new Atomicon;System.out.println("""t1  : week1.SimpleThread.Atomicon = """ + $show(t1 ));$skip(13); 
  t1.start()}
}
