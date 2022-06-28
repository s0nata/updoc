package parser.nodes;

import com.github.javaparser.ast.Node;
import mapper.Identifier;
import mapper.WordBag;
import org.apache.commons.lang3.Range;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class to represent non-specific AST nodes
 */
public class OtherNode implements AbstractNode {

    private final Range<Integer> LOC;

    private final Optional<List<InlineComment>> comments;

    private final Node astNode;

    public OtherNode(Node astNode, Optional<List<InlineComment>> comments, int lBegin, int lEnd) {
        this.LOC = Range.between(lBegin, lEnd);
        this.comments = comments;
        this.astNode = astNode;
    }

    @Override
    public Range<Integer> getLOCs() {
        return this.LOC;
    }

    public Optional<List<InlineComment>> getComments() {
        return this.comments;
    }

    @Override
    public Node getASTNode() {
        return this.astNode;
    }

    @Override
    public String toString() {

        String out = "\n";
        out += "[" + this.getClass() + "] ";
        out += "\n[LOC: " + this.LOC.toString() + "]";
        out += "\n[AST node class: " + astNode.getClass() + "]";
        out += "\n[Extracted source code:]";
        out += "\n" + astNode;
        return out;
    }

    @Override
    public WordBag toIdentifierList(boolean synonyms) {
        WordBag identifierList = toIdentifierList();
        if (!synonyms) {
            return identifierList;
        } else {
            Map<String, List<String>> synonymList = SpecializedNode.getSynonymLists();
            identifierList.toList().stream()
                    .filter(word -> synonymList.containsKey(word))
                    .map(word -> synonymList.get(word))
                    .flatMap(List::stream)
                    .forEach(word -> identifierList.add(word));
            return identifierList;
        }
    }

    @Override
    public WordBag toExpandedIdentifierList(boolean synonyms) {
        WordBag expandedIdentifierList = toExpandedIdentifierList();
        if (!synonyms) {
            return expandedIdentifierList;
        } else {
            Map<String, List<String>> synonymList = SpecializedNode.getSynonymLists();
            expandedIdentifierList.toList().stream()
                    .filter(word -> synonymList.containsKey(word))
                    .map(word -> synonymList.get(word))
                    .flatMap(List::stream)
                    .forEach(word -> expandedIdentifierList.add(word));
        }
        return expandedIdentifierList;
    }

    @Override
    public WordBag toExpandedIdentifierList() {
        return new WordBag(
                toIdentifierList().toList().stream()
                        .map(
                                identifier ->
                                        new Identifier(identifier, Identifier.KindOfID.GENERIC).getSplitName())
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
    }

    @Override
    public WordBag toIdentifierList() {
        String raw_code = astNode.toString();

        // remove comments that could be contained in ast node
        if (comments.isPresent()) {
            for (InlineComment comment : comments.get()) {
                raw_code = raw_code.replace(comment.toString(), "");
            }
        }

        // extract unique keywords delimited by non-alphanumerical symbols
        List<String> identifierList =
                List.of(raw_code.split("\\W+")).stream().distinct().collect(Collectors.toList());

        return new WordBag(identifierList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherNode)) return false;
        OtherNode otherNode = (OtherNode) o;
        return Objects.equals(LOC, otherNode.LOC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(LOC);
    }
}
