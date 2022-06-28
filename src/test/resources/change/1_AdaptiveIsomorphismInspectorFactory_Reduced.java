package org._3pq.jgrapht.alg.isomorphism;

public class AdaptiveIsomorphismInspectorFactory
{

    /**
     * Checks if one of the graphs is from unsupported graph type and throws
     * IllegalArgumentException if it is. The current unsupported types are
     * graphs with multiple-edges.
     *
     * @param graph1
     * @param graph2
     *
     * @throws IllegalArgumentException
     */
    protected static void assertUnsupportedGraphTypes( Graph g )
        throws IllegalArgumentException
    {
        if ((g instanceof Multigraph)
            || (g instanceof DirectedMultigraph)
            || (g instanceof Pseudograph)) {
            throw new IllegalArgumentException(
                "graph type not supported for the graph" + g);
        }
    }
}
