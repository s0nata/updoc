package mapper;

import mapper.Identifier.KindOfID;
import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ASTNodeTest {

    static final String TEST_FPATH = "mapping/";
    static final String TEST_FNAME = "AdaptiveIsomorphismInspectorFactory.java";

    @Test
    public void constructorTest() {

        MethodNode testMethod = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FNAME);

        ASTNode testNode = new ASTNode(testMethod.getMethodSignature(), 0);

        LinkedHashSet<Identifier> expectedIdentifiers = new LinkedHashSet<Identifier>();
        expectedIdentifiers.add(new Identifier("assertUnsupportedGraphTypes", KindOfID.METHOD_NAME));
        expectedIdentifiers.add(new Identifier("graph1", KindOfID.VAR_NAME));
        expectedIdentifiers.add(new Identifier("Graph", KindOfID.TYPE_NAME));
        expectedIdentifiers.add(new Identifier("graph2", KindOfID.VAR_NAME));
        expectedIdentifiers.add(new Identifier("void", KindOfID.TYPE_NAME));
        expectedIdentifiers.add(new Identifier("IllegalArgumentException", KindOfID.TYPE_NAME));

        LinkedHashSet<Identifier> actualIdentifiers = testNode.getIdentifierList();

        assertEquals(expectedIdentifiers, actualIdentifiers);
    }

    @Test
    public void toBagOfWordsTest() {

        MethodNode testMethod = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FNAME);

        ASTNode testNode = new ASTNode(testMethod.getMethodSignature(), 0);

        WordBag expectedBoW =
                new WordBag(List.of(
                        "void",
                        "assert",
                        "unsupported",
                        "graph",
                        "graph",
                        "graph",
                        "graph",
                        "type",
                        "illegal",
                        "argument",
                        "exception"));

        WordBag actualBoW = testNode.toBagOfWords();

        assertEquals(expectedBoW.size(), actualBoW.size());
//    assertEquals(expectedBoW.raw(), actualBoW.raw());

    }

}
