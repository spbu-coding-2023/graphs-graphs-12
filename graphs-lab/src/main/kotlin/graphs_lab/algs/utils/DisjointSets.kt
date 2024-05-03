package graphs_lab.algs.utils

class DisjointSets<T>(elements: Set<T>) {
	private val parents: MutableMap<T, T> = mutableMapOf()
	private val rangs: MutableMap<T, Int> = mutableMapOf()
	var size = elements.size
		private set

	init {
		elements.forEach() { element ->
			parents.putIfAbsent(element, element)
			rangs.putIfAbsent(element, 0)
		}
	}

	fun findRoot(element: T): T {
		if (element !in parents.keys) throw IllegalArgumentException(
			"DisjointSet structure is not contains element $element"
		)

		val root: T = getParent(element)
		updateRoot(element, root)
		return root
	}

	fun isConnected(firstElement: T, secondElement: T): Boolean {
		val firstRoot = findRoot(firstElement)
		val secondRoot = findRoot(secondElement)

		return firstRoot == secondRoot
	}

	fun unionSets(firstElement: T, secondElement: T) {
		val firstRoot = findRoot(firstElement)
		val secondRoot = findRoot(secondElement)

		if (firstRoot == secondRoot) return

		val firstRang: Int = getSetRang(firstRoot)
		val secondRang: Int = getSetRang(secondRoot)
		if (firstRang < secondRang) {
			parents[firstRoot] = secondRoot
		} else {
			parents[secondRoot] = firstRoot
			if (firstRang == secondRang) rangs[firstRoot] = firstRang + 1
		}
		size--
	}

	fun getDisjointSets(): Set<Set<T>> {
		val mappedSets = mutableMapOf<T, MutableSet<T>>()
		for (element in parents.keys) {
			val root = findRoot(element)
			val set: MutableSet<T> = mappedSets.getOrDefault(root, mutableSetOf())
			set.add(element)
			mappedSets[root] = set
		}

		return mappedSets.values.toSet()
	}

	private fun getSetRang(element: T): Int {
		return rangs.getOrDefault(element, 0)
	}

	private fun updateRoot(element: T, root: T) {
		var current: T = element
		while (true) {
			val parent = getParent(current)
			if (parent == root) break
			parents[current] = root
			current = parent
		}
	}

	private fun getParent(element: T): T {
		var current: T = element
		while (true) {
			val parent: T = parents.getOrDefault(current, current)
			if (parent == current) break
			current = parent
		}
		return current
	}

	override fun toString(): String {
		return "DisjointSet(${getDisjointSets()})"
	}

}
