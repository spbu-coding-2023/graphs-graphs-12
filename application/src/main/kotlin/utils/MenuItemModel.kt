package utils

/**
 * A data class representing a menu item model.
 *
 * @param title the title of the menu item
 * @param currentIndex the index of the currently selected value. Default is 0
 * @param values a list of possible values for the menu item
 */
data class MenuItemModel(
	val title: String,
	val currentIndex: Int = 0,
	val values: List<String>
)
