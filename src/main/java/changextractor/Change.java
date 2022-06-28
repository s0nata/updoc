package changextractor;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.io.LineReader;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.Tree;
import org.apache.commons.lang3.Range;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A class that contains a tuple of AST nodes before and after change.
 */
public class Change {

    private final String changeType; // {update, add, delete}. Only update at the moment

    // before change
    private final Tree srcNodeFineAST;
    private final NodeInfo srcNodeFineInfo;
    private final NodeInfo srcNodeCoarseInfo;
    private final String srcNodeFilePath; // file before change

    // after change
    private final Tree dstNodeFineAST;
    private final NodeInfo dstNodeFineInfo;
    private final NodeInfo dstNodeCoarseInfo;
    private final String dstNodeFilePath; // file after change


    public Change(String filePathOld, String filePathNew, Action changeAction, MappingStore treeMappings) {

        changeType = changeAction.getName();

        // source node = before change

        srcNodeFilePath = filePathOld;

        srcNodeFineAST = changeAction.getNode();

        srcNodeFineInfo = prepareFineNodeInfo(srcNodeFilePath, srcNodeFineAST);

        srcNodeCoarseInfo = prepareCoarseNodeInfo(srcNodeFilePath, srcNodeFineAST);

        // destination node = after change

        dstNodeFilePath = filePathNew;

        dstNodeFineAST = treeMappings.getDstForSrc(srcNodeFineAST);

        dstNodeFineInfo = prepareFineNodeInfo(dstNodeFilePath, dstNodeFineAST);

        dstNodeCoarseInfo = prepareCoarseNodeInfo(dstNodeFilePath, dstNodeFineAST);
    }

    public String getSrcNodeFineType() {
        return srcNodeFineInfo.getTreeNodeTypeName();
    }

    public String getSrcNodeCoarseType() {
        return srcNodeCoarseInfo.getTreeNodeTypeName();
    }

    public String getDstNodeFineType() {
        return dstNodeFineInfo.getTreeNodeTypeName();
    }

    public String getDstNodeCoarseType() {
        return dstNodeCoarseInfo.getTreeNodeTypeName();
    }

    public Range<Integer> getSrcNodeFineLOCs() {
        return Range.between(srcNodeFineInfo.getRowBegin(), srcNodeFineInfo.getRowEnd());
    }

    public Range<Integer> getSrcNodeCoarseLOCs() {
        return Range.between(srcNodeCoarseInfo.getRowBegin(), srcNodeCoarseInfo.getRowEnd());
    }

    public Range<Integer> getDstNodeFineLOCs() {
        return Range.between(dstNodeFineInfo.getRowBegin(), dstNodeFineInfo.getRowEnd());
    }

    public Range<Integer> getDstNodeCoarseLOCs() {
        return Range.between(dstNodeCoarseInfo.getRowBegin(), dstNodeCoarseInfo.getRowEnd());
    }

    public String getSrcNodeFineSource() {
        return srcNodeFineInfo.getTreeNodePrettySource();
    }

    public String getSrcNodeCoarseSource() {
        return srcNodeCoarseInfo.getTreeNodePrettySource();
    }

    public String getDstNodeFineSource() {
        return dstNodeFineInfo.getTreeNodePrettySource();
    }

    public String getDstNodeCoarseSource() {
        return dstNodeCoarseInfo.getTreeNodePrettySource();
    }

    @Override
    public String toString() {

        return "\n| in original source sub-element "
                + this.getSrcNodeFineType()
                + " in lines "
                + this.getSrcNodeFineLOCs()
                + ":\n"
                + this.getSrcNodeFineSource()
                + "\n| inside of "
                + this.getSrcNodeCoarseType()
                + " in lines " + this.getSrcNodeCoarseLOCs()
                + ":\n"
                + this.getSrcNodeCoarseSource()
                + "\n| was changed into sub-element "
                + this.getDstNodeFineType()
                + " in lines "
                + this.getDstNodeFineLOCs()
                + ":\n"
                + this.getDstNodeFineSource()
                + "\n| inside of "
                + this.getDstNodeCoarseType()
                + " in lines "
                + this.getDstNodeCoarseLOCs()
                + ":\n"
                + this.getDstNodeCoarseSource();
    }

