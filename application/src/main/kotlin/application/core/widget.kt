package application.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@Composable
fun widget(isOpenedContextEdge: Boolean) {
	var isOpenedContextGraph by remember { mutableStateOf(false) }

	Box(
		modifier = Modifier.fillMaxSize()
	) {
		Column(
			modifier = Modifier
				.padding(2.dp)
				.align(Alignment.BottomEnd)
		) {
			Button(
				onClick = { isOpenedContextGraph = !isOpenedContextGraph },
				modifier = Modifier
					.width(50.dp)
					.padding(2.dp)
					.align(Alignment.End)
					.pointerHoverIcon(PointerIcon.Hand),
				colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
			) {
				if (isOpenedContextGraph || isOpenedContextEdge) Text("↓") else Text("↑")
			}

			AnimatedVisibility(
				visible = if (!isOpenedContextEdge) isOpenedContextGraph else false,
				modifier = Modifier
					.clip(RoundedCornerShape(10.dp))
					.background(Color(217, 217, 217))
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(2.dp)
						.background(Color(217, 217, 217)),
					horizontalArrangement = Arrangement.spacedBy(1.dp),
				) {
					Column(
						modifier = Modifier.weight(1f)
					) {
						Text("Graph has vertices and edges", Modifier
							.fillMaxWidth()
							.padding(1.dp)
							.clip(RoundedCornerShape(4.dp))
							.background(Color.White))
					}
				}
			}

			AnimatedVisibility(
				visible = isOpenedContextEdge,
				modifier = Modifier
					.clip(RoundedCornerShape(10.dp))
					.background(Color(217, 217, 217))
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(2.dp)
						.background(Color(217, 217, 217)),
					horizontalArrangement = Arrangement.spacedBy(1.dp),
				) {
					Column(
						modifier = Modifier.weight(1f)
					) {
						Text("It is the weighted edge", Modifier
							.fillMaxWidth()
							.padding(1.dp)
							.clip(RoundedCornerShape(4.dp))
							.background(Color.White))
					}
					Column(
						modifier = Modifier.weight(1f)
					) {
						Text("Edge: A -> B", Modifier
							.fillMaxWidth()
							.padding(1.dp)
							.clip(RoundedCornerShape(4.dp))
							.background(Color.White))
					}
				}
			}
		}
	}
}
