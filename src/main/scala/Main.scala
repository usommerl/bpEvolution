object Main {

    def main(args: Array[String]) {
        val problems = Utils.beasleyProblemInstances

        if (args.length < 1) {
          printHelp()
        } else if (problems.get(args(0)).isDefined) {
          val problem = problems.get(args(0)).get
          val population = Population.initialize(25, problem)
          println(problem)
          population.individuals.foreach(println)
          println("Max: "+ population.individuals.maxBy(_.quality()))
        }else {
          println("Problem \"" + args(0) +"\" not found")
        }          
    }

    private def printHelp() {
        println("CMD-HElp")
    }
}
