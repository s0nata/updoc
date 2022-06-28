package parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import mapper.Identifier;
import mapper.WordBag;
import org.apache.commons.lang3.Range;
import parser.nodes.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Method IR produced by {@code MethodInspector}
 * // FIXME I feel like this class should contain all the children nodes
 * // FIXME with the respective IDs (hashcodes computed on their LOCs).
 * // FIXME While now this hashcode is just a hashcode on the whole
 * // FIXME method body LOCs -- not fine-grained at all.
 */
public class MethodNode implements AbstractNode {

    // signature

    private final String methodName;

    private final StructuredSignature methodStructuredSignature;

    private final StructuredComment methodDocComment;

    // body

    private final List<AbstractNode> bodyNodesSequence;

    private final Optional<List<InlineComment>> comments;

    //meta

    private final SignatureNode signatureNode;

    private final Range<Integer> LOC;  // part of unique identifier of a node

    private final String filePath; // part of unique identifier of a node

    private final Node astNode;

    // demo

    /* code coverage by Nodes other than OtherNode in methodNodesSequence of the total length of source code
    inside methodBlocknode between 0 and 1*/
    private final double codeCoverage;

// ---------------------------------------------------------------------------------------------- //

    public MethodNode(
            Node astNode,
            //
            String methodName,
            NodeList<Parameter> params,
            Type returnType,
            NodeList<ReferenceType> thrownExceptions,
            Optional<JavadocComment> javadocComment,
            //
            SignatureNode signatureNode,
            //
            List<AbstractNode> bodyNodesSequence,
            Optional<List<InlineComment>> comments,
            //
            Range<Integer> fullMethodLines,
            String sourceFilePath) {


        this.astNode = astNode;

        this.methodName = methodName;
        this.methodStructuredSignature =
                new StructuredSignature(methodName, params, returnType, thrownExceptions);
        this.signatureNode = signatureNode;
        this.methodDocComment = new StructuredComment(javadocComment);

        this.bodyNodesSequence = bodyNodesSequence;
        this.comments = comments;
        // by passing the comments to the nodes in the methodNodeSequence, those nodes can ignore the
        // comments in the code vocabulary
        this.bodyNodesSequence.stream()
                .filter(node -> node instanceof SpecializedNode)
                .map(node -> (SpecializedNode) node)
                .forEach(node -> node.addPotentialOrphanComments(comments));

        this.LOC = fullMethodLines;
        this.filePath = sourceFilePath;

        this.codeCoverage =
                (double)
                        this.getMethodNodesSequence(false).stream()
                                .map(node -> createNodeForCoverageCount(node))
                                .map(node -> node.getASTNode().toString().replaceAll("\\s+|\\{|\\}", "").trim())
                                .mapToInt(s -> s.length())
                                .sum()
                        / this.getASTNode().toString().replaceAll("\\s+|\\{|\\}", "").trim().length();
    }


    public double getCodeCoverage() {
        return this.codeCoverage;
    }

    public SignatureNode getSignatureNode() {
        return signatureNode;
    }

