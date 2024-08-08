package graphs_lab.algs

import graphs_lab.core.graphs.WeightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestBellmanFordShortestPath {

	@Test
	@DisplayName("the source vertex does not exist")
	fun nonExistentSourceVertex() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		graph.addEdge('A', 'B', 52.0)
		Assertions.assertThrows(
			NoSuchElementException::class.java
		) { inspectorForGetPath.getPath('C', 'B') }
		Assertions.assertThrows(
			NoSuchElementException::class.java
		) { inspectorForGetPathWeight.getPathWeight('C', 'B') }
	}

	@Test
	@DisplayName("the target vertex does not exist")
	fun nonExistentTargetVertex() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		graph.addEdge('A', 'B', 52.0)
		Assertions.assertThrows(
			NoSuchElementException::class.java
		) { inspectorForGetPath.getPath('A', 'C') }
		Assertions.assertThrows(
			NoSuchElementException::class.java
		) { inspectorForGetPathWeight.getPathWeight('A', 'C') }
	}

	@Test
	@DisplayName("negative weight cycle check in directed graph")
	fun containsNegativeWeightCycleInDirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 * A  →  B  →  D
		 *       ↓     ↑  ↘
		 *       C  →  E  ←  F
		 */
		graph.addEdge('A', 'B', 5.0)
		graph.addEdge('B', 'D', 2.0)
		graph.addEdge('B', 'C', 1.0)
		graph.addEdge('D', 'F', 2.0)
		graph.addEdge('F', 'E', -3.0)
		graph.addEdge('E', 'D', -1.0)
		graph.addEdge('C', 'E', 1.0)
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { inspectorForGetPath.getPath('A', 'E') }
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { inspectorForGetPathWeight.getPathWeight('A', 'E') }
	}

	@Test
	@DisplayName("negative weight cycle check in undirected graph")
	fun containsNegativeWeightCycleInUndirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = false, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 * A ─ B ─ D
		 *     | /
		 *     C
		 */
		graph.addEdge('A', 'B', 5.0)
		graph.addEdge('B', 'D', -2.0)
		graph.addEdge('B', 'C', 1.0)
		graph.addEdge('D', 'C', 2.0)
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { inspectorForGetPath.getPath('A', 'C') }
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { inspectorForGetPathWeight.getPathWeight('A', 'C') }
	}

	@Test
	@DisplayName("path is not exist in directed graph")
	fun nonExistentPathInDirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B', 10.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 3.0)
		Assertions.assertEquals(null, inspectorForGetPath.getPath('B', 'A'))
		Assertions.assertEquals(Double.POSITIVE_INFINITY, inspectorForGetPathWeight.getPathWeight('B', 'A'))
	}

	@Test
	@DisplayName("path is not exist in undirected graph")
	fun nonExistentPathInUndirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = false, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 * A ─ B
		 * | /
		 * C   D
		 */
		graph.addEdge('A', 'B', 10.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 3.0)
		graph.addVertex('D')
		Assertions.assertEquals(null, inspectorForGetPath.getPath('B', 'D'))
		Assertions.assertEquals(Double.POSITIVE_INFINITY, inspectorForGetPathWeight.getPathWeight('B', 'D'))
	}

	@Test
	@DisplayName("finding the shortest path in directed graph")
	fun findingTheShortestPathInDirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B', 10.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 3.0)
		Assertions.assertEquals(
			listOf('A', 'C', 'B'),
			inspectorForGetPath.getPath('A', 'B')
		)
		Assertions.assertEquals(5.0, inspectorForGetPathWeight.getPathWeight('A', 'B'))
	}

	@Test
	@DisplayName("finding the shortest path in undirected graph")
	fun findingTheShortestPathInIUndirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = false, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 * A ─ B
		 * | /
		 * C
		 */
		graph.addEdge('A', 'B', 10.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 3.0)
		Assertions.assertEquals(
			listOf('A', 'C', 'B'),
			inspectorForGetPath.getPath('A', 'B')
		)
		Assertions.assertEquals(5.0, inspectorForGetPathWeight.getPathWeight('A', 'B'))
	}

	@Test
	@DisplayName("recalculation of the path when the source vertex changes in directed graph")
	fun recalculationForAnotherStartingVertexInDirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 *       F
		 *       ↑
		 * A  →  B  →  C
		 * ↑     ↕     ↓
		 * G     E  ←  D
		 */
		graph.addEdge('A', 'B', 1.0)
		graph.addEdge('B', 'C', 1.0)
		graph.addEdge('C', 'D', 1.0)
		graph.addEdge('D', 'E', 1.0)
		graph.addEdge('E', 'B', 10.0)
		graph.addEdge('B', 'E', 9.0)
		graph.addEdge('B', 'F', 10.0)
		graph.addEdge('G', 'A', 1.0)
		inspectorForGetPath.getPath('G', 'F')
		Assertions.assertEquals(
			listOf('A', 'B', 'C', 'D', 'E'),
			inspectorForGetPath.getPath('A', 'E')
		)
		inspectorForGetPathWeight.getPathWeight('G', 'F')
		Assertions.assertEquals(4.0, inspectorForGetPathWeight.getPathWeight('A', 'E'))
	}

	@Test
	@DisplayName("recalculation of the path when the source vertex changes in undirected graph")
	fun recalculationForAnotherStartingVertexInUndirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = false, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 *     F
		 *     |
		 * A ─ B ─ C
		 * |   |   |
		 * G   E ─ D
		 */
		graph.addEdge('A', 'B', 1.0)
		graph.addEdge('B', 'C', 1.0)
		graph.addEdge('C', 'D', 1.0)
		graph.addEdge('D', 'E', 1.0)
		graph.addEdge('E', 'B', 10.0)
		graph.addEdge('B', 'E', 9.0)
		graph.addEdge('B', 'F', 10.0)
		graph.addEdge('G', 'A', 1.0)
		inspectorForGetPath.getPath('G', 'F')
		Assertions.assertEquals(
			listOf('A', 'B', 'C', 'D', 'E'),
			inspectorForGetPath.getPath('A', 'E')
		)
		inspectorForGetPathWeight.getPathWeight('G', 'F')
		Assertions.assertEquals(4.0, inspectorForGetPathWeight.getPathWeight('A', 'E'))
	}

	@Test
	@DisplayName("without recalculating the path in directed graph")
	fun withoutRecalculatingThePathInDirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 *       F
		 *       ↑
		 * A  →  B  →  C
		 * ↑     ↕     ↓
		 * G     E  ←  D
		 */
		graph.addEdge('A', 'B', 1.0)
		graph.addEdge('B', 'C', 1.0)
		graph.addEdge('C', 'D', 1.0)
		graph.addEdge('D', 'E', 1.0)
		graph.addEdge('E', 'B', 10.0)
		graph.addEdge('B', 'E', 9.0)
		graph.addEdge('B', 'F', 10.0)
		graph.addEdge('G', 'A', 1.0)
		inspectorForGetPath.getPath('G', 'F')
		Assertions.assertEquals(
			listOf('G', 'A', 'B', 'C', 'D', 'E'),
			inspectorForGetPath.getPath('G', 'E')
		)
		inspectorForGetPathWeight.getPathWeight('G', 'F')
		Assertions.assertEquals(5.0, inspectorForGetPathWeight.getPathWeight('G', 'E'))
	}

	@Test
	@DisplayName("without recalculating the path in undirected graph")
	fun withoutRecalculatingThePathInUndirectedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = false, isAutoAddVertex = true)
		val inspectorForGetPath = BellmanFordShortestPath(graph)
		val inspectorForGetPathWeight = BellmanFordShortestPath(graph)

		/**
		 * Image of a graph.
		 *     F
		 *     |
		 * A ─ B ─ C
		 * |   |   |
		 * G   E ─ D
		 */
		graph.addEdge('A', 'B', 1.0)
		graph.addEdge('B', 'C', 1.0)
		graph.addEdge('C', 'D', 1.0)
		graph.addEdge('D', 'E', 1.0)
		graph.addEdge('E', 'B', 10.0)
		graph.addEdge('B', 'E', 9.0)
		graph.addEdge('B', 'F', 10.0)
		graph.addEdge('G', 'A', 1.0)
		inspectorForGetPath.getPath('G', 'F')
		Assertions.assertEquals(
			listOf('G', 'A', 'B', 'C', 'D', 'E'),
			inspectorForGetPath.getPath('G', 'E')
		)
		inspectorForGetPathWeight.getPathWeight('G', 'F')
		Assertions.assertEquals(5.0, inspectorForGetPathWeight.getPathWeight('G', 'E'))
	}
}
