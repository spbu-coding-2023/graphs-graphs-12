package views.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import graphs_lab.core.edges.WeightedEdge
import models.VertexID
import themes.*
import utils.VertexIDType
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import viewmodels.pages.GraphPageViewModel
import views.graphs.GraphView
import views.graphs.colorChangeFlag

// TODO(docs and refactor its)

/**
 * This function represents the GraphViewPage composable in the application.
 *
 * @param graphPageViewModel the view model for the GraphViewPage. It provides data and functionality for the composable
 */
@Composable
fun GraphViewPage(graphPageViewModel: GraphPageViewModel) {
	val graphViewModel = graphPageViewModel.graphViewModel

	val changeCenter = remember { mutableStateOf(false) }

	val isOpenedContextGraph = remember { mutableStateOf(true) }
	val isOpenedContextEdge = remember { mutableStateOf(false) }
	val idVerticesInfo = remember { mutableStateOf<VertexViewModel?>(null) }
	LaunchedEffect(idVerticesInfo.value) {
		if (idVerticesInfo.value == null) {
			isOpenedContextEdge.value = false
		} else {
			isOpenedContextGraph.value = false
			isOpenedContextEdge.value = true
		}
	}

	val modifierButtons = Modifier
		.size(sizeBottom)
		.padding(paddingCustom)
		.clip(RoundedCornerShape(25.dp))
		.background(JetTheme.colors.primaryBackground, RoundedCornerShape(25.dp))

	if (graphViewModel != null) {
		BoxWithConstraints(
			Modifier
				.fillMaxSize()
				.padding(paddingCustom)
				.clip(JetTheme.shapes.cornerStyle)
				.background(Color(175, 218, 252), JetTheme.shapes.cornerStyle)
		) {
			GraphView(graphViewModel, idVerticesInfo, Offset(maxWidth.value, maxHeight.value), changeCenter)

			IconResetGraph(
				graphViewModel,
				changeCenter,
				Modifier.padding(paddingCustom).align(Alignment.TopEnd),
				modifierButtons,
			)
			Box(Modifier.fillMaxSize()) {
				ColumnInfoGraph(
					graphViewModel, idVerticesInfo, isOpenedContextGraph, isOpenedContextEdge,
					Modifier.align(Alignment.BottomEnd), modifierButtons
				)
			}
		}
	}
}

/**
 * This is a composable function for drawing the icon that resets the [GraphViewModel] display.
 *
 * @param graphViewModel the graph view model whose display will be reset
 * @param changeCenter the boolean variable that sets [graphViewModel] in center of the monitor if true
 * @param modifierBox the box modifier that contains the reset icon
 * @param modifierButtons the iconButton modifier
 */
@Composable
fun IconResetGraph(
	graphViewModel: GraphViewModel,
	changeCenter: MutableState<Boolean>,
	modifierBox: Modifier,
	modifierButtons: Modifier
) {
	Column(modifierBox) {
		IconButton(
			onClick = {
				colorChangeFlag.value = true

				graphViewModel.vertices.forEach {
					it.radius = radiusVerticesStart
					it.color = colorVerticesStart
					it.visibility = true
				}
				graphViewModel.edges.forEach {
					it.width = widthEdgesStart
					it.color = colorEdgesStart
					it.visibility = false
				}
				changeCenter.value = true
			},
			modifier = modifierButtons.pointerHoverIcon(PointerIcon.Hand),
			content = {
				Icon(
					imageVector = Icons.Default.ResetTv,
					contentDescription = "Reset the graph",
					tint = JetTheme.colors.tintColor,
				)
			}
		)
	}
}

/**
 * This is a composable function for drawing the column that represents info about the [GraphViewModel].
 *
 * @param graphViewModel the graph view model whose info will be represented
 * @param idVerticesInfo the vertex id whose info will be represented
 * @param isOpenedContextGraph the boolean variable that opens vertices context of the graph if true
 * @param isOpenedContextEdge the boolean variable that opens edges context of the graph if true
 * @param modifierBox the box modifier that contains info about [graphViewModel]
 * @param modifierButtons the iconButton modifier
 */
