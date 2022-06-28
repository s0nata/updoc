package parser.nodes;

import com.github.javaparser.ast.Node;
import mapper.WordBag;
import org.apache.commons.lang3.Range;

/**
 * Minimal API provided for the upDoc's intermediate representation of an AST node
 *
 * @see parser.nodes.SpecializedNode
 * @see parser.nodes.OtherNode
 */
public interface AbstractNode {

    Range<Integer> getLOCs();

    Node getASTNode();

    WordBag toIdentifierList();

    WordBag toExpandedIdentifierList();

    WordBag toIdentifierList(boolean synonyms);

    WordBag toExpandedIdentifierList(boolean synonyms);
}
