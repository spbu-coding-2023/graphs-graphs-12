package models.utils

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A class representing an item in a list.
 *
 * @property mainText the main text to be displayed in the list item
 * @property subText the secondary text to be displayed in the list item. Optional, defaults to null
 * @property icon the icon to be displayed in the list item. Optional, defaults to null
 * @property isHidden a flag indicating whether the list item should be hidden. Defaults to false
 * @property alignment the alignment of the list item's content. Defaults to [Alignment.CenterStart]
 * @property onClick a lambda function to be executed when the list item is clicked.
 * 						Defaults to printing a message with the main text
 */
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
