package mapper;

import parser.StructuredSignature;
import parser.nodes.AbstractNode;

import java.util.LinkedHashSet;

/**
 * This class represents an AST node of a method body as a collection of identifiers.
 *
 * <p>Currently only method signature nodes are supported.
 */
public class ASTNode {

    // all supported AST node kinds
    public enum NodeType {
        SIGNATURE,
        METHODBLOCK
    }

    Integer nodeID;

    NodeType nodeType;

    //  Range<Integer> LOCs;

    LinkedHashSet<Identifier> identifiers;

    AbstractNode node;

    StructuredSignature methodSignature;

    /**
     * Construct an {@code mapping.ASTNode} from a {@code parsing.StructuredSignature}
     *
     * @param methodSignature method signature to be converted into as ASTNode representation
     */
    public ASTNode(StructuredSignature methodSignature, int id) {

        this.nodeID = id;

        this.nodeType = NodeType.SIGNATURE;

        this.methodSignature = methodSignature;

        this.identifiers = new LinkedHashSet<>();

        // Method name:
        identifiers.add(methodSignature.getMethodName());
        // Parameters:
        for (java.util.Map.Entry<mapper.Identifier, mapper.Identifier> entry :
                methodSignature.getParametersTyped().entrySet()) {
            identifiers.add(entry.getKey());
            identifiers.add(entry.getValue());
        }
        // Returns:
        identifiers.add(methodSignature.getReturnTypeIdentifier());
        // Exceptions:
        identifiers.addAll(methodSignature.getExceptionsAsIdentifiers());
    }

    public ASTNode(AbstractNode node, int id) {
        this.nodeID = id;

        this.nodeType = NodeType.METHODBLOCK;

        this.identifiers = new LinkedHashSet<>();

        this.node = node;
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public StructuredSignature getSignature() {
        return this.methodSignature;
    }

    public AbstractNode getNode() {
        return this.node;
    }

    public Integer getNodeId() {
        return this.nodeID;
    }

    public LinkedHashSet<Identifier> getIdentifierList() {
        return this.identifiers;
    }

    public WordBag toBagOfWords() {
        if (this.nodeType == NodeType.METHODBLOCK) {
            return this.node.toExpandedIdentifierList();
        }

        WordBag bow = new WordBag();

        for (Identifier id : this.identifiers) {
            bow.addAll(id.split());
        }

        return bow;
    }

    @Override
    public String toString() {
        return "[" + this.nodeID + "] "
                + this.nodeType.toString() + "\n\t"
                + toBagOfWords();
    }
}
