class Item(val id: Int, val size: Double) extends Ordered[Item]{

    override def toString = "[Item id="+id+"]"

    def compare(that: Item) = this.size.compare(that.size)
    
    override def hashCode = this.id.hashCode
    
    override def equals(other: Any) = other match {
        case that: Item => (that canEqual this) && that.id == this.id 
        case _ => false
    }
    
    def canEqual(other: Any): Boolean = other.isInstanceOf[Item]
}
