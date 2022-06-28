package parser.nodes;

import com.github.javaparser.ast.Node;

import java.util.List;
import java.util.Optional;

public class TryCatchNode extends SpecializedNode {
    public TryCatchNode(Node astNode, Optional<List<InlineComment>> comments, int lBegin, int lEnd) {
        super(astNode, comments, lBegin, lEnd);
    }
}
