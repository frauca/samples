object waterpouring{
  class Pouring(capacity : Vector[Int]){
    type State = Vector[Int]
    val initialState = capacity map (x=>0)

    trait Move{
      def change(state: State):State
    }
    case class Empty(glass:Int) extends Move{
      def change(state: State) = state updated(glass, 0)
    }
    case class Full(glass:Int) extends Move{
      def change(state: State) = state updated(glass,capacity(glass))
    }
    case class  Pour(from:Int,to:Int) extends Move{
      def change(state: State) = {
        val amount = state(from) min (capacity(to)-state(to))
        state updated (from,state(from) - amount) updated(to,state(to)+amount)
      }
    }

    val glasses = 0 until capacity.length

    val moves =
      (for (g <- glasses ) yield  Empty(g)) ++
      (for (g <- glasses ) yield  Full(g)) ++
      (for (from <- glasses ; to <- glasses if from != to) yield Pour(from,to))

    class Path(history:List[Move],val endState:State){
     /* def endState:State= trackState(history)
      private trackState(history:List[Moves]):State = history match {
        case List() => initialState
        case move::tail => move.change(trackState(tail))
      }the same as the lowe*/
      // moved for optimitzaiondef endState:State = (history foldRight initialState)(_ change _)
      def extend(move:Move) = new Path(move::history,move change(endState))

      override def toString: String = (history.reverse mkString " ") + "-->"+endState

    }

    val initialPath:Path=new Path(Nil,initialState)

    def from(paths:Set[Path],explored:Set[State]):Stream[Set[Path]] = {
      if(paths.isEmpty)  Stream.empty
      else {
        val more = for{
          path <- paths
          next <- moves map path.extend
          if(!(explored contains(next.endState)))
        }yield next
        paths #:: from(more, explored ++ (more map (_.endState)))
      }
    }

    val pathSets = from(Set(initialPath),Set(initialState))

    def solution(target: Int): Stream[Path] ={
      for{
        paths  <- pathSets
        path <- paths
        if (path.endState contains( target))
      }yield path
    }
  }

  val problem = new Pouring(Vector(4,9))
  problem.moves
  problem.pathSets(1)
  problem.solution(6).take(2)
}