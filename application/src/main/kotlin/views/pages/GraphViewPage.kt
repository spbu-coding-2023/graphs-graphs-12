package views.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
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
import views.graphs.GraphViewTest

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
		BoxWithConstraints(Modifier
			.fillMaxSize()
			.padding(paddingCustom)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.secondaryBackground, JetTheme.shapes.cornerStyle)
		) {
//			GraphView(graphViewModel, idVerticesInfo, centerBox = Offset(maxWidth.value, maxHeight.value), changeCenter)
			GraphViewTest(graphViewModel, idVerticesInfo, centerBox = Offset(maxWidth.value, maxHeight.value), changeCenter)

			IconResetGraph(graphViewModel, changeCenter, modifierButtons, Modifier.padding(paddingCustom).align(Alignment.TopEnd))
			Box(Modifier.fillMaxSize()) {
				ColumnInfoGraph(graphViewModel, idVerticesInfo, isOpenedContextGraph, isOpenedContextEdge,
					Modifier.align(Alignment.BottomEnd), modifierButtons)
			}
		}
	}
}

@Composable
fun IconResetGraph(graphViewModel: GraphViewModel, changeCenter: MutableState<Boolean>, modifierButtons: Modifier, modifierBox: Modifier) {
	Column(modifierBox){
		IconButton(
			onClick = {
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
					contentDescription = "Reset the graph"
				)
			}
		)
	}
}

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
		modifier = modifierBox.padding(paddingCustom),
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
					contentDescription = "Show menu"
				)
			}
		)

		AnimatedVisibility(
			visible = isOpenedContextGraph.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)
		) {
			showAddItem(graphViewModel, isShownWeigh, isShownId)
		}

		AnimatedVisibility(
			visible = isOpenedContextEdge.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)
		) {
			showEditItem(graphViewModel, idVerticesInfo)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun showAddItem(graphViewModel: GraphViewModel, isShownWeigh: MutableState<Boolean>, isShownId: MutableState<Boolean>) {
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
		graphViewModel.edges.find { it.edge == WeightedEdge(idSourceNew, idTargetNew, 1.0) }!!.visibility = isShownWeigh.value
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
			modifier = Modifier.weight(1f),
			verticalArrangement = Arrangement.spacedBy(paddingCustom)
		) {
			val fontSizeText = 14.sp
			val fontWeightText = FontWeight.W200

			Text(
				"Graph name: ${graphViewModel.graph.label}",
				fontSize = 20.sp,
				maxLines = 1
			)
			Row(horizontalArrangement = Arrangement.spacedBy(paddingCustom)) {
				Column(
					modifier = Modifier.weight(1f),
					verticalArrangement = Arrangement.spacedBy(paddingCustom)
				) {
					if (graphViewModel.graph.isDirected) {
						Text("Directed", fontSize = fontSizeText, fontWeight = fontWeightText)
					} else {
						Text("Undirected", fontSize = fontSizeText, fontWeight = fontWeightText)
					}
					if (!graphViewModel.isUnweighted) {
						Text("Weighted", fontSize = fontSizeText, fontWeight = fontWeightText)
					} else {
						Text("Unweighted", fontSize = fontSizeText, fontWeight = fontWeightText)
					}
					if (graphViewModel.vertexType == VertexIDType.INT_TYPE) {
						Text("Int type", fontSize = fontSizeText, fontWeight = fontWeightText)
					} else {
						Text("String type", fontSize = fontSizeText, fontWeight = fontWeightText)
					}

					val modifierCheckbox = Modifier
						.size(20.dp)
						.padding(paddingCustom)
					val colorCheckbox = CheckboxDefaults.colors(Color.Black)

					Spacer(Modifier.size(paddingCustom))

					if (!graphViewModel.isUnweighted) {
						Box(Modifier.fillMaxWidth()) {
							Text("Show weight", fontSize = fontSizeText)
							Checkbox(
								checked = isShownWeigh.value,
								onCheckedChange = { isShownWeigh.value = !isShownWeigh.value },
								modifier = modifierCheckbox.align(Alignment.CenterEnd),
								colors = colorCheckbox
							)
						}
					}

					Box(Modifier.fillMaxWidth()) {
						Text("Show id", fontSize = fontSizeText)
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
					BoxAddItem("Vertices:", graphViewModel.vertices.size, addingVertex)
					BoxAddItem("Edges:", graphViewModel.countEdges, addingEdge)
				}
			}
		}

		Column(Modifier
			.weight(1f)
			.height(145.dp)
		) { // todo(size!!!)
			HorizontalPager( // todo(how to fix double show pagers?)
				modifier = Modifier
					.fillMaxSize()
					.clip(JetTheme.shapes.cornerStyle)
					.background(whiteCustom, JetTheme.shapes.cornerStyle),
				state = statePager,
				userScrollEnabled = true
			) { index ->
				when (index) {
					0 -> showMenuVertex(graphViewModel, isShownId.value)
					1 -> showMenuEdge(graphViewModel, isShownId.value, isShownWeigh.value)
				}
			}
		}
	}
}

