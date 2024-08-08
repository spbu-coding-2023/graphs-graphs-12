package graphs_lab.algs.utils

/**
 * Implementation of splitting sets.
 * Use it if you need fast moving one element from one set to another.
 * You can add new elements or remove elements from sets collection.
 *
 * __Important__: this class not implements *unionSets* method.
 *
 * @param T type of elements in splitting sets
 * @property elements value which contains in splitting sets
 * @property size count of splitting sets
 */
class SetPartition<T>(elements: Collection<T>) {
	private val partition = mutableMapOf<T, MutableSet<T>>()
	val elements: Set<T>
		get() = partition.keys.toSet()
	var size: Int = 0
		private set

	init {
		elements.forEach { addElement(it) }
	}

	constructor(vararg elements: T) : this(elements.toSet())

	/**
	 * Add new elements to splitting sets.
	 * Its add as new set of one element.
	 *
	 * @param element new element
	 * @return `false` if element already exists in partition else `true`
	 */
	fun addElement(element: T): Boolean {
		if (element in partition) return false
		partition[element] = mutableSetOf(element)
		size++
		return true
	}

	/**
	 * Remove element from splitting sets.
	 *
	 * @param element what need remove
	 * @return `false` if element not exists in partition else `true`
	 */
	fun removeElement(element: T): Boolean {
		if (element !in partition) return false
		disconnectElement(element)
		partition.remove(element)
		size--
		return true
	}

	/**
	 * Selects an element into a separate set.
	 *
	 * @param element what need disconnect
	 * @throws IllegalArgumentException if [element] is not exists in splitting sets
	 */
	fun disconnectElement(element: T) {
		val elementSet = getElementSet(element)

		if (elementSet.size != 1) {
			elementSet.remove(element)
			size++
		}
		partition[element] = mutableSetOf(element)
	}

	/**
	 * Add [firstElement] to set which contains [secondElement].
	 *
	 * @param firstElement which need to connect with [secondElement]'s set
	 * @param secondElement identifier of set, where to add the first element
	 * @throws IllegalArgumentException if [firstElement] or [secondElement] not exists in splittings set
	 */
	fun connectElements(firstElement: T, secondElement: T) {
		val firstSet = getElementSet(firstElement)
		val secondSet = getElementSet(secondElement)

		if (firstSet == secondSet) return

		disconnectElement(firstElement)
		secondSet.add(firstElement)
		partition[firstElement] = secondSet
		partition[secondElement] = secondSet
		size--
	}

	/**
	 * Checks if the elements are contained in the same set.
	 *
	 * @param firstElement element of one of splitting sets
	 * @param secondElement element of one of splitting sets
	 * @return `true` if sets is equals else `false`
	 * @throws IllegalArgumentException if [firstElement] or [secondElement] not exists in splittings set
	 */
	fun isConnected(firstElement: T, secondElement: T): Boolean {
		return getElementSet(firstElement) == getElementSet(secondElement)
	}

	/**
	 * Generate splitting sets collection.
	 *
	 * @return collection of all splittings sets
	 */
	fun getPartition(): Set<Set<T>> {
		val visitedElements = mutableSetOf<T>()
		val result = mutableSetOf<Set<T>>()

		partition.forEach { element, elementSet ->
			if (element !in visitedElements) {
				result.add(elementSet)
				visitedElements.addAll(elementSet)
			}
		}
		return result.toSet()
	}

	/**
	 * Find splitting set which contains [element].
	 *
	 * @param element to find its set
	 * @return set which contains [element]
	 * @throws IllegalArgumentException if [element] is not exists in splitting sets
	 */
	fun getElementSet(element: T): MutableSet<T> {
		require(element in elements) { "Element $element is not contains in splittings set" }
		return partition.getOrDefault(element, mutableSetOf(element))
	}

	override fun toString(): String {
		return "SetPartition(partition=${getPartition()})"
	}
}
