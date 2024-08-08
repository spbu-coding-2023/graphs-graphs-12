package graphs_lab.core.graphs

import graphs_lab.core.edges.Edge
import graphs_lab.fillGraphEdges
import graphs_lab.fillGraphVertices
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestDirectedGraph {
	private val graphLabel = "directed-graph"
	private lateinit var graph: UnweightedGraph<Int>

	@BeforeEach
	fun setUp() {
		graph = UnweightedGraph(
			graphLabel,
			isDirected = true,
			isAutoAddVertex = true
		)
	}

	@Test
	@DisplayName("default initializing graph")
	fun testInitGraph() {
		graph = UnweightedGraph(graphLabel)
		Assertions.assertEquals(graphLabel, graph.label)
		Assertions.assertFalse(graph.isAutoAddVertex)
		Assertions.assertFalse(graph.isDirected)
		Assertions.assertEquals(0, graph.idVertices.size)
		Assertions.assertEquals(0, graph.size)
	}

	@Test
	@DisplayName("simple initializing directed graph")
	fun testInitDirectedGraph() {
		graph = UnweightedGraph(graphLabel, isDirected = true, isAutoAddVertex = false)
		Assertions.assertEquals(graphLabel, graph.label)
		Assertions.assertFalse(graph.isAutoAddVertex)
		Assertions.assertTrue(graph.isDirected)
		Assertions.assertEquals(0, graph.idVertices.size)
		Assertions.assertEquals(0, graph.size)
	}

	@Test
	@DisplayName("simple initializing directed graph and with auto add vertex mode")
	fun testInitDirectedGraphWithAutoAddVertexMode() {
		graph = UnweightedGraph(graphLabel, isDirected = true, isAutoAddVertex = true)
		Assertions.assertEquals(graphLabel, graph.label)
		Assertions.assertTrue(graph.isAutoAddVertex)
		Assertions.assertTrue(graph.isDirected)
		Assertions.assertEquals(0, graph.idVertices.size)
		Assertions.assertEquals(0, graph.size)
	}

	@Test
	@DisplayName("add vertices to graph")
	fun testAddVerticesToGraph() {
		val vertices = listOf(1, 2, 3, 4, 5)
		fillGraphVertices(graph, vertices)
		Assertions.assertEquals(vertices.toSet(), graph.idVertices)
		Assertions.assertEquals(vertices.toSet().size, graph.size)
	}

	@Test
	@DisplayName("is graph contains added vertex")
	fun testIsGraphContainsAddedVertex() {
		val vertices = listOf(1, 2, 3, 4, 5)
		fillGraphVertices(graph, vertices)
		Assertions.assertTrue(graph.containsVertex(1))
		Assertions.assertTrue(graph.containsVertex(2))
		Assertions.assertTrue(graph.containsVertex(3))
		Assertions.assertTrue(graph.containsVertex(4))
		Assertions.assertTrue(graph.containsVertex(5))
	}

	@Test
	@DisplayName("is graph contains not added vertex")
	fun testIsGraphContainsNotAddedVertex() {
		val vertices = listOf(1, 2, 3, 4, 5)
		fillGraphVertices(graph, vertices)
		Assertions.assertFalse(graph.containsVertex(-1))
	}

	@Test
	@DisplayName("add eges to graph")
	fun testAddEdgeToGraph() {
		val vertices = listOf(1, 2, 3, 4, 5)
		fillGraphVertices(graph, vertices)
		fillGraphEdges(
			graph,
			listOf(
				Pair(1, 2),
				Pair(3, 4),
				Pair(3, 2),
				Pair(2, 4),
			)
		)
		Assertions.assertEquals(
			setOf(Edge(1, 2)),
			graph.vertexEdges(1)
		)
		Assertions.assertEquals(
			setOf(Edge(2, 4)),
			graph.vertexEdges(2)
		)
		Assertions.assertEquals(
			setOf(
				Edge(3, 2),
				Edge(3, 4)
			),
			graph.vertexEdges(3)
		)
		Assertions.assertEquals(
			emptySet<Edge<Int>>(),
			graph.vertexEdges(4)
		)
		Assertions.assertEquals(
			emptySet<Edge<Int>>(),
			graph.vertexEdges(5)
		)
	}

	@Test
	@DisplayName("remove exists vertex from graph")
	fun testRemoveExistsVertex() {
		fillGraphEdges(
			graph,
			listOf(
				Pair(1, 2),
				Pair(3, 2),
				Pair(5, 1),
			)
		)
		graph.removeVertex(1)
		Assertions.assertEquals(3, graph.size)
		Assertions.assertEquals(setOf(2, 3, 5), graph.idVertices)
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(1))
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(2))
		Assertions.assertEquals(setOf(Edge(3, 2)), graph.vertexEdges(3))
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(5))
	}

	@Test
	@DisplayName("remove not exists vertex from graph")
	fun testRemoveNotExistsVertex() {
		fillGraphEdges(
			graph,
			listOf(
				Pair(1, 2),
				Pair(3, 2),
				Pair(5, 1),
			)
		)
		graph.removeVertex(4)
		Assertions.assertEquals(4, graph.size)
		Assertions.assertEquals(setOf(1, 2, 3, 5), graph.idVertices)
	}

	@Test
	@DisplayName("remove edge with exists source and target")
	fun testRemoveEdgeWithExistsSourceAndTarget() {
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
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(1))
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(2))
		Assertions.assertEquals(setOf(Edge(3, 2)), graph.vertexEdges(3))
		Assertions.assertEquals(setOf(Edge(5, 1)), graph.vertexEdges(5))
	}

	@Test
	@DisplayName("remove edge with not exists source or targe")
	fun testRemoveEdgeWithNotExistsSourceOrTarget() {
		fillGraphEdges(
			graph,
			listOf(
				Pair(1, 2),
				Pair(3, 2),
				Pair(5, 1),
			)
		)
		graph.removeEdge(-1, 2)
		Assertions.assertEquals(4, graph.size)
		Assertions.assertEquals(setOf(1, 2, 3, 5), graph.idVertices)
		Assertions.assertEquals(setOf(Edge(1, 2)), graph.vertexEdges(1))
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(2))
		Assertions.assertEquals(setOf(Edge(3, 2)), graph.vertexEdges(3))
		Assertions.assertEquals(setOf(Edge(5, 1)), graph.vertexEdges(5))
		graph.removeEdge(1, -2)
		Assertions.assertEquals(4, graph.size)
		Assertions.assertEquals(setOf(1, 2, 3, 5), graph.idVertices)
		Assertions.assertEquals(setOf(Edge(1, 2)), graph.vertexEdges(1))
		Assertions.assertEquals(emptySet<Edge<Int>>(), graph.vertexEdges(2))
		Assertions.assertEquals(setOf(Edge(3, 2)), graph.vertexEdges(3))
		Assertions.assertEquals(setOf(Edge(5, 1)), graph.vertexEdges(5))
	}

	@Test
	@DisplayName("add vertices to graph with repetitions")
	fun testAddVerticesToGraphWithRepetitions() {
		val vertices = listOf(1, 2, 3, 4, 5, -1, 5)
		fillGraphVertices(graph, vertices)
		Assertions.assertEquals(vertices.toSet(), graph.idVertices)
		Assertions.assertEquals(vertices.toSet().size, graph.size)
	}
}
