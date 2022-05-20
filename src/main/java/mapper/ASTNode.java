package mapper;

import parser.StructuredSignature;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * This class represents an AST node of a method body as a collection of identifiers.
 *
 * <p>
 * Currently, only method signature nodes are supported.
 */
public class ASTNode {

    // all supported AST node kinds
    public enum NodeType {
        SIGNATURE
    }

    Integer nodeID;

    NodeType nodeType;

//  Range<Integer> LOCs;

    LinkedHashSet<Identifier> identifiers;

    /**
     * Construct an {@code mapping.ASTNode} from a {@code parsing.StructuredSignature}
     *
     * @param methodSignature method signature to be converted into as ASTNode representation
     */
    public ASTNode(StructuredSignature methodSignature, Integer id) {

        this.nodeID = id;

        this.nodeType = NodeType.SIGNATURE;

        this.identifiers = new LinkedHashSet<>();

        // Method name:
        identifiers.add(methodSignature.getMethodName());
        // Parameters:
        // FIXME here, multiple words that look exactly the same may be all added to the identifier sets.
        // FIXME It seems to depend on a not 100% fair strategy: for example, graph1 and graph2 will result
        // FIXME both in "graph" but both will be added since the original names were indeed different.
        // FIXME But if they both have same type Graph, the word "graph" will be in this case added only once.
        // FIXME -->  Verify if this is the expected behavior <---
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

    public Integer getNodeId() {
        return this.nodeID;
    }

    public LinkedHashSet<Identifier> getIdentifierList() {
        return this.identifiers;
    }

    public WordBag toBagOfWords() {

        WordBag bow = new WordBag();

        for (Identifier id : this.identifiers) {
            bow.addAll(id.split());
        }

        return bow;

    }

    public WordBag toBagOfWords(CommentSentence.CommentPart part) {

        WordBag bow = new WordBag();

        for (Identifier id : this.identifiers) {
            ArrayList<String> IDs = id.split();
            bow.addAll(IDs);

            if (part != null &&
                    part.equals(CommentSentence.CommentPart.RETURN)) {
                if (id.getKindOfID().equals(Identifier.KindOfID.TYPE_NAME)) {
                    bow.addAll(IDs);
                    bow.addAll(IDs);
                }
//            else if(id.getKindOfID().equals(Identifier.KindOfID.METHOD_NAME)) {
//              bow.addAll(IDs);
//            }
            }
        }

        return bow;

    }


}
