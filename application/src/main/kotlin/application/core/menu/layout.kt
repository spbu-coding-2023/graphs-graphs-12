package application.core.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.core.widget

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun showLayout() {
	var isOpenedContext by remember { mutableStateOf(false) }

	Surface(
//		modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars).fillMaxSize().graphicsLayer()
		modifier = Modifier
			.padding(4.dp)
			.clip(RoundedCornerShape(14.dp))
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color(175, 218, 252))
		) {
			Button(
				onClick = { isOpenedContext = !isOpenedContext },
				enabled = true,
				modifier = Modifier
					.padding(4.dp)
					.align(Alignment.Center)
					.pointerMoveFilter(
						onEnter = { isOpenedContext = true; false},
						onExit = { isOpenedContext = false; false}),
				colors = ButtonDefaults.buttonColors(Color.White)
			) {
				Text("Some Edge", fontSize = 22.sp)
			}
			Button(
				onClick = { isOpenedContext = !isOpenedContext },
				enabled = true,
				modifier = Modifier
					.padding(4.dp)
					.align(Alignment.TopCenter)
					.pointerMoveFilter(
						onEnter = { isOpenedContext = true; false},
						onExit = { isOpenedContext = false; false}),
				colors = ButtonDefaults.buttonColors(Color.White)
			) {
				Text("Some Edge", fontSize = 22.sp)
			}
		}

		widget(isOpenedContext)
	}
}
