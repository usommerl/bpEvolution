import Utils._

object Main {

    def main(args: Array[String]) {
      if (args.length > 0) {
            val problems = readProblemsFromResource(args(0))
            problems.foreach(printProblem)
       } else
         Console.err.println("File path missing")
    }
}
