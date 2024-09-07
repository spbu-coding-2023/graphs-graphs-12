package graphs_lab.algs.utils

/**
 * Represents a Disjoint Sets data structure.
 *
 * @param T the type of elements in the disjoint sets
 * @property size counts of disjoint sets at objects
 */
class DisjointSets<T>(elements: Collection<T>) {
	private val parents: MutableMap<T, T> = mutableMapOf()
	private val rangs: MutableMap<T, Int> = mutableMapOf()
	var size = elements.toSet().size
		private set

	init {
		require(elements.isNotEmpty()) { "Empty elements collection, can't init disjoint sets" }
		elements.forEach { element ->
			parents.putIfAbsent(element, element)
			rangs.putIfAbsent(element, 0)
		}
	}

	/**
	 * Finds the root of the set that the element belongs to.
	 *
	 * @param element The element to find the root for
	 * @return The root of the set that the element belongs to
	 * @throws IllegalArgumentException if the element is not found in the disjoint sets
	 */
	fun findRoot(element: T): T {
		require(element in parents.keys) { "DisjointSet structure is not contains element $element" }

		val root: T = getParent(element)
		updateRoot(element, root)
		return root
	}

	/**
	 * Checks if two elements are connected in the disjoint sets.
	 *
	 * @param firstElement The first element to check
	 * @param secondElement The second element to check
	 * @return True if the elements are connected (belong to the same set), false otherwise
	 */
	fun isConnected(firstElement: T, secondElement: T): Boolean {
		val firstRoot = findRoot(firstElement)
		val secondRoot = findRoot(secondElement)

		return firstRoot == secondRoot
	}

	/**
	 * Unites the sets that contain the given two elements.
	 *
	 * @param firstElement The first element to unite
	 * @param secondElement The second element to unite
	 */
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

	/**
	 * Returns the sets of disjoint sets in the structure.
	 *
	 * @return A set of sets representing the disjoint sets in the structure
	 */
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

	/**
	 * Returns the rang of set which contains [element].
	 *
	 * @param element the element of disjoint sets
	 * @return rang of set which contains [element] or 0 by default value
	 */
	private fun getSetRang(element: T): Int {
		return rangs.getOrDefault(element, 0)
	}

	/**
	 * Updates the root of the set that the element belongs to.
	 *
	 * @param element The element to update the root for
	 * @param root The new root of the set that the element belongs to
	 */
	private fun updateRoot(element: T, root: T) {
		var current: T = element
		while (true) {
			val parent = getParent(current)
			if (parent == root) break
			parents[current] = root
			current = parent
		}
	}

	/**
	 * Returns the parent of the given element in the Disjoint Sets.
	 *
	 * @param element The element to find the parent for
	 * @return The parent of the given element in the disjoint sets
	 */
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
		return "DisjointSets${getDisjointSets()}"
	}
}