@Composable
fun ColumnInfoGraph(
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	isOpenedContextGraph: MutableState<Boolean>,
	isOpenedContextEdge: MutableState<Boolean>,
	modifierBox: Modifier,
	modifierButtons: Modifier
) {
	val isShownWeigh = remember { mutableStateOf(false) }
	LaunchedEffect(isShownWeigh.value) {
		graphViewModel.edges.forEach {
			it.visibility = isShownWeigh.value
		}
	}
	val isShownId = remember { mutableStateOf(true) }
	LaunchedEffect(isShownId.value) {
		graphViewModel.vertices.forEach {
			it.visibility = isShownId.value
		}
	}

	Column(
		modifier = modifierBox.padding(paddingCustom).clip(JetTheme.shapes.cornerStyle),
		verticalArrangement = Arrangement.spacedBy(paddingCustom)
	) {
		IconButton(
			onClick = { isOpenedContextGraph.value = !isOpenedContextGraph.value },
			modifier = modifierButtons
				.align(Alignment.End)
				.pointerHoverIcon(PointerIcon.Hand),
			content = {
				Icon(
					imageVector = if (isOpenedContextGraph.value) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
					contentDescription = "Show menu interaction",
					tint = JetTheme.colors.tintColor
				)
			}
		)

		AnimatedVisibility(
			visible = isOpenedContextGraph.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)
		) {
			MenuAddingItem(graphViewModel, isShownId, isShownWeigh)
		}

		AnimatedVisibility(
			visible = isOpenedContextEdge.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)
		) {
			MenuEditingItem(graphViewModel, idVerticesInfo)
		}
	}
}

