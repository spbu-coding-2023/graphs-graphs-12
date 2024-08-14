package utils.placement_strategy

import androidx.compose.ui.unit.dp
import mu.KotlinLogging
import themes.radiusVerticesStart
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import kotlin.math.*

private val logger = KotlinLogging.logger("ForceDirectedPlacementStrategy")

/**
 * Error message of unexpected algorithm behavior.
 */
const val StandardErrorMessage = "Undefined behaviour: an unfounded vertex."

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
 *
 * @property viewModel the [GraphViewModel] to place
 */
class ForceDirectedPlacementStrategy(
	private val viewModel: GraphViewModel,
) : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		placeForceDirected(
			width,
			height,
			null,
			2000, // recommend 10000
			60.0, // recommend 10
			120.0, // recommend 100
			2.0, // recommend 5
			8.0, // recommend 0.1
			30.0, // recommend 10
			1.5, // recommend 1
			0.999
		)
	}

	/**
	 * Places the [viewModel] vertices but [vertex] on an imaginary canvas.
	 */
	fun placeWithoutVertex(width: Double, height: Double, vertex: VertexViewModel) {
		placeForceDirected(
			width,
			height,
			vertex,
			3,
			30.0,
			60.0,
			0.0,
			0.0005,
			0.6,
			1.0,
			0.999
		)
	}

	/**
	 * Places the [viewModel] vertices but [vertex] on an imaginary canvas with the help of some coefficients.
	 */
	private fun placeForceDirected(
		width: Double,
		height: Double,
		vertex: VertexViewModel?,
		countIteration: Int,
		kRepul: Double,
		kRepulOver: Double,
		kGrav: Double,
		kSpeed: Double,
		kSpeedMax: Double,
		kTolerance: Double,
		inaccuracy: Double
	) {
		if (viewModel.vertices.isEmpty()) {
			logger.info { "There is nothing to place: vertices collection is empty" }
			return
		}
		logger.info { "Place graph on field with size ${width}x$height" }

		var speedGlobal: Double
		var speedGlobalLast: Double = Double.POSITIVE_INFINITY
		var switchingGlobal = 0.0
		var tractionGlobal = 0.0
		val tableForceLastAndSwitching = viewModel.vertices.associateWith {
			mutableListOf(0.0, 0.0)
		}.toMutableMap()

		repeat (countIteration) {
			val tableForce = viewModel.vertices.associateWith {
				mutableListOf(0.0, 0.0)
			}.toMutableMap()

			viewModel.vertices.forEach { vertexSourceViewModel ->
				val length = getNorm(vertexSourceViewModel.xPos.value.toDouble(), vertexSourceViewModel.yPos.value.toDouble())
				val forceGrav = kGrav * (vertexSourceViewModel.degree + 1) * length

				tableForce[vertexSourceViewModel] = mutableListOf(forceGrav, forceGrav)

				viewModel.vertices.filter { it != vertexSourceViewModel }.forEach { vertexTargetViewModel ->
					val difX = (vertexTargetViewModel.xPos - vertexSourceViewModel.xPos).value.toDouble()
					val difY = (vertexTargetViewModel.yPos - vertexSourceViewModel.yPos).value.toDouble()

					val distance = getNorm(difX, difY)
					val distanceOverlapping = distance - vertexSourceViewModel.radius / radiusVerticesStart -
						vertexTargetViewModel.radius / radiusVerticesStart - 3 * radiusVerticesStart.value // adapting the distance
					val productDegrees = (vertexSourceViewModel.degree + 1) * (vertexTargetViewModel.degree + 1)
					val forceRepul = when {
						distanceOverlapping > 0 -> kRepul * productDegrees / distanceOverlapping
						distanceOverlapping < 0 -> kRepulOver * productDegrees
						else -> 0.0
					}

					checkAndAddToFirst(tableForce[vertexSourceViewModel], -forceRepul * difX)
					checkAndAddToSecond(tableForce[vertexSourceViewModel], -forceRepul * difY)
				}
			}

			viewModel.edges.forEach { edgeViewModel ->
				val difX = (edgeViewModel.target.xPos - edgeViewModel.source.xPos).value.toDouble()
				val difY = (edgeViewModel.target.yPos - edgeViewModel.source.yPos).value.toDouble()

				val distance = getNorm(difX, difY)
				val distanceOverlapping = distance - edgeViewModel.source.radius / radiusVerticesStart -
					edgeViewModel.target.radius / radiusVerticesStart - 3 * radiusVerticesStart.value
				val forceAttr = max(distanceOverlapping, 0.0)

				checkAndAddToFirst(tableForce[edgeViewModel.source], forceAttr * difX)
				checkAndAddToSecond(tableForce[edgeViewModel.source], forceAttr * difY)
			}

			viewModel.vertices.forEach { vertexViewModel ->
				val forceCur = getNorm(checkAndGetFirst(tableForce[vertexViewModel]), checkAndGetSecond(tableForce[vertexViewModel]))
				val switchingCur = abs(forceCur - checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel]))
				val tractionCur = abs(forceCur + checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel])) / 2

				switchingGlobal += ((vertexViewModel.degree + 1) * switchingCur)
				tractionGlobal += ((vertexViewModel.degree + 1) * tractionCur)
				tableForceLastAndSwitching[vertexViewModel] = mutableListOf(forceCur, switchingCur)
			}

			speedGlobal = kTolerance * tractionGlobal / switchingGlobal // adapting the global speed
			if (speedGlobal > speedGlobalLast / 2) {
				speedGlobal = speedGlobalLast / 2 * inaccuracy
			}

			viewModel.vertices.filter { it != vertex }.forEach { vertexViewModel ->
				val forceCur = checkAndGetFirst(tableForceLastAndSwitching[vertexViewModel])

				var speedForce = kSpeed * speedGlobal /
					(1 + speedGlobal * sqrt(checkAndGetSecond(tableForceLastAndSwitching[vertexViewModel]))) // adapting the local speed
				if (speedForce >= kSpeedMax / forceCur) {
					speedForce = kSpeedMax / forceCur * inaccuracy
				}

				vertexViewModel.xPos += (speedForce * checkAndGetFirst(tableForce[vertexViewModel])).dp
				vertexViewModel.yPos += (speedForce * checkAndGetSecond(tableForce[vertexViewModel])).dp
			}

			speedGlobalLast = speedGlobal
		}
	}
}

/**
 * Returns the Euclidean norm.
 *
 * @param [x] the x coordinate
 * @param [y] the y coordinate
 *
 * @return the Euclidean norm
 */
fun getNorm(x: Double, y: Double): Double {
	return sqrt(x.pow(2) + y.pow(2))
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
	return list?.get(0) ?: throw ExceptionInInitializerError(StandardErrorMessage)
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
	return list?.get(1) ?: throw ExceptionInInitializerError(StandardErrorMessage)
}

/**
 * Adds the given value to the first item of the given list or throws the exception if the list is null.
 *
 * @param [list] the list whose item need to get
 * @param [value] the value whose item need to add to the given list
 *
 * @throws ExceptionInInitializerError if the list is null
 */
fun checkAndAddToFirst(list: MutableList<Double>?, value: Double) {
	if (list == null) throw ExceptionInInitializerError(StandardErrorMessage)
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
fun checkAndAddToSecond(list: MutableList<Double>?, value: Double) {
	if (list == null) throw ExceptionInInitializerError(StandardErrorMessage)
	list[1] += value
}
