package views.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ResetTv
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import graphs_lab.core.edges.WeightedEdge
import models.VertexID
import mu.KotlinLogging
import themes.JetTheme
import themes.colorEdgesStart
import themes.colorVerticesStart
import themes.paddingCustom
import themes.radiusVerticesStart
import themes.sizeBottom
import themes.whiteCustom
import themes.WidthEdgesStart
import utils.VertexIDType
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import viewmodels.pages.GraphPageViewModel
import views.graphs.GraphView
import views.graphs.colorChangeFlag

private val logger = KotlinLogging.logger("GraphViewPage")

/**
 * This is a composable function for representing [GraphViewModel].
 *
 * @param graphPageViewModel the view model for the GraphViewPage. It provides data and functionality for the composable
 */
@Composable
fun GraphViewPage(graphPageViewModel: GraphPageViewModel) {
	val graphViewModel = graphPageViewModel.graphViewModel

	val changeCenter = remember { mutableStateOf(false) }

	val isOpenedMenuGraph = remember { mutableStateOf(true) }
	val isOpenedMenuVertex = remember { mutableStateOf(false) }
	val idVerticesInfo = remember { mutableStateOf<VertexViewModel?>(null) }
	LaunchedEffect(idVerticesInfo.value) {
		if (idVerticesInfo.value == null) {
			isOpenedMenuVertex.value = false
		} else {
			isOpenedMenuGraph.value = false
			isOpenedMenuVertex.value = true
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
				.testTag("graph-view-page")
		) {
			GraphView(
				graphViewModel,
				idVerticesInfo,
				Offset(maxWidth.value, maxHeight.value),
				changeCenter,
				graphPageViewModel.settings
			)
			ButtonResetGraphDisplay(
				graphViewModel,
				changeCenter,
				Modifier.padding(paddingCustom).align(Alignment.TopEnd),
				modifierButtons,
			)
			Box(Modifier.fillMaxSize()) {
				MenuInteraction(
					graphViewModel,
					idVerticesInfo,
					isOpenedMenuGraph,
					isOpenedMenuVertex,
					Modifier.align(Alignment.BottomEnd),
					modifierButtons
				)
			}
		}
	}
}

/**
 * This is a composable function for interaction with [GraphViewModel] items.
 *
 * @param graphViewModel the graph view model whose info is represented
 * @param idVerticesInfo the vertex id of [graphViewModel]
 * @param isOpenedMenuGraph true if [MenuGraph] is opened
 * @param isOpenedMenuVertex true if [MenuVertex] is opened
 * @param modifierParent the parent, that contains a button, modifier
 * @param modifierButton the button modifier
 */
@Composable
fun MenuInteraction(
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	isOpenedMenuGraph: MutableState<Boolean>,
	isOpenedMenuVertex: MutableState<Boolean>,
	modifierParent: Modifier,
	modifierButton: Modifier
) {
	val isShownWeigh = remember { mutableStateOf(false) }
	LaunchedEffect(isShownWeigh.value) {
		logger.info { "Change visibility of vertex ID to ${if (isShownWeigh.value) "visible" else "hidden"}" }
		graphViewModel.edges.forEach {
			it.visibility = isShownWeigh.value
		}
	}
	val isShownId = remember { mutableStateOf(true) }
	LaunchedEffect(isShownId.value) {
		logger.info { "Change visibility of vertex ID to ${if (isShownId.value) "visible" else "hidden"}" }
		graphViewModel.vertices.forEach {
			it.visibility = isShownId.value
		}
	}

	Column(
		modifier = modifierParent.padding(paddingCustom).clip(JetTheme.shapes.cornerStyle),
		verticalArrangement = Arrangement.spacedBy(paddingCustom)
	) {
		ButtonOpeningMenuGraph(isOpenedMenuGraph, modifierButton.align(Alignment.End))

		val modifierAnimation = Modifier
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)
		AnimatedVisibility(
			visible = isOpenedMenuGraph.value,
			modifier = modifierAnimation
		) {
			MenuGraph(graphViewModel, isShownId, isShownWeigh)
		}
		AnimatedVisibility(
			visible = isOpenedMenuVertex.value,
			modifier = modifierAnimation
		) {
			MenuVertex(graphViewModel, idVerticesInfo)
		}
	}
}

