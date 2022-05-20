package mapper;

import parser.MethodNode;

import java.util.ArrayList;

/**
 * This class converts basic method body AST nodes (as given by the Javaparser)
 * into our {@code mapping.ASTNode} representation and stores them in a list.
 */
public class MethodBody {

    private final ArrayList<ASTNode> bodyNodes;

    private int nodesNumber;

    public MethodBody(MethodNode mn) {

        nodesNumber = 0;
        bodyNodes = new ArrayList<>();

        // add signature
        bodyNodes.add(new ASTNode(mn.getMethodSignature(), nodesNumber));
        nodesNumber++;

    }

    public ArrayList<ASTNode> getBodyNodes() {
        return bodyNodes;
    }
}
