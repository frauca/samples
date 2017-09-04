package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    def balanceRec(idx:Int, counter: Int ): Boolean = {
      if( counter <0 ) { false }
      else if(idx>=chars.length){         counter == 0}
      else if(chars(idx)=='('){   balanceRec(idx+1,counter+1) }
      else if (chars(idx)==')'){  balanceRec(idx+1,counter-1) }
      else{                       balanceRec(idx+1,counter)}
    }
    balanceRec(0,0)
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {
    /**
      * Returns the unmatched right, and the unmatched on the left
      * @param idx
      * @param until
      * @param unmatchRight
      * @param unmatchLeft
      * @return
      */
    def traverse(idx: Int, until: Int, unmatchedRight: Int, unmatchLeft: Int) : (Int,Int) = {
      if(idx>=until){
        //println(s"return travese $balanced $unmatchLeft $idx $until ")
        (unmatchedRight,unmatchLeft)
      }else{
        //println(s"""travese $idx $until $balanced $unmatchLeft -- ${chars(idx)}""")
        var newur=unmatchedRight
        var newul=unmatchLeft

        if(chars(idx)=='('){
          newur=newur+1;
        }else if(chars(idx)==')'){
          if(unmatchedRight>0){
            newur=newur-1;
          }else {
            newul = newul - 1;
          }
        }
        traverse(idx+1,until,newur,newul);
      }
    }

    def reduce(from: Int, until: Int) : (Int,Int) = {
      //println(s"""reduce $until $from $threshold """)
      if(until-from<=threshold){
        val (balanced,unmatchedLeft)=traverse(from,until,0,0)
        //println(s"return reduce $balanced $unmatchedLeft $from $until ")
        (balanced,unmatchedLeft)
      }else{
        val mid = ((until-from)/2)+from
        val ((missingLeftR,missingLeftL),(missingRightR,missingRightL))= parallel(reduce(from,mid),
                                    reduce(mid,until))
        //println(s"return from paralel no  $missingLeftR $missingLeftL $missingRightR $missingRightL  $from $until")
        val right=missingLeftR+missingRightL
        if(right<0){
          (missingRightR,missingLeftL+right)
        }else{
          (missingRightR+right,missingLeftL)
        }
      }
    }

    reduce(0, chars.length) == (0,0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
