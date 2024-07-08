package themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The height of the bottom part of the custom shape.
 */
val sizeBottom = 50.dp

/**
 * The radius of the vertices at the start of the custom shape.
 */
val radiusVerticesStart = 20.dp

/**
 * The width of the edges at the start of the custom shape.
 */
const val WidthEdgesStart = 1f

/**
 * The padding value for the custom shape.
 */
val paddingCustom = 4.dp

/**
 * Data class representing the color scheme for the [JetTheme].
 *
 * @property primaryBackground the color used for primary backgrounds
 * @property secondaryBackground the color used for secondary backgrounds
 * @property tertiaryBackground the color used for tertiary backgrounds
 * @property primaryText the color used for primary text
 * @property secondaryText the color used for secondary text
 * @property tintColor the color used for tinting elements
 *
 * @sample themes.JetTheme.colors
 *
 * @see Color
 */
data class JetColors(
	val primaryBackground: Color,
	val secondaryBackground: Color,
	val tertiaryBackground: Color,
	val primaryText: Color,
	val secondaryText: Color,
	val tintColor: Color
)

/**
 * Data class representing the typography scheme for the [JetTheme].
 *
 * @property heading the text style used for heading texts
 * @property body the text style used for body texts
 * @property toolbar the text style used for toolbar texts
 * @property mini the text style used for mini texts
 *
 * @sample themes.JetTheme.typography
 *
 * @see TextStyle
 */
data class JetTypography(
	val heading: TextStyle,
	val body: TextStyle,
	val toolbar: TextStyle,
	val mini: TextStyle
)

/**
 * Data class representing the shape scheme for the [JetTheme].
 *
 * @property padding the padding value for the shape
 * @property cornerStyle the shape used for the corners
 *
 * @sample themes.JetTheme.shapes
 *
 * @see Shape
 * @see Dp
 */
data class JetShape(
	val padding: Dp,
	val cornerStyle: Shape
)

/**
 * An object representing the theme of the application.
 * It provides access to the color, typography, and shape schemes defined in the JetTheme.
 *
 * @property colors the color scheme for the theme
 * @property typography the typography for the theme
 * @property shapes the shape for the theme
 *
 * @sample themes.JetTheme.colors
 * @sample themes.JetTheme.typography
 * @sample themes.JetTheme.shapes
 */
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

/**
 * An enumeration representing different styles for the theme.
 *
 * @property Black represents a black color scheme
 * @property White represents a white color scheme
 * @property Purple represents a purple color scheme
 * @property Orange represents an orange color scheme
 * @property Pink represents a pink color scheme
 */
enum class JetStyle {
	Black, White, Purple, Orange, Pink
}

/**
 * An enumeration representing different sizes for the theme elements.
 *
 * @property Small represents a small size
 * @property Medium represents a medium size
 * @property Big represents a big size
 */
enum class JetSize {
	Small, Medium, Big
}

/**
 * An enumeration representing different corner styles for the theme shapes.
 *
 * @property Flat represents a flat corner style
 * @property Rounded represents a rounded corner style
 */
enum class JetCorners {
	Flat, Rounded
}

/**
 * An enumeration representing different font families for the theme typography.
 *
 * @property Default represents the default font family
 * @property Monospace represents the monospace font family
 * @property Cursive represents the cursive font family
 * @property Serif represents the serif font family
 * @property SansSerif represents the sans-serif font family
 */
enum class JetFontFamily {
	Default, Monospace, Cursive, Serif, SansSerif
}

/**
 * A static composition local that holds the color scheme for the JetTheme.
 * If no color scheme is provided, it will throw an error with the message "No color provided".
 *
 * @sample themes.JetTheme.colors
 *
 * @see JetColors
 * @see androidx.compose.runtime.staticCompositionLocalOf
 */
val LocalJetColors = staticCompositionLocalOf<JetColors> {
	error("No color provided")
}

/**
 * A static composition local that holds the color scheme for the JetTheme.
 * If no color scheme is provided, it will throw an error with the message "No color provided".
 *
 * @sample themes.JetTheme.colors
 *
 * @see JetColors
 * @see androidx.compose.runtime.staticCompositionLocalOf
 */
val LocalJetTypography = staticCompositionLocalOf<JetTypography> {
	error("No font provided")
}

/**
 * A static composition local that holds the shape scheme for the JetTheme.
 * If no shape scheme is provided, it will throw an error with the message "No shapes provided".
 *
 * @sample themes.JetTheme.shapes
 *
 * @see JetShape
 * @see androidx.compose.runtime.staticCompositionLocalOf
 */
val LocalJetShape = staticCompositionLocalOf<JetShape> {
	error("No shapes provided")
}
