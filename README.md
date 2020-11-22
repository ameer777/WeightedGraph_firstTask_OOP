Project:
              this project represents an undirectional and weighted graph that support a large number of nodes,
              based on afficient compact representation.

              Data struct:
              this project based on HashTable. HashTable allow i‎nsert search and remove in O(1) (most cases)
              every node has HashTable that represent his neighbor list.
              every graph  create an HashTable and every node has collection of his neighbor.    

Classes:
             WGraph_DS: implements graph interface represent the graph. every vertex (node) stored in hashTable, and every vertex has collection of his neighbors.
             It also contains an implementation of the node_info interface, in an internal class:(interface node_info),
             implemants node_info interface represents the set of operations applicable
            on a node (vertex) in an undirectional and  weighted graph.

            methods:(node_info)
		constructor
		getNi : return a collection with all the neighbor nodes if this node
		hasN : return if this node have (parameter-key) node neighbor
		addNi: add vertex(node) to the neighbor collection
		removeNode: remove the edges of this nodes
		getters & setters
 
            methods:weighted_graph),
		constructor
		getNode : return the vertex from the graph 
		hasEdge : return if has edge between 2 vertex,and return the weight of the edge 
		addNode : add vertex to the graph 
		connect : add edge between 2 vertex , and update the weight of the edge
		getV() : return collection of all vertex in the graph
		getV(int node_id) : return collection of node neighbors
		removNode : remove the vertex from the graph and disconnected all edegs
		removeEdge : remove edge between 2 vertex
		nodeSize
		edgeSize
		getMC : return the mode count (number of changes on the graph)

               WGraph_Algo: implemants weighted_graph_algorithms,
                                 methods:
			init(graph g): init the graph which this set of algorithms operates on.
			copy : compute a deep copy of this graph
			isConnected : check if there is a valid path from every vertex to each other vertex
			shortestPathDist (int source , int desteny): return the length of the shortest path between 2 vertex.
			shortestPath (int source , int desteny): return the collection of the shortest path
                                                    save: Saves this weighted (undirected) graph to the given file name.
                                                    load:This method load a graph to this graph algorithm.if the file was successfully loaded - the underlying graph
                                                    of this class will be changed (to the loaded one), in case the
                                                    graph was not loaded the original graph should remain "as is".
