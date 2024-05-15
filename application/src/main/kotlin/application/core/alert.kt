package application.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun alert(center: Alignment) {
	Box(
		modifier = Modifier
	) {
		Column {
			Text("What type of the graph?")
			Text("Enter the name graph:")
		}
	}
}