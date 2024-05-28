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
		val weight = getEdgeWeightOrDefault(edge)
		Assertions.assertEquals(1.0, weight)
	}

	@Test
	@DisplayName("get weight from unweighted edge with custom weight")
	fun testGettingWeightFromUnweightedEdgeCase2() {
		val edge = Edge(1, 2)
		val weight = getEdgeWeightOrDefault(edge, 1.5)
		Assertions.assertEquals(1.5, weight)
	}

	@Test
	@DisplayName("get weight from weighted edge with default weight")
	fun testGettingWeightFromWeightedEdgeCase1() {
		val edge = WeightedEdge(1, 2, 1.25)
		val weight = getEdgeWeightOrDefault(edge)
		Assertions.assertEquals(1.25, weight)
	}

	@Test
	@DisplayName("get weight from weighted edge with custom weight")
	fun testGettingWeightFromWeightedEdgeCase2() {
		val edge = WeightedEdge(1, 2, 1.25)
		val weight = getEdgeWeightOrDefault(edge, 1.5)
		Assertions.assertEquals(1.25, weight)
	}

	@Test
	@DisplayName("check and get the first element of the pair")
	fun testCheckAndGetFirst() {
		var pair: Pair<Char?, Int?>? = null
		Assertions.assertThrows(ExceptionInInitializerError::class.java) { checkAndGetFirst(pair) }

		pair = Pair(null, 1)
		Assertions.assertThrows(ExceptionInInitializerError::class.java) { checkAndGetFirst(pair) }

		pair = Pair('A', 1)
		Assertions.assertEquals(checkAndGetFirst(pair), 'A')
	}

	@Test
	@DisplayName("check and get the second element of the pair")
	fun testCheckAndGetSecond() {
		var pair: Pair<Char?, Int?>? = null
		Assertions.assertThrows(ExceptionInInitializerError::class.java) { checkAndGetSecond(pair) }

		pair = Pair('A', null)
		Assertions.assertThrows(ExceptionInInitializerError::class.java) { checkAndGetSecond(pair) }

		pair = Pair('A', 1)
		Assertions.assertEquals(checkAndGetSecond(pair), 1)
	}

	@Test
	@DisplayName("check is double equals if its equals")
	fun testDoubleEqualsCase1() {
		Assertions.assertTrue(doubleEquality(0.0000001, 0.0000001))
	}

	@Test
	@DisplayName("check is double not equals if its not equals")
	fun testDoubleEqualsCase2() {
		Assertions.assertFalse(doubleEquality(0.00000001, 0.0000001))
	}

	@Test
	@DisplayName("check is double equality after summary operation")
	fun testDoubleEqualsCase3() {
		Assertions.assertTrue(doubleEquality(0.2 + 0.1, 0.3))
	}

	@Test
	@DisplayName("check is double equality after subtract operation")
	fun testDoubleEqualsCase4() {
		Assertions.assertTrue(doubleEquality(0.2 - 0.1, 0.1))
	}
}
