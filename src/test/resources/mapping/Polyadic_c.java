  /**
     * <p>
     * Answer true if this graph contains the given graph as a sub-component.
     * </p>
     * 
     * @param graph A graph to test
     * @return True if the graph is this graph, or is a sub-graph of this one.
     * @see com.hp.hpl.jena.graph.Graph#mightContain(Graph)
     */
	 
	 public boolean mightContain( Graph graph ) {
	    return (graph == this) || m_subGraphs.contains( graph );
    }