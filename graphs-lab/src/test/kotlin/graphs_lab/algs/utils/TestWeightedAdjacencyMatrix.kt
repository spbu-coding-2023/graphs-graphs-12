package graphs_lab.algs.utils

import graphs_lab.core.graphs.UnweightedGraph
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestWeightedAdjacencyMatrix {
	private val graph = UnweightedGraph<Int>("test-matrix", isAutoAddVertex = true)
	private var totalWeight = 0.0

	init {
		for (i in 1..10) {
			for (j in 1..10) {
				if (i % j == 0 || j % j == 0) {
					totalWeight += 1.0
					graph.addEdge(i, j)
				}
			}
		}
	}

	@Test
	@DisplayName("initialize")
	fun testInitialize() {
		val matrix = WeightedAdjacencyMatrix(graph)
		for (i in 1..10) {
			var vertexDegree = 0.0
			for (j in 1..10) {
				if (i % j == 0 || j % j == 0) {
					Assertions.assertEquals(1.0, matrix.getEdgeWeight(i, j))
					Assertions.assertEquals(1.0, matrix.getEdgeWeight(i, j, 0.0))
					vertexDegree += 1.0
				} else {
					Assertions.assertEquals(0.0, matrix.getEdgeWeight(i, j))
					Assertions.assertEquals(-1.0, matrix.getEdgeWeight(i, j, -1.0))
				}
			}
			Assertions.assertEquals(vertexDegree, matrix.getVertexWeightedDegree(i))
		}
		Assertions.assertEquals(totalWeight, matrix.totalWeight)
	}
}
