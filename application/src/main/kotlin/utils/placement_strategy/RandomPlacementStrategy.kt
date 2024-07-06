package utils.placement_strategy

import androidx.compose.ui.unit.dp
import themes.radiusVerticesStart
import viewmodels.graphs.VertexViewModel
import kotlin.random.Random
import kotlin.random.asJavaRandom
import kotlin.random.asKotlinRandom

/**
 * A class representing a random placement strategy for vertex placement in a graph representation.
 * This strategy randomly positions each vertex within the given width and height, ensuring that
 * the vertices do not overlap and are placed within the specified radius from the start vertices.
 */
class RandomPlacementStrategy : RepresentationStrategy {
	private val randomizer = Random(Random.nextLong())
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		vertices.forEach { vertex ->
			vertex.xPos = randomizer.nextDouble(
				radiusVerticesStart.value.toDouble(),
				width - radiusVerticesStart.value
			).dp
			vertex.yPos = randomizer.nextDouble(
				radiusVerticesStart.value.toDouble(),
				height - radiusVerticesStart.value
			).dp
		}
	}
}