/**
 * This is a composable function for drawing menu that adds an item in the [GraphViewModel].
 *
 * @param graphViewModel the graph view model whose info will be represented
 * @param isShownId the boolean variable that shows ids of the graph if true
 * @param isShownWeigh the boolean variable that shows weighs of the graph if true
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuAddingItem(graphViewModel: GraphViewModel, isShownId: MutableState<Boolean>, isShownWeigh: MutableState<Boolean>) {
	val idFastAdd = remember { mutableIntStateOf(graphViewModel.vertices.size + 1) }

	val statePager = rememberPagerState { 2 }

	val addingVertex = {
		val idNew = VertexID(checkAndGet(graphViewModel, idFastAdd), graphViewModel.vertexType)
		graphViewModel.addVertex(idNew)

		graphViewModel.vertices.find { it.id == idNew }!!.visibility = isShownId.value
	}
	val addingEdge = {
		val idSourceNew = VertexID(checkAndGet(graphViewModel, idFastAdd), graphViewModel.vertexType)
		graphViewModel.addVertex(idSourceNew)
		val idTargetNew = VertexID(checkAndGet(graphViewModel, idFastAdd), graphViewModel.vertexType)
		graphViewModel.addVertex(idTargetNew)
		graphViewModel.addEdge(idSourceNew, idTargetNew, 1.0)

		graphViewModel.vertices.find { it.id == idSourceNew }!!.visibility = isShownId.value
		graphViewModel.vertices.find { it.id == idTargetNew }!!.visibility = isShownId.value
		graphViewModel.edges.find {
			it.edge == WeightedEdge(idSourceNew, idTargetNew, 1.0)
		}!!.visibility = isShownWeigh.value
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(paddingCustom)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle),
		horizontalArrangement = Arrangement.spacedBy(paddingCustom)
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.height(145.dp),
			verticalArrangement = Arrangement.spacedBy(paddingCustom)
		) {
			val fontSizeText = 14.sp
			val fontWeightText = FontWeight.W200

			Text(
				"Graph name: ${graphViewModel.graph.label}",
				style = JetTheme.typography.toolbar,
				maxLines = 1,
				color = JetTheme.colors.secondaryText
			)
			Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
				Column(
					modifier = Modifier.weight(1f),
					verticalArrangement = Arrangement.spacedBy(paddingCustom)
				) {
					if (graphViewModel.graph.isDirected) {
						Text(
							"Directed",
							fontSize = fontSizeText,
							fontWeight = fontWeightText,
							color = JetTheme.colors.secondaryText
						)
					} else {
						Text(
							"Undirected",
							fontSize = fontSizeText,
							fontWeight = fontWeightText,
							color = JetTheme.colors.secondaryText
						)
					}
					if (!graphViewModel.isUnweighted) {
						Text(
							"Weighted",
							fontSize = fontSizeText,
							fontWeight = fontWeightText,
							color = JetTheme.colors.secondaryText
						)
					} else {
						Text(
							"Unweighted",
							fontSize = fontSizeText,
							fontWeight = fontWeightText,
							color = JetTheme.colors.secondaryText
						)
					}
					if (graphViewModel.vertexType == VertexIDType.INT_TYPE) {
						Text(
							"Int type",
							fontSize = fontSizeText,
							fontWeight = fontWeightText,
							color = JetTheme.colors.secondaryText
						)
					} else {
						Text(
							"String type",
							fontSize = fontSizeText,
							fontWeight = fontWeightText,
							color = JetTheme.colors.secondaryText
						)
					}

					val modifierCheckbox = Modifier
						.size(20.dp)
						.padding(paddingCustom)
					val colorCheckbox = CheckboxDefaults.colors(Color.Black)

					Spacer(Modifier.size(paddingCustom))

					if (!graphViewModel.isUnweighted) {
						Box(Modifier.fillMaxWidth()) {
							Text("Show weight", fontSize = fontSizeText, color = JetTheme.colors.secondaryText)
							Checkbox(
								checked = isShownWeigh.value,
								onCheckedChange = { isShownWeigh.value = !isShownWeigh.value },
								modifier = modifierCheckbox.align(Alignment.CenterEnd),
								colors = colorCheckbox
							)
						}
					}

					Box(Modifier.fillMaxWidth()) {
						Text("Show id", fontSize = fontSizeText, color = JetTheme.colors.secondaryText)
						Checkbox(
							checked = isShownId.value,
							onCheckedChange = { isShownId.value = !isShownId.value },
							modifier = modifierCheckbox.align(Alignment.CenterEnd),
							colors = colorCheckbox
						)
					}
				}

				Column(
					modifier = Modifier.weight(2f),
					verticalArrangement = Arrangement.spacedBy(paddingCustom)
				) {
					BoxFastAddingItem("Vertices:", graphViewModel.vertices.size, addingVertex)
					BoxFastAddingItem("Edges:", graphViewModel.countEdges, addingEdge)
				}
			}
		}

		Column(
			Modifier
				.weight(1f)
				.height(145.dp)
		) {
			VerticalPager(
				modifier = Modifier
					.fillMaxSize()
					.clip(JetTheme.shapes.cornerStyle)
					.background(whiteCustom, JetTheme.shapes.cornerStyle),
				state = statePager,
				userScrollEnabled = true
			) { index ->
				when (index) {
					0 -> MenuAddingVertex(graphViewModel, isShownId.value)
					1 -> MenuAddingEdge(graphViewModel, isShownId.value, isShownWeigh.value)
				}
			}
		}
	}
}

/**
 * This is a composable function for drawing menu that edits an item in the [GraphViewModel].
 *
 * @param graphViewModel the graph view model whose info will be represented
 * @param idVerticesInfo the vertex id whose info will be represented
 */
