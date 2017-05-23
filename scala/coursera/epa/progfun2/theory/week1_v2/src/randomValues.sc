
object randomValues{
  trait Generator[+T]{
    def generate: T
  }
//  val integers = new Generator[Int] {
//    val rand = new java.util.Random
//    def generate = rand.nextInt()
//  }
  integers.generate
  val booleans = new Generator[Boolean] {
    def generate = integers.generate > 0
  }
  val pairs = new Generator[(Int, Int)] {
    def generate = (integers.generate, integers.generate)
  }
}

object generators{
  trait Generator[+T] {
    self => // an alias for ”this”.
    def generate: T
    def map[S](f: T => S): Generator[S] = new Generator[S] {
      def generate = f(self.generate)
    }
    def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
      def generate = f(self.generate).generate
    }
  }
  val integers = new Generator[Integer] {
    def generate = scala.util.Random.nextInt()
  }

  val booleans = integers.map(_ => 0)

  5

}