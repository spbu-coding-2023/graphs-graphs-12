package graphs_lab.core.edges

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestWeightedEdge {
	private val randomizer = Random(42)
	private lateinit var edge: WeightedEdge<Int>
	private var idSource: Int = 0
	private var idTarget: Int = 0
	private var weight: Double = 0.0

	@BeforeEach
	fun setup() {
		idSource = randomizer.nextInt(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)
		idTarget = randomizer.nextInt(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)
		weight = randomizer.nextDouble(Int.MIN_VALUE / 2.0, Int.MAX_VALUE / 2.0)
		edge = WeightedEdge(idSource, idTarget, weight)
	}

	@Test
	@DisplayName("initializing edge")
	fun testInitEdge() {
		Assertions.assertEquals(idSource, edge.idSource)
		Assertions.assertEquals(idTarget, edge.idTarget)
		Assertions.assertEquals(weight, edge.weight)
	}

	@Test
	@DisplayName("edge to string method")
	fun testEdgeToStringMethod() {
		Assertions.assertEquals(
			"WeightedEdge(source = $idSource, target = $idTarget, weight = $weight)",
			edge.toString()
		)
	}

	@Test
	@DisplayName("is equals edge and itself with other weight")
	fun testEdgeEqualsMethodCase1() {
		Assertions.assertTrue(edge.equals(WeightedEdge(idSource, idTarget, -weight)))
	}
}
