package graphs_lab.algs

import graphs_lab.algs.utils.DisjointSets
import graphs_lab.algs.utils.PriorityPair
import graphs_lab.algs.utils.getEdgeWeight
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import java.util.PriorityQueue

/**
 * Provides algorithms for finding the Minimum Spanning Tree (MST) of an undirected graph.
 *
 * @param I the type of the vertex identifiers in the graph
 * @param E the type of the edges in the graph, which must implement the [Edge] interface
 * @property graph the input graph for which the MST will be computed
 *
 * @throws IllegalArgumentException if the input graph is directed, as MST algorithms only work for undirected graphs
 */
class MSTAlgorithms<I, E : Edge<I>>(val graph: Graph<I, E>) {

	init {
		require(!graph.isDirected) { "Can't find MST of directed graph '${graph.label}'" }
	}

	/**
	 * Computes the Minimum Spanning Tree (MST) of the input graph using Kruskal's algorithm.
	 * More information: [Wiki](https://en.wikipedia.org/wiki/Kruskal%27s_algorithm).
	 *
	 * @return a set of edges that form the MST
	 */
	fun kruskalAlgorithm(): Set<E> {
		val mst = mutableSetOf<E>()
		val (priorityQueue, verticesComponents) = initKruskal()
		while (priorityQueue.isNotEmpty()) {
			val priorityPair: PriorityPair<Double, E> = priorityQueue.poll()
			val edge: E = priorityPair.value

			if (verticesComponents.isConnected(edge.idSource, edge.idTarget)) continue
			verticesComponents.unionSets(edge.idSource, edge.idTarget)
			mst.add(edge)
		}
		return mst
	}

	/**
	 * Computes the Minimum Spanning Tree (MST) of the input graph using Prim's algorithm.
	 * More information: [Wiki](https://en.wikipedia.org/wiki/Prim%27s_algorithm).
	 *
	 * @return a set of edges that form the MST
	 */
	fun primAlgorithm(): Set<E> {
		val mst = mutableSetOf<E>()
		val unvisitedVertices: MutableSet<I> = graph.idVertices.toMutableSet()
		val (priorityQueue, verticesComponents) = initPrim()
		while (priorityQueue.isNotEmpty()) {
			val priorityPair: PriorityPair<Double, E> = priorityQueue.poll()
			val edge: E = priorityPair.value
			unvisitedVertices.remove(edge.idSource)

			if (!verticesComponents.isConnected(edge.idSource, edge.idTarget)) {
				verticesComponents.unionSets(edge.idSource, edge.idTarget)
				mst.add(edge)
			}

			if (edge.idTarget in unvisitedVertices) primAddVertex(edge.idTarget, priorityQueue)
			if (unvisitedVertices.isEmpty()) break
			if (priorityQueue.isEmpty()) expandQueue(priorityQueue, unvisitedVertices)
		}
		return mst.toSet()
	}

	/**
	 * Initializes the Kruskal algorithm by setting up the priority queue and disjoint sets.
	 * Priority queue contains edges, which sorted by weight.
	 * Disjoint sets contains [graph]'s vertices splitting.
	 *
	 * @return Pair containing priority queue of edge weights and disjoint sets
	 */
	private fun initKruskal(): Pair<PriorityQueue<PriorityPair<Double, E>>, DisjointSets<I>> {
		val priorityQueue = PriorityQueue<PriorityPair<Double, E>>()
		for (idVertex in graph.idVertices) {
			for (edge in graph.vertexEdges(idVertex)) {
				priorityQueue.add(
					PriorityPair(
						getEdgeWeight(edge),
						edge
					)
				)
			}
		}
		return Pair(priorityQueue, DisjointSets(graph.idVertices))
	}

	/**
	 * Initializes the Prim algorithm by setting up the priority queue and disjoint sets.
	 * Priority queue contains edges, which sorted by weight from one of [graph]'s vertex.
	 * Disjoint sets contains [graph]'s vertices splitting.
	 *
	 * @return Pair containing priority queue of edge weights and disjoint sets
	 */
	private fun initPrim(): Pair<PriorityQueue<PriorityPair<Double, E>>, DisjointSets<I>> {
		val priorityQueue = PriorityQueue<PriorityPair<Double, E>>()
		expandQueue(priorityQueue, graph.idVertices)
		return Pair(priorityQueue, DisjointSets(graph.idVertices))
	}

	/**
	 * Adds vertices to the prim priority queue based on the edges connected to the vertex.
	 */
	private fun primAddVertex(idVertex: I, priorityQueue: PriorityQueue<PriorityPair<Double, E>>) {
		for (edge in graph.vertexEdges(idVertex)) {
			priorityQueue.add(
				PriorityPair(
					getEdgeWeight(edge),
					edge
				)
			)
		}
	}

	/**
	 * This function expands the priority queue
	 * by iteratively adding vertices to it until the queue is no longer empty.
	 * It calls the [primAddVertex] function to add vertices based on the provided set of vertices.
	 *
	 * @param priorityQueue the priority queue containing the edge weights
	 * @param vertices the set of vertices to be considered for adding to the priority queue
	 */
	private fun expandQueue(priorityQueue: PriorityQueue<PriorityPair<Double, E>>, vertices: Set<I>) {
		if (priorityQueue.isNotEmpty()) return

		for (vertex in vertices) {
			primAddVertex(vertex, priorityQueue)
			if (priorityQueue.isNotEmpty()) break
		}
	}
}
