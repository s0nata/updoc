/**
 * Checks if one of the graphs is from unsupported graph type and throws
 * IllegalArgumentException if it is. The current unsupported types are
 * graphs with multiple-edges.
 *
 * @param g
 *
 * @throws IllegalArgumentException
 */
protected static void assertUnsupportedGraphTypes(
    Graph g)
    throws IllegalArgumentException
{
    Graph [] graphArray = new Graph [] {
            graph1, graph2
        };
    for (int i = 0; i < graphArray.length; i++) {
        Graph g = graphArray[i];
        if ((g instanceof Multigraph)
            || (g instanceof DirectedMultigraph)
            || (g instanceof Pseudograph)) {
            throw new IllegalArgumentException(
                "graph type not supported for the graph" + g);
        }
    }
}
