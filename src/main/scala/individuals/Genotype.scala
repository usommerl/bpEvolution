import scala.collection.LinearSeq
import scala.collection.LinearSeqLike
import scala.collection.mutable.Builder
import scala.collection.mutable.ListBuffer
import scala.collection.generic.CanBuildFrom

class Genotype(val items: List[Item]) extends LinearSeq[Item] with LinearSeqLike[Item, Genotype] {
  
  def apply(idx: Int): Item = items.apply(idx)
  def length: Int = items.length

  override def hashCode = this.items.hashCode
 
  override def equals(other: Any) = other match {
        case that: Genotype => (that canEqual this) && that.items == this.items 
        case _ => false
    }
    
  override def canEqual(other: Any): Boolean = other.isInstanceOf[Genotype]
 
  override def iterator: Iterator[Item] = items.iterator
  
  override def newBuilder: Builder[Item, Genotype] = Genotype.newBuilder
}

object Genotype {
  
  def newBuilder: Builder[Item, Genotype] = {
    val buffer = new ListBuffer[Item]
    buffer.mapResult(result => new Genotype(result))
  }

  implicit def canBuildFrom: CanBuildFrom[Genotype, Item, Genotype] = {
    new CanBuildFrom[Genotype, Item, Genotype] {
        def apply(): Builder[Item, Genotype] = newBuilder
        def apply(from: Genotype): Builder[Item, Genotype] = newBuilder
    }
  }

  implicit def itemListToGenotype(items: List[Item]): Genotype = new Genotype(items)
}
