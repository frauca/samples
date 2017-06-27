
object SimpleThread{
//  class HelloThread extends Thread {
//    override def run() {
//      println("Hello")
//      println("World!!")
//    }
//  }
//  val t = new HelloThread
//  val t2 = new HelloThread
//  val t3 = new HelloThread
//  t.start()
//  t2.start()
//  t3.start()
//  t.join()
//  t2.join()
//  t3.join()



  for(i <- 0 until 10) yield i

  val t = new Atomicon
  t.start()
  val t2 = new Atomicon
  t2.start()

}