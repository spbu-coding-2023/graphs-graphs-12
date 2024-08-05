import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import models.JetSettings
import models.SettingsModel
import models.utils.GraphInfo
import mu.KotlinLogging
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import views.MainScreen
import viewmodels.MainScreenViewModel
import java.awt.Dimension
import java.io.File
import java.io.FileWriter

private val logger = KotlinLogging.logger("EntryPoint")

/**
 * A pair representing the initial size of the application window.
 */
val windowSizeStart = Pair(1000f, 700f)

/**
 * Application entry point.
 */
fun main() {
	application {
		logger.info { "Starting YMOM graph-lab application..." }
		val isDarkModeValue = isSystemInDarkTheme()
		val jetSettings = JetSettings(
			currentStyle = remember { mutableStateOf(JetStyle.Black) },
			currentFontSize = remember { mutableStateOf(JetSize.Medium) },
			currentCornersStyle = remember { mutableStateOf(JetCorners.Rounded) },
			currentFontFamily = remember { mutableStateOf(JetFontFamily.Default) },
			isDarkMode = remember { mutableStateOf(isDarkModeValue) }
		)

		val settings: SettingsModel = SettingsModel.loadSettings(jetSettings)
		val mainScreenViewModel = MainScreenViewModel(settings)

		Window(
			onCloseRequest = {
				saveSettings(settings, jetSettings)
				saveHistory(settings, mainScreenViewModel.homePageViewModel.previouslyLoadedGraph)
				logger.info { "Close YMOM graph-lab application" }
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
 * Writes the current customization parameters to the 'settings' file.
 *
 * @param jetSettings an object that stores the current customization parameters
 */
private fun saveSettings(settings: SettingsModel, jetSettings: JetSettings) {
	val file = File(settings.applicationContextDirectory, ".settings")
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
private fun saveHistory(settings: SettingsModel, previouslyLoadedGraph: List<GraphInfo>) {
	val file = File(settings.applicationContextDirectory, ".history")
	findOrCreateFile(file)

	file.writeText("")
	FileWriter(file, true).use { writer ->
		previouslyLoadedGraph.forEach { writer.write("$it\n") }
	}
}
