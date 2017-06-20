import scala.util.DynamicVariable

object signals{


  class StackableVariable[T](init: T) {
    var values: List[T] = List(init)
    def value: T = values.head
    def withValue[R](newValue: T)(op: => R): R = {
      println("withValue "+newValue.hashCode())
      values = newValue :: values
      try op finally values = values.tail
    }
  }

  object NoSignal extends Signal[Nothing](???) {
    override def computeValue() = ()
  }

  class Signal[T](expr: => T) {
    import Signal._
    private var myExpr: () => T = _
    private var myValue: T = _
    private var observers: Set[Signal[_]] = Set()
    update(expr)
    protected def update(expr: => T): Unit = {
      println("update"+this.hashCode())
      myExpr = () => expr
      computeValue()
      println("after update"+this.hashCode()+" observer "+observers+" caller ")
    }
    protected def computeValue(): Unit = {
      println("compute"+this.hashCode())
      val newValue = caller.withValue(this)(myExpr())
      if (myValue != newValue) {
        myValue = newValue
        val obs = observers
        observers = Set()
        println("call all observers"+this.hashCode()+" "+observers)
        obs.foreach(_.computeValue())
      }
    }
    def apply() = {
      println("call apply "+this.hashCode()+" ob"+observers)
      observers += caller.value
      assert(!caller.value.observers.contains(this),"cyclic signal definition")
      println("after apply "+this.hashCode()+" ob"+observers)
      myValue
    }

    override def toString: String = "Signal"+this.hashCode()+"["+myValue+"]"
  }
  object Signal {
    //val caller = new DynamicVariable[Signal[_]](NoSignal)
    val caller = new StackableVariable[Signal[_]](NoSignal)
    def apply[T](expr: => T) = new Signal(expr)
  }

  class Var[T](expr: => T) extends Signal[T](expr) {
    override def update(expr: => T): Unit = super.update(expr)
  }
  object Var {
    def apply[T](expr: => T) = new Var(expr)
  }

  val s1 = Var(3)
  val s2 = Var(s1()+2)
  s1()
  s1()=2
  /*val s2 = Var(s1()+2)
  s1()
  s2()
  s1()
  val v1 = Var(s2()+5)
  val v2 = Var(v1()-2)
  v1()
  v2()
  v1()=4
  v1()=s2()+3
  v2()
  s1()=4
  v1()
  v2()*/
}