package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.UnweightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

class TestTarjanStrongConnectivityInspector {
	private lateinit var inspector: TarjanStrongConnectivityInspector<Char, Edge<Char>>

	@Test
	@DisplayName("initialize by undirected graph")
	fun testInitByUndirectedGraph() {
		val graph = UnweightedGraph<Char>("1", isDirected = false, isAutoAddVertex = true)
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { TarjanStrongConnectivityInspector(graph) }
	}

	@Test
	@DisplayName("initialize by directed graph")
	fun testInitByDirectedGraph() {
		val graph = UnweightedGraph<Char>("2", isDirected = true, isAutoAddVertex = true)
		Assertions.assertDoesNotThrow { TarjanStrongConnectivityInspector(graph) }
	}

	@Test
	@DisplayName("search for strongly connected components 1")
	fun findingStronglyConnectedComponents1() {
		/**
		 * Image of a graph.
		 * A  →  B  →  E  ↔  F
		 * ↑  ↙  ↓     ↑  ↗
		 * G  →  C  ↔  D
		 */
		val graph = UnweightedGraph<Char>("3", isDirected = true, isAutoAddVertex = true)
		graph.addEdge('A', 'B')
		graph.addEdge('B', 'C')
		graph.addEdge('B', 'E')
		graph.addEdge('B', 'G')
		graph.addEdge('C', 'D')
		graph.addEdge('D', 'C')
		graph.addEdge('D', 'E')
		graph.addEdge('D', 'F')
		graph.addEdge('E', 'F')
		graph.addEdge('F', 'E')
		graph.addEdge('G', 'A')
		graph.addEdge('G', 'C')
		inspector = TarjanStrongConnectivityInspector(graph)
		Assertions.assertEquals(
			mapOf(
				0 to setOf('F', 'E'),
				1 to setOf('D', 'C'),
				2 to setOf('G', 'B', 'A')
			),
			inspector.stronglyConnectedComponents()
		)
	}
}
