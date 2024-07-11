package models

import androidx.compose.runtime.MutableState
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle

/**
 * Data class representing the settings for application.
 *
 * @property currentStyle the current style of application, represented by a MutableState of [JetStyle]
 * @property currentFontSize the current font size of text in application, represented by a MutableState of [JetSize]
 * @property currentCornersStyle the current corner style of application, represented by a MutableState of [JetCorners]
 * @property currentFontFamily the current font family of text in application, represented by a MutableState of [JetFontFamily]
 * @property isDarkMode a boolean MutableState indicating whether the application is in dark mode or not
 */
data class JetSettings(
	val currentStyle: MutableState<JetStyle>,
	val currentFontSize: MutableState<JetSize>,
	val currentCornersStyle: MutableState<JetCorners>,
	val currentFontFamily: MutableState<JetFontFamily>,
	val isDarkMode: MutableState<Boolean>
)
