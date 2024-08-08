package utils.placement_strategy

import androidx.compose.ui.unit.dp
import mu.KotlinLogging
import themes.radiusVerticesStart
import viewmodels.graphs.VertexViewModel
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val logger = KotlinLogging.logger("CircularPlacementStrategy")

/**
 * A class that implements the [RepresentationStrategy] interface and places vertices in a circular pattern.
 */
class CircularPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		if (vertices.isEmpty()) {
			logger.info { "There is nothing to place: vertices collection is empty" }
			return
		}
		logger.info { "Place graph on field with size ${width}x$height" }
		val center = Pair(width / 2, height / 2)
		val angle = 2 * Math.PI / vertices.size

		val sorted = vertices.sortedBy { it.label }
		val first = sorted.first()
		var point = Pair(center.first, center.second - min(width, height) / 2 - radiusVerticesStart.value)
		first.xPos = point.first.dp
		first.yPos = point.second.dp

		sorted
			.drop(1)
			.onEach {
				point = point.rotate(center, angle)
				it.xPos = point.first.dp
				it.yPos = point.second.dp
			}
	}

	/**
	 * Rotates a point around a pivot point by the given angle.
	 *
	 * @param pivot the pivot point around which the point will be rotated
	 * @param angle the angle in radians by which the point will be rotated
	 * @return the rotated point
	 */
	private fun Pair<Double, Double>.rotate(pivot: Pair<Double, Double>, angle: Double): Pair<Double, Double> {
		val sin = sin(angle)
		val cos = cos(angle)

		val diff = first - pivot.first to second - pivot.second
		val rotated = Pair(
			diff.first * cos - diff.second * sin,
			diff.first * sin + diff.second * cos,
		)
		return rotated.first + pivot.first to rotated.second + pivot.second
	}
}
