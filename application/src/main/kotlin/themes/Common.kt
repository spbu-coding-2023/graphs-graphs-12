package themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val sizeBottom = 50.dp
val radiusVerticesStart = 20.dp
val widthEdgesStart = 1f
val paddingCustom = 4.dp

data class JetColors(
	val primaryBackground: Color,
	val secondaryBackground: Color,
	val tertiaryBackground: Color,
	val primaryText: Color,
	val secondaryText: Color,
	val tintColor: Color
)

data class JetTypography(
	val heading: TextStyle,
	val body: TextStyle,
	val toolbar: TextStyle,
	val mini: TextStyle
)

data class JetShape(
	val padding: Dp,
	val cornerStyle: Shape
)

object JetTheme {
	val colors: JetColors
		@Composable
		get() = LocalJetColors.current

	val typography: JetTypography
		@Composable
		get() = LocalJetTypography.current

	val shapes: JetShape
		@Composable
		get() = LocalJetShape.current
}

enum class JetStyle {
	Black, White, Purple, Orange, Pink
}

enum class JetSize {
	Small, Medium, Big
}

enum class JetCorners {
	Flat, Rounded
}

enum class JetFontFamily {
	Default, Monospace, Cursive, Serif, SansSerif
}

val LocalJetColors = staticCompositionLocalOf<JetColors> {
	error("No color provided")
}

val LocalJetTypography = staticCompositionLocalOf<JetTypography> {
	error("No font provided")
}

val LocalJetShape = staticCompositionLocalOf<JetShape> {
	error("No shapes provided")
}
