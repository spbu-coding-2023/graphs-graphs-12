package utils.representation

import viewmodels.graphs.VertexViewModel

interface RepresentationStrategy {
    fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>)
}
