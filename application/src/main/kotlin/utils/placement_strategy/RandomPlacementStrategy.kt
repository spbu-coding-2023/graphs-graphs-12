package utils.placement_strategy

import androidx.compose.ui.unit.dp
import mu.KotlinLogging
import themes.radiusVerticesStart
import viewmodels.graphs.VertexViewModel
import kotlin.random.Random

private val logger = KotlinLogging.logger("RandomPlacementStrategy")

/**
 * A class representing a random placement strategy for vertex placement in a graph representation.
 * This strategy randomly positions each vertex within the given width and height, ensuring that
 * the vertices do not overlap and are placed within the specified radius from the start vertices.
 */
class RandomPlacementStrategy : RepresentationStrategy {
	private val randomizer = Random(Random.nextLong())
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		if (vertices.isEmpty()) {
			logger.info { "There is nothing to place: vertices collection is empty" }
			return
		}
		logger.info { "Place graph on field with size ${width}x$height" }
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
