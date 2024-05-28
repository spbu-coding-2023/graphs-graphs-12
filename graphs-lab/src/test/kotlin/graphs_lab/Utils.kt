package graphs_lab

import graphs_lab.algs.utils.getEdgeWeight
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import graphs_lab.core.graphs.UnweightedGraph
import graphs_lab.core.graphs.WeightedGraph
import org.junit.jupiter.api.Assertions
import kotlin.math.abs

/**
 * Fills the graph with vertices from the provided iterable of elements.
 *
 * @param graph The graph to be filled with vertices
 * @param elements An iterable of elements to be added as vertices to the graph
 */
fun <I, E : Edge<I>> fillGraphVertices(graph: Graph<I, E>, elements: Iterable<I>) {
	for (element in elements) {
		graph.addVertex(element)
	}
}

/**
 * Fills the unweighted graph with edges from the provided iterable of links.
 *
 * @param graph The unweighted graph to be filled with edges
 * @param edges An iterable of links to be added as edges to the graph
 */
fun <I> fillGraphEdges(graph: UnweightedGraph<I>, edges: Iterable<Pair<I, I>>) {
	for (edge in edges) {
		graph.addEdge(edge.first, edge.second)
	}
}

/**
 * Fills the weighted graph with edges from the provided iterable of links.
 *
 * @param graph The weighted graph to be filled with edges
 * @param edges An iterable of links to be added as edges to the graph
 */
fun <I> fillGraphEdges(graph: WeightedGraph<I>, edges: Iterable<Triple<I, I, Double>>) {
	for (edge in edges) {
		graph.addEdge(edge.first, edge.second, edge.third)
	}
}

/**
 * A function to assert the correctness of a collection of edges against the expected edges.
 *
 * @param expected the expected collection of edge triples consisting of source vertex, target vertex, and weight
 * @param edges the actual collection of edges to be validated
 * @param isDirected a flag indicating whether the graph is directed or not
 * @throws AssertionError if can't find expected edge in [edges] or if size of [expected] equals size of [edges]
 */
fun <I, E : Edge<I>> assertEdgesCollection(
	expected: Collection<Triple<I, I, Double>>,
	edges: Collection<E>,
	isDirected: Boolean
) {
	Assertions.assertEquals(expected.size, edges.size) {
		"Invalid count of edges:\nexpected: $expected\nfind: $edges"
	}
	expected.forEach { edgeView: Triple<I, I, Double> ->
		var isFind = false
		for (edge in edges) {
			val firstDirection = edge.idSource == edgeView.first && edge.idTarget == edgeView.second
			val secondDirection = edge.idSource == edgeView.second && edge.idTarget == edgeView.first
			if (firstDirection || (!isDirected && secondDirection)) {
				// Comparing double or float numbers by epsilon method
				if (abs(getEdgeWeight(edge) - edgeView.third) >= 1e-9) continue
				isFind = true
				break
			}
		}
		Assertions.assertTrue(isFind) {
			"Can't find {${edgeView.first}, ${edgeView.second}, ${edgeView.third}}.\nFound edges: $edges."
		}
	}
}

/**
 * A function to validate whether a collection of edges matches the expected edge collection.
 *
 * @param expected the expected collection of edge triples consisting of source vertex, target vertex, and weight
 * @param edges the actual collection of edges to be validated
 * @param isDirected a flag indicating whether the graph is directed or not
 * @return a pair consisting of a boolean representing the validation result
 * 			(true if valid, false otherwise) and an optional AssertionError if validation fails
 */
fun <I, E : Edge<I>> isValidEdgeCollection(
	expected: Collection<Triple<I, I, Double>>,
	edges: Collection<E>,
	isDirected: Boolean
): Pair<Boolean, AssertionError?> {
	try {
		assertEdgesCollection(expected, edges, isDirected)
	} catch (ex: AssertionError) {
		return Pair(false, ex)
	}
	return Pair(true, null)
}
