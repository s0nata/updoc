package changextractor;

/**
 * A class that contains various information about an AST node.
 */
public class NodeInfo {

    private final int rowBegin;

    private final int rowEnd;

    private final String treeNodeTypeName;

    private final String treeNodePrettySource;

    public NodeInfo(int lBegin, int lEnd, String typeName, String source) {
        rowBegin = lBegin;
        rowEnd = lEnd;

        treeNodeTypeName = typeName;
        treeNodePrettySource = source;
    }

    public int getRowBegin() {
        return rowBegin;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public String getTreeNodeTypeName() {
        return treeNodeTypeName;
    }

    public String getTreeNodePrettySource() {
        return treeNodePrettySource;
    }

    @Override
    public String toString() {
        return "> node: " + treeNodeTypeName
                + "\n> rows: " + rowBegin + ":" + rowEnd
                + "\n> body:\n" + treeNodePrettySource + "\n";
    }
}
