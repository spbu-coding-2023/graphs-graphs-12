package application.core.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun showSettings() {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(40.dp)
			.background(Color(254, 249, 231), RoundedCornerShape(14.dp))
	) {
		Text("Settings", modifier = Modifier.padding(10.dp))
	}
}