/**
 * This is a composable function for displaying info about [GraphViewModel] and adding [GraphViewModel] items.
 *
 * @param graphViewModel the graph view model whose info is represented
 * @param isShownId true if ids is shown
 * @param isShownWeigh true if weighs is shown
 */
@Composable
fun MenuGraph(graphViewModel: GraphViewModel, isShownId: MutableState<Boolean>, isShownWeigh: MutableState<Boolean>) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(paddingCustom)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle),
		horizontalArrangement = Arrangement.spacedBy(paddingCustom)
	) {
		MenuGraphInfo(graphViewModel, isShownId, isShownWeigh, Modifier.weight(1f).height(145.dp))
		MenuAddingItemGraph(graphViewModel, isShownId, isShownWeigh, Modifier.weight(1f).height(145.dp))
	}
}

/**
 * This is a composable function for displaying info about [GraphViewModel] and fast adding [GraphViewModel] items.
 *
 * @param graphViewModel the graph view model whose info is represented
 * @param isShownId true if ids is shown
 * @param isShownWeigh true if weighs is shown
 * @param modifierParent the parent, that contains a graph info, modifier
 */
@Composable
fun MenuGraphInfo(
	graphViewModel: GraphViewModel,
	isShownId: MutableState<Boolean>,
	isShownWeigh: MutableState<Boolean>,
	modifierParent: Modifier
) {
	val idFastAdding = remember { mutableIntStateOf(graphViewModel.vertices.size + 1) }
	val addingVertex = {
		val idNew = VertexID(checkAndGet(graphViewModel, idFastAdding), graphViewModel.vertexType)
		graphViewModel.addVertex(idNew)

		graphViewModel.vertices.find { it.id == idNew }!!.visibility = isShownId.value
	}
	val addingEdge = {
		val idSourceNew = VertexID(checkAndGet(graphViewModel, idFastAdding), graphViewModel.vertexType)
		graphViewModel.addVertex(idSourceNew)
		val idTargetNew = VertexID(checkAndGet(graphViewModel, idFastAdding), graphViewModel.vertexType)
		graphViewModel.addVertex(idTargetNew)
		graphViewModel.addEdge(idSourceNew, idTargetNew, 1.0)

		graphViewModel.vertices.find { it.id == idSourceNew }!!.visibility = isShownId.value
		graphViewModel.vertices.find { it.id == idTargetNew }!!.visibility = isShownId.value
		graphViewModel.edges.find {
			it.edge == WeightedEdge(idSourceNew, idTargetNew, 1.0)
		}!!.visibility = isShownWeigh.value
	}

	Column(
		modifier = modifierParent,
		verticalArrangement = Arrangement.spacedBy(paddingCustom)
	) {
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
				val fontSizeText = 14.sp

				ListGraphProperty(graphViewModel, fontSizeText)

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
				BoxGraphInfoAndFastAddingItem("Vertices:", graphViewModel.vertices.size, addingVertex)
				BoxGraphInfoAndFastAddingItem("Edges:", graphViewModel.countEdges, addingEdge)
			}
		}
	}
}

/**
 * This is a composable function for displaying [GraphViewModel] properties.
 *
 * @param graphViewModel the graph view model whose properties is represented
 * @param fontSizeText the text size
 */
@Composable
fun ListGraphProperty(graphViewModel: GraphViewModel, fontSizeText: TextUnit) {
	val fontWeightText = FontWeight.W200

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
}

/**
 * This is a composable function for displaying count of items and adding [GraphViewModel] items with idFastAdding.
 *
 * @param text "Vertices:" or "Edges:"
 * @param count count of vertices/edges in the graph view model
 * @param action adding vertex/edge in the graph view model
 */
