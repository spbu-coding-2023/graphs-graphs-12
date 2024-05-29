package utils.representation

import androidx.compose.ui.unit.dp
import themes.radiusVerticesStart
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import kotlin.math.*

/**
 * The ForceAtlas2 algorithm.
 *
 * It is the force-directed graph drawing algorithm, which is a class of algorithms for drawing graphs in an
 * aesthetically-pleasing way. Their purpose is to position the nodes of a graph in
 * two-dimensional or three-dimensional space so that all the edges are of more or
 * less equal length and there are as few crossing edges as possible, by assigning forces
 * among the set of edges and the set of nodes, based on their relative positions,
 * and then using these forces either to simulate the motion of the edges and nodes or to minimize their energy.
 *
 * @references https://en.wikipedia.org/wiki/Force-directed_graph_drawing
 * @references https://medialab.sciencespo.fr/publications/Jacomy_Heymann_Venturini-Force_Atlas2.pdf
 */
class ForceDirectedPlacementStrategy(
	private val viewModel: GraphViewModel,
) : RepresentationStrategy {
	/**
	 * PLaces the [viewModel] vertices on an imaginary canvas.
	 */
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		placeForceDirected(
			width,
			height,
			null,
			7000, // recommend 10000 // test 750
			1.0, // attraction
			1000.0, // repulsion: recommend 10
			1000.0, // repulsion overlapping: recommend 100
			2.0, // gravity: recommend 5
			1, // the weight effects: 0 / 1 / 2
			6.0, // recommend 0.1 // test 0.01 // test 0.002
			20.0, // recommend 10
			1.0, // recommend 1
			0.999
		)
	}

	/**
	 * PLaces the [viewModel] vertices but [vertex] on an imaginary canvas.
	 */
	fun placeWithoutVertex(width: Double, height: Double, vertex: VertexViewModel) {
		placeForceDirected(
			width,
			height,
			vertex,
			2,
			1.0,
			100.0,
			100.0,
			0.0,
			0,
			0.00005, // test 0.001
			10.0,
			1.0,
			0.999
		)
	}

	/**
	 * PLaces the [viewModel] vertices but [vertex] on an imaginary canvas with the help of some coefficients.
	 */
	private fun placeForceDirected(
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
//				val forceGrav = kGrav * (vertexSourceViewModel.degree + 1) * length
				val forceGrav = kGrav * (vertexSourceViewModel.degree + 1)

				tableForce[vertexSourceViewModel] = mutableListOf(forceGrav, forceGrav)

				viewModel.vertices.filter { it != vertexSourceViewModel }.forEach { vertexTargetViewModel ->
					val difX = (vertexTargetViewModel.xPos - vertexSourceViewModel.xPos).value.toDouble()
					val difY = (vertexTargetViewModel.yPos - vertexSourceViewModel.yPos).value.toDouble()

					val distance = sqrt(difX.pow(2) + difY.pow(2))
					val distanceOverlapping = distance - vertexSourceViewModel.radius / radiusVerticesStart - vertexTargetViewModel.radius / radiusVerticesStart

					var forceRepul = 0.0
					if (distanceOverlapping > 0) {
						forceRepul = kRepul * (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1) / distanceOverlapping
					} else if (distanceOverlapping < 0) {
						forceRepul = kRepulOver * (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1)
					}
//					val forceRepul = kRepul * (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1) / (inaccuracy + distance)

					checkAndAddFirst(tableForce[vertexSourceViewModel],  - forceRepul * difX)
					checkAndAddSecond(tableForce[vertexSourceViewModel],  - forceRepul * difY)
				}
			}

			viewModel.edges.forEach { edgeViewModel ->
				val difX = (edgeViewModel.target.xPos - edgeViewModel.source.xPos).value.toDouble()
				val difY = (edgeViewModel.target.yPos - edgeViewModel.source.yPos).value.toDouble()

				val distance = sqrt(difX.pow(2) + difY.pow(2))
				val distanceOverlapping = distance - edgeViewModel.source.radius / radiusVerticesStart - edgeViewModel.target.radius / radiusVerticesStart

				var forceAttr = 0.0
				if (distanceOverlapping > 0) {
					forceAttr = distanceOverlapping
//					forceAttr = kAttr * (log2(1 + distanceOverlapping))
				}
//				val forceAttr = kAttr * (log2(1 + distance))
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
			if (speedGlobal > speedGlobalLast / 2 ) {
				speedGlobal = speedGlobalLast / 2 * inaccuracy
			}

			viewModel.vertices.filter { it != vertex }.forEach { vertexViewModel ->
				val forceCur = checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel])

				var speedForce = kSpeed * speedGlobal /
					(1 + speedGlobal * sqrt(checkAndGetSecond(tableForceLastAndSwitching[vertexViewModel])))
				if (speedForce >= kSpeedMax / forceCur) { // todo(analyse forceCur?)
					speedForce = kSpeedMax / forceCur * inaccuracy
				}

				vertexViewModel.xPos += (speedForce * checkAndGetFirst(tableForce[vertexViewModel])).dp
				vertexViewModel.yPos += (speedForce * checkAndGetSecond(tableForce[vertexViewModel])).dp
			}

			speedGlobalLast = speedGlobal
		}

//		viewModel.vertices.forEach { vertexViewModel -> // todo(test)
//			if (vertexViewModel.xPos > 0.dp) vertexViewModel.xPos = min(vertexViewModel.xPos.value, width.toFloat()).dp
//			else vertexViewModel.xPos = max(vertexViewModel.xPos.value, 0f).dp
//
//			if (vertexViewModel.yPos > 0.dp) vertexViewModel.yPos = min(vertexViewModel.yPos.value, height.toFloat()).dp
//			else vertexViewModel.yPos = max(vertexViewModel.yPos.value, 0f).dp
//		}
	}
}

/**
 * Returns the first item of the given list or throws the exception if the list is null.
 *
 * @param [list] the list whose item need to get
 *
 * @return the first item of the given list
 * @throws ExceptionInInitializerError if the list is null
 */
fun checkAndGetFirst(list: MutableList<Double>?): Double {
	return list?.get(0) ?: throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
}

/**
 * Returns the second item of the given list or throws the exception if the list is null.
 *
 * @param [list] the list whose item need to get
 *
 * @return the second item of the given list
 * @throws ExceptionInInitializerError if the list is null
 */
fun checkAndGetSecond(list: MutableList<Double>?): Double {
	return list?.get(1) ?: throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
}

/**
 * Adds the given value to the first item of the given list or throws the exception if the list is null.
 *
 * @param [list] the list whose item need to get
 * @param [value] the value whose item need to add to the given list
 *
 * @throws ExceptionInInitializerError if the list is null
 */
fun checkAndAddFirst(list: MutableList<Double>?, value: Double) {
	if (list == null) throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
	list[0] += value
}

/**
 * Adds the given value to the second item of the given list or throws the exception if the list is null.
 *
 * @param [list] the list whose item need to get
 * @param [value] the value whose item need to add to first element of the given list
 *
 * @throws ExceptionInInitializerError if the list is null
 */
fun checkAndAddSecond(list: MutableList<Double>?, value: Double) {
	if (list == null) throw ExceptionInInitializerError("Undefined behaviour: an unfounded vertex.")
	list[1] += value
}