@Composable
fun MenuEditingItem(graphViewModel: GraphViewModel, idVerticesInfo: MutableState<VertexViewModel?>) {
	var setEdges by remember { mutableStateOf(graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id)) }

	var isChanged by remember { mutableStateOf(false) }
	LaunchedEffect(isChanged) {
		if (idVerticesInfo.value != null) {
			setEdges = graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id)
		}
		isChanged = false
	}
	LaunchedEffect(idVerticesInfo.value) {
		if (idVerticesInfo.value != null) {
			setEdges = graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id)
		}
	}

	val removingVertexSource: () -> Unit = {
		graphViewModel.removeVertex(idVerticesInfo.value!!.id)
		idVerticesInfo.value = null
	}
	val removingVertexTarget: (WeightedEdge<VertexID>) -> Unit = {
		graphViewModel.removeVertex(it.idTarget)
		isChanged = true
	}
	val removingEdge: (WeightedEdge<VertexID>) -> Unit = {
		graphViewModel.removeEdge(it.idSource, it.idTarget)
		isChanged = true
	}

	val stringVertex: (WeightedEdge<VertexID>) -> String = {
		"Vertex: ${it.idTarget.valueToString()}"
	}
	val stringEdge: (WeightedEdge<VertexID>) -> String = {
		"Edge: ${it.idSource.valueToString()} -> ${it.idTarget.valueToString()}" +
			if (!graphViewModel.isUnweighted) {
				if (it.weight.rem(1) == 0.0) {
					", weight: ${it.weight.toInt()}"
				} else {
					", weight: ${it.weight}"
				}
			} else {
				""
			}
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(paddingCustom)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle),
		horizontalArrangement = Arrangement.spacedBy(paddingCustom)
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(paddingCustom),
			verticalArrangement = Arrangement.spacedBy(paddingCustom)
		) {
			if (idVerticesInfo.value != null) {
				Text(
					"Vertex: ${idVerticesInfo.value!!.id.valueToString()}",
					color = JetTheme.colors.secondaryText,
					style = JetTheme.typography.mini,
					maxLines = 1
				)
				Text(
					"Count edges: ${graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id).size}",
					color = JetTheme.colors.secondaryText,
					style = JetTheme.typography.mini,
					maxLines = 1,
				)
				ButtonCustom(removingVertexSource, "Delete", Modifier)
			}
		}
		ColumnBoxEditingItem(
			setEdges,
			removingVertexTarget,
			"Doesn't have adjacent vertices",
			stringVertex,
			Modifier.weight(2f),
		)
		ColumnBoxEditingItem(
			setEdges,
			removingEdge,
			"Doesn't have edges",
			stringEdge,
			Modifier.weight(3f)
		)
	}
}

/**
 * This is a composable function for drawing menu in MenuAddingItem that adds a vertex in the [GraphViewModel].
 *
 * @param graphViewModel the graph view model whose info will be represented
 * @param isShownId the boolean variable that shows ids of the graph if true
 */
@Composable
fun MenuAddingVertex(graphViewModel: GraphViewModel, isShownId: Boolean) {
	val idVertex = remember { mutableStateOf("") }
	var statusIdVertex by remember { mutableStateOf(false) }

	val creationVertex = {
		var id: VertexID? = null

		if (idVertex.value != "") {
			if (graphViewModel.vertexType == VertexIDType.STRING_TYPE) {
				id = VertexID(idVertex.value, graphViewModel.vertexType)
				statusIdVertex = false
			} else {
				if (idVertex.value.all { it.isDigit() }) {
					id = VertexID(idVertex.value, graphViewModel.vertexType)
					statusIdVertex = false
				} else statusIdVertex = true
			}
		} else statusIdVertex = true

		if (id != null) {
			graphViewModel.addVertex(id)
			graphViewModel.vertices.find { it.id == id }!!.visibility = isShownId
		}
	}

	Box(Modifier.fillMaxSize()) {
		Column(Modifier.align(Alignment.TopStart)) {
			Row(
				modifier = Modifier.padding(paddingCustom),
				horizontalArrangement = Arrangement.spacedBy(paddingCustom)
			) {
				TextFieldItem(idVertex, statusIdVertex, "Vertex", Modifier.weight(1f))
				Spacer(Modifier.weight(1f))
			}
			ButtonCustom(creationVertex, "Create", Modifier.padding(paddingCustom))
		}
		Icon(
			imageVector = Icons.Default.ArrowDownward,
			contentDescription = "Show the menu edge",
			modifier = Modifier
				.padding(paddingCustom)
				.align(Alignment.BottomEnd)
		)
	}
}

/**
 * This is a composable function for drawing menu in MenuAddingItem that adds an edge in the [GraphViewModel].
 *
 * @param graphViewModel the graph view model whose info will be represented
 * @param isShownId the boolean variable that shows ids of the graph if true
 * @param isShownWeigh the boolean variable that shows weighs of the graph if true
 */
