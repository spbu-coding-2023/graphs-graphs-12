package graphs_lab.core.edges

/**
 * Represents a weighted edge in a graph.
 *
 * @param I the type of the edge identifiers
 * @param W the type of the weight associated with the edge (must be a subtype of Number)
 * @property weight the weight of the edge
 */
open class WeightedEdge<I>(
	idSource: I,
	idTarget: I,
	val weight: Double
) : Edge<I>(idSource, idTarget) {

	override fun toString(): String {
		return "${javaClass.simpleName}(source = $idSource, target = $idTarget, weight = $weight)"
	}

}
