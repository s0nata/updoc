package parser.nodes;

import com.github.javaparser.ast.Node;
import mapper.WordBag;
import org.apache.commons.lang3.Range;

import java.util.Objects;

/**
 * A class that contains fine-grained internal representation of a method signature as an AST node.
 * <p>
 * Relation to @{class StructuredSignature}
 */
public class SignatureNode implements AbstractNode {

    protected Range<Integer> LOC;

    private final Node astNode;


    public SignatureNode(Node astNode, Range<Integer> LOC) {

        this.astNode = astNode;

        this.LOC = LOC;
    }


    @Override
    public Range<Integer> getLOCs() {
        return this.LOC;
    }

    @Override
    public Node getASTNode() {
        return this.astNode;
    }

    @Override
    public WordBag toIdentifierList() {
        return null; // TODO
    }

    @Override
    public WordBag toExpandedIdentifierList() {
        return null; // TODO
    }

    @Override
    public WordBag toIdentifierList(boolean synonyms) {
        return null; // TODO
    }

    @Override
    public WordBag toExpandedIdentifierList(boolean synonyms) {
        return null; // TODO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignatureNode)) return false;
        SignatureNode that = (SignatureNode) o;
        return Objects.equals(LOC, that.LOC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(LOC);
    }
}
