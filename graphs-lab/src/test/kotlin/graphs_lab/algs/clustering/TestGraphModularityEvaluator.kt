package graphs_lab.algs.clustering

import graphs_lab.algs.utils.SetPartition
import graphs_lab.algs.utils.doubleEquality
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.UnweightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestGraphModularityEvaluator {
	private val graph = UnweightedGraph<Int>("test-modularity", isAutoAddVertex = true)
	private val modularityEvaluator: GraphModularityEvaluator<Int, Edge<Int>>

	init {
		for (i in 1..4) {
			for (j in 1..4) {
				if (i == j) continue
				if (i % j == 0 || j % i == 0) {
					graph.addEdge(i, j)
				}
			}
		}
		modularityEvaluator = GraphModularityEvaluator(graph)
	}

	@Test
	@DisplayName("base partition, each vertex in itself set")
	fun testBasePartition() {
		Assertions.assertEquals(0.0, modularityEvaluator.evaluateModularity(SetPartition(graph.idVertices)))
	}

	@Test
	@DisplayName("custom partition, some vertex in one set")
	fun testCustomPartition() {
		val partition = SetPartition(graph.idVertices)
		partition.connectElements(1, 2)
		partition.connectElements(2, 4)
		Assertions.assertEquals(0.125, modularityEvaluator.evaluateModularity(partition))
	}

	@Test
	@DisplayName("check modularity change")
	fun testModularityChangeCase1() {
		val partition = SetPartition(graph.idVertices)
		val modularity = modularityEvaluator.evaluateModularity(partition)
		val (modularityChange, _) = modularityEvaluator.evaluateModularityChange(
			1,
			partition.getElementSet(1),
			partition.getElementSet(2)
		)
		partition.connectElements(1, 2)
		Assertions.assertTrue(
			doubleEquality(modularityEvaluator.evaluateModularity(partition), modularity + modularityChange)
		)
	}
}
