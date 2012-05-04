object Main {

  def main(args: Array[String]) {

    val parser = ConfigurationParser
    parser.parse(args, Configuration()) map { config =>
      
      println(config)
      val evolver = new CustomEvolver(config)
      evolver.run()
      
    } 
  }
}


