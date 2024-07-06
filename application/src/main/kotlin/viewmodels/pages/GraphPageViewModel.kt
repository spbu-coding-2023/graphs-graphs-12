package viewmodels.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import models.SettingsModel
import models.VertexID
import models.utils.AlgorithmModel
import themes.JetTheme
import utils.GraphSavingType
import utils.PageType
import utils.placement_strategy.*
import viewmodels.graphs.GraphViewModel
import windowSizeStart
import java.io.File

/**
 * ViewModel for the GraphPage. Manages the state of the graph, algorithms, and representation strategies.
 *
 * @property settings the SettingsModel instance for saving and loading graph data
 * @property indexSelectedPage the MutableState of the currently selected page index
 * @property representationStrategy the MutableState of the current representation strategy
 * @property dbType the current database type
 * @property dbPath the current database path
 * @property graphViewModel the GraphViewModel instance for the current graph
 * @property algorithms the list of algorithms to be displayed in the side menu
 * @property mapRepresentationModes the list of map representation modes to be displayed in the side menu
 */
class GraphPageViewModel(val settings: SettingsModel, val indexSelectedPage: MutableState<Int>) {
	private val _representationStrategy = mutableStateOf<RepresentationStrategy>(RandomPlacementStrategy())
	private val _graph = mutableStateOf<GraphViewModel?>(null)
	var representationStrategy: RepresentationStrategy
		get() = _representationStrategy.value
		set(newStrategy) {
			_representationStrategy.value = newStrategy
			updateGraphRepresentation()
		}
	var dbType = GraphSavingType.LOCAL_FILE
	var dbPath = ""
	var graphViewModel: GraphViewModel?
		get() = _graph.value
		set(newModel) {
			save()
			_graph.value = newModel
			if (newModel == null) indexSelectedPage.value = PageType.HOME_PAGE.ordinal
			updateGraphRepresentation()
		}
	val algorithms = setOf<AlgorithmModel>(
		AlgorithmModel(
			"Key Vertices",
			{ graphViewModel, _ -> graphViewModel.parseLeaderRank() },
		),
		AlgorithmModel(
			"Clusters",
			{ graphViewModel, _ -> graphViewModel.parseLouvainClustering() }
		),
		AlgorithmModel(
			"Strongly Connected Component",
			{ graphViewModel, _ ->
				graphViewModel.parseTarjanStrongConnectivityAlgorithm()
			},
			directionalRequirement = true
		),
		AlgorithmModel(
			"Bridges",
			{ graphViewModel, _ ->
				graphViewModel.parseTarjanBridgeFinding()
			},
			nonDirectionalRequirement = true
		),
		AlgorithmModel(
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
		AlgorithmModel(
			"Minimum Spanning Tree (Kruskal)",
			{ graphViewModel, _ ->
				graphViewModel.parseKruskalAlgorithm()
			},
			nonDirectionalRequirement = true
		),
		AlgorithmModel(
			"Minimum Spanning Tree (Prim)",
			{ graphViewModel, _ ->
				graphViewModel.parsePrimAlgorithm()
			},
			nonDirectionalRequirement = true
		),
		AlgorithmModel(
			"Shortest Path (Bellman)",
			{ graphViewModel, input -> graphViewModel.parseBellmanFordAlgorithm(input.first(), input.last()) },
			{ algButton ->
				dropDownMenuForSP(graphViewModel, algButton)
			}
		),
		AlgorithmModel(
			"Shortest Path (Dijkstra)",
			{ graphViewModel, input -> graphViewModel.parseDijkstraAlgorithm(input.first(), input.last()) },
			{ algButton ->
				dropDownMenuForSP(graphViewModel, algButton)
			}
		),
	)
	val mapRepresentationModes: Map<String, (GraphPageViewModel) -> Unit> = mapOf(
		"Random" to { graphPageViewModel -> graphPageViewModel.representationStrategy = RandomPlacementStrategy() },
		"Circular" to { graphPageViewModel -> graphPageViewModel.representationStrategy = CircularPlacementStrategy() },
		"Force-directed" to { graphPageViewModel ->
			graphPageViewModel.representationStrategy = ForceDirectedPlacementStrategy(graphViewModel!!)
		}
	)

	/**
	 * Updates the graph representation based on the current strategy.
	 *
	 * This function is responsible for repositioning the vertices of the graph based on the selected
	 * representation strategy. It retrieves the current graph model from the `_graph` state, and if it's not null,
	 * it calls the `place` method of the `representationStrategy` with the current window size and the vertices
	 * of the graph model.
	 *
	 * @see RepresentationStrategy.place
	 * @see windowSizeStart
	 * @see GraphViewModel.vertices
	 */
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

	/**
	 * Saves the current graph state to the specified database.
	 *
	 * This function retrieves the current graph model from the `_graph` state and checks if it's not null.
	 * If the graph model is not null, it calls the `saveGraph` method of the `settings` instance, passing
	 * the current `GraphPageViewModel` instance, the `dbType`, and the parent directory of the `dbPath`.
	 *
	 * @throws Exception if an error occurs during the saving process
	 */
	fun save() {
		val graph = _graph.value
		if (graph != null) settings.saveGraph(this, dbType, if (dbPath == "") "" else File(dbPath).parent)
	}
}

// TODO(Move to `colorsForOutlinedTextField` to `colorsForTextButton` and themes/Color.kt)

/**
 * Creates and returns a [TextFieldColors] instance with custom colors for an outlined text field.
 *
 * @return a [TextFieldColors] instance with custom colors
 *
 * @see TextFieldDefaults.textFieldColors
 * @see JetTheme.colors
 * @see Color
 */
@Composable
private fun colorsForOutlinedTextField(): TextFieldColors {
	return TextFieldDefaults.textFieldColors(
		focusedIndicatorColor = JetTheme.colors.secondaryText,
		focusedLabelColor = JetTheme.colors.secondaryText,
		cursorColor = JetTheme.colors.tintColor,
		errorIndicatorColor = Color(176, 0, 0) // Red
	)
}

/**
 * Creates and returns a [ButtonColors] instance with custom colors for a text button.
 *
 * @return a [ButtonColors] instance with custom colors
 *
 * @see ButtonDefaults.buttonColors
 * @see JetTheme.colors
 */
@Composable
private fun colorsForTextButton(): ButtonColors {
	return ButtonDefaults.buttonColors(
		backgroundColor = JetTheme.colors.tertiaryBackground,
		contentColor = JetTheme.colors.secondaryText,
		disabledContentColor = JetTheme.colors.secondaryText,
		disabledBackgroundColor = JetTheme.colors.tertiaryBackground
	)
}

/**
 * Creates a drop-down menu for entering source and target vertices for shortest path algorithms.
 *
 * This function is used to create a user interface for entering the source and target vertices for
 * shortest path algorithms (Bellman-Ford, Dijkstra) in the GraphPageViewModel. It uses Jetpack Compose
 * components to create an input form with error handling.
 *
 * @param graphViewModel the GraphViewModel instance for the current graph
 * @param algButton the AlgorithmModel instance representing the selected shortest path algorithm
 *
 * @see AlgorithmModel
 * @see GraphViewModel
 * @see OutlinedTextField
 * @see TextButton
 * @see colorsForOutlinedTextField
 * @see colorsForTextButton
 */
@Composable
private fun dropDownMenuForSP(
	graphViewModel: GraphViewModel?,
	algButton: AlgorithmModel,
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
