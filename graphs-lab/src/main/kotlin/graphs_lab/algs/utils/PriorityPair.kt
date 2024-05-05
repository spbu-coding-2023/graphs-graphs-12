package graphs_lab.algs.utils

/**
 * Represents a pair of priority and value.
 *
 * @param P The type of priority, which must be Comparable
 * @param I The type of value
 * @property priority The priority of the pair
 * @property value The value associated with the priority
 */
data class PriorityPair<P : Comparable<P>, I>(val priority: P, val value: I) : Comparable<PriorityPair<P, I>> {

	override fun compareTo(other: PriorityPair<P, I>): Int {
		return priority.compareTo(other.priority)
	}

	override fun toString(): String {
		return "PriorityPair(priority=$priority, value=$value)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as PriorityPair<*, *>

		if (priority != other.priority) return false
		if (value != other.value) return false

		return true
	}

	override fun hashCode(): Int {
		var result = javaClass.name.hashCode()
		result = 31 * result + priority.hashCode()
		result = 31 * result + (value?.hashCode() ?: 0)
		return result
	}

}
