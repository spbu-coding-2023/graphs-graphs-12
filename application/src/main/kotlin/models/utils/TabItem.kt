package models.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a tab item in a tabbed interface.
 *
 * @property title the title of the tab item
 * @property iconSelected the selected icon of the tab item
 * @property iconUnselected the unselected icon of the tab item
 * @property isHidden a mutable state indicating whether the tab item is hidden or not. Default is false
 * @property isSelectablePage indicates whether the tab item is a selectable page or not. Default is false
 * @property dropDownMenuContext a composable function that provides a context menu for the tab item. Default is null
 * @property onItemClick a function that is called when the tab item is clicked. Default is null
 */
data class TabItem(
	val title: String,
	val iconSelected: ImageVector,
	val iconUnselected: ImageVector,
	val isHidden: MutableState<Boolean> = mutableStateOf(false),
	val isSelectablePage: Boolean = false,
	val dropDownMenuContext: @Composable ((TabItem) -> (Unit))? = null,
	private val onItemClick: ((TabItem) -> (Unit))? = null,
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TabItem

		return title == other.title
	}

	override fun hashCode(): Int {
		return title.hashCode()
	}

	override fun toString(): String {
		return "TabItem(title = '$title', isHidden = ${isHidden.value})"
	}

	/**
	 * Returns a function that calls the [onItemClick] function when invoked.
	 * If [onItemClick] is null, returns null.
	 */
	fun onClick(): (() -> (Unit))? {
		val onItemClick = this.onItemClick ?: return null
		return { onItemClick(this) }
	}
}
