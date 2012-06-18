import java.io.File
import scala.io.Source
import scala.collection.mutable.ListBuffer

object Ressources {

  lazy val ProblemInstances = loadProblemInstances()

  private def loadProblemInstances(): Map[String, BinPackProblem] = {
    val filenamesProblems = new ListBuffer[String]
    (1 to 8).foreach(i => filenamesProblems += ("binpack%d.txt").format(i))
    val problemBuffer = new ListBuffer[BinPackProblem]
    filenamesProblems.foreach(name => problemBuffer ++= readResource(name))
    problemBuffer.map(problem => (problem.id, problem)).toMap
  }

  private def readResource(resource: String): List[BinPackProblem] = {
    val problemBuffer = new ListBuffer[BinPackProblem]
    val it = Source.fromURL(getClass.getResource(resource)).getLines()
    val numberOfProblems = if (it.hasNext) it.next.toInt
    while(it.hasNext) {
      val line = it.next
      if (line.matches("""^\s[ut]\d{2,4}_\d{2}\s*$"""))
        problemBuffer += readInstance(it, line.trim())
    }
    assert(numberOfProblems == problemBuffer.length, 
           "Expected "+numberOfProblems+" but read "+problemBuffer.length+" problems")  
    problemBuffer.toList
  }

  private def readInstance(it: Iterator[String], problemID: String): BinPackProblem = {
    val itemBuffer = new ListBuffer[Item]
    val properties = it.next.trim().split("\\s")
    val binCapacity = properties(0).toDouble
    val numberOfItems = properties(1).toInt
    val bestKnownSolution = properties(2).toInt
    for (i <- 1 to numberOfItems) {
      if (it.hasNext)
        itemBuffer += new Item(i ,it.next().toDouble)
    }
    assert(numberOfItems == itemBuffer.length, 
           "Expected "+numberOfItems+" but read "+itemBuffer.length+" items")
    new BinPackProblem(problemID, binCapacity, bestKnownSolution, itemBuffer.toList)
  }
}
