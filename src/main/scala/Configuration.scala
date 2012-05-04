import scopt.immutable.OptionParser
import scala.collection.mutable.ListBuffer

case class Configuration(
  problem: BinPackProblem = null, 
  populationSize: Int = 500, 
  maxGenerations: Int = 10, 
  parentSelection: Selection = BestSelection, 
  recombination: Recombination = OrderedRecombination, 
  mutations: List[Mutation] = List(ShiftingMutation, InversionMutation), 
  environmentSelection: Selection = TournamentSelection(),
  decoderKeyword: String = ConfigurationParser.FirstFitDecoderKeyword
) {
  lazy val genotypeDecoder = this.decoderKeyword match {
    case ConfigurationParser.SimpleDecoderKeyword => SimpleDecoder(problem)
    case ConfigurationParser.FirstFitDecoderKeyword => FirstFitDecoder(problem)
  }
}

object ConfigurationParser extends OptionParser[Configuration]("bpEvolver") {

      val iLF = "\n"++" "*8
      val selectionValues = iLF ++ "Valid SELECTION keywords are: best|random|tournament=<INTEGER>"
      val FirstFitDecoderKeyword = "first-fit"
      val SimpleDecoderKeyword = "simple"
      
      def options = { Seq(
        arg( "<problem-id>", "Identifier of the E. Falkenauer problem instance (see: http://goo.gl/Noa4S)."){ 
          (v: String, c: Configuration) => val problem = Utils.problemInstances.get(v)
             (problem: @unchecked) match { case Some(p) => c.copy(problem = p) }
        },
        intOpt("s", "population-size", "<INTEGER>", "Size of the population."){
          (v: Int, c: Configuration) => c.copy(populationSize = v)
        },
        intOpt("g", "generations", "<INTEGER>", "Maximum number of generations."){
          (v: Int, c: Configuration) => c.copy(maxGenerations = v)
        },
        opt("d", "genotype-decoder", "<"+SimpleDecoderKeyword+"|"+FirstFitDecoderKeyword+">", "Decoder algorithm which translates the genotype of a"+iLF+"individual to its corresponding phenotype."){
          (v: String, c: Configuration) => v match {
            case SimpleDecoderKeyword => c.copy(decoderKeyword = v)
            case FirstFitDecoderKeyword => c.copy(decoderKeyword = v)
          }
        },
        opt("ps", "parent-selection", "<SELECTION>", "Algorithm which selects individuals for recombination."+selectionValues){
          (v: String, c: Configuration) => val selection = getSelectionByKeyword(v)
            (selection: @unchecked) match { case Some(s) => c.copy(parentSelection = s) }
        },
        opt("r", "recombination", "<ordered>", "Algorithm which recombines two parent individuals."){
          (v: String, c: Configuration) => v match {
            case "ordered" => c.copy(recombination = OrderedRecombination)
          }
        },
        opt("m", "mutation", "<none|MUTATION|{MUTATION,}>", "Algorithm which is used to mutate child individuals. If there is more than"+iLF+"one MUTATION specified, the algorithms will be apllied in the defined order."+iLF+"Valid MUTATION keywords are: inversion|shift|exhange "){
          (v: String, c: Configuration) =>
            val mutationsBuffer = new ListBuffer[Mutation]
            for(mutationString <- v.split(',')) { mutationString match {
              case "inversion" => mutationsBuffer += InversionMutation
              case "shift" => mutationsBuffer += ShiftingMutation
              case "exchange" => mutationsBuffer += ExchangeMutation
              case "none" =>
              }
            }
            c.copy(mutations = mutationsBuffer.toList.reverse )
        },
        opt("es", "environment-selection", "<SELECTION>", "Algorithm which selects individuals for the next generation."+selectionValues){
          (v: String, c: Configuration) => val selection = getSelectionByKeyword(v)
            (selection: @unchecked) match { case Some(s) => c.copy(environmentSelection = s) }
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
