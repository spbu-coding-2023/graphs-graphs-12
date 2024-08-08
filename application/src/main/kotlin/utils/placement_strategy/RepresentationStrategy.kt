package utils.placement_strategy

import viewmodels.graphs.VertexViewModel

/**
 * Representation strategy interface to call its implementation.
 * It's needed to create valid placements of graph on plane.
 */
interface RepresentationStrategy {
    /**
     * Function to replace graph on plane which size: [width]x[height].
     *
     * @param width width of plane
     * @param height height of plane
     * @param vertices collection of graph vertices
     */
    fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>)
}
