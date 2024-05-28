package graphs_lab.algs.utils

import graphs_lab.core.edges.Edge
import graphs_lab.core.edges.WeightedEdge
import kotlin.math.abs

private const val DEFAULT_COMPARISON_MODULE = 1e-9

/**
 * Retrieves the weight of the specified edge.
 *
 * @param edge The edge for which the weight needs to be retrieved
 * @param defaultWeight The default weight to return if the edge is not a WeightedEdge
 * @return The weight of the edge if it is a WeightedEdge, otherwise the defaultWeight
 */
fun <I, E : Edge<I>> getEdgeWeightOrDefault(edge: E, defaultWeight: Double = 1.0): Double {
	if (edge::class == WeightedEdge::class) {
		edge as WeightedEdge<*>
		return edge.weight
	}
	return defaultWeight
}

/**
 * Returns the first element of the given pair or throws the exception if the vertex is not contained in the graph.
 *
 * @param pair the pair whose element need to get
 *
 * @return the second element of the given pair
 * @throws ExceptionInInitializerError if the pair or the first element of it is null
 */
fun <I, T> checkAndGetFirst(pair: Pair<I?, T?>?): I {
	return pair?.first ?: throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
}

/**
 * Returns the second element of the given pair or throws the exception if the vertex is not contained in the graph.
 *
 * @param pair the pair whose element need to get
 *
 * @return the second element of the given pair
 * @throws ExceptionInInitializerError if the pair or the second element of it is null
 */
fun <I, T> checkAndGetSecond(pair: Pair<I?, T?>?): T {
	return pair?.second ?: throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
}

/**
 * Removes the given item from the list if the item is contained in it.
 *
 * @param item the item to be deleted
 *
 * @return [item] in any case
 */
fun <I> MutableList<I>.removeAndReturn(item: I): I {
	remove(item)
	return item
}

/**
 * Compares by equals two floating point numbers with double precision.
 *
 * __Important__: comparison is made according to module 1e-9.
 *
 * @param first is number for comparing
 * @param second is number for comparing
 * @return `true` if numbers is equals else `false`
 */
fun doubleEquality(first: Double, second: Double): Boolean {
	return doubleEquality(first, second, DEFAULT_COMPARISON_MODULE)
}

/**
 * Compares by equals two floating point numbers with double precision.
 *
 * @param first is number for comparing
 * @param second is number for comparing
 * @param comparisonModule to validate is numbers equals
 * @return `true` if numbers is equals else `false`
 */
fun doubleEquality(first: Double, second: Double, comparisonModule: Double): Boolean {
	return abs(abs(first) - abs(second)) < abs(comparisonModule)
}
