import java.io.File
import scala.io.Source
import scala.collection.mutable.ListBuffer

object Utils {

    def getProblems(): Map[String, BinPackProblem] = {
      val resourceDirectory = new File(getClass.getResource(".").toURI())
      val filenamesProblems = resourceDirectory.list().filter(_.matches("binpack\\d*\\.txt"))
      val problemBuffer = new ListBuffer[BinPackProblem]
      filenamesProblems.foreach(name => problemBuffer ++= readProblemsFromResource(name))
      problemBuffer.map(problem => (problem.id, problem)).toMap
    }

    private def readProblemsFromResource(resource: String): List[BinPackProblem] = {
        val problemBuffer = new ListBuffer[BinPackProblem]
        val it = Source.fromURL(getClass.getResource(resource)).getLines()
        val numberOfProblems = if (it.hasNext) it.next.toInt

        while(it.hasNext) {
            val line = it.next
            if (line.matches("(^\\su.*$)|(^\\st.*$)")) {
                problemBuffer += readSingleProblem(it, line.trim())
            }
        }
        assert(numberOfProblems == problemBuffer.length)  
        problemBuffer.toList
    }

    private def readSingleProblem(it: Iterator[String], id: String): BinPackProblem = {
      val itemBuffer = new ListBuffer[Item]
      val properties = it.next.trim().split("\\s")
      val binCapacity = properties(0).toDouble
      val numberOfItems = properties(1).toInt
      val bestKnownSolution = properties(2).toInt
      for (i <- 1 to numberOfItems) {
        if (it.hasNext)
            itemBuffer += new Item(i, it.next().toDouble)
      }
      assert(numberOfItems == itemBuffer.length)
      new BinPackProblem(id, binCapacity, bestKnownSolution, itemBuffer.toList)
    }
}
