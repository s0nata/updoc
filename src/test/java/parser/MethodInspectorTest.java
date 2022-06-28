package parser;

import org.apache.commons.lang3.Range;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MethodInspectorTest {


    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    @Test
    public void testMethodLOCextractionRegular() {
        // expected data
        ArrayList<Range<Integer>> expectedLOCs = new ArrayList<>();
        expectedLOCs.add(Range.between(82, 99));   // BhandariKDisjointShortestPaths, constructor
        expectedLOCs.add(Range.between(115, 151)); // getPaths, override method
        expectedLOCs.add(Range.between(154, 158)); // getPaths, override method, NO JAVADOC
        expectedLOCs.add(Range.between(167, 182)); // prepare
        expectedLOCs.add(Range.between(194, 205)); // resolvePaths
        expectedLOCs.add(Range.between(216, 254)); // buildPaths
        expectedLOCs.add(Range.between(263, 292)); // findOverlappingEdges
        expectedLOCs.add(Range.between(294, 300)); // findOverlappingEdges, NO JAVADOC

        // actual data
        String file = FILE_DIR_CHANGE + "0_BhandariKDisjointShortestPaths.java";
        ArrayList<MethodNode> commentedMethods = MethodInspector.getMethodNodesFromFile(new File(file));

        assertEquals(expectedLOCs.size(), commentedMethods.size());

        for (int i = 0; i < commentedMethods.size(); i++) {
            assertEquals(expectedLOCs.get(i), commentedMethods.get(i).getLOCs());
        }

    }


    @Test
    public void testMethodLOCextractionFactory() {

        // expected data
        ArrayList<Range<Integer>> expectedLOCs = new ArrayList<>();
        expectedLOCs.add(Range.between(84, 98));   // createIsomorphismInspector
        expectedLOCs.add(Range.between(109, 114)); // createIsomorphismInspector
        expectedLOCs.add(Range.between(126, 140)); // createIsomorphismInspectorByType
        expectedLOCs.add(Range.between(153, 165)); // createIsomorphismInspectorByType
        expectedLOCs.add(Range.between(178, 206)); // createAppropriateConcreteInspector
        expectedLOCs.add(Range.between(218, 235)); // assertUnsupportedGraphTypes
        expectedLOCs.add(Range.between(237, 240)); // checkGraphsType, NO JAVADOC
        expectedLOCs.add(Range.between(247, 267)); //  createTopologicalExhaustiveInspector

        // actual data
        String file = FILE_DIR_CHANGE + "0_AdaptiveIsomorphismInspectorFactory.java";
        ArrayList<MethodNode> commentedMethods = MethodInspector.getMethodNodesFromFile(new File(file));

        assertEquals(expectedLOCs.size(), commentedMethods.size());

        for (int i = 0; i < commentedMethods.size(); i++) {
            assertEquals(expectedLOCs.get(i), commentedMethods.get(i).getLOCs());
        }

    }

    @Test
    public void testMethodLOCextractionInnerClass() {

        // expected data
        ArrayList<Range<Integer>> expectedLOCs = new ArrayList<>();
        // -- main class
        expectedLOCs.add(Range.between(100, 102));  // JGraphModelAdapter, constructor
        expectedLOCs.add(Range.between(114, 143));  // JGraphModelAdapter, constructor
        expectedLOCs.add(Range.between(154, 156));  // getEdgeCell
        expectedLOCs.add(Range.between(168, 170));  // getVertexCell
        expectedLOCs.add(Range.between(182, 191));  // getVertexPort
        expectedLOCs.add(Range.between(210, 218));  // edit
        expectedLOCs.add(Range.between(227, 229));  // edit
        expectedLOCs.add(Range.between(243, 247));  // insert
        expectedLOCs.add(Range.between(257, 260)); // remove
        expectedLOCs.add(Range.between(268, 281)); // addJGraphTEdge
        expectedLOCs.add(Range.between(289, 301));  // addJGraphTVertex
        expectedLOCs.add(Range.between(312, 327));  // createDefaultEdgeAttributes
        expectedLOCs.add(Range.between(337, 350));  // createDefaultVertexAttributes
        expectedLOCs.add(Range.between(360, 365));  // removeJGraphTEdge
        expectedLOCs.add(Range.between(375, 381));  // removeJGraphTVertex
        // -- private inner class
        expectedLOCs.add(Range.between(395, 397)); // edgeAdded
        expectedLOCs.add(Range.between(403, 405)); // edgeRemoved
        expectedLOCs.add(Range.between(411, 413)); // vertexAdded
        expectedLOCs.add(Range.between(419, 421)); // vertexRemoved


        // actual data
        String file = FILE_DIR_CHANGE + "0_JGraphModelAdapter.java";
        ArrayList<MethodNode> commentedMethods = MethodInspector.getMethodNodesFromFile(new File(file));

        assertEquals(expectedLOCs.size(), commentedMethods.size());

        for (int i = 0; i < commentedMethods.size(); i++) {
            assertEquals(expectedLOCs.get(i), commentedMethods.get(i).getLOCs());
        }

    }
}
