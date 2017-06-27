package week1

/**
  * Created by rofc on 27/06/2017.
  */
object SimpleApp {

  private var uidCount = 0L
  private var insideruidCount = 0L

  def getUniqueId(): Long = {
    uidCount = uidCount + 1
    uidCount
  }

  class Atomic extends Thread{

    def getInsiderUniqueId(): Long = {
      insideruidCount = insideruidCount + 1
      insideruidCount
    }

    override def run() {
      val uids = for(i <- 0 to 10) yield getUniqueId()
      println ("outsider"+uids)
      val uids2 = for(i <- 0 to 10) yield getInsiderUniqueId
      println ("insider"+uids2)
    }
  }




  def main(args: Array[String]): Unit = {
    println ("Aixo funciona?")
    val a = new Atomic
    val b = new Atomic
    a.start()
    b.start()
  }
}
