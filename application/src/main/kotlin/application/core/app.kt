package application.core

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import application.core.menu.menu

data class TabItem(
	val title: String,
	val iconSelected: ImageVector,
	val iconUnselected: ImageVector,
) // why here?

val whiteCustom = Color(254, 249, 231)
val grayCustom = Color(217, 217, 217)
val greenCustom = Color(218, 228, 205)
val blueCustom = Color(175, 218, 252)

val roundedCustom = RoundedCornerShape(14.dp)

val weightBottom = 50.dp

@Composable
fun app() {
	menu()
//	alert()
//	exception()
}
