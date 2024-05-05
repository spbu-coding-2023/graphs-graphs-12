package graphs_lab.algs

import graphs_lab.algs.utils.DisjointSets
import graphs_lab.algs.utils.PriorityPair
import graphs_lab.algs.utils.getEdgeWeight
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import java.util.PriorityQueue

class MSTAlgorithms<I, E : Edge<I>>(val graph: Graph<I, E>) {

	init {
		if (graph.isDirected) throw IllegalArgumentException(
			"Can't find MST of directed graph '${graph.label}'"
		)
	}

	fun kruskalAlgorithm(): Set<E> {
		val mst = mutableSetOf<E>()
		val unvisitedVertices: MutableSet<I> = graph.idVertices.toMutableSet()
		val (priorityQueue, verticesComponents) = initKruskal()
		while (priorityQueue.isNotEmpty()) {
			val priorityPair: PriorityPair<Double, E> = priorityQueue.poll()
			val edge: E = priorityPair.value
			unvisitedVertices.remove(edge.idSource)
			unvisitedVertices.remove(edge.idTarget)

			if (verticesComponents.isConnected(edge.idSource, edge.idTarget)) continue
			verticesComponents.unionSets(edge.idSource, edge.idTarget)
			mst.add(edge)

			if (unvisitedVertices.isEmpty()) break
		}
		return mst
	}

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
			if (priorityQueue.isEmpty()) primAddVertex(unvisitedVertices.first(), priorityQueue)

		}
		return mst.toSet()
	}

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

	private fun initPrim(): Pair<PriorityQueue<PriorityPair<Double, E>>, DisjointSets<I>> {
		val priorityQueue = PriorityQueue<PriorityPair<Double, E>>()
		primAddVertex(graph.idVertices.first(), priorityQueue)
		return Pair(priorityQueue, DisjointSets(graph.idVertices))
	}

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

}
