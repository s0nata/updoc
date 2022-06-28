package parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import org.junit.Assert;
import org.junit.Test;
import parser.nodes.OtherNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodNodesSequenceExtractionTest {

    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    private static final String FILE_DIR_ASTPARSE = "src/test/resources/ast-parsing/";

    /**
     * Tests if there are no AST nodes deeper than any node of class maxDepthASTNodeClass that are
     * extracted in the methodNodesSequence
     */
    @Test
    public void maxDepthASTNodeClassTest() {
        String fileName = FILE_DIR_ASTPARSE + "differentStatements.java";
        Class<? extends Node> maxDepthASTNodeClass = ExpressionStmt.class;

        List<MethodNode> collectedNodes =
                MethodInspector.getMethodNodesFromFile(new File(fileName), maxDepthASTNodeClass);

        // In the file there is only one block node, therefore collectedNodes.get(0) is used
        List<Node> ASTNodes =
                collectedNodes.get(0).getBodyNodesSequence().stream()
                        .filter(abstract_node -> abstract_node instanceof OtherNode)
                        .map(abstract_node -> (OtherNode) abstract_node)
                        .map(othernode -> othernode.getASTNode())
                        .collect(Collectors.toList());

        // No children of nodes of class maxDepthASTNodeClass should be in the ASTNodes list
        List<Node> ASTNodesChildren =
                ASTNodes.stream()
                        .filter(node -> maxDepthASTNodeClass.isInstance(node))
                        .map(node -> node.getChildNodes())
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

        boolean areChildrenPresent =
                ASTNodes.stream().anyMatch(n1 -> ASTNodesChildren.stream().anyMatch(n2 -> n1 == n2));

        Assert.assertFalse(areChildrenPresent);
    }

    //Checks that there is no null element in the node sequence inside methodBlockNode
    @Test
    public void testNoNullNodeInNodeSequence() {
        // test files
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        fileNames =
                fileNames.stream()
                        .map(filename -> FILE_DIR_CHANGE + "0_" + filename + ".java")
                        .collect(Collectors.toList());

        fileNames.add(FILE_DIR_ASTPARSE + "differentStatements.java");
        fileNames.add(FILE_DIR_ASTPARSE + "coarseGrainedParsingExample.java");

        fileNames.forEach(
                fileName ->
                        MethodInspector.getMethodNodesFromFile(new File(fileName))
                                .forEach(
                                        mb ->
                                                Assert.assertTrue(
                                                        mb.getBodyNodesSequence().stream().allMatch(node -> node != null))));
    }
}
