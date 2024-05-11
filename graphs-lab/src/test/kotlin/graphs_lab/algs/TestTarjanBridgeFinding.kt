package graphs_lab.algs

import graphs_lab.core.Vertex
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.UnweightedGraph
import graphs_lab.core.graphs.WeightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestTarjanBridgeFinding {

	@Test
	@DisplayName("empty unweighted undirected graph")
	fun testEmptyUnweightedUndirectedGraph() {
		val graph = UnweightedGraph<Char>("Tarjan test", isDirected = false, isAutoAddVertex = true)

		val graphTarjan = TarjanBridgeFinding(graph)
		Assertions.assertEquals(graphTarjan.getBridges(), setOf<Char>())

		Assertions.assertEquals(graphTarjan.graph, graph)
	}

	@Test
	@DisplayName("empty unweighted directed graph")
	fun testEmptyUnweightedDirectedGraph() {
		val graph = UnweightedGraph<Char>("Tarjan test", isDirected = true, isAutoAddVertex = true)

		Assertions.assertThrows(IllegalArgumentException::class.java) { TarjanBridgeFinding(graph) }
	}

	@Test
	@DisplayName("one unweighted edge")
	fun testOneUnweightedEdge() {
		val graph = UnweightedGraph<Char>("Tarjan test", isDirected = false, isAutoAddVertex = true)
		graph.addEdge('1', '2')

		val graphTarjan = TarjanBridgeFinding(graph)
		Assertions.assertEquals(graphTarjan.getBridges(), graph.vertexEdges('1'))
	}

	@Test
	@DisplayName("one weighted edge")
	fun testOneWeightedEdge() {
		val graph = WeightedGraph<Char>("Tarjan test", isDirected = false, isAutoAddVertex = true)
		graph.addEdge('1', '2', 1.0)

		val graphTarjan = TarjanBridgeFinding(graph)
		Assertions.assertEquals(graphTarjan.getBridges(), graph.vertexEdges('1'))
	}

	@Test
	@DisplayName("unweighted connected graph")
	fun testUnweightedConnectedGraph() {
		val graph = UnweightedGraph<String>("Tarjan test", isDirected = false, isAutoAddVertex = true)

		graph.addEdge("1", "2")
		graph.addEdge("1", "5")
		graph.addEdge("1", "10")
		graph.addEdge("1", "13")
		graph.addEdge("2", "13")
		graph.addEdge("12", "13")
		graph.addEdge("10", "11")
		graph.addEdge("2", "3")
		graph.addEdge("4", "3")
		graph.addEdge("9", "3")
		graph.addEdge("9", "8")
		graph.addEdge("7", "8")
		graph.addEdge("7", "6")
		graph.addEdge("5", "6")

		val graphTarjan = TarjanBridgeFinding(graph)
		val setBridges = mutableSetOf<Edge<String>>()

		setBridges.add(graph.vertexEdges("13").find { it.idTarget == "12" }!!)
		setBridges.add(graph.vertexEdges("3").find { it.idTarget == "4" }!!)
		setBridges.add(graph.vertexEdges("10").find { it.idTarget == "11" }!!)
		setBridges.add(graph.vertexEdges("1").find { it.idTarget == "10" }!!)

		Assertions.assertEquals(graphTarjan.getBridges(), setBridges)
	}

	@Test
	@DisplayName("unweighted disconnected graph")
	fun testUnweightedDisconnectedGraph() {
		val graph = UnweightedGraph<String>("Tarjan test", isDirected = false, isAutoAddVertex = true)

		graph.addEdge("1", "2")
		graph.addEdge("1", "5")
		graph.addEdge("1", "10")
		graph.addEdge("1", "13")
		graph.addEdge("2", "13")
		graph.addEdge("12", "13")
		graph.addEdge("2", "3")
		graph.addEdge("4", "3")
		graph.addEdge("9", "3")
		graph.addEdge("9", "8")
		graph.addEdge("5", "8")

		graph.addVertex("11")

		graph.addEdge("7", "6")

		val graphTarjan = TarjanBridgeFinding(graph)
		val setBridges = mutableSetOf<Edge<String>>()

		setBridges.add(graph.vertexEdges("13").find { it.idTarget == "12" }!!)
		setBridges.add(graph.vertexEdges("3").find { it.idTarget == "4" }!!)
		setBridges.add(graph.vertexEdges("1").find { it.idTarget == "10" }!!)
		setBridges.add(graph.vertexEdges("7").find { it.idTarget == "6" }!!)

		Assertions.assertEquals(graphTarjan.getBridges(), setBridges)
	}
}
