package parser;

import org.junit.Assert;
import org.junit.Test;
import parser.nodes.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpecializedNodeExtractionTest {

    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    private static final String FILE_DIR_ASTPARSE = "src/test/resources/ast-parsing/";

    /**
     * Tests if the test files contain the expected number of IfNodes
     */
    @Test
    public void ifNodeCountExtractionTest() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected method block count for each test file
        List<Integer> ifCount = new ArrayList<Integer>();
        ifCount.add(1);
        ifCount.add(15);
        ifCount.add(0);
        ifCount.add(5);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        testSpecializedNodeCount(fileNames, ifCount, IfNode.class);
    }

    /**
     * Tests if the test files contain the expected number of ForNodes
     */
    @Test
    public void forNodeCountExtractionTest() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected method block count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(1);
        nodeCount.add(9);
        nodeCount.add(0);
        nodeCount.add(2);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        testSpecializedNodeCount(fileNames, nodeCount, ForNode.class);
    }

    /**
     * Tests if the test files contain the expected number of WhileNodes
     */
    @Test
    public void whileNodeCountExtractionTest() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected method block count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(0);
        nodeCount.add(1);
        nodeCount.add(0);
        nodeCount.add(0);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        fileNames.add(FILE_DIR_ASTPARSE + "differentStatements.java");
        nodeCount.add(3);

        testSpecializedNodeCount(fileNames, nodeCount, WhileNode.class);
    }

    /**
     * Tests if the test files contain the expected number of TryCatchNodes
     */
    @Test
    public void TryCatchNodeCountExtractionTest() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected method block count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(0);
        nodeCount.add(0);
        nodeCount.add(0);
        nodeCount.add(0);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        fileNames.add(FILE_DIR_ASTPARSE + "differentStatements.java");
        nodeCount.add(3);

        testSpecializedNodeCount(fileNames, nodeCount, TryCatchNode.class);
    }

    /**
     * Tests if the test files contain the expected number of SwitchNodes
     */
    @Test
    public void switchNodeCountExtractionTest() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected method block count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(1);
        nodeCount.add(0);
        nodeCount.add(0);
        nodeCount.add(0);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        fileNames.add(FILE_DIR_ASTPARSE + "differentStatements.java");
        nodeCount.add(1);

        testSpecializedNodeCount(fileNames, nodeCount, SwitchNode.class);
    }

    private void testSpecializedNodeCount(
            List<String> fileNames,
            List<Integer> nodeCount,
            Class<? extends SpecializedNode> specializedNodeClass) {
        Map<String, Integer> fileNamesWithCount =
                IntStream.range(0, fileNames.size())
                        .boxed()
                        .collect(Collectors.toMap(fileNames::get, nodeCount::get));

        for (Map.Entry<String, Integer> entry : fileNamesWithCount.entrySet()) {
            String fileName = entry.getKey();
            int expectedCount = entry.getValue();

            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));

            long actualCount =
                    collectedNodes.stream()
                            .map(node -> node.getBodyNodesSequence())
                            .flatMap(List::stream)
                            .filter(node -> specializedNodeClass.isInstance(node))
                            .count();
            Assert.assertEquals(expectedCount, actualCount);

            // Another method to extract the nodes
            actualCount =
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    new File(fileName), specializedNodeClass)
                            .size();
            Assert.assertEquals(expectedCount, actualCount);
        }
    }
}
