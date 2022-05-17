package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;
import parser.StructuredComment;

import static org.junit.Assert.assertEquals;

public class MethodCommentTest {

    static final String TEST_FPATH = "src/test/resources/paper-example/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory_c.java";

    @Test
    public void shouldGetDescriptiveSentences() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);

        StructuredComment structuredComment = mn.getDocComment();
        assertEquals(2, structuredComment.getDescriptiveSentences().size());
    }

    @Test
    public void shouldGetTaggedSentences() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);

        StructuredComment structuredComment = mn.getDocComment();
        assertEquals(3, structuredComment.getTaggedSentences().size());
    }

}