    public String sourceToString() {
        return "\n| in original source sub-element "
                + this.getSrcNodeFineType()
                + " in lines "
                + this.getSrcNodeFineLOCs()
                + ":\n"
                + this.getSrcNodeFineSource()
                + "\n| inside of "
                + this.getSrcNodeCoarseType()
                + " in lines " + this.getSrcNodeCoarseLOCs()
                + ":\n"
                + this.getSrcNodeCoarseSource();
    }

    public String dstToString() {
        return "\n| changed into sub-element "
                + this.getDstNodeFineType()
                + " in lines "
                + this.getDstNodeFineLOCs()
                + ":\n"
                + this.getDstNodeFineSource()
                + "\n| inside of "
                + this.getDstNodeCoarseType()
                + " in lines "
                + this.getDstNodeCoarseLOCs()
                + ":\n"
                + this.getDstNodeCoarseSource();
    }


    private NodeInfo prepareFineNodeInfo(String filePath, Tree nodeAST) {

        Range<Integer> nodeLOCs = extractASTNodeLOCs(filePath, nodeAST);

        String nodeTypeName = nodeAST.getType().toString();

        String nodeSource = extractASTNodeSource(filePath, nodeAST);

        return new NodeInfo(nodeLOCs.getMinimum(), nodeLOCs.getMaximum(), nodeTypeName, nodeSource);
    }

