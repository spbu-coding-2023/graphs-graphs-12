package graphs_lab.core.graphs

import graphs_lab.fillGraphEdges
import graphs_lab.fillGraphVertices
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestWeightedGraph {
	private val GRAPH_LABEL = "weighted-graph"
	private lateinit var graph: WeightedGraph<Int>

	@BeforeEach
	fun setUp() {
		graph = WeightedGraph(GRAPH_LABEL)
	}

	@Test
	@DisplayName("add invalid edge to graph (source | target not contains in graph)")
	fun testAddInvalidEdgeGraph() {
		Assertions.assertThrows(
			AssertionError::class.java
		) { fillGraphEdges(graph, listOf(Triple(1, 2, 1.0))) }
		graph.addVertex(1)
		Assertions.assertThrows(
			AssertionError::class.java
		) { fillGraphEdges(graph, listOf(Triple(1, 2, -1.0))) }
	}

	@Test
	@DisplayName("empty graph to string method")
	fun testEmptyGraphToString() {
		Assertions.assertEquals(
			"${graph.javaClass.name}(label = $GRAPH_LABEL, vertices = [], edges = [])",
			graph.toString()
		)
	}

	@Test
	@DisplayName("empty graph to string method")
	fun testFilledGraphToString() {
		fillGraphVertices(graph, listOf(1, 2, 3, 4, 5))
		fillGraphEdges(
			graph,
			listOf(
				Triple(1, 1, -1.0),
				Triple(2, 3, 5.0),
			)
		)
		val edgesView = "[{1, 1, -1.0}, {2, 3, 5.0}, {3, 2, 5.0}]"
		Assertions.assertEquals(
			"${graph.javaClass.name}(label = $GRAPH_LABEL, vertices = [1, 2, 3, 4, 5], edges = $edgesView)",
			graph.toString()
		)
	}

}
