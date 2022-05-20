package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;
import parser.StructuredComment;

import static org.junit.Assert.assertEquals;

public class MethodCommentTest {

    static final String TEST_FPATH = "mapping/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory.java";

    @Test
    public void shouldGetDescriptiveSentences() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);
        //MethodComment mc = new MethodComment(mn.getDocComment());

        StructuredComment structuredComment = mn.getDocComment();
        assertEquals(2, structuredComment.getDescriptiveSentences().size());
    }

    @Test
    public void shouldGetTaggedSentences() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);
        // MethodComment mc = new MethodComment(mn.getDocComment());

        StructuredComment structuredComment = mn.getDocComment();
        assertEquals(3, structuredComment.getTaggedSentences().size());
    }

}
