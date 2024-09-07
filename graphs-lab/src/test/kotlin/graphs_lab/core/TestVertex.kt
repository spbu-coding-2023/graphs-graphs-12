package graphs_lab.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestVertex {
	private val randomizer = Random(42)
	private lateinit var vertex: Vertex<Int>
	private var id: Int = 0

	@BeforeEach
	fun setup() {
		id = randomizer.nextInt(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)
		vertex = Vertex(id)
	}

	@Test
	@DisplayName("initializing vertex")
	fun testInitVertex() {
		Assertions.assertEquals(id, vertex.id)
	}

	@Test
	@DisplayName("vertex to string method")
	fun testVertexToStringMethod() {
		Assertions.assertEquals("Vertex(id = $id)", vertex.toString())
	}

	@Test
	@DisplayName("is equals vertex and itself")
	fun testVertexEqualsMethodCase1() {
		Assertions.assertTrue(vertex.equals(vertex))
	}

	@Test
	@DisplayName("is not equals vertex and null")
	fun testVertexEqualsMethodCase2() {
		Assertions.assertFalse(vertex.equals(null))
	}

	@Test
	@DisplayName("is equals vertex and object by other class")
	fun testVertexEqualsMethodCase3() {
		Assertions.assertFalse(vertex.equals(id))
	}

	@Test
	@DisplayName("is vertex's equals-method save symmetric property")
	fun testVertexEqualsMethodSymmetric() {
		// vertex == other
		var other = Vertex(id)
		Assertions.assertEquals(
			vertex.equals(other),
			other.equals(vertex)
		)
		// vertex != other
		other = Vertex(-id)
		Assertions.assertEquals(
			vertex.equals(other),
			other.equals(vertex)
		)
	}

	@Test
	@DisplayName("is vertex's equals-method save symmetric property")
	fun testVertexEqualsMethodTransitive() {
		// vertex == other1 && other1 == other2 --> vertex == other2
		var other1 = Vertex(id)
		var other2 = Vertex(id)
		Assertions.assertEquals(
			vertex.equals(other1) && other1.equals(other2),
			vertex.equals(other2)
		)
		// vertex == other1 && other1 != other2 --> vertex != other2
		other1 = Vertex(id)
		other2 = Vertex(-id)
		Assertions.assertEquals(
			vertex.equals(other1) && other1.equals(other2),
			vertex.equals(other2)
		)
	}

	@Test
	@DisplayName("validate hash code generation")
	fun testVertexHashCodeMethod() {
		Assertions.assertEquals(
			31 * vertex.javaClass.name.hashCode() + id,
			vertex.hashCode()
		)
	}
}