@Composable
fun MenuAddingEdge(graphViewModel: GraphViewModel, isShownId: Boolean, isShownWeigh: Boolean) {
	val idVertexSource = remember { mutableStateOf("") }
	val idVertexTarget = remember { mutableStateOf("") }
	val weight = remember { mutableStateOf("") }
	var statusIdVertexSource by remember { mutableStateOf(false) }
	var statusIdVertexTarget by remember { mutableStateOf(false) }
	var statusWeight by remember { mutableStateOf(false) }

	val creationEdge = {
		var idSource: VertexID? = null
		var idTarget: VertexID? = null

		if (idVertexSource.value != "") {
			if (graphViewModel.vertexType == VertexIDType.STRING_TYPE) {
				idSource = VertexID(idVertexSource.value, graphViewModel.vertexType)
				statusIdVertexSource = false
			} else {
				if (idVertexSource.value.all { it.isDigit() }) {
					idSource = VertexID(idVertexSource.value, graphViewModel.vertexType)
					statusIdVertexSource = false
				} else statusIdVertexSource = true
			}
		} else statusIdVertexSource = true

		if (idVertexTarget.value != "" && idVertexTarget.value != idVertexSource.value) {
			if (graphViewModel.vertexType == VertexIDType.STRING_TYPE) {
				idTarget = VertexID(idVertexTarget.value, graphViewModel.vertexType)
				statusIdVertexTarget = false
			} else {
				if (idVertexTarget.value.all { it.isDigit() }) {
					idTarget = VertexID(idVertexTarget.value, graphViewModel.vertexType)
					statusIdVertexTarget = false
				} else statusIdVertexTarget = true
			}
		} else statusIdVertexTarget = true

		if (graphViewModel.isUnweighted) {
			if (idSource != null && idTarget != null) {
				graphViewModel.addEdge(idSource, idTarget, 1.0)

				graphViewModel.vertices.find { it.id == idSource }!!.visibility = isShownId
				graphViewModel.vertices.find { it.id == idTarget }!!.visibility = isShownId
				graphViewModel.edges.find {
					it.edge == WeightedEdge(idSource, idTarget, 1.0)
				}!!.visibility = isShownWeigh
			}
		} else {
			if (weight.value.toDoubleOrNull() != null) {
				if (idSource != null && idTarget != null) {
					graphViewModel.addEdge(idSource, idTarget, weight.value.toDouble())

					graphViewModel.vertices.find { it.id == idSource }!!.visibility = isShownId
					graphViewModel.vertices.find { it.id == idTarget }!!.visibility = isShownId
					graphViewModel.edges.find {
						it.edge == WeightedEdge(idSource, idTarget, weight.value.toDouble())
					}!!.visibility = isShownWeigh
					statusWeight = false
				}
			} else statusWeight = true
		}
	}

	Column {
		Row(
			modifier = Modifier.padding(paddingCustom),
			horizontalArrangement = Arrangement.spacedBy(paddingCustom)
		) {
			TextFieldItem(idVertexSource, statusIdVertexSource, "Source vertex", Modifier.weight(1f))
			TextFieldItem(idVertexTarget, statusIdVertexTarget, "Target vertex", Modifier.weight(1f))
		}

		Row(Modifier.padding(paddingCustom)) {
			Box(Modifier.weight(1f)) {
				Spacer(Modifier.fillMaxWidth())
				ButtonCustom(creationEdge, "Create", Modifier.align(Alignment.CenterStart))
			}
			if (!graphViewModel.isUnweighted) TextFieldItem(weight, statusWeight, "Weight", Modifier.weight(1f))
		}
	}
}

/**
 * This is a composable function for drawing box that displays count of items and adds an item in the [GraphViewModel].
 *
 * @param text "Vertex" or "Edge"
 * @param count count of vertices/edges in the graph
 * @param action adding vertex/edge in the graph
 */
@Composable
fun BoxFastAddingItem(text: String, count: Int, action: () -> Unit) {
	Box(
		Modifier
			.fillMaxWidth()
			.clip(JetTheme.shapes.cornerStyle)
			.background(whiteCustom, JetTheme.shapes.cornerStyle)
	) {
		Text(
			text,
			style = JetTheme.typography.mini,
			modifier = Modifier
				.padding(15.dp)
				.align(Alignment.CenterStart)
		)
		Row(Modifier.align(Alignment.TopEnd)) {
			Text("$count", style = JetTheme.typography.mini, modifier = Modifier.padding(15.dp))
			IconButton(
				onClick = action,
				enabled = true,
				modifier = Modifier.width(sizeBottom),
				content = {
					Icon(
						imageVector = Icons.Default.Add,
						contentDescription = "Add item",
					)
				}
			)
		}
	}
}

