import scala.util.Random

trait Selection {
  def select(individuals: List[Individual], quantity: Int): List[Individual]
}

object BestSelection extends Selection {
  def select(individuals: List[Individual], quantity: Int): List[Individual] = {
    individuals.sorted.take(quantity)
  }
}

object RandomSelection extends Selection {
  def select(individuals: List[Individual], quantity: Int): List[Individual] = {
    Random.shuffle(individuals).take(quantity)
  }
}

case class TournamentSelection(rounds: Int = 3) extends Selection {
  def select(individuals: List[Individual], quantity: Int): List[Individual] = {
    val scoreboard = individuals.map((_,0)).toArray
    for (i <- 0 until scoreboard.size) {
      for (_ <- 1 to this.rounds) {
          if (scoreboard(i)._1.quality < scoreboard(Random.nextInt(scoreboard.size))._1.quality)
            scoreboard(i) = (scoreboard(i)._1, scoreboard(i)._2 + 1)
        }
    }
    scoreboard.toList.sortWith(_._2 > _._2).map(_._1).take(quantity)
  }
}