@Composable
fun showEditItem(graphViewModel: GraphViewModel, idVerticesInfo: MutableState<VertexViewModel?>) {
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
			modifier = Modifier.weight(1f),
			verticalArrangement = Arrangement.spacedBy(paddingCustom)
		) {
			if (idVerticesInfo.value != null) {
				Text("Vertex: ${idVerticesInfo.value!!.id.valueToString()}", maxLines = 1) // todo(fontSize and clip!!!)
				Text("Count edges: ${graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id).size}", maxLines = 1) // todo(fontSize)
				ButtonCustom(removingVertexSource, "Delete", Modifier)
			}
		}
		ColumnBoxEditItem(
			setEdges,
			removingVertexTarget,
			"Doesn't have adjacent vertices",
			stringVertex,
			Modifier.weight(2f),
		)
		ColumnBoxEditItem(
			setEdges,
			removingEdge,
			"Doesn't have edges",
			stringEdge,
			Modifier.weight(3f)
		)
	}
}

@Composable
fun showMenuVertex(graphViewModel: GraphViewModel, isShownId: Boolean) {
	val idVertex = remember { mutableStateOf("") }

	val creationVertex = {
		if (idVertex.value != "") {
			if (graphViewModel.vertexType == VertexIDType.INT_TYPE && idVertex.value.all { it.isDigit() }) {
				val id = VertexID(idVertex.value, graphViewModel.vertexType)
				graphViewModel.addVertex(id)

				graphViewModel.vertices.find { it.id == id }!!.visibility = isShownId
			}
		}
	}

	Box(Modifier.fillMaxSize()) {
		Column(Modifier.align(Alignment.TopStart)) {
			Row(
				modifier = Modifier.padding(paddingCustom),
				horizontalArrangement = Arrangement.spacedBy(paddingCustom)
			) {
				TextFieldItem(idVertex, "Vertex", Modifier.weight(1f))
				Spacer(Modifier.weight(1f))
			}
			ButtonCustom(creationVertex, "Create", Modifier.padding(paddingCustom))
		}
		Icon(
			imageVector = Icons.Default.ArrowRight,
			contentDescription = "Show the menu edge",
			modifier = Modifier.align(Alignment.BottomEnd)
		)
	}
}