@Composable
fun BoxGraphInfoAndFastAddingItem(text: String, count: Int, action: () -> Unit) {
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
 * This is a composable function for adding a [GraphViewModel] item.
 *
 * @param graphViewModel the graph view model whose is represented
 * @param isShownId true if ids is shown
 * @param isShownWeigh true if weighs is shown
 * @param modifierParent the parent, that contains MenuAddingItemGraph, modifier
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuAddingItemGraph(
	graphViewModel: GraphViewModel,
	isShownId: MutableState<Boolean>,
	isShownWeigh: MutableState<Boolean>,
	modifierParent: Modifier
) {
	val statePager = rememberPagerState { 2 }

	Column(modifierParent) {
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

/**
 * This is a composable function for adding a [GraphViewModel] vertex.
 *
 * @param graphViewModel the graph view model whose vertices is represented
 * @param isShownId true if ids is shown
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
				} else {
					statusIdVertex = true
				}
			}
		} else {
			statusIdVertex = true
		}

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
			ButtonAction(creationVertex, "Create", Modifier.padding(paddingCustom))
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
 * This is a composable function for adding a [GraphViewModel] edge.
 *
 * @param graphViewModel the graph view model whose edges is represented
 * @param isShownId true if ids is shown
 * @param isShownWeigh true if weighs is shown
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
				} else {
					statusIdVertexSource = true
				}
			}
		} else {
			statusIdVertexSource = true
		}

		if (idVertexTarget.value != "" && idVertexTarget.value != idVertexSource.value) {
			if (graphViewModel.vertexType == VertexIDType.STRING_TYPE) {
				idTarget = VertexID(idVertexTarget.value, graphViewModel.vertexType)
				statusIdVertexTarget = false
			} else {
				if (idVertexTarget.value.all { it.isDigit() }) {
					idTarget = VertexID(idVertexTarget.value, graphViewModel.vertexType)
					statusIdVertexTarget = false
				} else {
					statusIdVertexTarget = true
				}
			}
		} else {
			statusIdVertexTarget = true
		}

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
			} else {
				statusWeight = true
			}
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
				ButtonAction(creationEdge, "Create", Modifier.align(Alignment.CenterStart))
			}
			if (!graphViewModel.isUnweighted) TextFieldItem(weight, statusWeight, "Weight", Modifier.weight(1f))
		}
	}
}

/**
 * This is a composable function for displaying and editing [GraphViewModel] items.
 *
 * @param graphViewModel the graph view model whose items is represented
 * @param idVerticesInfo the chosen vertex view model id
 */
@Composable
fun MenuVertex(graphViewModel: GraphViewModel, idVerticesInfo: MutableState<VertexViewModel?>) {
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
				ButtonAction(removingVertexSource, "Delete", Modifier)
			}
		}
		ListEditingItemGraph(
			setEdges,
			removingVertexTarget,
			"Doesn't have adjacent vertices",
			stringVertex,
			Modifier.weight(2f),
		)
		ListEditingItemGraph(
			setEdges,
			removingEdge,
			"Doesn't have edges",
			stringEdge,
			Modifier.weight(3f)
		)
	}
}

/**
 * This is a composable function for drawing boxes info about the chosen vertex and editing [GraphViewModel] items.
 *
 * @param setEdges the edges set of the chosen vertex view model id
 * @param action removing vertex/edge in the graph view model
 * @param message message if the chosen vertex don't have chosen vertex in the graph view model
 * @param string label linked vertices or edges of the chosen vertex in the graph view model
 * @param modifierParent the parent, that consists ListEditingItemGraph, modifier
 */
@Composable
fun ListEditingItemGraph(
	setEdges: Set<WeightedEdge<VertexID>>,
	action: (WeightedEdge<VertexID>) -> Unit,
	message: String,
	string: (WeightedEdge<VertexID>) -> String,
	modifierParent: Modifier
) {
	Column(
		modifierParent
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
			BoxEditingItemGraph(it, action, string)
		}
	}
}

