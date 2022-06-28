package parser;

import org.apache.commons.lang3.Range;
import org.junit.Assert;
import org.junit.Test;
import parser.nodes.SingleAssignmentStatementNode;
import parser.nodes.SingleMethodCallSequenceNode;
import parser.nodes.SingleMethodReturnStatementNode;
import parser.nodes.SpecializedNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CoarseGrainedParsingTest {

    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    private static final String FILE_DIR_ASTPARSE = "src/test/resources/ast-parsing/";

    /* Comparator is used to compare the 2 different extraction methods, because the returned lists
    don't always have the same order*/
    private static final Comparator<SpecializedNode> comparator =
            Comparator.comparing(
                    SpecializedNode::getLOCs,
                    (loc1, loc2) -> Integer.compare(loc1.getMinimum(), loc2.getMinimum()));

    /*Tests if the 2 implemented extraction methods are equivalent, independent of order of extracted nodes*/
    @Test
    public void testExtractionMethodEquivalence() {
        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FILE_DIR_CHANGE + "0_AdaptiveIsomorphismInspectorFactory.java");
        fileNames.add(FILE_DIR_CHANGE + "0_BhandariKDisjointShortestPaths.java");
        fileNames.add(FILE_DIR_CHANGE + "0_DefaultDirectedWeightedEdgeTest.java");
        fileNames.add(FILE_DIR_CHANGE + "0_JGraphModelAdapter.java");
        fileNames.add(FILE_DIR_ASTPARSE + "coarseGrainedParsingExample.java");

        for (String fileName : fileNames) {

            File classFile = new File(fileName);
            List<MethodNode> collectedMethodNodes =
                    MethodInspector.getMethodNodesFromFile(classFile);

            Assert.assertEquals(
                    extractGivenCoarseNodesFromMethodBlockNodes(collectedMethodNodes, SingleAssignmentStatementNode.class),
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    classFile, SingleAssignmentStatementNode.class)
                            .stream()
                            .sorted(comparator)
                            .collect(Collectors.toList()));

            Assert.assertEquals(
                    extractGivenCoarseNodesFromMethodBlockNodes(collectedMethodNodes, SingleMethodCallSequenceNode.class),
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    classFile, SingleMethodCallSequenceNode.class)
                            .stream()
                            .sorted(comparator)
                            .collect(Collectors.toList()));

            Assert.assertEquals(
                    extractGivenCoarseNodesFromMethodBlockNodes(collectedMethodNodes, SingleMethodReturnStatementNode.class),
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    classFile, SingleMethodReturnStatementNode.class)
                            .stream()
                            .sorted(comparator)
                            .collect(Collectors.toList()));
        }
    }

    /*Converts a list of MethodNode to a list of nodes of a given class that are inside of MethodBlockNodes*/
    private List<SpecializedNode> extractGivenCoarseNodesFromMethodBlockNodes(
            List<MethodNode> methodNodeList, Class<? extends SpecializedNode> coarseNodeClass) {
        return methodNodeList.stream()
                .map(blockNode -> blockNode.getBodyNodesSequence())
                .flatMap(List::stream)
                .filter(node -> coarseNodeClass.isInstance(node))
                .map(node -> coarseNodeClass.cast(node))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /* Tests if there are no overlapping/nesting of nodes (e.g. if a method is called inside another method call,
    only the external method call should be extracted) */
    @Test
    public void testCoarseGrainedNodeNesting() {
        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FILE_DIR_CHANGE + "0_AdaptiveIsomorphismInspectorFactory.java");
        fileNames.add(FILE_DIR_CHANGE + "0_BhandariKDisjointShortestPaths.java");
        fileNames.add(FILE_DIR_CHANGE + "0_DefaultDirectedWeightedEdgeTest.java");
        fileNames.add(FILE_DIR_CHANGE + "0_JGraphModelAdapter.java");
        fileNames.add(FILE_DIR_ASTPARSE + "coarseGrainedParsingExample.java");

        for (String fileName : fileNames) {
            List<Range<Integer>> LOCRanges =
                    MethodInspector.getMethodNodesFromFile(new File(fileName)).stream()
                            .map(blockNode -> blockNode.getBodyNodesSequence())
                            .flatMap(List::stream)
                            .filter(
                                    node ->
                                            node instanceof SingleMethodReturnStatementNode
                                                    || node instanceof SingleAssignmentStatementNode
                                                    || node instanceof SingleMethodCallSequenceNode)
                            .map(node -> node.getLOCs())
                            .collect(Collectors.toList());
            Assert.assertFalse(checkLOCNesting(LOCRanges));
        }
    }

    private boolean checkLOCNesting(List<Range<Integer>> LOCRanges) {
        return LOCRanges.stream()
                .anyMatch(
                        loc1 ->
                                LOCRanges.stream()
                                        .anyMatch(
                                                loc2 ->
                                                        loc1.getMinimum() > loc2.getMinimum()
                                                                && loc1.getMaximum() < loc2.getMaximum()));
    }

    /**
     * Tests if the test files contain the expected number of SingleAssignmentStatementNodes
     */
    @Test
    public void testSingleAssignmentStatementNodeCount() {
        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected singleAssignmentStatementNode count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(8);
        nodeCount.add(28);
        nodeCount.add(8);
        nodeCount.add(21);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        Map<String, Integer> fileNamesWithNodeCount =
                IntStream.range(0, fileNames.size())
                        .boxed()
                        .collect(Collectors.toMap(fileNames::get, nodeCount::get));

        for (Map.Entry<String, Integer> entry : fileNamesWithNodeCount.entrySet()) {
            String fileName = entry.getKey();
            int expectedNodeCount = entry.getValue();
            List<SingleAssignmentStatementNode> collectedNodes =
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    new File(fileName), SingleAssignmentStatementNode.class)
                            .stream()
                            .map(node -> (SingleAssignmentStatementNode) node)
                            .collect(Collectors.toList());
            Assert.assertEquals(expectedNodeCount, collectedNodes.size());
        }
    }

    /**
     * Tests if the test files contain the expected number of SingleMethodReturnStatementNodes
     */
    @Test
    public void testSingleMethodReturnStatementNodeCount() {
        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected singleAssignmentStatementNode count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(7);
        nodeCount.add(5);
        nodeCount.add(0);
        nodeCount.add(6);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        Map<String, Integer> fileNamesWithNodeCount =
                IntStream.range(0, fileNames.size())
                        .boxed()
                        .collect(Collectors.toMap(fileNames::get, nodeCount::get));

        for (Map.Entry<String, Integer> entry : fileNamesWithNodeCount.entrySet()) {
            String fileName = entry.getKey();
            int expectedNodeCount = entry.getValue();
            List<SingleMethodReturnStatementNode> collectedNodes =
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    new File(fileName), SingleMethodReturnStatementNode.class)
                            .stream()
                            .map(node -> (SingleMethodReturnStatementNode) node)
                            .collect(Collectors.toList());
            Assert.assertEquals(expectedNodeCount, collectedNodes.size());
        }
    }

    /**
     * Tests if the test files contain the expected number of SingleMethodCallSequenceNodes
     */
    @Test
    public void testSingleMethodCallSequenceNodeCount() {
        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected singleAssignmentStatementNode count for each test file
        List<Integer> nodeCount = new ArrayList<Integer>();
        nodeCount.add(2);
        nodeCount.add(16);
        nodeCount.add(10);
        nodeCount.add(31);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        Map<String, Integer> fileNamesWithNodeCount =
                IntStream.range(0, fileNames.size())
                        .boxed()
                        .collect(Collectors.toMap(fileNames::get, nodeCount::get));

        for (Map.Entry<String, Integer> entry : fileNamesWithNodeCount.entrySet()) {
            String fileName = entry.getKey();
            int expectedNodeCount = entry.getValue();
            List<SingleMethodCallSequenceNode> collectedNodes =
                    MethodInspector.getGivenSpecializedNodesFromFile(
                                    new File(fileName), SingleMethodCallSequenceNode.class)
                            .stream()
                            .map(node -> (SingleMethodCallSequenceNode) node)
                            .collect(Collectors.toList());
            Assert.assertEquals(expectedNodeCount, collectedNodes.size());
        }
    }
}
