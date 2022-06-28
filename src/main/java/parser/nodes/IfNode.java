package parser.nodes;

import com.github.javaparser.ast.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IfNode extends SpecializedNode {

    private static final List<String> expectedVocabulary = Arrays.asList("if", "else");

    public IfNode(Node astNode, Optional<List<InlineComment>> comments, int lBegin, int lEnd) {
        super(astNode, comments, lBegin, lEnd);
    }

    public static List<String> getExpectedVocabulary() {
        return expectedVocabulary;
    }
}