    /**
     * Given the fine-grained AST node as extracted by GumTreeDiff, return the smallest coarse-grained
     * node among the parents that upDoc IR supports.
     *
     * @param filePath
     * @param nodeASTFine
     * @return information about the coarse-grained node
     */
    private NodeInfo prepareCoarseNodeInfo(String filePath, Tree nodeASTFine) {

        int rowBegin, rowEnd;
        String nodeTypeName;
        String nodePrettyBody;

        // based on first parent decide if the changed element belongs to a class or a method signature

        boolean isPartOfClassSignature = false;
        boolean isPartOfMethodSignature = false;

        boolean isPartOfMethodBody = false; // class fields are recognized coarse nodes
        // see extractCoarseBodyNode() below

        Tree firstParentNode = nodeASTFine.getParent();
        Tree firstGrandParentNode = firstParentNode.getParent();

        // METHOD SIGNATURE CHANGES
        // method signature change in modifier or name
        if (firstParentNode.getType().toString().equals("MethodDeclaration")
                || firstParentNode.getType().toString().equals("ConstructorDeclaration")) {
            isPartOfMethodSignature = true;
        }
        // method signature change in return type
        else if (firstParentNode.getType().toString().equals("ClassOrInterfaceType")
                && (firstGrandParentNode.getType().toString().equals("MethodDeclaration")
                || firstGrandParentNode.getType().toString().equals("ConstructorDeclaration"))) {
            isPartOfMethodSignature = true;
        }
        // method signature change in parameter
        else if (firstParentNode.getType().toString().equals("Parameter")
                && (firstGrandParentNode.getType().toString().equals("MethodDeclaration")
                || firstGrandParentNode.getType().toString().equals("ConstructorDeclaration"))) {
            isPartOfMethodSignature = true;
        }
        // CLASS SIGNATURE CHANGES
        // class signature change
        else if (firstParentNode.getType().toString().equals("ClassOrInterfaceDeclaration")) {
            isPartOfClassSignature = true;
        }
        // CLASS FIELD OR INSIDE-BODY CHANGES
        else {
            // figure out if the changed element is inside a method body or is a class field
            // at the moment upDoc does not understand class fields

            // GTD returns parent nodes sorted in ascending order (last one is the top of the tree)
            for (Tree parentNode : nodeASTFine.getParents()) {
                if (parentNode.getType().toString().equals("BlockStmt")) {
                    isPartOfMethodBody = true;
                    break;
                }
            }
        }

        if (isPartOfClassSignature) {

            /* class (or interface) signature ends 1 character before the first character of the
             * class fields, constructors, or methods.
             * So we look for siblings of the current node that are Field/Constructor/Method Declarations,
             * and we take the first one (with minimal starting char position), we bet it's begin and
             * subtract 1 for it, and this is how a class signature end boundary is defined
             */
            List<Tree> siblingNodes = firstParentNode.getChildren();
            int classSignatureEndPos = firstParentNode.getEndPos(); // the maximal possible endPos
            for (Tree currentSibling : siblingNodes) {
                String siblingTypeName = currentSibling.getType().toString();
                if (siblingTypeName.equals("FieldDeclaration")
                        || siblingTypeName.equals("ConstructorDeclaration")
                        || siblingTypeName.equals("MethodDeclaration")) {
                    if (currentSibling.getPos() < classSignatureEndPos) {
                        classSignatureEndPos = currentSibling.getPos() - 1;
                    }
                }
            }

            // here NodeInfo can be already prepared
            Range<Integer> classSignatureLOCs = extractASTNodeLOCs(filePath, firstParentNode, classSignatureEndPos);

            rowBegin = classSignatureLOCs.getMinimum();
            rowEnd = classSignatureLOCs.getMaximum();
            nodeTypeName = "ClassSignature";
            nodePrettyBody = extractSource(filePath, firstParentNode.getPos(), classSignatureEndPos);
        }


        // treat a node that is a part of a signature of a class or a method
        else if (isPartOfMethodSignature) {

            // full method as signature + body, the "MethodDeclaration" node
            Tree methodNodeFull = nodeASTFine;
            for (Tree curParentNode : nodeASTFine.getParents()) {
                if (curParentNode.getType().toString().equals("MethodDeclaration")
                        || curParentNode.getType().toString().equals("ConstructorDeclaration")) {
                    methodNodeFull = curParentNode;
                    break;
                }
            }

            // just the method body inside the { }
            Tree methodNodeBody = methodNodeFull;
            for (Tree curChild : methodNodeFull.getChildren()) {
                if (curChild.getType().toString().equals("BlockStmt")) {
                    methodNodeBody = curChild;
                    break;
                }
            }

            Range<Integer> fullLOCs = extractASTNodeLOCs(filePath, methodNodeFull);
            Range<Integer> bodyLOCs = extractASTNodeLOCs(filePath, methodNodeBody);

            rowBegin = fullLOCs.getMinimum();
            rowEnd = bodyLOCs.getMinimum();
            nodeTypeName = "MethodSignature";
            nodePrettyBody = extractSource(filePath, methodNodeFull.getPos(), methodNodeBody.getPos());
        }

        // otherwise we go up the parents in the AST until we meet a node of one of the types upDoc
        // recognises
        else {

            Tree coarseNode = extractCoarseBodyNode(nodeASTFine);

            Range<Integer> coarseNodeLOCs = extractASTNodeLOCs(filePath, coarseNode);

            rowBegin = coarseNodeLOCs.getMinimum();
            rowEnd = coarseNodeLOCs.getMaximum();
            nodeTypeName = coarseNode.getType().toString();
            nodePrettyBody = extractSource(filePath, coarseNode.getPos(), coarseNode.getEndPos() - 1);

        }

        return new NodeInfo(rowBegin, rowEnd, nodeTypeName, nodePrettyBody);
    }

