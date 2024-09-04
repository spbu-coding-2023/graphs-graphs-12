package integrations

import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import models.JetSettings
import models.SettingsModel
import org.junit.Rule
import org.junit.Test
import viewmodels.MainScreenViewModel
import views.MainScreen

class TestGraphLoading {
	@OptIn(ExperimentalTestApi::class)
	@Test
	fun testMainScreenView() = runJetSettingsComposeUiTest(
		context = { jetSettings: JetSettings ->
			val settings: SettingsModel = SettingsModel.loadSettings(jetSettings)
			val mainScreenViewModel = MainScreenViewModel(settings)
			MainScreen(mainScreenViewModel, jetSettings)
		}
	) { uiTest: ComposeUiTest ->
		uiTest.onNodeWithTag("side-menu").assertExists("Not found side-menu")
		uiTest.onNodeWithTag("side-menu-item#Home").assertExists("Not found side-menu-item#Home")
		uiTest.onNodeWithTag("side-menu-item#Settings").assertExists("Not found side-menu-item#Settings")
		uiTest.onNodeWithTag("home-page").assertExists("Not found home-page")
		uiTest.onNodeWithTag("settings-page").assertDoesNotExist()
		// Click on settings-tab item
		uiTest.onNodeWithTag("side-menu-item#Settings").performClick()
		uiTest.onNodeWithTag("settings-page").assertExists("Not found settings-page")
		uiTest.onNodeWithTag("home-page").assertDoesNotExist()
	}
}
