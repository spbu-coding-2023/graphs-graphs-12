package application.core

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import application.core.menu.menu

data class TabItem(
	val title: String,
	val iconSelected: ImageVector,
	val iconUnselected: ImageVector,
)

@Composable
fun app() {
	menu()
//	alert()
//	exception()
}
