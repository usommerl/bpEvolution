import scopt.immutable.OptionParser
import scala.collection.mutable.ListBuffer

object Main {

  def main(args: Array[String]) {

    val parser = new OptionParser[EvolverConfiguration]("bpEvolver") {

      val TournamentPattern = """(tournament)=(\d+)""".r
      val iLF = "\n"++" "*8
      val selectionValues = iLF ++ "Possible values for SELECTION are: best|random|tournament=<INTEGER>" 
      
      def options = { Seq(
        arg( "<problem-id>", "Identifier of the E. Falkenauer problem instance"){ 
          (v: String, c:EvolverConfiguration) => 
              if (Utils.problemInstances.get(v).isDefined) {
                c.copy(problem = Utils.problemInstances.get(v).get)
              }else {
                println("\nProblem width identifier \""+v+"\" not in ressources (see: http://goo.gl/Noa4S)\n")
                sys.exit()
              }
        },
        
        intOpt("s", "population-size", "<INTEGER>", "Size of the population"){
          (v: Int, c: EvolverConfiguration) => c.copy(populationSize = v)
        },
        
        intOpt("g", "generations", "<INTEGER>", "Maximum number of generations"){
          (v: Int, c: EvolverConfiguration) => c.copy(maxGenerations = v)
        },
        
        opt("ps", "parent-selection", "<SELECTION>", "Algorithm which selects individuals for recombination"+selectionValues){
          (v: String, c:EvolverConfiguration) => v match {
              case "best" => c.copy(parentSelection = BestSelection)
              case "random" => c.copy(parentSelection = RandomSelection)
              case TournamentPattern(_,q) => c.copy(parentSelection = TournamentSelection(q.toInt))
              case _ => {this.showUsage; sys.exit()}
          }
        },
        
        opt("r", "recombination", "<ordered>", "Algorithm which recombines two parent individuals"){
          (v: String, c:EvolverConfiguration) => v match {
            case "ordered" => c.copy(recombination = OrderedRecombination)
            case _ => {this.showUsage; sys.exit()}
          }
        },
        
        opt("m", "mutation", "<none|MUTATION|{MUTATION,}>", "Algorithm which is used to mutated child individuals. If there is more than"+iLF+"one MUTATION specified, the algorithms will be apllied in the defined order."+iLF+"Possible values for MUTATION are: inversion|shift|exhange "){
          (v: String, c:EvolverConfiguration) =>
            val mutationsBuffer = new ListBuffer[Mutation]
            for(mutationString <- v.split(',')) { mutationString match {
              case "inversion" => mutationsBuffer += InversionMutation
              case "shift" => mutationsBuffer += ShiftingMutation
              case "exchange" => mutationsBuffer += ExchangeMutation
              case "none" =>
              case _ => {this.showUsage; sys.exit()}
              }
            }
            c.copy(mutations = mutationsBuffer.toList.reverse )
        },
        
        opt("es", "environment-selection", "<SELECTION>", "Algorithm which selects individuals for the next generation"+selectionValues){
          (v: String, c:EvolverConfiguration) => v match {
              case "best" => c.copy(environmentSelection = BestSelection)
              case "random" => c.copy(environmentSelection = RandomSelection)
              case TournamentPattern(_,q) => c.copy(environmentSelection = TournamentSelection(q.toInt))
              case _ => {this.showUsage; sys.exit()}
          }
        }
      )}
    } 

    parser.parse(args, EvolverConfiguration()) map { config =>
      
      println(config)
      val evolver = new CustomEvolver(config)
      evolver.run()
      
    } 
  }
}

case class EvolverConfiguration(
  problem: BinPackProblem = null, 
  populationSize: Int = 500, 
  maxGenerations: Int = 10, 
  parentSelection: Selection = BestSelection, 
  recombination: Recombination = OrderedRecombination, 
  mutations: List[Mutation] = List(InversionMutation, ShiftingMutation), 
  environmentSelection: Selection = TournamentSelection() 
) {
  lazy val genotypeDecoder = new SimpleDecoder(problem)
}

