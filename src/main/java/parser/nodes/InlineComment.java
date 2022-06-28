package parser.nodes;

public class InlineComment {

    private final String originalString;

    public InlineComment(String originalText) {
        this.originalString = originalText;
    }

    @Override
    public String toString() {
        return this.originalString;
    }

}
