package graphs_lab.core

open class Vertex<I>(
	val id: I
) {

	override fun toString(): String {
		return "${javaClass.simpleName}(id = $id)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Vertex<*>
		return id == other.id
	}

	override fun hashCode(): Int {
		return 31 * javaClass.name.hashCode() + id.hashCode()
	}

}
