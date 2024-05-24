package views.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.VertexID
import themes.JetTheme
import utils.VertexIDType
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import viewmodels.pages.GraphPageViewModel
import views.*
import views.graphs.GraphView

val listZoom = listOf(
	0.1f,
	0.5f,
	1f,
	1.5f,
	2f,
)

@Composable
fun GraphViewPage(graphPageViewModel: GraphPageViewModel) {
	val graphViewModel = graphPageViewModel.graphViewModel

	val abilityZoom = remember { mutableStateOf(false) }

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

	val indexListZoom = remember { mutableIntStateOf(2) }
	val modifierButtons = Modifier
		.size(sizeBottom)
		.padding(4.dp)
		.clip(RoundedCornerShape(25.dp))
		.background(JetTheme.colors.primaryBackground, RoundedCornerShape(25.dp))

	if (graphViewModel != null) {
		Surface(Modifier
			.padding(4.dp)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.secondaryBackground, JetTheme.shapes.cornerStyle)
		) {
			GraphView(graphViewModel = graphViewModel, abilityZoom.value, indexListZoom.intValue, idVerticesInfo)
			Box(Modifier.fillMaxSize()) {
				if (abilityZoom.value) ColumnZoomButtons(indexListZoom, Modifier.align(Alignment.TopEnd), modifierButtons)
				ColumnChangeAbility(abilityZoom, Modifier.align(Alignment.CenterEnd), modifierButtons)
				ColumnInfoGraph(graphViewModel, Modifier.align(Alignment.BottomEnd), modifierButtons, isOpenedContextGraph, isOpenedContextEdge, idVerticesInfo)
			}
		}
	}
}

@Composable
fun ColumnZoomButtons( indexListZoom: MutableIntState, modifierBox: Modifier, modifierButtons: Modifier) {
	Column(modifierBox) {
		IconButton(
			onClick = {
				if (indexListZoom.intValue == 4) indexListZoom.intValue = 2
				else indexListZoom.intValue++
			},
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = "Zoom in",
				)
			}
		)
		IconButton(
			onClick = {
				if (indexListZoom.intValue == 0) indexListZoom.intValue = 2
				else indexListZoom.intValue--
			},
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = Icons.Default.Remove,
					contentDescription = "Zoom out"
				)
			}
		)
	}
}

@Composable
fun ColumnChangeAbility(abilityZoom: MutableState<Boolean>, modifierBox: Modifier, modifierButtons: Modifier) {
	Column(modifierBox) {
		IconButton(
			onClick = { abilityZoom.value = false },
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = if (abilityZoom.value) Icons.Filled.Timeline else Icons.Outlined.Timeline,
					contentDescription = "Ability move",
				)
			}
		)
		IconButton(
			onClick = { abilityZoom.value = true },
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = if (abilityZoom.value) Icons.Filled.AspectRatio else Icons.Outlined.AspectRatio,
					contentDescription = "Ability zoom"
				)
			}
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnInfoGraph(
	graphViewModel: GraphViewModel,
	modifierBox: Modifier,
	modifierButtons: Modifier,
	isOpenedContextGraph: MutableState<Boolean>,
	isOpenedContextEdge: MutableState<Boolean>,
	idVerticesInfo: MutableState<VertexViewModel?>
) {
	Column(modifierBox.padding(4.dp)) {
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
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle) // fix double greenCustom
		) {
			showAddItem(graphViewModel)
		}

		AnimatedVisibility(
			visible = isOpenedContextEdge.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle) // fix double greenCustom
		) {
			showEditItem(graphViewModel, idVerticesInfo)
		}
	}
}

