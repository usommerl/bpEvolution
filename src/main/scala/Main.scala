object Main {

  def main(args: Array[String]) {
    val problems = Utils.beasleyProblemInstances

    if (args.length < 1) {
      printHelp()
    } else if (problems.get(args(0)).isDefined) {
      val problem = problems.get(args(0)).get
      val evolver = new Evolver(problem)
      evolver.run()
    }else {
      println("Problem \"" + args(0) +"\" not found")
    }          
  }

  private def printHelp() {
    println("Help")
    
  }
}
