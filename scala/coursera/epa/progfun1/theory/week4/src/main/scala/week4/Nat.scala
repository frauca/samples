package week4


/**
  * Created by rofc on 29/03/2017.
  */
abstract class Nat {
  def isZero: Boolean
  def successor: Nat =  new Succ(this)
  def predecessor: Nat
  def +(that: Nat): Nat
  def -(that: Nat): Nat
}

object Zero extends  Nat{
  def isZero = true

  def predecessor = throw new Error("No predecessor for 0")

  def +(that: Nat): Nat = that

  def -(that: Nat): Nat = if(that.isZero) Zero else throw new Error("negative number")
}

class Succ(n: Nat) extends Nat{
  def isZero: Boolean = false



  def predecessor: Nat = n

  def +(that: Nat): Nat = new Succ(n + that)

  def -(that: Nat): Nat = {
    if (that.isZero) this
    else n - that.predecessor
  }
}