@Composable
fun showMenuEdge(graphViewModel: GraphViewModel, isShownId: Boolean, isShownWeigh: Boolean) {
	val idVertexSource = remember { mutableStateOf("") }
	val idVertexTarget = remember { mutableStateOf("") }
	val weight = remember { mutableStateOf("") }

	val creationEdge = {
		if (idVertexSource.value != "" && idVertexTarget.value != "" && idVertexSource.value != idVertexTarget.value
			&& weight.value.toDoubleOrNull() != null) {
			if (graphViewModel.vertexType == VertexIDType.INT_TYPE && idVertexSource.value.all { it.isDigit() }
				&& idVertexTarget.value.all { it.isDigit() }) {
				val idSource = VertexID(idVertexSource.value, graphViewModel.vertexType)
				val idTarget = VertexID(idVertexTarget.value, graphViewModel.vertexType)
				graphViewModel.addEdge(idSource, idTarget, weight.value.toDouble())

				graphViewModel.vertices.find { it.id == idSource }!!.visibility = isShownId
				graphViewModel.vertices.find { it.id == idTarget }!!.visibility = isShownId
				graphViewModel.edges.find { it.edge == WeightedEdge(idSource, idTarget, 1.0) }!!.visibility = isShownWeigh
			}
		}
	}

	Column {
		Row(
			modifier = Modifier.padding(paddingCustom),
			horizontalArrangement = Arrangement.spacedBy(paddingCustom)
		) {
			TextFieldItem(idVertexSource, "Source vertex", Modifier.weight(1f))
			TextFieldItem(idVertexTarget, "Target vertex", Modifier.weight(1f))
		}

		Row(Modifier.padding(paddingCustom)) {
			Box(Modifier.weight(1f)) {
				Spacer(Modifier.fillMaxWidth())
				ButtonCustom(creationEdge, "Create", Modifier.align(Alignment.CenterStart))
			}
			TextFieldItem(weight, "Weight", Modifier.weight(1f))
		}
	}
}

@Composable
fun BoxAddItem(text: String, count: Int, action: () -> Unit) {
	Box(Modifier
		.fillMaxWidth()
		.clip(JetTheme.shapes.cornerStyle)
		.background(whiteCustom, JetTheme.shapes.cornerStyle)
	) {
		Text(text, fontSize = 16.sp, modifier = Modifier.padding(15.dp).align(Alignment.CenterStart))
		Row(Modifier
			.padding(paddingCustom)
			.align(Alignment.TopEnd)
		) {
			Text("$count", fontSize = 16.sp, modifier = Modifier.padding(15.dp))
			IconButton(
				onClick = action,
				enabled = true,
				modifier = Modifier.width(50.dp),
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

@Composable
fun ColumnBoxEditItem(
	setEdges: Set<WeightedEdge<VertexID>>,
	action: (WeightedEdge<VertexID>) -> Unit,
	message: String,
	string: (WeightedEdge<VertexID>) -> String,
	modifierRow: Modifier
) {
	Column(modifierRow
		.heightIn(100.dp, 180.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(whiteCustom, JetTheme.shapes.cornerStyle)
		.verticalScroll(rememberScrollState())
	) {
		if (setEdges.isEmpty()) {
			Box(Modifier
				.fillMaxWidth()
				.height(100.dp)
			) {
				Text(message, Modifier.align(Alignment.Center))
			}
		}
		setEdges.forEach {
			BoxEditItem(it, action, string)
		}
	}
}

@Composable
fun BoxEditItem(
	edge: WeightedEdge<VertexID>,
	action: (WeightedEdge<VertexID>) -> Unit,
	string: (WeightedEdge<VertexID>) -> String
) {
	Box(Modifier.fillMaxWidth()) {
		Text(
			text = string(edge),
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

@Composable
fun ButtonCustom(action: () -> Unit, message: String, modifier: Modifier) {
	Button(
		onClick = action,
		modifier = modifier,
		colors = ButtonDefaults.buttonColors(Color.White)
	) {
		Text(message)
	}
}

@Composable
fun TextFieldItem(string: MutableState<String>, label: String, modifier: Modifier) {
	OutlinedTextField(
		value = string.value,
		onValueChange = { string.value = it },
		label = { Text(label) },
		modifier = modifier
	)
}

fun checkAndGet(graphViewModel: GraphViewModel, idFastAdd: MutableIntState): Int {
	while (graphViewModel.graph.idVertices.contains(VertexID(idFastAdd.intValue, graphViewModel.vertexType))) {
		idFastAdd.intValue++
	}
	return idFastAdd.intValue
}
