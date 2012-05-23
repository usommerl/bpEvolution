import java.io.File
import scala.math.pow
import scopt.immutable.OptionParser
import scala.collection.mutable.ListBuffer

case class Configuration(
  problem: BinPackProblem = null, 
  populationSize: Int = 500, 
  maxGenerations: Int = 1000,
  parentSelectionImpact: Int = 3,
  parentSelection: Selection = TournamentSelection(), 
  recombination: Recombination = PartiallyMappedCrossover, 
  mutations: List[Mutation] = List(ShiftingMutation), 
  environmentSelection: Selection = TournamentSelection(),
  decoderKeyword: String = ConfigurationParser.KeywordBestFitDecoder,
  qualityFunctionKeyword: String = ConfigurationParser.KeywordQualityFunction1,
  earlyAbort: Boolean = true,
  outputFile: Option[File] = None
) {
  lazy val genotypeDecoder = this.decoderKeyword match {
    case ConfigurationParser.KeywordNextFitDecoder => NextFitDecoder(problem)
    case ConfigurationParser.KeywordFirstFitDecoder => FirstFitDecoder(problem)
    case ConfigurationParser.KeywordBestFitDecoder => BestFitDecoder(problem)
  }

  lazy val qualityFunction = this.qualityFunctionKeyword match {
    case ConfigurationParser.KeywordQualityFunction1 => qF1
    case ConfigurationParser.KeywordQualityFunction2 => qF2
  }

  private val qF1: Phenotype => Double = (phenotype: Phenotype) => phenotype.size.toDouble
  
  private val qF2: Phenotype => Double = (phenotype: Phenotype) => {
    val quotient = ( 0.0 /: phenotype ) ( (sum,bin) => sum + pow(bin.remainingCapacity, 2.0))
    val divisor = pow(this.problem.binCapacity, 2.0) * this.problem.items.size
    phenotype.size.toDouble + (quotient / divisor)
  }
}

object ConfigurationParser extends OptionParser[Configuration]("bpEvolver") {

      val iLF = "\n"++" "*8
      val defaultPrefix = "The default value is '"
      val defaulSuffix = "'"
      val selectionValues = iLF ++ "Valid SELECTION keywords are: best|random|tournament=<INTEGER>"
      val KeywordBestFitDecoder = "best-fit"
      val KeywordFirstFitDecoder = "first-fit"
      val KeywordNextFitDecoder = "next-fit"
      val KeywordQualityFunction1 = "1"
      val KeywordQualityFunction2 = "2"
      
      def options = { Seq(
        arg( "<problem-id>", "Identifier of the E. Falkenauer problem instance (see: http://goo.gl/Noa4S)."){ 
          (v: String, c: Configuration) => val problem = Ressources.ProblemInstances.get(v)
             (problem: @unchecked) match { case Some(p) => c.copy(problem = p) }
        },
        intOpt("s", "population-size", "<INTEGER>", "Size of the population. "+defaultPrefix+"500"+defaulSuffix){
          (v: Int, c: Configuration) => c.copy(populationSize = v)
        },
        intOpt("g", "max-generations", "<INTEGER>", "Maximum number of generations. "+defaultPrefix+"1000"+defaulSuffix){
          (v: Int, c: Configuration) => c.copy(maxGenerations = v)
        },
        booleanOpt("a", "abort-early", "<true|false>" ,"Abort the evolution immediately if a individual is on par with the best"+iLF+"known solution. "+defaultPrefix+"true"+defaulSuffix) { 
          (v: Boolean, c: Configuration) => c.copy(earlyAbort = v) 
        },
        opt("d", "genotype-decoder", "<"+KeywordNextFitDecoder+"|"+KeywordFirstFitDecoder+"|"+KeywordBestFitDecoder+">", "Decoder heuristic which translates the genotype of a individual to its"+iLF+"corresponding phenotype. "+defaultPrefix+KeywordBestFitDecoder+defaulSuffix){
          (v: String, c: Configuration) => v match {
            case x if (x == KeywordNextFitDecoder   || 
                       x == KeywordFirstFitDecoder || 
                       x == KeywordBestFitDecoder)    => c.copy(decoderKeyword = v)
          }
        },
        opt("q", "quality-function", "<"+KeywordQualityFunction1+"|"+KeywordQualityFunction2+">", "Quality function which evaluates the phenotype of a individual."+iLF+defaultPrefix+KeywordQualityFunction1+defaulSuffix){
          (v: String, c: Configuration) => v match {
            case x if (x == KeywordQualityFunction1 || 
                       x == KeywordQualityFunction2 ) => c.copy(qualityFunctionKeyword = v)
          }
        },
        opt("ps", "parent-selection", "<SELECTION>", "Algorithm which selects individuals for recombination."+selectionValues){
          (v: String, c: Configuration) => val selection = getSelectionByKeyword(v)
            (selection: @unchecked) match { case Some(s) => c.copy(parentSelection = s) }
        },
        opt("r", "recombination", "<ordered|mapped>", "Algorithm which recombines two parent individuals."){
          (v: String, c: Configuration) => v match {
            case "ordered" => c.copy(recombination = OrderedRecombination)
            case "mapped" => c.copy(recombination = PartiallyMappedCrossover)
          }
        },
        opt("m", "mutation", "<none|MUTATION{,MUTATION}>", "Algorithm which is used to mutate child individuals. If there is more than"+iLF+"one MUTATION specified, the algorithms will be applied in the defined order."+iLF+"Valid MUTATION keywords are: inversion|shift|exhange. "+defaultPrefix+"shift"+defaulSuffix){
          (v: String, c: Configuration) =>
            val mutationsBuffer = new ListBuffer[Mutation]
            for(mutationString <- v.split(',')) { mutationString match {
              case "inversion" => mutationsBuffer += InversionMutation
              case "shift" => mutationsBuffer += ShiftingMutation
              case "exchange" => mutationsBuffer += ExchangeMutation
              case "none" if mutationsBuffer.isEmpty =>
              }
            }
            c.copy(mutations = mutationsBuffer.toList.reverse )
        },
        opt("es", "environment-selection", "<SELECTION>", "Algorithm which selects individuals for the next generation."+selectionValues){
          (v: String, c: Configuration) => val selection = getSelectionByKeyword(v)
            (selection: @unchecked) match { case Some(s) => c.copy(environmentSelection = s) }
        },
        opt("o", "output", "<file>", "Writes the program output to a file. In addition to the command line output,"+iLF+"detailed information about the bin occupancy of the overall best individual"+iLF+"will be written to the file as well."){
          (v: String, c: Configuration) => 
            val file = new File(v)
            if (file.exists) file.delete()
            c.copy(outputFile = Some(file))
        })
    }

   private def getSelectionByKeyword(keyword: String): Option[Selection] = {
     val TournamentPattern = """(tournament)=(\d+)""".r
     keyword match {
        case "best" => Some(BestSelection)
        case "random" => Some(RandomSelection)
        case TournamentPattern(_,q) => Some(TournamentSelection(q.toInt))
        case _ => None
      }
   } 
} 
