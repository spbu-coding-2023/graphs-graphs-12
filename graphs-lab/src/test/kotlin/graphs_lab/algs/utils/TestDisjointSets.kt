package graphs_lab.algs.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestDisjointSets {
	private val elements = listOf<Int>(1, 2, 4, 8, -1, -2, 4, 8)
	private val startSetsCount: Int = elements.toSet().size
	private lateinit var disjointSets: DisjointSets<Int>

	@BeforeEach
	fun setUp() {
		disjointSets = DisjointSets(elements)
	}

	@Test
	@DisplayName("initialize disjoint sets by not empty collection")
	fun testInitDisjointSetsByNotEmptyCollection() {
		val elements = elements.toSet()
		Assertions.assertEquals(startSetsCount, disjointSets.size)
		for (element in elements) {
			Assertions.assertEquals(element, disjointSets.findRoot(element))
		}
	}

	@Test
	@DisplayName("initialize disjoint sets by empty collection")
	fun testInitDisjointSetsByEmptyCollection() {
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { DisjointSets<Int>(emptyList()) }
	}

	@Test
	@DisplayName("disjoint sets find root of not contained element")
	fun testFindRootOfNotContainedElement() {
		Assertions.assertThrows(
			IllegalArgumentException::class.java
		) { disjointSets.findRoot(3) }
	}

	@Test
	@DisplayName("disjoint sets find root of contained element before union sets")
	fun testFindRootOfContainedElementBeforeUnionSets() {
		Assertions.assertEquals(2, disjointSets.findRoot(2))
	}

	@Test
	@DisplayName("disjoint sets find root of contained element after union sets")
	fun testFindRootOfContainedElementAfterUnionSets() {
		disjointSets.unionSets(1, 2) // parent of union sets is first argument parent(1)
		Assertions.assertEquals(1, disjointSets.findRoot(1))
		Assertions.assertEquals(1, disjointSets.findRoot(2))
		Assertions.assertEquals(startSetsCount - 1, disjointSets.size)
	}

	@Test
	@DisplayName("disjoint sets find root of contained element after big union sets")
	fun testFindRootOfContainedElementAfterBigUnionSets() {
		disjointSets.unionSets(1, 2) // parent of union sets is first argument parent(1)
		disjointSets.unionSets(2, -2) // parent of union sets is first argument parent(1)
		Assertions.assertEquals(1, disjointSets.findRoot(-2))
		disjointSets.unionSets(8, 4) // parent of union sets is first argument parent(8)
		disjointSets.unionSets(-1, 4) // parent of union sets is second argument parent(8)
		Assertions.assertEquals(8, disjointSets.findRoot(-1))
		disjointSets.unionSets(2, 4) // big union
		Assertions.assertEquals(1, disjointSets.findRoot(1))
		Assertions.assertEquals(1, disjointSets.findRoot(2))
		Assertions.assertEquals(1, disjointSets.findRoot(4))
		Assertions.assertEquals(1, disjointSets.findRoot(8))
		Assertions.assertEquals(1, disjointSets.findRoot(-1))
		Assertions.assertEquals(1, disjointSets.findRoot(-2))
		Assertions.assertEquals(1, disjointSets.size)
	}

	@Test
	@DisplayName("disjoint sets union element with itself")
	fun testUnionElementWithItself() {
		disjointSets.unionSets(1, 1) // parent of union sets is first argument parent(1)
		Assertions.assertEquals(1, disjointSets.findRoot(1))
		Assertions.assertEquals(startSetsCount, disjointSets.size)
	}

	@Test
	@DisplayName("is not connected elements from different disjoint sets")
	fun testIsNotConnectedElementsFromDifferentDisjointSets() {
		Assertions.assertFalse(disjointSets.isConnected(1, 2))
	}

	@Test
	@DisplayName("is connected elements from one disjoint sets")
	fun testIsConnectedElementsFromOneDisjointSets() {
		disjointSets.unionSets(1, 2)
		Assertions.assertTrue(disjointSets.isConnected(1, 2))
	}

	@Test
	@DisplayName("disjoint sets to string view")
	fun testDisjointSetsToStringView() {
		Assertions.assertEquals(
			"DisjointSets[[1], [2], [4], [8], [-1], [-2]]",
			disjointSets.toString()
		)
	}
}
