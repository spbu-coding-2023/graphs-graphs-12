package models

import graphs_lab.core.graphs.WeightedGraph
import utils.GraphSavingType
import utils.VertexIDType

class SettingsModel {
	val graphNameRegEx = Regex("[a-zA-Z][a-zA-Z0-9_-]*")
	// TODO(Implements save graph)
	fun saveGraph(graph: WeightedGraph<VertexID>, vertexIDType: VertexIDType, value: GraphSavingType) {
		println(graph)
	}

	companion object {
		@JvmStatic
		fun loadSettings(): SettingsModel {
			// TODO(valid load of settings from configuration)
			return SettingsModel()
		}
	}
}
