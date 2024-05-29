package viewmodels.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import models.VertexID
import models.utils.AlgorithmButton
import themes.JetTheme
import utils.representation.*
import viewmodels.graphs.GraphViewModel
import windowSizeStart

class GraphPageViewModel {
	private val _representationStrategy = mutableStateOf<RepresentationStrategy>(RandomPlacementStrategy())
	private val _graph = mutableStateOf<GraphViewModel?>(null)
	var representationStrategy: RepresentationStrategy
		get() = _representationStrategy.value
		set(newStrategy) {
			_representationStrategy.value = newStrategy
			updateGraphRepresentation()
		}
	var graphViewModel: GraphViewModel?
		get() = _graph.value
		set(newModel) {
			_graph.value = newModel
			updateGraphRepresentation()
		}
	val algorithms = setOf<AlgorithmButton>(
		AlgorithmButton(
			"Key Vertices",
			{ graphViewModel, _ -> graphViewModel.parseLeaderRank() },
		),

		AlgorithmButton(
			"Clusters",
			{ graphViewModel, _ -> graphViewModel.parseLouvainClustering() }
		),

		AlgorithmButton(
			"Strongly Connected Component",
			{ graphViewModel, _ ->
				if (graphViewModel.graph.isDirected) {
					graphViewModel.parseTarjanStrongConnectivityAlgorithm()
				}
			}
		),

		AlgorithmButton(
			"Bridges",
			{ graphViewModel, _ ->
				if (!graphViewModel.graph.isDirected) {
					graphViewModel.parseTarjanBridgeFinding()
				}
			}
		),

		AlgorithmButton(
			"Cycles",
			{ graphViewModel, input -> graphViewModel.parseCyclesSearchAlgorithm(input.first()) },
			{ algButton ->
				var vertex by remember { mutableStateOf("") }
				var errorVertex by remember { mutableStateOf<Boolean>(false) }

				Text("Enter input details", style = JetTheme.typography.toolbar)
				OutlinedTextField(
					value = vertex,
					onValueChange = { vertex = it },
					label = { Text("Vertex") },
					singleLine = true,
					textStyle = JetTheme.typography.mini,
					isError = errorVertex,
					colors = colorsForOutlinedTextField(),
					modifier = Modifier.padding(4.dp)
				)
				TextButton(
					colors = colorsForTextButton(),
					onClick = {
						try {
							if (graphViewModel!!.graph.containsVertex(
									VertexID.vertexIDFromString(vertex, graphViewModel!!.vertexType)
								)
							) {
								algButton.inputs.value = listOf(vertex)
								algButton.isRun.value = true
							} else errorVertex = true
						} catch (e: Exception) {
							errorVertex = true
						}
					}
				) {
					Text("Run it!")
				}
			}
		),

		AlgorithmButton(
			"Minimum Spanning Tree (Kruskal)",
			{ graphViewModel, _ ->
				if (!graphViewModel.graph.isDirected) {
					graphViewModel.parseKruskalAlgorithm()
				}
			}
		),

		AlgorithmButton(
			"Minimum Spanning Tree (Prim)",
			{ graphViewModel, _ ->
				if (!graphViewModel.graph.isDirected) {
					graphViewModel.parsePrimAlgorithm()
				}
			}
		),

		AlgorithmButton(
			"Shortest Path (Bellman)",
			{ graphViewModel, input -> graphViewModel.parseBellmanFordAlgorithm(input.first(), input.last()) },
			{ algButton ->
				dropDownMenuForSP(graphViewModel, algButton)
			}
		),

		AlgorithmButton(
			"Shortest Path (Dijkstra)",
			{ graphViewModel, input -> graphViewModel.parseDijkstraAlgorithm(input.first(), input.last()) },
			{ algButton ->
				dropDownMenuForSP(graphViewModel, algButton)
			}
		),
	)
	val mapRepresentationModes: Map<String, (GraphPageViewModel) -> Unit> = mapOf(
		"Central" to { graphPageViewModel -> graphPageViewModel.representationStrategy = AllCenterPlacementStrategy() },
		"Random" to { graphPageViewModel -> graphPageViewModel.representationStrategy = RandomPlacementStrategy() },
		"Circular" to { graphPageViewModel -> graphPageViewModel.representationStrategy = CircularPlacementStrategy() },
		"Force-directed" to { graphPageViewModel -> graphPageViewModel.representationStrategy = ForceDirectedPlacementStrategy(graphViewModel!!) } // todo(!!)
	)

	private fun updateGraphRepresentation() {
		// TODO(run it by coroutine scope)
		// TODO(change values of `width` and `height`)
		val model = _graph.value ?: return
		representationStrategy.place(
			windowSizeStart.first.toDouble(),
			windowSizeStart.second.toDouble(),
			model.vertices
		)
	}
}

@Composable
private fun colorsForOutlinedTextField(): TextFieldColors {
	return TextFieldDefaults.textFieldColors(
		focusedIndicatorColor = JetTheme.colors.secondaryText,
		focusedLabelColor = JetTheme.colors.secondaryText,
		cursorColor = JetTheme.colors.tintColor,
		errorIndicatorColor = Color(176, 0, 0) // Red
	)
}

@Composable
private fun colorsForTextButton(): ButtonColors {
	return ButtonDefaults.buttonColors(
		backgroundColor = JetTheme.colors.tertiaryBackground,
		contentColor = JetTheme.colors.secondaryText,
		disabledContentColor = JetTheme.colors.secondaryText,
		disabledBackgroundColor = JetTheme.colors.tertiaryBackground
	)
}

@Composable
private fun dropDownMenuForSP(
	graphViewModel: GraphViewModel?,
	algButton: AlgorithmButton,
) {
	var source by remember { mutableStateOf("") }
	var target by remember { mutableStateOf("") }
	var errorSource by remember { mutableStateOf<Boolean>(false) }
	var errorTarget by remember { mutableStateOf<Boolean>(false) }

	Text("Enter input details", style = JetTheme.typography.toolbar)
	OutlinedTextField(
		value = source,
		onValueChange = { source = it },
		label = { Text("Source") },
		singleLine = true,
		textStyle = JetTheme.typography.mini,
		isError = errorSource,
		colors = colorsForOutlinedTextField(),
		modifier = Modifier.padding(4.dp)
	)
	OutlinedTextField(
		value = target,
		onValueChange = { target = it },
		label = { Text("Target") },
		singleLine = true,
		textStyle = JetTheme.typography.mini,
		isError = errorTarget,
		colors = colorsForOutlinedTextField(),
		modifier = Modifier.padding(4.dp)
	)
	TextButton(
		colors = colorsForTextButton(),
		onClick = {
			try {
				if (
					graphViewModel!!.graph.containsVertex(
						VertexID.vertexIDFromString(source, graphViewModel.vertexType)
					) &&
					graphViewModel.graph.containsVertex(
						VertexID.vertexIDFromString(target, graphViewModel.vertexType)
					)
				) {
					algButton.inputs.value = listOf(source, target)
					algButton.isRun.value = true
				} else {
					errorSource = !graphViewModel.graph.containsVertex(
						VertexID.vertexIDFromString(source, graphViewModel.vertexType)
					)
					errorTarget = !graphViewModel.graph.containsVertex(
						VertexID.vertexIDFromString(target, graphViewModel.vertexType)
					)
				}
			} catch (e: Exception) {
				errorSource = true
				errorTarget = true
			}
		}
	) {
		Text("Run it!")
	}
}
