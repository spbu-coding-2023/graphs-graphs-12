package graphs_lab.algs

import graphs_lab.core.graphs.WeightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestDijkstraAlgorithm {

	@Test
	@DisplayName("empty graph")
	fun testEmptyGraph() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = false, isAutoAddVertex = true)

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertThrows(IllegalArgumentException::class.java) { graphDijkstra.getPath('1', '2') }

		Assertions.assertThrows(IllegalArgumentException::class.java) { graphDijkstra.getAllPaths('1') }
		Assertions.assertThrows(IllegalArgumentException::class.java) { graphDijkstra.getPath('1', '2') }
		Assertions.assertThrows(IllegalArgumentException::class.java) { graphDijkstra.getWeightPath('1', '2') }
	}

	@Test
	@DisplayName("is not contained vertices")
	fun testNotContainedVertices() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = false, isAutoAddVertex = true)
		graph.addEdge('1', '2', 3.0)

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertEquals(graphDijkstra.getPath('1', '3'), mutableListOf<Char>())

		Assertions.assertEquals(graphDijkstra.getAllPaths('1'), mutableMapOf('1' to Pair(null, 0.0), '2' to Pair('1', 3.0)))
		Assertions.assertEquals(graphDijkstra.getPath('1', '3'), mutableListOf<Char>())
		Assertions.assertEquals(graphDijkstra.getWeightPath('1', '3'), Double.POSITIVE_INFINITY)
	}

	@Test
	@DisplayName("negative weight")
	fun testNegativeWeight() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = false, isAutoAddVertex = true)
		graph.addEdge('1', '2', -3.0)

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertThrows(IllegalArgumentException::class.java) { graphDijkstra.getAllPaths('1') }
		Assertions.assertThrows(IllegalStateException::class.java) { graphDijkstra.getPath('1', '3') }
		Assertions.assertThrows(IllegalStateException::class.java) { graphDijkstra.getWeightPath('1', '3') }
	}

	@Test
	@DisplayName("test one undirected edge")
	fun testOneUndirectedEdge() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = false, isAutoAddVertex = true)
		graph.addEdge('1', '2', 3.0)
		graph.addVertex('3')

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertEquals(
			graphDijkstra.getAllPaths('1'),
			mutableMapOf(
				'1' to Pair(null, 0.0),
				'2' to Pair('1', 3.0),
				'3' to Pair(null, Double.POSITIVE_INFINITY)
			)
		)
		Assertions.assertEquals(graphDijkstra.getPath('1', '2'), mutableListOf('1', '2'))
		Assertions.assertEquals(graphDijkstra.getWeightPath('1', '2'), 3.0)
	}

	@Test
	@DisplayName("test one directed edge")
	fun testOneDirectedEdge() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = true, isAutoAddVertex = true)
		graph.addEdge('1', '2', 3.0)
		graph.addVertex('3')

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertEquals(
			graphDijkstra.getAllPaths('1'),
			mutableMapOf(
				'1' to Pair(null, 0.0),
				'2' to Pair('1', 3.0),
				'3' to Pair(null, Double.POSITIVE_INFINITY)
			)
		)
		Assertions.assertEquals(graphDijkstra.getPath('1', '2'), mutableListOf('1', '2'))
		Assertions.assertEquals(graphDijkstra.getWeightPath('1', '2'), 3.0)

		Assertions.assertEquals(
			graphDijkstra.getAllPaths('2'),
			mutableMapOf(
				'1' to Pair(null, Double.POSITIVE_INFINITY),
				'2' to Pair(null, 0.0),
				'3' to Pair(null, Double.POSITIVE_INFINITY)
			)
		)
		Assertions.assertEquals(graphDijkstra.getPath('2', '1'), mutableListOf<Char>())
		Assertions.assertEquals(graphDijkstra.getWeightPath('2', '1'), Double.POSITIVE_INFINITY)
	}

	@Test
	@DisplayName("undirected graph")
	fun testUndirectedGraph() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = false, isAutoAddVertex = true)
		graph.addEdge('A', 'B', 7.0)
		graph.addEdge('B', 'F', 4.0)
		graph.addEdge('C', 'F', 11.0)
		graph.addEdge('A', 'D', 4.0)
		graph.addEdge('E', 'D', 3.0)
		graph.addEdge('E', 'C', 5.0)

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertEquals(
			graphDijkstra.getAllPaths('A'),
			mutableMapOf(
				'A' to Pair(null, 0.0),
				'B' to Pair('A', 7.0),
				'C' to Pair('E', 12.0),
				'D' to Pair('A', 4.0),
				'E' to Pair('D', 7.0),
				'F' to Pair('B', 11.0)
			)
		)
		Assertions.assertEquals(graphDijkstra.getPath('A', 'E'), mutableListOf('A', 'D', 'E'))
		Assertions.assertEquals(graphDijkstra.getWeightPath('A', 'E'), 7.0)
	}

	@Test
	@DisplayName("directed graph")
	fun testDirectedGraph() {
		val graph = WeightedGraph<Char>("Dijkstra test", isDirected = true, isAutoAddVertex = true)
		graph.addEdge('A', 'B', 7.0)
		graph.addEdge('B', 'F', 4.0)
		graph.addEdge('C', 'F', 11.0)
		graph.addEdge('A', 'D', 4.0)
		graph.addEdge('E', 'D', 3.0)
		graph.addEdge('E', 'C', 5.0)
		graph.addEdge('C', 'E', 2.0)

		val graphDijkstra = DijkstraAlgorithm(graph)
		Assertions.assertEquals(
			graphDijkstra.getAllPaths('A'),
			mutableMapOf(
				'A' to Pair(null, 0.0),
				'B' to Pair('A', 7.0),
				'C' to Pair(null, Double.POSITIVE_INFINITY),
				'D' to Pair('A', 4.0),
				'E' to Pair(null, Double.POSITIVE_INFINITY),
				'F' to Pair('B', 11.0)
			)
		)
		Assertions.assertEquals(graphDijkstra.getPath('A', 'F'), mutableListOf('A', 'B', 'F'))
		Assertions.assertEquals(graphDijkstra.getWeightPath('A', 'F'), 11.0)

		Assertions.assertEquals(
			graphDijkstra.getAllPaths('C'),
			mutableMapOf(
				'A' to Pair(null, Double.POSITIVE_INFINITY),
				'B' to Pair(null, Double.POSITIVE_INFINITY),
				'C' to Pair(null, 0.0),
				'D' to Pair('E', 5.0),
				'E' to Pair('C', 2.0),
				'F' to Pair('C', 11.0)
			)
		)
		Assertions.assertEquals(graphDijkstra.getPath('C', 'D'), mutableListOf('C', 'E', 'D'))
		Assertions.assertEquals(graphDijkstra.getWeightPath('C', 'D'), 5.0)
	}
}
