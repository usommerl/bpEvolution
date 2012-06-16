import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import Formatter._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.StringBuilder

object Main {

  def main(args: Array[String]) {
    ConfigurationParser.parse(args, Configuration()).map{ configuration =>
      
      def outputToFile(data: Any) = 
        if (configuration.outputFile.isDefined) 
        appendToFile(configuration.outputFile.get, data.toString)

      def output(data: Any) = { 
        outputToFile(data)
        println(data.toString)
      }

      println()
      var bestIndividual: Individual = null
      formatConfiguration(configuration).foreach{ line => output(line) }
      val evolution = new Evolution(configuration)
      val stopWatch = new StopWatch(StopWatch.Precision.MILLISECONDS)
      stopWatch.start()
      for (population <- evolution) {
        formatPopulation(population, configuration).foreach{ line => output(line) }
        bestIndividual = population.best
      }
      output( ("\n# Total time: %.1f s").format(stopWatch.stop()/1000.0) )
      formatResult(bestIndividual).foreach{ line => outputToFile(line) }
      println()
    } 
  }

  private def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
    try { f(param) } finally { param.close() }
 
  private def appendToFile(file:File, textData:String) =
      using (new FileWriter(file, true)){
        fileWriter => using (new PrintWriter(fileWriter)) {
          printWriter => printWriter.println(textData)
        }
      }
}

