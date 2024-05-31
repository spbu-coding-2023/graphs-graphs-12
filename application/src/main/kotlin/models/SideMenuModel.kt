package models

import models.utils.TabItem

class SideMenuModel {
	private val _tabsItems = mutableListOf<List<TabItem>?>()
	private val tabsIdentifier = mutableMapOf<String, TabItem>()
	val tabsItems: List<List<TabItem>?>
		get() = _tabsItems.toList()
	val tabsCount: Int
		get() = _tabsItems.size

	fun addTabs(items: List<TabItem>) {
		_tabsItems.add(items)
		items.forEach { tabsIdentifier[it.title] = it }
	}

	fun addTabs(vararg items: TabItem) {
		addTabs(items.toList())
	}

	fun addSeparator() {
		_tabsItems.add(null)
	}

	fun changeTabVisibility(tabTitle: String, isHiddenState: Boolean) {
		val tabItem = tabsIdentifier[tabTitle] ?: return
		tabItem.isHidden.value = isHiddenState
	}

	override fun toString(): String {
		return "SideMenuModel(tabsCount = $tabsCount)"
	}
}
