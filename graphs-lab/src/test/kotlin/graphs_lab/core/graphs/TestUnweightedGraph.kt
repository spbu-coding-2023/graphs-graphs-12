package graphs_lab.core.graphs

import graphs_lab.core.edges.Edge
import graphs_lab.fillGraphEdges
import graphs_lab.fillGraphVertices
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestUnweightedGraph {
	private val graphLabel = "unweighted-graph"
	private lateinit var graph: UnweightedGraph<Int>

	@BeforeEach
	fun setUp() {
		graph = UnweightedGraph(graphLabel)
	}

	@Test
	@DisplayName("empty graph to string method")
	fun testEmptyGraphToString() {
		Assertions.assertEquals(
			"${graph.javaClass.name}(label = $graphLabel, vertices = [], edges = [])",
			graph.toString()
		)
	}

	@Test
	@DisplayName("add invalid edge to graph (source | target not contains in graph)")
	fun testAddInvalidEdgeGraph() {
		Assertions.assertThrows(
			AssertionError::class.java
		) { fillGraphEdges(graph, listOf(Pair(1, 2))) }
		graph.addVertex(1)
		Assertions.assertThrows(
			AssertionError::class.java
		) { fillGraphEdges(graph, listOf(Pair(1, 2))) }
	}

	@Test
	@DisplayName("remove edge with exists source and target")
	fun testRemoveEdgeWithExistsSourceAndTarget() {
		fillGraphVertices(graph, listOf(1, 2, 3, 5))
		fillGraphEdges(
			graph,
			listOf(
				Pair(1, 2),
				Pair(3, 2),
				Pair(5, 1),
			)
		)
		graph.removeEdge(1, 2)
		Assertions.assertEquals(4, graph.size)
		Assertions.assertEquals(setOf(1, 2, 3, 5), graph.idVertices)
		Assertions.assertEquals(setOf(Edge(1, 5)), graph.vertexEdges(1))
		Assertions.assertEquals(setOf(Edge(2, 3)), graph.vertexEdges(2))
		Assertions.assertEquals(setOf(Edge(3, 2)), graph.vertexEdges(3))
		Assertions.assertEquals(setOf(Edge(5, 1)), graph.vertexEdges(5))
	}

	@Test
	@DisplayName("filled graph to string method")
	fun testFilledGraphToString() {
		fillGraphVertices(graph, listOf(1, 2, 3, 4, 5))
		fillGraphEdges(
			graph,
			listOf(
				Pair(1, 1),
				Pair(2, 3),
			)
		)
		val edgesView = "[{1, 1}, {2, 3}, {3, 2}]"
		Assertions.assertEquals(
			"${graph.javaClass.name}(label = $graphLabel, vertices = [1, 2, 3, 4, 5], edges = $edgesView)",
			graph.toString()
		)
	}
}
