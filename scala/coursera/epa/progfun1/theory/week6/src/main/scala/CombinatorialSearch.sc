object CombinatorialSearch{
  def fruits:Set[String] = Set("apple","bannana")
  val s = (1 to 6).toSet

  s map (_ +2)
  s map ( _ / 2)

  def nqueens(n:Int):Set[List[Int]] = {
    def isSafe(col:Int, queens:List[Int]): Boolean ={
      def row = queens.length
      def queensPos = (row-1 to 0 by -1) zip queens
      queensPos forall{
        case (r,c) => col != c && math.abs(col -c) != row -r
      }
    }
    def placeQueens(k: Int):Set[List[Int]] =
      if(k==0) Set(List())
      else
        for{
          queens <- placeQueens(k-1)
          col <- (0 until n)
          if isSafe(col,queens)
        } yield col::queens
      placeQueens(n)
  }

  nqueens(4)

  def show(queens:List[Int]):String = {
    val lines =
      for(col <- queens.reverse)
        yield Vector.fill(queens.length)("* ").updated(col,"X ").mkString
    "\n" + (lines mkString "\n")
  }

  (nqueens(8) take 3 map show) mkString "\n"
}