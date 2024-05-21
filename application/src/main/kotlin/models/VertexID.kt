package models

import utils.VertexIDType

class VertexID(private val value: Any, val type: VertexIDType) {

	fun valueToInt(): Int = value.toString().toInt()
	fun valueToString(): String = value.toString()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as VertexID

		if (value != other.value) return false
		if (type != other.type) return false

		return true
	}

	override fun hashCode(): Int {
		var result = value.hashCode()
		result = 31 * result + type.hashCode()
		return result
	}

	override fun toString(): String {
		return "VertexID(value=$value, type=$type)"
	}


	companion object {
		@JvmStatic
		fun vertexIDFromString(value: String, type: VertexIDType): VertexID = when (type) {
			VertexIDType.INT_TYPE -> VertexID(value.toInt(), type)
			VertexIDType.STRING_TYPE -> VertexID(value, type)
		}
	}
}