@Composable
fun BoxComponentGraph(text: String, count: Int, action: () -> Unit) {
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(2.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(Color.White, JetTheme.shapes.cornerStyle)
	) {
		Text(text, fontSize = 16.sp, modifier = Modifier.padding(15.dp).align(Alignment.CenterStart))
		Text("$count", fontSize = 16.sp, modifier = Modifier.padding(15.dp).align(Alignment.Center))
		Button(
			onClick = action,
			enabled = true,
			modifier = Modifier
				.width(50.dp)
				.padding(4.dp)
				.align(Alignment.TopEnd),
			colors = ButtonDefaults.buttonColors(Color.White)
		) {
			Text("+")
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun showAddItem(graphViewModel: GraphViewModel) {
	val idFastAdd = remember { mutableIntStateOf(graphViewModel.vertices.size + 1) }

	val statePager = rememberPagerState { 2 } // todo(remember need?)

	val addingVertex = {
		graphViewModel.addVertex(VertexID(checkAndGet(graphViewModel, idFastAdd), VertexIDType.INT_TYPE))
	}
	val addingEdge = {
		val vertexSource = VertexID(checkAndGet(graphViewModel, idFastAdd), VertexIDType.INT_TYPE)
		graphViewModel.addVertex(vertexSource)
		val vertexTarget = VertexID(checkAndGet(graphViewModel, idFastAdd), VertexIDType.INT_TYPE)
		graphViewModel.addVertex(vertexTarget)
		graphViewModel.addEdge(vertexSource, vertexTarget, 1.0)
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(2.dp)
			.background(JetTheme.colors.primaryBackground, RoundedCornerShape(10.dp)),
		horizontalArrangement = Arrangement.spacedBy(1.dp),
	) {
		Column(Modifier.weight(1f)) {
			Text(text = graphViewModel.graph.label, modifier = androidx.compose.ui.Modifier.padding(4.dp))
			BoxComponentGraph("Vertices:", graphViewModel.vertices.size, addingVertex)
			BoxComponentGraph("Edges:", graphViewModel.sizeEdges, addingEdge)
		}
		Column(Modifier.weight(1f).height(147.dp)) { // todo(change size in dark theme or big size)
			HorizontalPager(
				modifier = Modifier
					.fillMaxSize()
					.padding(2.dp)
					.clip(JetTheme.shapes.cornerStyle)
					.background(whiteCustom, JetTheme.shapes.cornerStyle),
				state = statePager,
				userScrollEnabled = true,
			) { index ->
				when (index) {
					0 -> showMenuVertex(graphViewModel, Modifier)
					1 -> showMenuEdge(graphViewModel)
				}
			}
		}
	}
}

fun checkAndGet(graphViewModel: GraphViewModel, idFastAdd: MutableIntState): Int {
	while (graphViewModel.graph.idVertices.contains(VertexID(idFastAdd.intValue, VertexIDType.INT_TYPE))) {
		idFastAdd.intValue++
	}
	return idFastAdd.intValue
}

@Composable
fun showEditItem(graphViewModel: GraphViewModel, idVerticesInfo: MutableState<VertexViewModel?>) {
	val setEdges by remember { mutableStateOf(graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id)) }

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(4.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Column(Modifier
			.weight(1f)
			.verticalScroll(rememberScrollState())
		) {
			if (setEdges.isEmpty()) {
				Text("Doesn't have adjacent vertices", Modifier.padding(4.dp))
			}
			setEdges.forEach {
				Box(Modifier.fillMaxWidth()) {
					Text(it.idTarget.toString(), Modifier.padding(4.dp).align(Alignment.CenterStart))
					Button(
						onClick = { graphViewModel.removeVertex(it.idTarget) },
						modifier = Modifier
							.size(sizeBottom)
							.align(Alignment.CenterEnd),
						colors = ButtonDefaults.buttonColors(Color(whiteCustom.value))
					) {
						Icon(
							imageVector = Icons.Default.Delete,
							contentDescription = "Remove vertex"
						)
					}
				}
			}
		}
		Column(Modifier
			.weight(1f)
			.verticalScroll(rememberScrollState())
		) {
			if (setEdges.isEmpty()) {
				Text("Doesn't have edges", Modifier.padding(4.dp))
			}
			setEdges.forEach {
				Box(Modifier.fillMaxWidth()) {
					Text(it.toString(), Modifier.padding(4.dp).align(Alignment.CenterStart))
					Button(
						onClick = { graphViewModel.removeEdge(it.idSource, it.idTarget) },
						modifier = Modifier
							.size(sizeBottom)
							.align(Alignment.CenterEnd),
						colors = ButtonDefaults.buttonColors(Color(whiteCustom.value))
					) {
						Icon(
							imageVector = Icons.Default.Delete,
							contentDescription = "Remove edge"
						)
					}
				}
			}
		}
	}
}

//
//@Composable
//fun BoxEditItem(graphViewModel: GraphViewModel, item: , action: () -> Unit) {
//	Box(
//
//	) {
//
//	}
//}


@Composable
fun showMenuVertex(graphViewModel: GraphViewModel, modifier: Modifier) {
	var idVertex by remember { mutableStateOf("") }

	Column(modifier) {
		OutlinedTextField(
			value = idVertex,
			onValueChange = { idVertex = it },
			label = { Text("The vertex id") },
			modifier = Modifier.padding(4.dp)
		)

		Button(
			onClick = {
				if (idVertex != "") {
					graphViewModel.addVertex(VertexID(idVertex, VertexIDType.INT_TYPE))
				}
			},
			modifier = Modifier.padding(4.dp)
		) {
			Text("Create")
		}
	}
}

@Composable
fun showMenuEdge(graphViewModel: GraphViewModel) {
	var idVertexSource by remember { mutableStateOf("") }
	var idVertexTarget by remember { mutableStateOf("") }

	Column(
		modifier = Modifier
	) {
		Row(
			modifier = Modifier.padding(4.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			OutlinedTextField(
				value = idVertexSource,
				onValueChange = { idVertexSource = it },
				label = { Text("The vertex id") },
				modifier = Modifier.weight(1f)
			)
			OutlinedTextField(
				value = idVertexTarget,
				onValueChange = { idVertexTarget = it },
				label = { Text("The vertex id") },
				modifier = Modifier.weight(1f)
			)
		}

		Button(
			onClick = {
				if (idVertexSource != "" && idVertexTarget != "" && idVertexSource != idVertexTarget) {
					graphViewModel.addEdge(
						VertexID(idVertexSource, VertexIDType.INT_TYPE),
						VertexID(idVertexTarget, VertexIDType.INT_TYPE),
						1.0
					)
				}
			},
			modifier = Modifier.padding(4.dp)
		) {
			Text("Create")
		}
	}
}
