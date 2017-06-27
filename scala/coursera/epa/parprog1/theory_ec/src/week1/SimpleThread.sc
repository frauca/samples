package week1

object SimpleThread {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  
  var uidCount = 0L                               //> uidCount  : Long = 0
  def getUniqueId(): Long = {
    uidCount = uidCount + 1
    println (uidCount)
    uidCount

  }                                               //> getUniqueId: ()Long

  class Atomicon extends Thread{
	  override def run() {
	      println("Hello")
	      val uids = for(i <- 0 until 10) yield getUniqueId()
	      println(uids)
	    }
  }
  
  val t1=new Atomicon                             //> t1  : week1.SimpleThread.Atomicon = Thread[Thread-0,5,main]
  t1.start()
}