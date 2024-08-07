package themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * This function is a theme wrapper for Jetpack Compose applications. It provides customizable themes
 * based on the provided parameters.
 *
 * @param style the color scheme of the theme. Default is [JetStyle.Purple]
 * @param textSize the size of the text in the theme. Default is [JetSize.Medium]
 * @param paddingSize the size of the padding in the theme. Default is [JetSize.Medium]
 * @param corners the corner style of the theme. Default is [JetCorners.Rounded]
 * @param fonts the font family of the theme. Default is [JetFontFamily.Default]
 * @param darkTheme a boolean indicating whether the theme should be in dark mode.
 * 					Default is the system's dark theme setting
 * @param content the content composable function that will be wrapped with the theme
 *
 * @return the composable function that wraps the provided [content] with the custom theme.
 */
@Composable
fun MainTheme(
	style: JetStyle = JetStyle.Purple,
	textSize: JetSize = JetSize.Medium,
	paddingSize: JetSize = JetSize.Medium,
	corners: JetCorners = JetCorners.Rounded,
	fonts: JetFontFamily = JetFontFamily.Default,
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val colors = when (darkTheme) {
		true -> {
			when (style) {
				JetStyle.Black -> blackDarkPalette
				JetStyle.White -> whiteDarkPalette
				JetStyle.Purple -> purpleDarkPalette
				JetStyle.Orange -> orangeDarkPalette
				JetStyle.Pink -> pinkDarkPalette
			}
		}

		false -> {
			when (style) {
				JetStyle.Black -> blackLightPalette
				JetStyle.White -> whiteLightPalette
				JetStyle.Purple -> purpleLightPalette
				JetStyle.Orange -> orangeLightPalette
				JetStyle.Pink -> pinkLightPalette
			}
		}
	}

	val typography = JetTypography(
		heading = TextStyle(
			fontSize = when (textSize) {
				JetSize.Small -> 38.sp
				JetSize.Medium -> 42.sp
				JetSize.Big -> 46.sp
			},
			fontWeight = FontWeight.Bold,
			fontFamily = when (fonts) {
				JetFontFamily.Default -> FontFamily.Default
				JetFontFamily.Monospace -> FontFamily.Monospace
				JetFontFamily.Cursive -> FontFamily.Cursive
				JetFontFamily.Serif -> FontFamily.Serif
				JetFontFamily.SansSerif -> FontFamily.SansSerif
			}
		),
		body = TextStyle(
			fontSize = when (textSize) {
				JetSize.Small -> 22.sp
				JetSize.Medium -> 26.sp
				JetSize.Big -> 30.sp
			},
			fontWeight = FontWeight.Normal,
			fontFamily = when (fonts) {
				JetFontFamily.Default -> FontFamily.Default
				JetFontFamily.Monospace -> FontFamily.Monospace
				JetFontFamily.Cursive -> FontFamily.Cursive
				JetFontFamily.Serif -> FontFamily.Serif
				JetFontFamily.SansSerif -> FontFamily.SansSerif
			}
		),
		toolbar = TextStyle(
			fontSize = when (textSize) {
				JetSize.Small -> 16.sp
				JetSize.Medium -> 20.sp
				JetSize.Big -> 24.sp
			},
			fontWeight = FontWeight.Medium,
			fontFamily = when (fonts) {
				JetFontFamily.Default -> FontFamily.Default
				JetFontFamily.Monospace -> FontFamily.Monospace
				JetFontFamily.Cursive -> FontFamily.Cursive
				JetFontFamily.Serif -> FontFamily.Serif
				JetFontFamily.SansSerif -> FontFamily.SansSerif
			}
		),
		mini = TextStyle(
			fontSize = when (textSize) {
				JetSize.Small -> 14.sp
				JetSize.Medium -> 16.sp
				JetSize.Big -> 18.sp
			},
			fontWeight = FontWeight.Medium,
			fontFamily = when (fonts) {
				JetFontFamily.Default -> FontFamily.Default
				JetFontFamily.Monospace -> FontFamily.Monospace
				JetFontFamily.Cursive -> FontFamily.Cursive
				JetFontFamily.Serif -> FontFamily.Serif
				JetFontFamily.SansSerif -> FontFamily.SansSerif
			}
		)
	)

	val shapes = JetShape(
		padding = when (paddingSize) {
			JetSize.Small -> 12.dp
			JetSize.Medium -> 16.dp
			JetSize.Big -> 20.dp
		},
		cornerStyle = when (corners) {
			JetCorners.Flat -> RoundedCornerShape(3.dp)
			JetCorners.Rounded -> RoundedCornerShape(14.dp)
		}
	)

	CompositionLocalProvider(
		LocalJetColors provides colors,
		LocalJetTypography provides typography,
		LocalJetShape provides shapes,
		content = content
	)
}
