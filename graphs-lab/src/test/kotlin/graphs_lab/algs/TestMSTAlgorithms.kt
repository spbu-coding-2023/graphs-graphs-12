package graphs_lab.algs

import graphs_lab.assertEdgesCollection
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.WeightedGraph
import graphs_lab.fillGraphEdges
import graphs_lab.isValidEdgeCollection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestMSTAlgorithms {
	private val vertices = setOf(1, 2, 3, 4, 5, 6)
	private lateinit var graph: WeightedGraph<Int>

	@BeforeEach
	fun setup() {
		graph = WeightedGraph("mst-test-graph", isAutoAddVertex = true)
		vertices.forEach { vertex ->
			graph.addVertex(vertex)
		}
	}

	@Test
	@DisplayName("initialize by undirected graph")
	fun testInitByUndirectedGraph() {
		Assertions.assertDoesNotThrow {
			MSTAlgorithms(graph)
		}
		Assertions.assertEquals(graph, MSTAlgorithms(graph).graph)
	}

	@Test
	@DisplayName("initialize by directed graph")
	fun testInitByDirectedGraph() {
		Assertions.assertThrows(IllegalArgumentException::class.java) {
			MSTAlgorithms(
				WeightedGraph<Int>("mst-test-graph", isDirected = true)
			)
		}
	}

	@Test
	@DisplayName("search for MST by Kruscal in graph with a single connectivity component")
	fun testSearchMSTInSingleConnectivityComponentGraphCase1() {
		// See example view on test/resources/examples/kruskalCase1.png
		fillGraphEdges(
			graph,
			listOf(
				Triple(1, 2, 4.0),
				Triple(1, 5, -2.0),
				Triple(1, 3, 6.0),
				Triple(2, 3, -1.0),
				Triple(2, 6, 5.0),
				Triple(3, 5, 11.0),
				Triple(4, 5, 7.0),
				Triple(4, 6, -3.0),
				Triple(5, 6, 2.0),
			)
		)
		assertEdgesCollection(
			setOf(
				Triple(4, 6, -3.0),
				Triple(1, 5, -2.0),
				Triple(2, 3, -1.0),
				Triple(5, 6, 2.0),
				Triple(1, 2, 4.0),
			),
			MSTAlgorithms(graph).kruskalAlgorithm(),
			isDirected = false
		)
	}

	@Test
	@DisplayName("search for MST by Prim in graph with a single connectivity component")
	fun testSearchMSTInSingleConnectivityComponentGraphCase2() {
		// See example view on test/resources/example/primCase1.png
		fillGraphEdges(
			graph,
			listOf(
				Triple(1, 2, 4.0),
				Triple(1, 5, -2.0),
				Triple(1, 3, 6.0),
				Triple(2, 3, -1.0),
				Triple(2, 6, 5.0),
				Triple(3, 5, 11.0),
				Triple(4, 5, 7.0),
				Triple(4, 6, -3.0),
				Triple(5, 6, 2.0),
			)
		)
		assertEdgesCollection(
			setOf(
				Triple(1, 5, -2.0),
				Triple(5, 6, 2.0),
				Triple(4, 6, -3.0),
				Triple(1, 2, 4.0),
				Triple(2, 3, -1.0),
			),
			MSTAlgorithms(graph).primAlgorithm(),
			isDirected = false
		)
	}

	@Test
	@DisplayName("search for MST by Kruscal in graph with a lot of connectivity component")
	fun testSearchMSTInALotOfConnectivityComponentGraphCase1() {
		// See example view on test/resources/examples/kruskalCase2.png
		fillGraphEdges(
			graph,
			listOf(
				Triple(1, 3, -2.0),
				Triple(1, 7, 9.0),
				Triple(2, 6, -2.0),
				Triple(3, 7, -3.0),
				Triple(4, 5, 4.0),
			)
		)
		assertEdgesCollection(
			setOf(
				Triple(1, 3, -2.0),
				Triple(2, 6, -2.0),
				Triple(3, 7, -3.0),
				Triple(4, 5, 4.0),
			),
			MSTAlgorithms(graph).kruskalAlgorithm(),
			isDirected = false
		)
	}

	@Test
	@DisplayName("search for MST by Prim in graph with a lot of connectivity component")
	fun testSearchMSTInALotOfConnectivityComponentGraphCase2() {
		// See example view on test/resources/examples/primCase2.png
		fillGraphEdges(
			graph,
			listOf(
				Triple(1, 3, -2.0),
				Triple(1, 7, 9.0),
				Triple(2, 6, -2.0),
				Triple(3, 7, -3.0),
				Triple(4, 5, 4.0),
			)
		)
		assertEdgesCollection(
			setOf(
				Triple(1, 3, -2.0),
				Triple(2, 6, -2.0),
				Triple(3, 7, -3.0),
				Triple(4, 5, 4.0),
			),
			MSTAlgorithms(graph).primAlgorithm(),
			isDirected = false
		)
	}

	@Test
	@DisplayName("search for MST by Kruscal in graph with edges which weight is equals")
	fun setSearchMSTInGraphWithEdgesWhichWeightIsEqualsCase1() {
		// See example view on test/resources/examples/kruskalCase3.png
		fillGraphEdges(
			graph,
			listOf(
				Triple(3, 4, 1.0),
				Triple(3, 5, 1.0),
				Triple(4, 5, 1.0),
			)
		)
		val mst: Set<Edge<Int>> = MSTAlgorithms(graph).kruskalAlgorithm()
		var validation: Pair<Boolean, AssertionError?> = isValidEdgeCollection(
			setOf(
				Triple(3, 4, 1.0),
				Triple(3, 5, 1.0),
			),
			mst,
			isDirected = false
		)
		if (validation.first) return
		validation = isValidEdgeCollection(
			setOf(
				Triple(5, 4, 1.0),
				Triple(3, 5, 1.0),
			),
			mst,
			isDirected = false
		)
		if (validation.first) return
		assertEdgesCollection(
			setOf(
				Triple(3, 4, 1.0),
				Triple(4, 5, 1.0),
			),
			mst,
			isDirected = false
		)
	}

	@Test
	@DisplayName("search for MST by Prim in graph with edges which weight is equals")
	fun setSearchMSTInGraphWithEdgesWhichWeightIsEqualsCase2() {
		// See example view on test/resources/examples/primCase3.png
		fillGraphEdges(
			graph,
			listOf(
				Triple(3, 4, 1.0),
				Triple(3, 5, 1.0),
				Triple(4, 5, 1.0),
			)
		)
		val mst: Set<Edge<Int>> = MSTAlgorithms(graph).primAlgorithm()
		var validation: Pair<Boolean, AssertionError?> = isValidEdgeCollection(
			setOf(
				Triple(3, 4, 1.0),
				Triple(3, 5, 1.0),
			),
			mst,
			isDirected = false
		)
		if (validation.first) return
		validation = isValidEdgeCollection(
			setOf(
				Triple(5, 4, 1.0),
				Triple(3, 5, 1.0),
			),
			mst,
			isDirected = false
		)
		if (validation.first) return
		assertEdgesCollection(
			setOf(
				Triple(3, 4, 1.0),
				Triple(4, 5, 1.0),
			),
			mst,
			isDirected = false
		)
	}
}
