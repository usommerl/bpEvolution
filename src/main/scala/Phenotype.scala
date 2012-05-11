class Phenotype(bins: List[Bin]) extends Traversable[Bin] with Iterable[Bin] {
  def iterator: Iterator[Bin] = bins.iterator
}
