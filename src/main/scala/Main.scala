object Main {

  def main(args: Array[String]) {

    val parser = ConfigurationParser
    parser.parse(args, Configuration()) map { configuration =>
      
      println(configuration)
      val evolution = new CustomEvolution(configuration)
      for (population <- evolution) {
        println(population)
      }
      
    } 
  }
}


