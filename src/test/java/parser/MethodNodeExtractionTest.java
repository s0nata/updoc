package parser;

import org.apache.commons.lang3.Range;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MethodNodeExtractionTest {
    private static final String FILE_DIR = "src/test/resources/change/";

    /**
     * Tests if the test files contain the expected number of MethodNodes
     */
    @Test
    public void testMethodNodeCount() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected method block count for each test file
        List<Integer> methodBlockCount = new ArrayList<Integer>();
        methodBlockCount.add(8);
        methodBlockCount.add(8);
        methodBlockCount.add(4);
        methodBlockCount.add(19);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        Map<String, Integer> fileNamesWithBlockCount =
                IntStream.range(0, fileNames.size())
                        .boxed()
                        .collect(Collectors.toMap(fileNames::get, methodBlockCount::get));

        for (Map.Entry<String, Integer> entry : fileNamesWithBlockCount.entrySet()) {
            String fileName = entry.getKey();
            int expectedBlockCount = entry.getValue();
            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));
            Assert.assertEquals(expectedBlockCount, collectedNodes.size());
        }
    }

    /**
     * Tests if the extracted MethodBlockNodes are unique
     */
    @Test
    public void testMethodBlockNodeUniqueness() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        Set<Range<Integer>> uniqueLOCRanges = new HashSet<Range<Integer>>();
        for (String fileName : fileNames) {
            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));
            uniqueLOCRanges =
                    collectedNodes.stream().map(node -> node.getLOCs()).collect(Collectors.toSet());
            Assert.assertEquals(uniqueLOCRanges.size(), collectedNodes.size());
        }
    }

    /**
     * Tests if the extracted MethodBlockNodes are not nested
     */
    @Test
    public void testMethodBlockNodeNesting() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        List<Range<Integer>> LOCRanges = new ArrayList<Range<Integer>>();
        // Check if the checkLOCNesting function detects nesting
        LOCRanges.add(Range.between(100, 200));
        LOCRanges.add(Range.between(110, 190));

        Assert.assertTrue(checkLOCNesting(LOCRanges));

        for (String fileName : fileNames) {
            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));
            LOCRanges = collectedNodes.stream().map(node -> node.getLOCs()).collect(Collectors.toList());

            // No LOCs should be nested
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
     * Tests if the extracted MethodBlockNodes contain the expected number of line comments
     */
    @Test
    public void testMethodBlockNodeCommentCount() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // expected comment count inside method blocks for each test file
        // each line starting with "//" inside the method body counts as one comment
        List<Integer> methodBlockCommentCount = new ArrayList<Integer>();
        methodBlockCommentCount.add(0);
        methodBlockCommentCount.add(10);
        methodBlockCommentCount.add(0);
        methodBlockCommentCount.add(0);

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        Map<String, Integer> fileNamesWithBlockCount =
                IntStream.range(0, fileNames.size())
                        .boxed()
                        .collect(Collectors.toMap(fileNames::get, methodBlockCommentCount::get));

        for (Map.Entry<String, Integer> entry : fileNamesWithBlockCount.entrySet()) {
            String fileName = entry.getKey();
            int expectedCommentCount = entry.getValue();

            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));
            int actualCommentCount =
                    collectedNodes.stream()
                            .filter(node -> node.getComments().isPresent())
                            .mapToInt(node -> node.getComments().get().size())
                            .sum();

            Assert.assertEquals(expectedCommentCount, actualCommentCount);
        }
    }

    @Test
    public void testCodeCoverageValid() {

        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        for (String fileName : fileNames) {

            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));

            collectedNodes.forEach(node -> Assert.assertTrue(node.getCodeCoverage() <= 1.0));
        }
    }
}