/**
 * This is a composable function for drawing column in MenuEditingItem that displays info about the chosen vertex
 * and edits an item in the [GraphViewModel].
 *
 * @param setEdges the edges set of the chosen vertex
 * @param action removing vertex/edge in the graph
 * @param message label linked vertices in the graph
 * @param string label edges of the chosen vertex in the graph
 * @param modifierRow the row modifier that consists of ColumnBoxEditingItem
 */
@Composable
fun ColumnBoxEditingItem(
	setEdges: Set<WeightedEdge<VertexID>>,
	action: (WeightedEdge<VertexID>) -> Unit,
	message: String,
	string: (WeightedEdge<VertexID>) -> String,
	modifierRow: Modifier
) {
	Column(
		modifierRow
			.heightIn(100.dp, 180.dp)
			.clip(JetTheme.shapes.cornerStyle)
			.background(whiteCustom, JetTheme.shapes.cornerStyle)
			.verticalScroll(rememberScrollState())
	) {
		if (setEdges.isEmpty()) {
			Box(
				Modifier
					.fillMaxWidth()
					.height(100.dp)
			) {
				Text(message, Modifier.align(Alignment.Center))
			}
		}
		setEdges.forEach {
			BoxEditingItem(it, action, string)
		}
	}
}

/**
 * This is a composable function for drawing box in column in MenuEditingItem that edits an item in the [GraphViewModel].
 *
 * @param edge an edge in the graph
 * @param action removing vertex/edge in the graph
 * @param string label edges of the chosen vertex in the graph
 */
@Composable
fun BoxEditingItem(
	edge: WeightedEdge<VertexID>,
	action: (WeightedEdge<VertexID>) -> Unit,
	string: (WeightedEdge<VertexID>) -> String
) {
	Box(Modifier.fillMaxWidth()) {
		Text(
			text = string(edge),
			style = JetTheme.typography.mini,
			modifier = Modifier
				.padding(15.dp)
				.align(Alignment.CenterStart)
		)
		IconButton(
			onClick = { action(edge) },
			enabled = true,
			modifier = Modifier
				.width(sizeBottom)
				.align(Alignment.CenterEnd),
			content = {
				Icon(
					imageVector = Icons.Default.Delete,
					contentDescription = "Remove item"
				)
			}
		)
	}
}

/**
 * This is a composable function for drawing custom button in [GraphViewPage].
 *
 * @param action action on items in graph
 * @param message a button message
 * @param modifier the button modifier
 */
@Composable
fun ButtonCustom(action: () -> Unit, message: String, modifier: Modifier) {
	Button(
		onClick = action,
		modifier = modifier,
		shape = JetTheme.shapes.cornerStyle,
		colors = ButtonDefaults.buttonColors(JetTheme.colors.primaryText)
	) {
		Text(message)
	}
}

/**
 * This is a composable function for drawing custom text field in [GraphViewPage].
 *
 * @param string a text field message
 * @param status the boolean variable that shows an error notification if true
 * @param label a text field label
 * @param modifier the text field modifier
 */
@Composable
fun TextFieldItem(string: MutableState<String>, status: Boolean, label: String, modifier: Modifier) {
	OutlinedTextField(
		value = string.value,
		onValueChange = { string.value = it },
		label = { Text(label) },
		modifier = modifier,
		singleLine = true,
		textStyle = JetTheme.typography.mini,
		isError = status,
		colors = TextFieldDefaults.textFieldColors(
			focusedIndicatorColor = Color.Black,
			focusedLabelColor = Color.Black,
			cursorColor = JetTheme.colors.tintColor,
			errorIndicatorColor = Color(176, 0, 0) // Red
		)
	)
}

/**
 * This is a function for getting a nearby id that is not contained in the [GraphViewModel].
 *
 * @param graphViewModel the graph view model in which the search is going on
 * @param idFastAdd a last id that is contained in the [graphViewModel]
 *
 * @return an id that is not contained in the [graphViewModel]
 */
fun checkAndGet(graphViewModel: GraphViewModel, idFastAdd: MutableIntState): Int {
	while (graphViewModel.graph.idVertices.contains(VertexID(idFastAdd.intValue, graphViewModel.vertexType))) {
		idFastAdd.intValue++
	}
	return idFastAdd.intValue
}