    @Override
    public Node getASTNode() {
        return this.astNode;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public List<AbstractNode> getBodyNodesSequence() {
        return this.bodyNodesSequence;
    }

    public List<AbstractNode> getMethodNodesSequence(boolean otherNodes) {
        if (otherNodes) {
            return this.bodyNodesSequence;
        } else {
            return this.bodyNodesSequence.stream()
                    .filter(node -> !(node instanceof OtherNode))
                    .collect(Collectors.toList());
        }
    }

    public Optional<List<InlineComment>> getComments() {
        return this.comments;
    }

    public StructuredComment getDocComment() {
        return methodDocComment;
    }

    public StructuredSignature getMethodStructuredSignature() {
        return this.methodStructuredSignature;
    }

    @Override
    public Range<Integer> getLOCs() {
        return this.LOC;
    }

    @Override
    public String toString() {
        String out = "[MethodNode content:]\n";
        out +=
                getMethodNodesSequence(false).stream()
                        .map(node -> node.toString())
                        .reduce("", (s1, s2) -> s1 += s2 + "\n");
        return out;
    }

    @Override
    public WordBag toIdentifierList(boolean synonyms) {
        return new WordBag(
                this.bodyNodesSequence.stream()
                        .map(node -> node.toIdentifierList(synonyms).toList())
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
    }

    @Override
    public WordBag toExpandedIdentifierList(boolean synonyms) {
        return new WordBag(
                this.bodyNodesSequence.stream()
                        .map(node -> node.toExpandedIdentifierList(synonyms).toList())
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
    }

    @Override
    public WordBag toIdentifierList() {
        return new WordBag(
                this.bodyNodesSequence.stream()
                        .map(node -> node.toIdentifierList().toList())
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
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

//----------------------------------- COVERAGE DEMO METHODS --------------------------------------//

    /* For nodes that contain blocks, e.g. IfNode, remove the nodes' blocks because the content of those blocks
    are in other parsed nodes, without that the code coverage could be >1 because the same code would be in different nodes*/
    private AbstractNode createNodeForCoverageCount(AbstractNode node) {
        if (node instanceof IfNode) {
            return createIfNodeForCoverageCount(node.getASTNode());
        } else if (node instanceof ForNode) {
            return createForNodeForCoverageCount(node.getASTNode());
        } else if (node instanceof WhileNode) {
            return createWhileNodeForCoverageCount(node.getASTNode());
        } else if (node instanceof TryCatchNode) {
            return createTryCatchNodeForCoverageCount(node.getASTNode());
        } else if (node instanceof SwitchNode) {
            return createSwitchNodeForCoverageCount(node.getASTNode());
        } else {
            return node;
        }
    }

    private IfNode createIfNodeForCoverageCount(Node node) {
        IfStmt nodeCopy = ((IfStmt) node).clone();
    /* Remove else and then blocks, because they will be parsed anyway (avoid
    duplicates)*/
        if (nodeCopy.hasElseBlock()) {
            nodeCopy = nodeCopy.setElseStmt(new BlockStmt());
        }

        nodeCopy = nodeCopy.setThenStmt(new BlockStmt());

        int lBegin = 0, lEnd = 0;
        if (nodeCopy.getBegin().isPresent()) lBegin = nodeCopy.getBegin().get().line;
        if (nodeCopy.getEnd().isPresent()) lEnd = nodeCopy.getEnd().get().line;
        Optional<List<InlineComment>> comments =
                MethodInspector.MethodDataCollector.getJavaParserNodeComments(nodeCopy);
        return new IfNode(nodeCopy, comments, lBegin, lEnd);
    }

    private ForNode createForNodeForCoverageCount(Node node) {
        Node nodeCopy = null;
        // Remove body because it will be parsed anyway (avoid duplicates)
        if (node instanceof ForStmt) {
            ForStmt nodeCopySpecialized = (ForStmt) node.clone();
            nodeCopySpecialized = nodeCopySpecialized.setBody(new BlockStmt());
            nodeCopy = nodeCopySpecialized;
        } else if (node instanceof ForEachStmt) {
            ForEachStmt nodeCopySpecialized = (ForEachStmt) node.clone();
            nodeCopySpecialized = nodeCopySpecialized.setBody(new BlockStmt());
            nodeCopy = nodeCopySpecialized;
        }

        int lBegin = 0, lEnd = 0;
        if (nodeCopy.getBegin().isPresent()) lBegin = nodeCopy.getBegin().get().line;
        if (nodeCopy.getEnd().isPresent()) lEnd = nodeCopy.getEnd().get().line;
        Optional<List<InlineComment>> comments =
                MethodInspector.MethodDataCollector.getJavaParserNodeComments(nodeCopy);
        return new ForNode(nodeCopy, comments, lBegin, lEnd);
    }

    private WhileNode createWhileNodeForCoverageCount(Node node) {
        Node nodeCopy = null;
        // Remove body because it will be parsed anyway (avoid duplicates)
        if (node instanceof WhileStmt) {
            WhileStmt nodeCopySpecialized = (WhileStmt) node.clone();
            nodeCopySpecialized = nodeCopySpecialized.setBody(new BlockStmt());
            nodeCopy = nodeCopySpecialized;
        } else if (node instanceof DoStmt) {
            DoStmt nodeCopySpecialized = (DoStmt) node.clone();
            nodeCopySpecialized = nodeCopySpecialized.setBody(new BlockStmt());
            nodeCopy = nodeCopySpecialized;
        }

        int lBegin = 0, lEnd = 0;
        if (nodeCopy.getBegin().isPresent()) lBegin = nodeCopy.getBegin().get().line;
        if (nodeCopy.getEnd().isPresent()) lEnd = nodeCopy.getEnd().get().line;
        Optional<List<InlineComment>> comments =
                MethodInspector.MethodDataCollector.getJavaParserNodeComments(nodeCopy);
        return new WhileNode(nodeCopy, comments, lBegin, lEnd);
    }

    private TryCatchNode createTryCatchNodeForCoverageCount(Node node) {
        TryStmt nodeCopy = ((TryStmt) node).clone();
        // remove finally block and catch clauses because they will be parsed anyway
        // (avoid duplicates)
        if (nodeCopy.getFinallyBlock().isPresent()) {
            nodeCopy.setFinallyBlock(new BlockStmt());
        }
        nodeCopy.setTryBlock(new BlockStmt());

        for (CatchClause catchClause : nodeCopy.getCatchClauses()) {
            catchClause.setBody(new BlockStmt());
        }

        int lBegin = 0, lEnd = 0;
        if (nodeCopy.getBegin().isPresent()) lBegin = nodeCopy.getBegin().get().line;
        if (nodeCopy.getEnd().isPresent()) lEnd = nodeCopy.getEnd().get().line;
        Optional<List<InlineComment>> comments =
                MethodInspector.MethodDataCollector.getJavaParserNodeComments(nodeCopy);
        return new TryCatchNode(nodeCopy, comments, lBegin, lEnd);
    }

    private SwitchNode createSwitchNodeForCoverageCount(Node node) {
        SwitchStmt nodeCopy = ((SwitchStmt) node).clone();
        // remove switch entires because they will be parsed anyway (avoid duplicates)
        for (SwitchEntry switchEntry : nodeCopy.getEntries()) {
            switchEntry.getStatements().stream()
                    .collect(Collectors.toList())
                    .forEach(statement -> switchEntry.remove(statement));
        }
        int lBegin = 0, lEnd = 0;
        if (nodeCopy.getBegin().isPresent()) lBegin = nodeCopy.getBegin().get().line;
        if (nodeCopy.getEnd().isPresent()) lEnd = nodeCopy.getEnd().get().line;
        Optional<List<InlineComment>> comments =
                MethodInspector.MethodDataCollector.getJavaParserNodeComments(nodeCopy);
        return new SwitchNode(nodeCopy, comments, lBegin, lEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodNode)) return false;
        MethodNode that = (MethodNode) o;
        return Objects.equals(methodStructuredSignature, that.methodStructuredSignature) &&
                Objects.equals(LOC, that.LOC) &&
                Objects.equals(filePath, that.filePath);
    }

    @Override
    public int hashCode() {
//    return Objects.hash(methodSignature, LOC, filePath);
        return Objects.hash(LOC);
    }
}
