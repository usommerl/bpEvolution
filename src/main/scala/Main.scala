
object Main {

    def main(args: Array[String]) {
        val problems = Utils.getProblems()
        problems.values.toList.sort(_.id < _.id).foreach(println)

        if (args.length < 1) {
          printHelp()
        } else if (problems.get(args(0)).isDefined) {
//          println(problems.get(args(0)).get)
            
        }else {
          println("Problem \"" + args(0) +"\" not found")
        }          
    }

    private def printHelp() {
        println("CMD-HElp")
    
    }
}
