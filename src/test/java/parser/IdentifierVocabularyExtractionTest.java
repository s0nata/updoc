package parser;

import org.junit.Assert;
import org.junit.Test;
import parser.nodes.AbstractNode;
import parser.nodes.IfNode;
import parser.nodes.SpecializedNode;
import parser.nodes.TryCatchNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IdentifierVocabularyExtractionTest {

    private static final String FILE_DIR = "src/test/resources/ast-parsing/";

    // Test that identifiers does not contain comments (all comments in example files contain the word
    // "comment")
    @Test
    public void testNoCommentsInIdentifiers() {
        List<String> fileNames = new ArrayList<String>();

        fileNames.add(FILE_DIR + "differentStatements.java");
        fileNames.add(FILE_DIR + "coarseGrainedParsingExample.java");

        fileNames.forEach(
                fileName ->
                        Assert.assertFalse(
                                extractGivenSpecializedNodesFromFile(fileName, SpecializedNode.class).stream()
                                        .anyMatch(
                                                node ->
                                                        node.toIdentifierList().toList().stream()
                                                                .anyMatch(identifier -> identifier.contains("comment")))));
    }

    // Tests the identifier exclusion
    @Test
    public void testIdentifierExclusion() {
        String fileName = FILE_DIR + "differentStatements.java";
        List<SpecializedNode> allTryCatchNodes =
                extractGivenSpecializedNodesFromFile(fileName, TryCatchNode.class);
        testGivenIdentifierExclusion(allTryCatchNodes, "catch");
    }

    // Tests the identifier substitution
    @Test
    public void testIdentifierSubstitution() {
        String fileName = FILE_DIR + "differentStatements.java";
        List<SpecializedNode> allIfNodes =
                MethodInspector.getGivenSpecializedNodesFromFile(new File(fileName), IfNode.class);
        testGivenIdentifierSubstitution(allIfNodes, "else -> or");
    }

    private List<SpecializedNode> extractGivenSpecializedNodesFromFile(
            String fileName, Class<? extends SpecializedNode> specializedNodeClass) {
        return MethodInspector.getMethodNodesFromFile(new File(fileName)).stream()
                .map(node -> node.getBodyNodesSequence())
                .flatMap(List::stream)
                .filter(node -> specializedNodeClass.isInstance(node))
                .map(node -> specializedNodeClass.cast(node))
                .collect(Collectors.toList());
    }

    private void testGivenIdentifierExclusion(List<? extends SpecializedNode> nodes, String exclude) {

        // Tests that the raw code contains keyword
        Assert.assertTrue(
                nodes.stream()
                        .map(node -> node.getASTNode().toString())
                        .allMatch(code -> code.contains(exclude)));

        // Tests that the identifier list does not contain keyword
        Assert.assertFalse(nodes.stream().anyMatch(node -> node.toIdentifierList().contains(exclude)));
    }

    private void testGivenIdentifierSubstitution(
            List<? extends SpecializedNode> nodes, String substitution) {
        String beforeSubstitution = substitution.split("->")[0].trim();
        String afterSubstitution = substitution.split("->")[1].trim();

        // Tests that the raw code contains original keyword
        Assert.assertTrue(
                nodes.stream()
                        .map(node -> node.getASTNode().toString())
                        .allMatch(code -> code.contains(beforeSubstitution)));

        // Tests that the identifier list does not contain original keyword
        Assert.assertFalse(
                nodes.stream().anyMatch(node -> node.toIdentifierList().contains(beforeSubstitution)));

        // Tests that the identifier list code contains substituted keyword
        Assert.assertTrue(
                nodes.stream().allMatch(node -> node.toIdentifierList().contains(afterSubstitution)));
    }

    // Tests the synonym addition
    @Test
    public void testSynonymAddition() {
        List<String> fileNames = new ArrayList<String>();

        fileNames.add(FILE_DIR + "differentStatements.java");

        Map<String, List<String>> synonymMap = new HashMap<String, List<String>>();
        synonymMap.put(
                "add",
                List.of(
                        "register",
                        "create",
                        "generate",
                        "set",
                        "apply",
                        "check",
                        "throw",
                        "do",
                        "put",
                        "attach",
                        "record",
                        "append",
                        "plus"));

        for (String fileName : fileNames) {
            List<MethodNode> collectedNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));

            List<AbstractNode> nodes =
                    collectedNodes.stream()
                            .map(node -> node.getBodyNodesSequence())
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

            for (AbstractNode node : nodes) {
                List<String> identifierList = node.toIdentifierList().toList();
                List<String> identifierListWithSynonyms = node.toIdentifierList(true).toList();
                for (Map.Entry<String, List<String>> entry : synonymMap.entrySet()) {
                    String word = entry.getKey();
                    List<String> synonyms = entry.getValue();
                    if (identifierList.contains(word)) {
                        synonyms.forEach(
                                synonym -> Assert.assertTrue(identifierListWithSynonyms.contains(synonym)));
                    }
                }
            }
        }
    }

    /* Tests if the toIdentifierList method for MethodNode is equivalent to the union of the
    toIdentifierList methods of its methodNodeSequence*/
    @Test
    public void testMethodBlockNodeIdentifierExtraction() {
        List<String> fileNames = new ArrayList<String>();

        fileNames.add(FILE_DIR + "differentStatements.java");
        fileNames.add(FILE_DIR + "coarseGrainedParsingExample.java");

        for (String fileName : fileNames) {
            List<MethodNode> collectedBlockNodesNodes =
                    MethodInspector.getMethodNodesFromFile(new File(fileName));

            for (MethodNode mb : collectedBlockNodesNodes) {
                List<String> methodBlockIdentifierList = mb.toIdentifierList().toList();
                List<String> methodBlockIdentifierListWithSynonyms = mb.toIdentifierList(true).toList();
                List<AbstractNode> nodeSequence = mb.getBodyNodesSequence();
                for (AbstractNode node : nodeSequence) {
                    Assert.assertTrue(
                            node.toIdentifierList().toList().stream()
                                    .allMatch(identifier -> methodBlockIdentifierList.contains(identifier)));
                    Assert.assertTrue(
                            node.toIdentifierList(true).toList().stream()
                                    .allMatch(
                                            identifier -> methodBlockIdentifierListWithSynonyms.contains(identifier)));
                }
            }
        }
    }
}
