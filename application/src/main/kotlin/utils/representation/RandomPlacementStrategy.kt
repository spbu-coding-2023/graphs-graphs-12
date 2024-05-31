package utils.representation

import androidx.compose.ui.unit.dp
import themes.radiusVerticesStart
import viewmodels.graphs.VertexViewModel
import kotlin.random.Random

class RandomPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		val randomizer = Random(Random.nextLong())

		for (vertex in vertices) {
			vertex.xPos = randomizer.nextDouble(radiusVerticesStart.value.toDouble(), width - radiusVerticesStart.value).dp
			vertex.yPos = randomizer.nextDouble(radiusVerticesStart.value.toDouble(), height - radiusVerticesStart.value).dp
		}
	}
}
