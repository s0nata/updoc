package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import static org.junit.Assert.assertEquals;

public class MehtodBodyTest {
    static final String TEST_FPATH = "src/test/resources/paper-example/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory_c.java";

    @Test
    public void shouldGetMethodSignatureNode() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);

        MethodBody mb = new MethodBody(mn);

        assertEquals(1, mb.getBodyNodes().size());
    }
}
