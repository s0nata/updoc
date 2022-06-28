package mapper;

import parser.MethodNode;
import parser.StructuredSignature;
import parser.nodes.AbstractNode;

import java.util.ArrayList;

/**
 * This class converts basic method body AST nodes (as given by the Javaparser) into our {@code
 * mapping.ASTNode} representation and stores them in a list.
 */
public class MethodBody {

    private final ArrayList<ASTNode> bodyNodes;

    private final String originalMethodName;

    private final StructuredSignature originalSignature;


    public MethodBody(MethodNode mn) {
        this.originalMethodName = mn.getMethodName();
        this.originalSignature = mn.getMethodStructuredSignature();

        this.bodyNodes = new ArrayList<>();

        int signatureHashCode = mn.getSignatureNode().hashCode();

        // signature is also a part of full method body
        bodyNodes.add(new ASTNode(mn.getMethodStructuredSignature(), signatureHashCode));

        for (AbstractNode node : mn.getMethodNodesSequence(false)) {
            bodyNodes.add(new ASTNode(node, node.hashCode()));
        }
    }

    public ArrayList<ASTNode> getBodyNodes() {
        return bodyNodes;
    }

    public String getOriginalMethodName() {
        return originalMethodName;
    }

    public StructuredSignature getOriginalSignature() {
        return originalSignature;
    }

    @Override
    public String toString() {
        String methodBody = "";

        for (ASTNode bodyNode : this.bodyNodes) {
            methodBody += bodyNode.toString() + "\n";
        }

        return methodBody;
    }
}
