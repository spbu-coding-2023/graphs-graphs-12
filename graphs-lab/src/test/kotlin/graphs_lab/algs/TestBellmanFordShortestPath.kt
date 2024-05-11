package graphs_lab.algs

import graphs_lab.core.graphs.WeightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestBellmanFordShortestPath {
	private lateinit var graph: WeightedGraph<Char>
	private lateinit var inspectorForGetPath: BellmanFordShortestPath<Char>
	private lateinit var inspectorForGetPathWeight: BellmanFordShortestPath<Char>

	@BeforeEach
	fun setUp() {
		graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		inspectorForGetPath = BellmanFordShortestPath(graph)
		inspectorForGetPathWeight = BellmanFordShortestPath(graph)
	}

	@Test
	@DisplayName("the source vertex does not exist")
	fun nonExistentSourceVertex() {
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
		graph.addEdge('A', 'B', 52.0)
		Assertions.assertThrows(
			NoSuchElementException::class.java
		) { inspectorForGetPath.getPath('A', 'C') }
		Assertions.assertThrows(
			NoSuchElementException::class.java
		) { inspectorForGetPathWeight.getPathWeight('A', 'C') }
	}

	@Test
	@DisplayName("negative weight cycle check")
	fun containsNegativeWeightCycle() {
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
	@DisplayName("path is not exist")
	fun nonExistentPath() {
		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B', 10.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 3.0)
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { inspectorForGetPath.getPath('B', 'A') }
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { inspectorForGetPathWeight.getPathWeight('B', 'A') }
	}

	@Test
	@DisplayName("finding the shortest path")
	fun findingTheShortestPath() {
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
	@DisplayName("recalculation of the path when the source vertex changes")
	fun recalculationForAnotherStartingVertex() {
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
	}

	@Test
	@DisplayName("without recalculating the path")
	fun withoutRecalculatingThePath() {
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
	}
}
