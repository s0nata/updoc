package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import static org.junit.Assert.assertEquals;

public class MehtodBodyTest {
    static final String TEST_FPATH = "mapping/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory.java";

    @Test
    public void shouldGetMethodSignatureNode() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);

        MethodBody mb = new MethodBody(mn);

        assertEquals(1, mb.getBodyNodes().size());
    }
}
