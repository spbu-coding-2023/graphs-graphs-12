package application.core.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuScope
import application.core.greenCustom
import application.core.roundedCustom

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun showSettings() {
	var example by remember { mutableStateOf(false) }
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(80.dp)
			.background(greenCustom, roundedCustom)
	) {
		IconButton(
			onClick = { example = !example }
		) {
			Icon(
				imageVector = Icons.Outlined.Settings,
				contentDescription = "Settings"
			)
		}
//		CursorDropdownMenu(
//
//		) {
//
//		}
//
//		ExposedDropdownMenuBox(
//			expanded = example,
//			onExpandedChange = { example = it }
////			onDismissRequest = { example = false },
//////			modifier = Modifier.pointerMoveFilter(
//////				onEnter = {  }
//////			)
//		) {
//			Column {
//				DropdownMenuItem(
//					onClick = {  }
//				) {
//					Text("Colour")
//				}
//				DropdownMenuItem(
//					onClick = {  }
//				) {
//					Text("Font")
//				}
//			}
//
//		}
		Text("Settings", modifier = Modifier.padding(10.dp))
	}
}
