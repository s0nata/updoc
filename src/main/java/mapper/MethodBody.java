package mapper;

import parser.MethodNode;
import parser.StructuredSignature;

import java.util.ArrayList;

/**
 * This class converts basic method body AST nodes (as given by the Javaparser)
 * into our {@code mapping.ASTNode} representation and stores them in a list.
 */
public class MethodBody {

    private final ArrayList<ASTNode> bodyNodes;

    private final String originalMethodName;


    private final StructuredSignature originalSignature;

    private int nodesNumber;

    public MethodBody(MethodNode mn) {
        this.originalMethodName = mn.getMethodName();
        this.originalSignature = mn.getMethodSignature();

        this.nodesNumber = 0;
        this.bodyNodes = new ArrayList<>();

        // add signature
        bodyNodes.add(new ASTNode(mn.getMethodSignature(), nodesNumber));
        nodesNumber++;

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
}
