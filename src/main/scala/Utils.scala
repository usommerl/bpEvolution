import java.io.File
import scala.io.Source
import scala.collection.mutable.ListBuffer

object Utils {

  lazy val beasleyProblemInstances = loadBeasleyProblemInstances()

  private def loadBeasleyProblemInstances(): Map[String, BinPackProblem] = {
    val resourceDirectory = new File(getClass.getResource(".").toURI())
      val filenamesProblems = resourceDirectory.list().filter(_.matches("binpack\\d*\\.txt"))
      val problemBuffer = new ListBuffer[BinPackProblem]
    filenamesProblems.foreach(name => problemBuffer ++= readBeasleyResource(name))
    problemBuffer.map(problem => (problem.id, problem)).toMap
  }

  private def readBeasleyResource(resource: String): List[BinPackProblem] = {
    val problemBuffer = new ListBuffer[BinPackProblem]
    val it = Source.fromURL(getClass.getResource(resource)).getLines()
    val numberOfProblems = if (it.hasNext) it.next.toInt

    while(it.hasNext) {
      val line = it.next
      if (line.matches("""^\s[ut]\d{2,4}_\d{2}\s*$""")) {
        problemBuffer += readBeasleyInstance(it, line.trim())
      }
    }
    assert(numberOfProblems == problemBuffer.length, "Expected "+numberOfProblems+" but read "+problemBuffer.length+" problems")  
    problemBuffer.toList
  }

  private def readBeasleyInstance(it: Iterator[String], problemID: String): BinPackProblem = {
    val itemBuffer = new ListBuffer[Item]
    val properties = it.next.trim().split("\\s")
    val binCapacity = properties(0).toDouble
    val numberOfItems = properties(1).toInt
    val bestKnownSolution = properties(2).toInt
    for (i <- 1 to numberOfItems) {
      if (it.hasNext)
        itemBuffer += new Item((problemID+"_%0"+numberOfItems.toString.length+"d").format(i), it.next().toDouble)
    }
    assert(numberOfItems == itemBuffer.length, "Expected "+numberOfItems+" but read "+itemBuffer.length+" items")
    new BinPackProblem(problemID, binCapacity, bestKnownSolution, itemBuffer.toList)
  }
}
