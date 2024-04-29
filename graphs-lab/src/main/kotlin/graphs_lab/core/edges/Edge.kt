package graphs_lab.core.edges

open class Edge<I>(
	val idSource: I,
	val idTarget: I
) {

	override fun toString(): String {
		return "${javaClass.simpleName}(source = $idSource, target = $idTarget)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Edge<*>
		if (idSource != other.idSource) return false
		if (idTarget != other.idTarget) return false
		return true
	}

	override fun hashCode(): Int {
		var result = javaClass.name.hashCode()
		result = 31 * result + idSource.hashCode()
		result = 31 * result + idTarget.hashCode()
		return result
	}

}
