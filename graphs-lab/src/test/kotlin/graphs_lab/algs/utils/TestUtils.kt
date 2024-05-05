package graphs_lab.algs.utils

import graphs_lab.core.edges.Edge
import graphs_lab.core.edges.WeightedEdge
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestUtils {

	@Test
	@DisplayName("get weight from unweighted edge with default weight")
	fun testGettingWeightFromUnweightedEdgeCase1() {
		val edge = Edge(1, 2)
		val weight = getEdgeWeight(edge)
		Assertions.assertEquals(1.0, weight)
	}

	@Test
	@DisplayName("get weight from unweighted edge with custom weight")
	fun testGettingWeightFromUnweightedEdgeCase2() {
		val edge = Edge(1, 2)
		val weight = getEdgeWeight(edge, 1.5)
		Assertions.assertEquals(1.5, weight)
	}

	@Test
	@DisplayName("get weight from weighted edge with default weight")
	fun testGettingWeightFromWeightedEdgeCase1() {
		val edge = WeightedEdge(1, 2, 1.25)
		val weight = getEdgeWeight(edge)
		Assertions.assertEquals(1.25, weight)
	}

	@Test
	@DisplayName("get weight from weighted edge with custom weight")
	fun testGettingWeightFromWeightedEdgeCase2() {
		val edge = WeightedEdge(1, 2, 1.25)
		val weight = getEdgeWeight(edge, 1.5)
		Assertions.assertEquals(1.25, weight)
	}

}
