import Utils._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.StringBuilder

object Main {

  def main(args: Array[String]) {
    ConfigurationParser.parse(args, Configuration()).map{ configuration =>
      
      def outputToFile(data: Any) = 
        if (configuration.outputFile.isDefined) 
        Utils.appendToFile(configuration.outputFile.get, data.toString)

      def output(data: Any) = { 
        outputToFile(data)
        println(data.toString)
      }

      println()
      var bestIndividual: Individual = null
      formatConfiguration(configuration).foreach{ line => output(line) }
      val evolution = new CustomEvolution(configuration)
      for (population <- evolution) {
        formatPopulation(population, configuration).foreach{ line => output(line) }
        bestIndividual = population.best
      }
      formatResult(bestIndividual).foreach{ line => outputToFile(line) }
      println()
    } 
  }
}