/**
 * This is a composable function for displaying info about the chosen vertex and editing [GraphViewModel] items.
 *
 * @param edge an edge view model in the graph view model
 * @param action removing vertex/edge in the graph view model
 * @param string label linked vertices or edges of the chosen vertex in the graph view model
 */
@Composable
fun BoxEditingItemGraph(
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
 * This is a composable function for drawing a text field [GraphViewPage] item.
 *
 * @param string a text field message
 * @param status true if incorrect input
 * @param label a text field label
 * @param modifierField the text field modifier
 */
@Composable
fun TextFieldItem(string: MutableState<String>, status: Boolean, label: String, modifierField: Modifier) {
	OutlinedTextField(
		value = string.value,
		onValueChange = { string.value = it },
		label = { Text(label) },
		modifier = modifierField,
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
 * This is a composable function for drawing the button that resets the [GraphViewModel] display.
 *
 * @param graphViewModel the graph view model whose is represented
 * @param changeCenter sets [graphViewModel] display in center of the monitor if true
 * @param modifierParent the parent, that contains the reset button, modifier
 * @param modifierButton the button modifier
 */
@Composable
fun ButtonResetGraphDisplay(
	graphViewModel: GraphViewModel,
	changeCenter: MutableState<Boolean>,
	modifierParent: Modifier,
	modifierButton: Modifier
) {
	Column(modifierParent) {
		IconButton(
			onClick = {
				logger.info { "Clear algorithms results" }
				colorChangeFlag.value = true

				graphViewModel.vertices.forEach {
					it.radius = radiusVerticesStart
					it.color = colorVerticesStart
					it.visibility = true
				}
				graphViewModel.edges.forEach {
					it.width = WidthEdgesStart
					it.color = colorEdgesStart
					it.visibility = false
				}
				changeCenter.value = true
			},
			modifier = modifierButton.pointerHoverIcon(PointerIcon.Hand),
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
 * This is a composable function for drawing the button that opens/hides [MenuGraph].
 *
 * @param isOpenedMenuGraph true if [MenuGraph] is opened
 * @param modifierButton the button modifier
 */
@Composable
fun ButtonOpeningMenuGraph(isOpenedMenuGraph: MutableState<Boolean>, modifierButton: Modifier) {
	IconButton(
		onClick = {
			logger.info { "Update graph menu visibility to ${if (!isOpenedMenuGraph.value) "visible" else "hidden"}" }
			isOpenedMenuGraph.value = !isOpenedMenuGraph.value
		},
		modifier = modifierButton.pointerHoverIcon(PointerIcon.Hand),
		content = {
			Icon(
				imageVector = if (isOpenedMenuGraph.value) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
				contentDescription = "Show graph menu",
				tint = JetTheme.colors.tintColor
			)
		}
	)
}

/**
 * This is a composable function for drawing the button that do action.
 *
 * @param action action on a [GraphViewModel] item
 * @param message "Delete" or "Create"
 * @param modifierButton the button modifier
 */
@Composable
fun ButtonAction(action: () -> Unit, message: String, modifierButton: Modifier) {
	Button(
		onClick = action,
		modifier = modifierButton,
		shape = JetTheme.shapes.cornerStyle,
		colors = ButtonDefaults.buttonColors(JetTheme.colors.primaryText)
	) {
		Text(message)
	}
}

/**
 * This is a function for getting a nearby id that is not contained in the [GraphViewModel].
 *
 * @param graphViewModel the graph view model in which the search is going on
 * @param idFastAdding a last id that is contained in the [graphViewModel]
 *
 * @return an id that is not contained in the [graphViewModel]
 */
fun checkAndGet(graphViewModel: GraphViewModel, idFastAdding: MutableIntState): Int {
	while (graphViewModel.graph.idVertices.contains(VertexID(idFastAdding.intValue, graphViewModel.vertexType))) {
		idFastAdding.intValue++
	}
	return idFastAdding.intValue
}
