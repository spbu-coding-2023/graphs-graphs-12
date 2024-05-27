import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import models.SettingsModel
import views.MainScreen
import viewmodels.MainScreenViewModel
import java.awt.Dimension

val windowSizeStart = Pair(1000f, 700f)

/**
 * Application entry point.
 */
fun main() {
	application {
		val settings: SettingsModel = SettingsModel.loadSettings()
		Window(
			onCloseRequest = ::exitApplication,
			title = "AppYMOM",
			state = rememberWindowState(position = WindowPosition(Alignment.Center)),
		) {
			window.minimumSize = Dimension(windowSizeStart.first.toInt(), windowSizeStart.second.toInt())

			MaterialTheme { MainScreen(MainScreenViewModel(settings)) }
		}
	}
}
