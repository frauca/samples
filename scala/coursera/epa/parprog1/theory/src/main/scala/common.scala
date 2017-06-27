import scala.runtime.Nothing$

/**
  * Created by rofc on 27/06/2017.
  */
package object common {

  trait  Task[T]{
    def join: T
  }
  def task[T](body: => T):Task[T] = {
    class MyTask[T] extends Task[T]  {
      var res:T = null.asInstanceOf[T]
      val t=new MakeItParallel
      override def join = {
        if(res  == null){
          t.start()
          t.join()
        }
        res
      }
      class MakeItParallel extends Thread{
        override def run(): Unit = {
          res=body
        }
      }



    }

    new MyTask[T]
  }

  def parallel[A,B](taskA: =>A,taskB: =>B): (A,B) = {
    val ta = task{taskA}
    val tb = task{taskB}
    (ta.join,tb.join)
  }
}
