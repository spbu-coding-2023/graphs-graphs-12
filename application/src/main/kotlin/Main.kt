import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import models.SettingsModel
import models.utils.GraphInfo
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import views.MainScreen
import viewmodels.MainScreenViewModel
import java.awt.Dimension
import java.io.File
import java.io.FileWriter

val windowSizeStart = Pair(1000f, 700f)

/**
 * Data class representing the settings for application.
 *
 * @property currentStyle The current style of application, represented by a MutableState of [JetStyle]
 * @property currentFontSize The current font size of text in application, represented by a MutableState of [JetSize]
 * @property currentCornersStyle The current corner style of application, represented by a MutableState of [JetCorners]
 * @property currentFontFamily The current font family of text in application, represented by a MutableState of [JetFontFamily]
 * @property isDarkMode A boolean MutableState indicating whether the application is in dark mode or not
 */
data class JetSettings(
	val currentStyle: MutableState<JetStyle>,
	val currentFontSize: MutableState<JetSize>,
	val currentCornersStyle: MutableState<JetCorners>,
	val currentFontFamily: MutableState<JetFontFamily>,
	val isDarkMode: MutableState<Boolean>
)

/**
 * Application entry point.
 */
fun main() {
	application {
		val settings: SettingsModel = SettingsModel.loadSettings()
		val mainScreenViewModel = MainScreenViewModel(settings)

		val isDarkModeValue = isSystemInDarkTheme()
		val jetSettings = JetSettings(
			currentStyle = remember { mutableStateOf(JetStyle.Black) },
			currentFontSize = remember { mutableStateOf(JetSize.Medium) },
			currentCornersStyle = remember { mutableStateOf(JetCorners.Rounded) },
			currentFontFamily = remember { mutableStateOf(JetFontFamily.Default) },
			isDarkMode = remember { mutableStateOf(isDarkModeValue) }
		)

		val file = File("../settings")
		findOrCreateFile(file)
		loadSettings(file, jetSettings)

		Window(
			onCloseRequest = {
				saveSettings(jetSettings)
				saveHistory(mainScreenViewModel.homePageViewModel.previouslyLoadedGraph)
				exitApplication()
			},
			title = "YMOM",
			state = rememberWindowState(position = WindowPosition(Alignment.Center)),
		) {
			window.minimumSize = Dimension(windowSizeStart.first.toInt(), windowSizeStart.second.toInt())

			MaterialTheme { MainScreen(mainScreenViewModel, jetSettings) }
		}
	}
}

/**
 * Checks whether the file at the given path exists; if it does not exist, it creates this file.
 *
 * @param file [File] instance with abstract pathname
 */
fun findOrCreateFile(file: File) {
	if (!file.exists()) {
		if (file.createNewFile()) {
			println("The '${file.toString().substringAfter("/")}' file has been successfully created.")
		} else {
			println("Failed to create file '${file.toString().substringAfter("/")}'.")
		}
	}
}

/**
 * Reads the current customization parameters from [file] and writes them to [jetSettings].
 *
 * @param file storing current customization parameters
 * @param jetSettings an object that stores the current customization parameters
 */
fun loadSettings(file: File, jetSettings: JetSettings) {
	val converters = arrayOf<(String) -> Any>(
		{ string -> JetStyle.valueOf(string) },
		{ string -> JetSize.valueOf(string) },
		{ string -> JetCorners.valueOf(string) },
		{ string -> JetFontFamily.valueOf(string) }
	)

	file.readLines().withIndex().map { indexedValue -> indexedValue.index to indexedValue.value }
		.forEach { (index, setting) ->
			try {
				when (index) {
					0 -> jetSettings.currentStyle.value = converters[index](setting) as JetStyle
					1 -> jetSettings.currentFontSize.value = converters[index](setting) as JetSize
					2 -> jetSettings.currentCornersStyle.value = converters[index](setting) as JetCorners
					3 -> jetSettings.currentFontFamily.value = converters[index](setting) as JetFontFamily
					4 -> jetSettings.isDarkMode.value = setting.toBoolean()
				}
			} catch (e: IllegalArgumentException) {
				println("The element '$setting' in line $index does not match any element of the corresponding enumeration.")
			}
		}
}

/**
 * Writes the current customization parameters to the 'settings' file.
 *
 * @param jetSettings an object that stores the current customization parameters
 */
fun saveSettings(jetSettings: JetSettings) {
	val file = File("../settings")
	findOrCreateFile(file)

	file.writeText(
		"${jetSettings.currentStyle.value}\n" +
			"${jetSettings.currentFontSize.value}\n" +
			"${jetSettings.currentCornersStyle.value}\n" +
			"${jetSettings.currentFontFamily.value}\n" +
			"${jetSettings.isDarkMode.value}"
	)
}

/**
 * Writes a set of information about previously loaded graphs to the 'history' file.
 *
 * @param previouslyLoadedGraph a set of previously loaded graphs that will be saved
 */
fun saveHistory(previouslyLoadedGraph: List<GraphInfo>) {
	val file = File("../history")
	findOrCreateFile(file)

	file.writeText("")
	FileWriter(file, true).use { writer ->
		previouslyLoadedGraph.forEach { writer.write("$it\n") }
	}
}