    /**
     * This method returns a coarse AST node for the current node - the AST node that
     * encloses the @param{fineASTNode} and is of one of the coarse AST node types
     * recognized by upDoc.
     *
     * <p>There are several special cases.
     *
     * <p> Current node can be very high-level: ClassOrInterfaceDeclaration or MethodSignature.
     * In this case it is already considered "coarse".
     *
     * <p> For the rest of the nodes we go through the list of parents returned by Tree::getParents()
     * This method returns parents sorted depth first in order of tree traversal from the current node
     * to its root. We take the closest parent recognized by upDoc.
     */
    private Tree extractCoarseBodyNode(Tree fineASTNode) {

        List<Tree> parentNodes = fineASTNode.getParents();

        Tree recognizedParentNode = fineASTNode;

        for (Tree curParent : parentNodes) {
            recognizedParentNode = curParent;
            //        System.out.println("DBG: "+recognizedParentNode.getType().toString());
            switch (curParent.getType().toString()) {
                // for class field nodes
                case "FieldDeclaration":
                    break;
                // for method body nodes
                // first check if the changed node is a child of a non-scoped node:
                case "VariableDeclarationExpr": // = SingleAssignmentStatementNode
                    break;
                case "MethodCallExpr": // = SingleMethodCallSequenceNode
                    break;
                case "ReturnStmt": // = SingleMethodReturnStatementNode
                    break;
                // then check scoped nodes:
                case "IfStmt": // = IfNode
                    break;
                case "ForStmt": // = ForNode
                    break;
                case "SwitchStmt": // = SwitchNode
                    break;
                case "WhileStmt": // = WhileNode
                    break;
                case "TryStmt": // = TryCatchNode
                    break;
                case "DoStmt": // = WhileNode
                    break;
                default:
                    continue;
            }
            break;
        }

        return recognizedParentNode;
    }

    /**
     * Given a file and a node from its AST representation return the start and end lines of the node
     * in its source file (LOCs)
     *
     * @param filePath source file path
     * @param node     a node of the AST representation of the source file
     * @return begin and end LOCs of the node
     */
    private Range<Integer> extractASTNodeLOCs(String filePath, Tree node) {
        int lBegin = 0;
        int lEnd = 0;

        try {
            LineReader gtdReader = new LineReader(new FileReader(filePath));

            int charReadValue;
            String fileString = "";

            while ((charReadValue = gtdReader.read()) != -1) {
                fileString += (char) charReadValue;
            }
            gtdReader.close();

            // GTD assumption: [0] for line, [1] for column
            lBegin = gtdReader.positionFor(node.getPos())[0];
            lEnd = gtdReader.positionFor(node.getEndPos())[0];
        } catch (FileNotFoundException e) {
            System.err.println("file not found: ");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error: ");
            System.err.println(e.getMessage());
        }

        return Range.between(lBegin, lEnd);
    }

    /**
     * Given a file and a node from its AST representation return the start and end lines of the node
     * in its source file (LOCs). End position of a node is given explicitly as an argument
     * (e.g., for class and method signature nodes that exist in UpDoc's IR, but do not exist in the
     * ASTs built by the parsers)
     *
     * @param filePath   source file path
     * @param node       a node of the AST representation of the source file
     * @param nodeEndPos a char index in the source file where source code of the node ends
     * @return begin and end LOCs of the node
     * <p>
     */
    private Range<Integer> extractASTNodeLOCs(String filePath, Tree node, int nodeEndPos) {
        int lBegin = 0;
        int lEnd = 0;

        try {
            LineReader gtdReader = new LineReader(new FileReader(filePath));

            int charReadValue;
            String fileString = "";

            while ((charReadValue = gtdReader.read()) != -1) {
                fileString += (char) charReadValue;
            }
            gtdReader.close();

            // GTD assumption: [0] for line, [1] for column
            lBegin = gtdReader.positionFor(node.getPos())[0];
            lEnd = gtdReader.positionFor(nodeEndPos)[0];
        } catch (FileNotFoundException e) {
            System.err.println("file not found: ");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error: ");
            System.err.println(e.getMessage());
        }

        return Range.between(lBegin, lEnd);
    }


    /**
     * Given an AST node return its formatted source code
     *
     * @param node GTD AST node
     * @return node source
     */
    private String extractASTNodeSource(String filePath, Tree node) {
        return extractSource(filePath, node.getPos(), node.getEndPos() - 1);
    }

    /**
     * Given a file and char range begin and end positions, return the contents of the file string
     * between these positions.
     *
     * @param filePath file with source code
     * @param startPos begin of the char range
     * @param endPos   end of char range
     * @return originally formatted source of the node
     */
    private String extractSource(String filePath, int startPos, int endPos) {
        String nodeSource = "";
        try {
            String fullString = Files.readString(Path.of(filePath));
            nodeSource = fullString.substring(startPos, endPos);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return nodeSource;
    }

    public String getSrcNodeFilePath() {
        return srcNodeFilePath;
    }
}
