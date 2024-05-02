package graphs_lab

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import graphs_lab.core.graphs.UnweightedGraph
import graphs_lab.core.graphs.WeightedGraph

/**
 * Fills the graph with vertices from the provided iterable of elements.
 *
 * @param graph The graph to be filled with vertices.
 * @param elements An iterable of elements to be added as vertices to the graph.
 */
fun <I, E : Edge<I>> fillGraphVertices(graph: Graph<I, E>, elements: Iterable<I>) {
	for (element in elements) {
		graph.addVertex(element)
	}
}

/**
 * Fills the unweighted graph with edges from the provided iterable of links.
 *
 * @param graph The unweighted graph to be filled with edges.
 * @param edges An iterable of links to be added as edges to the graph.
 */
fun <I> fillGraphEdges(graph: UnweightedGraph<I>, edges: Iterable<Pair<I, I>>) {
	for (edge in edges) {
		graph.addEdge(edge.first, edge.second)
	}
}

/**
 * Fills the weighted graph with edges from the provided iterable of links.
 *
 * @param graph The weighted graph to be filled with edges.
 * @param edges An iterable of links to be added as edges to the graph.
 */
fun <I> fillGraphEdges(graph: WeightedGraph<I>, edges: Iterable<Triple<I, I, Double>>) {
	for (edge in edges) {
		graph.addEdge(edge.first, edge.second, edge.third)
	}
}
