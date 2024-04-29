package graphs_lab.core.edges

open class WeightedEdge<I, W : Number>(
	idSource: I,
	idTarget: I,
	val weight: W
) : Edge<I>(idSource, idTarget) {

	override fun toString(): String {
		return "${javaClass.simpleName}(source = $idSource, target = $idTarget, weight = $weight)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as WeightedEdge<*, *>
		if (idSource != other.idSource) return false
		if (idTarget != other.idTarget) return false
		if (weight != other.weight) return false
		return true
	}

	override fun hashCode(): Int {
		var result = javaClass.name.hashCode()
		result = 31 * result + idSource.hashCode()
		result = 31 * result + idTarget.hashCode()
		result = 31 * result + weight.hashCode()
		return result
	}

}
