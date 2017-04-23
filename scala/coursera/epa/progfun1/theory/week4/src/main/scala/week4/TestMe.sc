object Rofc{
  def nums=List(4,2,3)

  nums reduceLeft(_ + _)
  (nums foldLeft 0)(_ + _)
  nums.foldLeft(0)((r,c) => r+c)
}