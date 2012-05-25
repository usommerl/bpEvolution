import java.util.Calendar
import java.text.SimpleDateFormat
import scala.math.max
import scala.collection.mutable.ListBuffer

object Formatter {

  val MaxLineLength = 80
  
  def formatResult(individual: Individual, lineLength: Int = MaxLineLength): List[String] = {
    val buffer = new ListBuffer[String]; buffer += ""
    val pref = "# "; val binPref = pref+"bin %0"+individual.phenotype.size.toString.length+"d"
    val capPref = ", capacity: %.1f";  val itemsPref = ", items: %d"
    val rCapPref = ", remaining capacity: %.1f"
    buffer += pref+"-"*(lineLength-pref.length)
    buffer += pref+"best individual"
    buffer += pref+"-"*(lineLength-pref.length)
    individual.phenotype.zipWithIndex.foreach{ case (bin,i) =>
      buffer += binPref.format(i+1)+capPref.format(bin.capacity)+
                itemsPref.format(bin.items.size)+rCapPref.format(bin.remainingCapacity)
      buffer ++= formatItems(bin.items, lineLength)
    }
    buffer.map(adjStrgLength(_, lineLength)).toList
  }

  private def formatItems(items: List[Item], lineLength: Int): List[String] = {
    val buffer = new ListBuffer[String]; val lineBuffer = new StringBuilder
    val pref = "# "; val sep = " " 
    lineBuffer.append(pref)
    items.foreach{ item =>
      if ((lineBuffer.length + item.size.toString.length + sep.length) > lineLength) {
        buffer += lineBuffer.toString
        lineBuffer.clear()
        lineBuffer.append(pref+item.size.toString+sep)
      } else {
        lineBuffer.append(item.size.toString+sep)
      }
    }
    if (!lineBuffer.isEmpty) buffer += lineBuffer.toString
    buffer += pref+"-"*(lineLength-pref.length)
    buffer.toList
  }

  def formatConfiguration(config: Configuration, lineLength: Int = MaxLineLength): List[String] = {
    def getDateString(format: String): String = {
      val cal = Calendar.getInstance();
      val sdf = new SimpleDateFormat(format);
	  sdf.format(cal.getTime());
    } 
    def formatPrefix(prefix: String): String = "#"+" "+adjStrgLength(prefix, 25)+":"+" "*1
    val buffer = new ListBuffer[String]
    buffer += formatPrefix("date")+getDateString("yyyy-MM-dd HH:mm:ss")
    buffer += formatPrefix("problem identifier")+config.problem.id
    buffer += formatPrefix("theoretical optimum")+config.problem.theoreticalOptimum
    buffer += formatPrefix("best known solution")+config.problem.bestKnownSolution
    buffer += formatPrefix("population size")+config.populationSize
    buffer += formatPrefix("genotype decoder")+config.decoderKeyword
    buffer += formatPrefix("quality function")+"q"+config.qualityFunctionKeyword
    buffer += formatPrefix("parent selection")+formatSelection(config.parentSelection)
    buffer += formatPrefix("recombination")+formatRecombination(config.recombination)
    buffer += formatPrefix("mutation(s)")+formatMutations(config.mutations)
    buffer += formatPrefix("environment selection")+formatSelection(config.environmentSelection)
    buffer.map(adjStrgLength(_,lineLength)).toList
  } 

  def formatPopulation(population: Population, 
                       config: Configuration, 
                       lineLength: Int = MaxLineLength): List[String] = {
    val buffer = new ListBuffer[String]; val sep = " "*3 
    var genHeader = "# generation"; var bestHeader = "quality best"; 
    var worstHeader = "quality worst";  var diversityHeader = "diversity"
    val lengthMaxGenerations = config.maxGenerations.toString.length
    val a = config.problem.bestKnownSolution.toString.length+1; val b = 8 
    val bwFor = "%"+a+"."+b+"f"; val genFor = "%"+lengthMaxGenerations+"d"
    val bestWorstLength = max((a+b+1), worstHeader.length)
    val genLength = max(lengthMaxGenerations, genHeader.length)
    val diversityLength = max(diversityHeader.length, config.populationSize.toString.length)
    val genVal = adjStrgLength(genFor.format(population.generation), genLength,"")
    val bestVal = adjStrgLength(bwFor.format(population.best.quality), bestWorstLength,"")
    val worstVal = adjStrgLength(bwFor.format(population.worst.quality), bestWorstLength,"")
    val diversityVal = adjStrgLength("%2.2f".format(population.diversity), diversityLength, "")
    if (population.generation == 0) { // Add column header
      buffer += "" 
      genHeader = adjStrgLength(genHeader, genLength)
      bestHeader = adjStrgLength(bestHeader, bestWorstLength, "")
      worstHeader = adjStrgLength(worstHeader, bestWorstLength, "")
      diversityHeader = adjStrgLength(diversityHeader, diversityLength)
      buffer += adjStrgLength(genHeader+sep+bestHeader+sep+
                              worstHeader+sep+diversityHeader, lineLength)
      buffer += ""
    }
    buffer += adjStrgLength(genVal+sep+bestVal+sep+worstVal+sep+diversityVal, lineLength)
    buffer.toList
  }

  private def formatSelection(s: Selection): String = 
    s match {
      case x if s.isInstanceOf[TournamentSelection] => 
      "tournament [q="+s.asInstanceOf[TournamentSelection].rounds+"]"
      case x if (s == RandomSelection) => "random"
      case x if (s == BestSelection) => "best"
      case _ => throw new Exception("Don't know how to format "+s.toString)
    }

  private def formatRecombination(r: Recombination): String = 
    r match {
      case x if (r == OrderedRecombination) => "ordered"
      case x if (r == PartiallyMappedCrossover) => "mapped"
      case _ => throw new Exception("Don't know how to format "+r.toString)
    }

  private def formatMutations(mutations: List[Mutation]): String = {
    val result = new StringBuilder()
    mutations.foreach{ m =>
      m match {
        case x if (m == InversionMutation) => result.append("inversion,")
        case x if (m == ShiftingMutation) => result.append("shift,")
        case x if (m == ExchangeMutation) => result.append("exchange,") 
        case _ => throw new Exception("Don't know how to format "+m.toString)
      }
    }
    if (result.isEmpty) "none"
    else result.take(result.length-1).toString
  }

  private def adjStrgLength(line: String, length: Int, flag: String = "-"): String = {
    require(line.size <= length, "line longer than expected")
    ("%"+flag+length+"s").format(line)
  }
}

