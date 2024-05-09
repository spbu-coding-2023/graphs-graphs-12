package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.UnweightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TestCyclesSearchAlgorithms {

	@Test
	@DisplayName("init cycles search algs")
	fun testInitCyclesSearchAlgorithms() {
		val graph = UnweightedGraph<Int>("test-cycles", isDirected = false, isAutoAddVertex = true)
		val cyclesSearchAlgorithms = CyclesSearchAlgorithms(graph)
		Assertions.assertEquals(graph, cyclesSearchAlgorithms.graph)
	}

	@ParameterizedTest(name = "{displayName} [{index}] {0}")
	@MethodSource("undirectedVertexCycles")
	@DisplayName("search vertex cycles at undirected graph")
	fun testUndirectedVertexCycles(edges: Iterable<Edge<Int>>, expectedCycles: Map<Int, Set<List<Int>>>) {
		val graph = UnweightedGraph<Int>("test-cycles", isDirected = false, isAutoAddVertex = true)
		assertGraphCycles(graph, edges, expectedCycles)
	}

	@ParameterizedTest(name = "{displayName} {0}")
	@MethodSource("directedVertexCycles")
	@DisplayName("search vertex cycles at directed graph")
	fun testDirectedVertexCycles(edges: Iterable<Edge<Int>>, expectedCycles: Map<Int, Set<List<Int>>>) {
		val graph = UnweightedGraph<Int>("test-cycles", isDirected = true, isAutoAddVertex = true)
		assertGraphCycles(graph, edges, expectedCycles)
	}

	private fun assertGraphCycles(
		graph: UnweightedGraph<Int>,
		edges: Iterable<Edge<Int>>,
		expectedCycles: Map<Int, Set<List<Int>>>
	) {
		val cyclesSearchAlgorithms = CyclesSearchAlgorithms(graph)

		edges.forEach { edge ->
			graph.addEdge(edge.idSource, edge.idTarget)
		}

		for (idVertex in graph.idVertices) {
			val actualVertexCycles = cyclesSearchAlgorithms.searchVertexCycles(idVertex)
			val expectedVertexCycles = expectedCycles.getOrDefault(idVertex, emptySet())
			Assertions.assertEquals(
				expectedVertexCycles,
				actualVertexCycles
			)
		}
	}

	companion object {
		@JvmStatic
		fun undirectedVertexCycles(): Iterable<Arguments> {
			val argumentsList = mutableListOf<Arguments>()
			// See example view on test/resources/examples/undirectedCyclesCase1.png
			addUndirectedVertexCyclesCase1(argumentsList)
			// See example view on test/resources/examples/undirectedCyclesCase2.png
			addUndirectedVertexCyclesCase2(argumentsList)
			// See example view on test/resources/examples/undirectedCyclesCase3.png
			addUndirectedVertexCyclesCase3(argumentsList)
			// See example view on test/resources/examples/undirectedCyclesCase4.png
			addUndirectedVertexCyclesCase4(argumentsList)
			return argumentsList
		}

		@JvmStatic
		fun directedVertexCycles(): Iterable<Arguments> {
			val argumentsList = mutableListOf<Arguments>()
			addDirectedVertexCyclesCase1(argumentsList)
			return argumentsList
		}

		private fun addDirectedVertexCyclesCase1(argumentsList: MutableList<Arguments>) {
			argumentsList.add(
				Arguments.of(
					Named.of("one big cycle case", oneBigCycleEdges()),
					mapOf(
						Pair(1, setOf(listOf(1, 2, 3, 4))),
						Pair(2, setOf(listOf(2, 3, 4, 1))),
						Pair(3, setOf(listOf(3, 4, 1, 2))),
						Pair(4, setOf(listOf(4, 1, 2, 3))),
					)
				)
			)
		}

		private fun addUndirectedVertexCyclesCase4(argumentsList: MutableList<Arguments>) {
			argumentsList.add(
				Arguments.of(
					Named.of("no cycles case", noCycles()),
					emptyMap<Int, List<Int>>()
				)
			)
		}

		private fun addUndirectedVertexCyclesCase3(argumentsList: MutableList<Arguments>) {
			argumentsList.add(
				Arguments.of(
					Named.of("a lot of cycles case", aLotOfCycles()),
					mapOf(
						Pair(
							1,
							setOf(
								listOf(1, 2, 3),
								listOf(1, 3, 2),
								listOf(1, 2, 3, 4, 5),
								listOf(1, 3, 4, 5),
								listOf(1, 5, 4, 3, 2),
								listOf(1, 5, 4, 3)
							)
						),
						Pair(
							2,
							setOf(
								listOf(2, 1, 3),
								listOf(2, 3, 1),
								listOf(2, 1, 5, 4, 3),
								listOf(2, 3, 4, 5, 1),
							)
						),
						Pair(
							3,
							setOf(
								listOf(3, 1, 2),
								listOf(3, 2, 1),
								listOf(3, 4, 5, 1),
								listOf(3, 4, 5, 1, 2),
								listOf(3, 1, 5, 4),
								listOf(3, 2, 1, 5, 4)
							)
						),
						Pair(
							4,
							setOf(
								listOf(4, 5, 1, 3),
								listOf(4, 5, 1, 2, 3),
								listOf(4, 3, 1, 5),
								listOf(4, 3, 2, 1, 5)
							)
						),
						Pair(
							5,
							setOf(
								listOf(5, 1, 3, 4),
								listOf(5, 1, 2, 3, 4),
								listOf(5, 4, 3, 1),
								listOf(5, 4, 3, 2, 1)
							)
						)
					)
				)
			)
		}

		private fun addUndirectedVertexCyclesCase2(argumentsList: MutableList<Arguments>) {
			argumentsList.add(
				Arguments.of(
					Named.of("cycle with loops case", cycleWithLoops()),
					mapOf(
						Pair(
							1,
							setOf(
								listOf(1, 2, 3),
								listOf(1, 3, 2)
							)
						),
						Pair(
							2,
							setOf(
								listOf(2, 1, 3),
								listOf(2, 3, 1)
							)
						),
						Pair(
							3,
							setOf(
								listOf(3, 1, 2),
								listOf(3, 2, 1)
							)
						)
					)
				)
			)
		}

		private fun addUndirectedVertexCyclesCase1(argumentsList: MutableList<Arguments>) {
			argumentsList.add(
				Arguments.of(
					Named.of("one big cycle case", oneBigCycleEdges()),
					mapOf(
						Pair(
							1,
							setOf(
								listOf(1, 2, 3, 4),
								listOf(1, 4, 3, 2)
							)
						),
						Pair(
							2,
							setOf(
								listOf(2, 1, 4, 3),
								listOf(2, 3, 4, 1)
							)
						),
						Pair(
							3,
							setOf(
								listOf(3, 4, 1, 2),
								listOf(3, 2, 1, 4)
							)
						),
						Pair(
							4,
							setOf(
								listOf(4, 3, 2, 1),
								listOf(4, 1, 2, 3)
							)
						),
					)
				)
			)
		}

		private fun oneBigCycleEdges(): Iterable<Edge<Int>> {
			// See graph view on test/resources/examples/undirectedCyclesCase1.png
			return listOf(
				Edge(1, 2),
				Edge(2, 3),
				Edge(3, 4),
				Edge(4, 1),
			)
		}

		private fun cycleWithLoops(): Iterable<Edge<Int>> {
			// Loop it is path: A-->A and it is not a cycle
			// See graph view on test/resources/examples/undirectedCyclesCase2.png
			return listOf(
				Edge(1, 1),
				Edge(1, 2),
				Edge(1, 3),
				Edge(2, 2),
				Edge(2, 3),
				Edge(3, 3),
			)
		}

		private fun aLotOfCycles(): Iterable<Edge<Int>> {
			// See graph view on test/resources/examples/undirectedCyclesCase3.png
			return listOf(
				Edge(1, 2),
				Edge(1, 3),
				Edge(1, 5),
				Edge(2, 3),
				Edge(3, 4),
				Edge(4, 5),
			)
		}

		private fun noCycles(): Iterable<Edge<Int>> {
			return listOf(
				Edge(1, 2),
				Edge(2, 3)
			)
		}
	}
}
