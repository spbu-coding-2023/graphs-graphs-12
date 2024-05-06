package graphs_lab.algs

import graphs_lab.core.graphs.WeightedGraph

class BellmanFordShortestPath<I : Any>(val graph: WeightedGraph<I>) {
    private var weightOfShortestPath: Double = Double.POSITIVE_INFINITY
    private var countOfVertices: Int = 0
    private var matrix: Array<DoubleArray> = Array(countOfVertices) { DoubleArray(countOfVertices + 2) }
    private var pathMatrix: Array<IntArray> = Array(countOfVertices) { IntArray(countOfVertices + 2) }

    fun getPathWeight(idSource: I, idTarget: I): Double {
        if (!graph.containsVertex(idSource) || !graph.containsVertex(idTarget)) {
            throw NoSuchElementException("The id of a vertex that does not exist in the graph is passed.")
        }

        getPath(idSource, idTarget)
        return weightOfShortestPath
    }

    fun getPath(idSource: I, idTarget: I): MutableMap<Int, I> {
        if (!graph.containsVertex(idSource) || !graph.containsVertex(idTarget)) {
            throw Exception("The id of a vertex that does not exist in the graph is passed.")
        }

        //Mapping index to id.
        var index = 0
        val indexedVertices = mutableMapOf<I, Int>()
        val reverseIndexedVertices = mutableMapOf<Int, I>()
        graph.idVertices.forEach {
            indexedVertices[it] = index
            reverseIndexedVertices[index] = it
            index++
        }

        countOfVertices = graph.size
        //Checks whether all paths from the idSource should be built.
        if (matrix.isEmpty()) getPaths(idSource, indexedVertices)
        else if (matrix[indexedVertices[idSource]!!][0] == Double.POSITIVE_INFINITY) getPaths(idSource, indexedVertices)


        //Finding the length of the shortest path and the number of edges in this path
        weightOfShortestPath = Double.POSITIVE_INFINITY
        var numberOfEdgesInThePath = 0
        var tempIdTarget = indexedVertices[idTarget]!!
        for (q in 0 until countOfVertices) {
            if (matrix[tempIdTarget][q].compareTo(weightOfShortestPath) < 0) {
                weightOfShortestPath = matrix[tempIdTarget][q]
                numberOfEdgesInThePath = q
            }
        }

        if (weightOfShortestPath == Double.POSITIVE_INFINITY) {
            throw Exception("There is no path between vertex $idSource and vertex $idTarget.")
        }

        //Constructing a path using given indexes
        val resultInIndexes = IntArray(numberOfEdgesInThePath) { 0 }
        var tempNumberOfEdgesInThePath = numberOfEdgesInThePath
        while (tempNumberOfEdgesInThePath > 0) {
            resultInIndexes[tempNumberOfEdgesInThePath - 1] = tempIdTarget
            tempIdTarget = pathMatrix[tempIdTarget][tempNumberOfEdgesInThePath]
            tempNumberOfEdgesInThePath -= 1
        }

        //Constructing a path according to ID
        val resultInId = mutableMapOf<Int, I>()
        resultInId[0] = idSource
        for (i in 0 until numberOfEdgesInThePath) {
            resultInId[i + 1] = reverseIndexedVertices[resultInIndexes[i]] as I
        }
        return (resultInId)
    }
    
    private fun getPaths(idSource: I, indexedVertices: MutableMap<I, Int>) {

        // Construction of a matrix in which A_ij is the length of the shortest path from source to i,
        // containing no more than j edges.
        // Construction of a pathMatrix, in which A_ij contains the previous vertex to i in
        // one of these shortest paths.
        val start = indexedVertices[idSource]!!
        matrix = Array(countOfVertices) { DoubleArray(countOfVertices + 2) { Double.POSITIVE_INFINITY } }
        matrix[start][0] = 0.0
        pathMatrix = Array(countOfVertices) { IntArray(countOfVertices + 2) { 0 } }
        for (i in 1 until countOfVertices + 2) {
            graph.idVertices.forEach { vertex ->
                graph.vertexEdges(vertex).forEach { edge ->
                    val origin = indexedVertices[edge.idSource]!!
                    val destination = indexedVertices[edge.idTarget]!!
                    val edgeWeight = edge.weight

                    if (matrix[destination][i] > matrix[origin][i - 1] + edgeWeight) {
                        matrix[destination][i] = matrix[origin][i - 1] + edgeWeight
                        pathMatrix[destination][i] = origin
                    }
                }
            }
        }

        //Check for negative cycles
        for (i in 1 until countOfVertices) {
            var shortestDistance = Double.POSITIVE_INFINITY
            for (j in 1 until countOfVertices) {
                if (matrix[i][j] < shortestDistance) shortestDistance = matrix[i][j]
            }
            if (matrix[i][countOfVertices + 1] < shortestDistance) {
                throw Exception("Graph contains negative weight cycle.")
            }
        }

    }
}
