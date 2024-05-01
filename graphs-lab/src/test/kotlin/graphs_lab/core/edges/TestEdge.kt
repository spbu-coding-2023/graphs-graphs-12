package graphs_lab.core.edges

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestEdge {
	private val randomizer = Random(42)
	private lateinit var edge: Edge<Int>
	private var idSource: Int = 0
	private var idTarget: Int = 0

	@BeforeEach
	fun setup() {
		idSource = randomizer.nextInt(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)
		idTarget = randomizer.nextInt(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)
		edge = Edge(idSource, idTarget)
	}

	@Test
	@DisplayName("initializing edge")
	fun testInitEdge() {
		Assertions.assertEquals(idSource, edge.idSource)
		Assertions.assertEquals(idTarget, edge.idTarget)
	}

	@Test
	@DisplayName("edge to string method")
	fun testEdgeToStringMethod() {
		Assertions.assertEquals(
			"Edge(source = $idSource, target = $idTarget)",
			edge.toString()
		)
	}

	@Test
	@DisplayName("is equals edge and itself")
	fun testEdgeEqualsMethodCase1() {
		Assertions.assertTrue(edge.equals(edge))
	}

	@Test
	@DisplayName("is not equals edge and null")
	fun testEdgeEqualsMethodCase2() {
		Assertions.assertFalse(edge.equals(null))
	}

	@Test
	@DisplayName("is equals edge and object by other class")
	fun testEdgeEqualsMethodCase3() {
		Assertions.assertFalse(edge.equals(idSource))
	}

	@Test
	@DisplayName("is equals edges with different source's id")
	fun testEdgeEqualsMethodCase4() {
		Assertions.assertFalse(edge.equals(Edge(-idSource, idTarget)))
	}

	@Test
	@DisplayName("is equals edges with different target's id")
	fun testEdgeEqualsMethodCase5() {
		Assertions.assertFalse(edge.equals(Edge(idSource, -idTarget)))
	}

	@Test
	@DisplayName("is edge's equals-method save symmetric property")
	fun testEdgeEqualsMethodSymmetric() {
		// edge == other
		var other = Edge(idSource, idTarget)
		Assertions.assertEquals(edge.equals(other), other.equals(edge))
		// Edge != other
		other = Edge(idTarget, idSource)
		Assertions.assertEquals(edge.equals(other), other.equals(edge))
	}

	@Test
	@DisplayName("is edge's equals-method save symmetric property")
	fun testEdgeEqualsMethodTransitive() {
		// Edge == other1 && other1 == other2 --> Edge == other2
		var other1 = Edge(idSource, idTarget)
		var other2 = Edge(idSource, idTarget)
		Assertions.assertEquals(edge.equals(other1) && other1.equals(other2), edge.equals(other2))
		// Edge == other1 && other1 != other2 --> Edge != other2
		other1 = Edge(idSource, idTarget)
		other2 = Edge(idTarget, idSource)
		Assertions.assertEquals(edge.equals(other1) && other1.equals(other2), edge.equals(other2))
	}

	@Test
	@DisplayName("validate hash code generation")
	fun testVertexHashCodeMethod() {
		var hashCode = edge.javaClass.name.hashCode()
		hashCode = 31 * hashCode + idSource.hashCode()
		hashCode = 31 * hashCode + idTarget.hashCode()
		Assertions.assertEquals(hashCode, edge.hashCode())
	}

}
