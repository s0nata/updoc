package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MethodBodyTest {
    static final String TEST_FPATH = "src/test/resources/mapping/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory_c.java";

    @Test
    public void shouldGetMethodSignatureNode() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);

        MethodBody mb = new MethodBody(mn);

        // filter out signature node - should be only one
        List<ASTNode> signatureNodes = new ArrayList<>();
        for(ASTNode node: mb.getBodyNodes()) {
            if (node.getNodeType().equals(ASTNode.NodeType.SIGNATURE)) {
                signatureNodes.add(node);
            }
        }

        assertEquals(1, signatureNodes.size());
    }
}
