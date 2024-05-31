package models.utils

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector

open class ListWidgetItem(
	val mainText: String,
	val subText: String? = null,
	val icon: ImageVector? = null,
	var isHidden: Boolean = false,
	val alignment: Alignment = Alignment.CenterStart,
	val onClick: () -> Unit = { println("Click on list item: $mainText") }
) {
	override fun toString(): String {
		return "ListWidgetItem(mainText='$mainText', isHidden=$isHidden)"
	}
}
