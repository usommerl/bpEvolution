import scala.io.Source
import scala.collection.mutable.ListBuffer

object Utils {

    def readProblemsFromResource(resource: String): List[BinPackProblem] = {
        val problemBuffer = new ListBuffer[BinPackProblem]()
        val itemBuffer = new ListBuffer[Int]()
        val it = Source.fromURL(getClass.getResource(resource)).getLines()
        val numberOfProblems = if (it.hasNext) it.next().toInt

        while(it.hasNext) {
            val line = it.next()
            if (line.matches("(^\\su.*$)|(^\\st.*$)")) {
                val identifier = line.trim()
                val prop = it.next().trim().split("\\s")
                val capacity = prop(0).toInt
                val numberOfItems = prop(1).toInt
                val bestKnownSolution = prop(2).toInt
                itemBuffer.clear()
                for (i <- 1 to numberOfItems) {
                    itemBuffer += it.next().toInt 
                }
                assert(numberOfItems == itemBuffer.length)
                problemBuffer += new BinPackProblem(identifier, capacity, numberOfItems, bestKnownSolution, itemBuffer.toList)
            }
        }
        assert(numberOfProblems == problemBuffer.length)  
        problemBuffer.toList
    }


    def printProblem(problem: BinPackProblem) {
      println("identifier: " + problem.identifier)
      println("container capacity: " + problem.containerCapacity)
      println("number of items: " + problem.numberOfItems)
      println("best known solution: " + problem.bestKnownSolution)
      print("items: ")
      problem.items.foreach(item => print(item + " "))
      println("\n" + "-" * 100)
    }
}
