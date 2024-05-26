package utils.representation

import androidx.compose.ui.unit.dp
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import views.radiusStart
import kotlin.math.*

class ForceDirectedPlacementStrategy(
	private val viewModel: GraphViewModel,
) : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		forceDirected(
			width,
			height,
			null,
			750, // recommend 10000 // test 750
			1.0, // attraction
			10.0, // repulsion: recommend 10
			100.0, // repulsion overlapping: recommend 100
			5.0, // gravity: recommend 5
			1, // the weight effects: 0 / 1 / 2
			0.002, // recommend 0.1 // test 0.01 // test 0.002
			10.0, // recommend 10
			1.0, // recommend 1
			0.00001 // recommend 0.00001
		)
	}

	fun placeWithoutVertex(width: Double, height: Double, vertex: VertexViewModel) {
		forceDirected(
			width,
			height,
			vertex,
			2,
			1.0,
			10.0,
			10.0,
			0.0,
			0,
			50.0, // test 0.001
			10.0,
			1.0,
			0.00001
		)
	}

	private fun forceDirected(
		width: Double,
		height: Double,
		vertex: VertexViewModel?,
		countIteration: Int,
		kAttr: Double,
		kRepul: Double,
		kRepulOver: Double,
		kGrav: Double,
		kDegree: Int,
		kSpeed: Double,
		kSpeedMax: Double,
		kTolerance: Double,
		inaccuracy: Double
	) {
		if (viewModel.vertices.isEmpty()) return

		var speedGlobal: Double
		var switchingGlobal = 0.0
		var tractionGlobal = 0.0
		val tableForceLastAndSwitching = viewModel.vertices.associateWith {
			mutableListOf(0.0, 0.0)
		}.toMutableMap()

		var speedGlobalLast: Double = Double.POSITIVE_INFINITY
		var maxForceTotal = 1.0 // todo

		var countCur = 0
		while (countCur++ < countIteration) {
			val tableForce = viewModel.vertices.associateWith {
				mutableListOf(0.0, 0.0)
			}.toMutableMap()

			viewModel.vertices.forEach { vertexSourceViewModel ->
				val length = sqrt(vertexSourceViewModel.xPos.value.toDouble().pow(2) + vertexSourceViewModel.yPos.value.toDouble().pow(2))
				val forceGrav = kGrav * (vertexSourceViewModel.degree + 1) * length
//				val forceGrav = kGrav * (vertexSourceViewModel.degree + 1)

				tableForce[vertexSourceViewModel] = mutableListOf(forceGrav, forceGrav)

				viewModel.vertices.filter { it != vertexSourceViewModel }.forEach { vertexTargetViewModel ->
					val difX = (vertexTargetViewModel.xPos - vertexSourceViewModel.xPos).value.toDouble()
					val difY = (vertexTargetViewModel.yPos - vertexSourceViewModel.yPos).value.toDouble()

					val distance = sqrt(difX.pow(2) + difY.pow(2))
					val distanceOverlapping = distance - vertexSourceViewModel.radius / radiusStart - vertexTargetViewModel.radius / radiusStart

					var forceRepul = 0.0
					if (distanceOverlapping > 0) {
						forceRepul = kRepul * (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1) / distanceOverlapping
					} else if (distanceOverlapping < 0) {
						forceRepul = kRepulOver * (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1)
					}
//					val forceRepul = kRepul * (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1) / (inaccuracy + distance)

					checkAndAddFirst(tableForce[vertexSourceViewModel], - forceRepul * difX)
					checkAndAddSecond(tableForce[vertexSourceViewModel], - forceRepul * difY)
				}
			}

			viewModel.edges.forEach { edgeViewModel ->
				val difX = (edgeViewModel.target.xPos - edgeViewModel.source.xPos).value.toDouble()
				val difY = (edgeViewModel.target.yPos - edgeViewModel.source.yPos).value.toDouble()

				val distance = sqrt(difX.pow(2) + difY.pow(2))
				val distanceOverlapping = distance - edgeViewModel.source.radius / radiusStart - edgeViewModel.target.radius / radiusStart

				var forceAttr = 0.0
				if (distanceOverlapping > 0) {
					forceAttr = distanceOverlapping
				}
//				val forceAttr = kAttr * edgeViewModel.edge.weight.pow(kDegree) * (distance / edgeViewModel.source.degree + 1)
//				val forceAttr = kAttr * edgeViewModel.edge.weight.pow(kDegree) * (log2(1 + distance) / edgeViewModel.source.degree + 1) // todo(test)

				checkAndAddFirst(tableForce[edgeViewModel.source], forceAttr * difX)
				checkAndAddSecond(tableForce[edgeViewModel.source], forceAttr * difY)
			}

			viewModel.vertices.forEach { vertexViewModel ->
				val forceCur = sqrt(
					checkAndGetFirst(tableForce[vertexViewModel]).pow(2) +
						checkAndGetSecond(tableForce[vertexViewModel]).pow(2)
				)
				maxForceTotal = max(maxForceTotal, forceCur) // todo(test)

				val switchingCur = abs(forceCur - checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel]))
				val tractionCur = abs(forceCur + checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel])) / 2

				switchingGlobal += ((vertexViewModel.degree + 1) * switchingCur)
				tractionGlobal += ((vertexViewModel.degree + 1) * tractionCur)
				tableForceLastAndSwitching[vertexViewModel] = mutableListOf(forceCur, switchingCur)
			}

			speedGlobal = kTolerance * tractionGlobal / switchingGlobal

			viewModel.vertices.filter { it != vertex }.forEach { vertexViewModel ->
				val forceCur = checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel])

				var speedForce = kSpeed * speedGlobal /
					(1.0 + speedGlobal * sqrt(checkAndGetSecond(tableForceLastAndSwitching[vertexViewModel])))
				if (speedForce >= kSpeedMax / (inaccuracy + forceCur)) { // todo(analyse forceCur?)
					speedForce = (speedForce - kSpeedMax / forceCur) * inaccuracy
				}

				vertexViewModel.xPos += (speedForce * checkAndGetFirst(tableForce[vertexViewModel])).dp
				vertexViewModel.yPos += (speedForce * checkAndGetSecond(tableForce[vertexViewModel])).dp
			}
		}

		viewModel.vertices.forEach { vertexViewModel ->
			if (vertexViewModel.xPos > 0.dp) vertexViewModel.xPos = min(vertexViewModel.xPos.value, width.toFloat()).dp
			else vertexViewModel.xPos = max(vertexViewModel.xPos.value, 0f).dp

			if (vertexViewModel.yPos > 0.dp) vertexViewModel.yPos = min(vertexViewModel.yPos.value, height.toFloat()).dp
			else vertexViewModel.yPos = max(vertexViewModel.yPos.value, 0f).dp
		}
	}
}

fun checkAndGetFirst(list: MutableList<Double>?): Double {
	return list?.get(0) ?: throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
}

fun checkAndGetSecond(list: MutableList<Double>?): Double {
	return list?.get(1) ?: throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
}

fun checkAndAddFirst(list: MutableList<Double>?, value: Double) {
	if (list == null) throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
	list[0] += value
}

fun checkAndAddSecond(list: MutableList<Double>?, value: Double) {
	if (list == null) throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
	list[1] += value
}
