package application.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.core.menu.BottomItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun widget(
	isOpenedContextEdge: Boolean,
	isOpenedAlgorithms: MutableState<Boolean>,
	isOpenedAlgorithmsW: MutableState<Boolean>
) {
	var isOpenedContextGraph by remember { mutableStateOf(true) }

	val listAlgorithms = listOf(
		"Use the Dijkstra's algo",
		"Use the Tarjan's algo for finding bridges",
		"Use the Tarjan's algo for scc",
		"Use the Kruskal's algo",
		"Use the Prim's algo",
		"Use the algo for finding cycles",
		"Use the Bellman-Ford's algo"
	)

	Box(
		modifier = Modifier.fillMaxSize()
	) {
		Row(
			modifier = Modifier
				.padding(4.dp)
				.clip(roundedCustom)
				.background(greenCustom, roundedCustom)
		) {
			AnimatedVisibility(
				visible = isOpenedAlgorithms.value || isOpenedAlgorithmsW.value,
				modifier = Modifier
					.clip(roundedCustom)
					.background(greenCustom, roundedCustom)
			) {
				Column(
					modifier = Modifier
						.width(380.dp) // delete
						.padding(2.dp)
						.clip(roundedCustom)
						.background(greenCustom)
						.pointerMoveFilter(
							onEnter = { isOpenedAlgorithmsW.value = true; false},
							onExit = { isOpenedAlgorithmsW.value = false; false}),
				) {
					listAlgorithms.forEach { item ->
						Button(
							onClick = { println("TODO") },
							enabled = true,
							modifier = Modifier
								.fillMaxWidth()
								.padding(2.dp) // delete space between other
								.align(Alignment.CenterHorizontally),
							border = BorderStroke(1.dp, blueCustom),
							colors = ButtonDefaults.buttonColors(Color.White)
						) {
							Text(item)
						}
					}
				}
			}
		}

		Column(
			modifier = Modifier
				.padding(2.dp)
				.align(Alignment.BottomEnd)
		) {
			Button(
				onClick = { isOpenedContextGraph = !isOpenedContextGraph },
				modifier = Modifier
					.width(weightBottom)
					.padding(2.dp)
					.align(Alignment.End)
					.pointerHoverIcon(PointerIcon.Hand),
				colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
			) {
				if (isOpenedContextGraph) Text("↓") else Text("↑")
			}

			AnimatedVisibility(
				visible = if (!isOpenedContextEdge) isOpenedContextGraph else false,
				modifier = Modifier
					.clip(RoundedCornerShape(10.dp))
					.background(greenCustom, RoundedCornerShape(10.dp)) // fix double greenCustom and bad animation
			) {
				var countVertices by remember { mutableStateOf(0) }
				var countEdges by remember { mutableStateOf(0) }

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(2.dp)
						.background(greenCustom, RoundedCornerShape(10.dp)),
					horizontalArrangement = Arrangement.spacedBy(1.dp),
				) {
					Column(
						modifier = Modifier.weight(1f)
					) {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(2.dp)
								.clip(RoundedCornerShape(14.dp))
								.background(Color.White)
						) {
							Text("Vertices:", fontSize = 22.sp, modifier = Modifier.padding(15.dp))
							Button(
								onClick = { }, // showContentVertex()
								enabled = true,
								modifier = Modifier
									.padding(4.dp)
									.align(Alignment.TopCenter),
								colors = ButtonDefaults.buttonColors(Color.White)
							) {
								Text("$countVertices", fontSize = 22.sp)
							}
							Button(
								onClick = { countVertices++ },
								enabled = true,
								modifier = Modifier
									.width(50.dp)
									.padding(4.dp)
									.align(Alignment.TopEnd),
								colors = ButtonDefaults.buttonColors(Color.White)
							) {
								Text("+", color = Color(0, 80, 255))
							}
						}
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(2.dp)
								.clip(RoundedCornerShape(14.dp))
								.background(Color.White)
						) {
							Text("Edges: ", fontSize = 22.sp, modifier = Modifier.padding(15.dp))
							Button(
								onClick = { }, // showContentEdge()
								enabled = true,
								modifier = Modifier
									.padding(4.dp)
									.align(Alignment.TopCenter),
								colors = ButtonDefaults.buttonColors(Color.White)
							) {
								Text("$countEdges", fontSize = 22.sp)
							}
							Button(
								onClick = { countEdges++ },
								enabled = true,
								modifier = Modifier
									.width(50.dp)
									.padding(4.dp)
									.align(Alignment.TopEnd),
								colors = ButtonDefaults.buttonColors(Color.White)
							) {
								Text("+", color = Color(0, 80, 255))
							}
						}
					}
					Column(
						modifier = Modifier.weight(1f)
					) {
						Row(
							modifier = Modifier.fillMaxWidth(1f)
						) {
							AnimatedVisibility(
								visible = countVertices != 0,
								modifier = Modifier
									.clip(RoundedCornerShape(10.dp))
									.background(greenCustom, RoundedCornerShape(10.dp))
							) {
								Text("Creation a vertex")
							}
						}
						Row(
							modifier = Modifier.fillMaxWidth(1f)
						) {
							AnimatedVisibility(
								visible = countEdges != 0,
								modifier = Modifier
									.clip(RoundedCornerShape(10.dp))
									.background(greenCustom, RoundedCornerShape(10.dp))
							) {
								Text("Creation a edge")
							}
						}
					}
				}
			}

			AnimatedVisibility( // not equal window from algos
				visible = isOpenedContextEdge,
				modifier = Modifier
					.clip(RoundedCornerShape(10.dp))
					.background(greenCustom, RoundedCornerShape(10.dp))
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clip(RoundedCornerShape(10.dp))
						.background(greenCustom, RoundedCornerShape(10.dp)),
					horizontalArrangement = Arrangement.spacedBy(1.dp),
				) {
					Column(
						modifier = Modifier
							.weight(1f)
							.padding(2.dp)
					) {
						Text("It is the weighted edge")
					}
					Column(
						modifier = Modifier
							.weight(1f)
							.padding(2.dp)
					) {
						Text("Edge: A -> B")
					}
				}
			}
		}
	}
}
