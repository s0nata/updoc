package parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import org.apache.commons.lang3.Range;
import parser.nodes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains static operations to get method nodes from different pointers to a Java
 * source code (could be a file, a string path...)
 */
public class MethodInspector {

    /**
     * Map between upDoc and parser (Javaparser here) AST node types
     * key:   node class that we want to extract,
     * value: list of javaparser AST node classes to look  for to extract those Node Classes
     */
    private static final HashMap<Class<? extends SpecializedNode>, List<Class<? extends Node>>>
            mapSpecializedNodeClassToASTNodeClass =
            new HashMap<>() {
                {
                    put(IfNode.class, List.of(IfStmt.class));
                    put(ForNode.class, List.of(ForStmt.class, ForEachStmt.class));
                    put(WhileNode.class, List.of(WhileStmt.class, DoStmt.class));
                    put(TryCatchNode.class, List.of(TryStmt.class));
                    put(SwitchNode.class, List.of(SwitchStmt.class));
                    put(
                            SingleAssignmentStatementNode.class,
                            List.of(AssignExpr.class, VariableDeclarationExpr.class));
                    put(SingleMethodReturnStatementNode.class, List.of(ReturnStmt.class));
                    put(SingleMethodCallSequenceNode.class, List.of(MethodCallExpr.class));
                }
            };

//--------------------------------- EXTRACTING FULL METHODS --------------------------------------//

    /**
     * Collect all methods (including constructors) implemented in a source file.
     *
     * @param classFile an absolute Java source code file name.
     * @return Returns a list of {@code MethodNode}s of a source code file.
     */
    public static ArrayList<MethodNode> getMethodNodesFromFile(File classFile) {

        ArrayList<MethodNode> methods = new ArrayList<MethodNode>();

        try {

            // this may throw an exception
            CompilationUnit cu = StaticJavaParser.parse(classFile);

            MethodDataCollector mdc = new MethodDataCollector();
            mdc.visit(cu, methods);

        } catch (FileNotFoundException e) {
            System.err.println("[javaparser]: method parsing failed for file " + classFile.getPath());
            e.printStackTrace();
        }

        return methods;
    }


    public static List<MethodNode> getMethodNodesFromFile(
            File classFile, boolean parseOnlyMethodsWithJavaDoc) {

        List<MethodNode> blocks = new ArrayList<MethodNode>();

        try {
            // this may throw an exception
            CompilationUnit cu = StaticJavaParser.parse(classFile);

            MethodDataCollector mdc = new MethodDataCollector(parseOnlyMethodsWithJavaDoc);
            mdc.visit(cu, blocks);

        } catch (FileNotFoundException e) {
            System.err.println("[javaparser]: method parsing failed for file " + classFile.getPath());
            e.printStackTrace();
        }

        return blocks;
    }

    public static List<MethodNode> getMethodNodesFromFile(
            File classFile, Class<?> maxDepthASTNodeClass) {

        List<MethodNode> blocks = new ArrayList<MethodNode>();

        try {
            // this may throw an exception
            CompilationUnit cu = StaticJavaParser.parse(classFile);

            MethodInspector.MethodDataCollector
                    mdc = new MethodInspector.MethodDataCollector(maxDepthASTNodeClass);
            mdc.visit(cu, blocks);

        } catch (FileNotFoundException e) {
            System.err.println("[javaparser]: method parsing failed for file " + classFile.getPath());
            e.printStackTrace();
        }

        return blocks;
    }

    public static List<SpecializedNode> getGivenSpecializedNodesFromFile(
            File classFile, Class<? extends SpecializedNode> specializedNodeClass) {
        List<SpecializedNode> nodes = new ArrayList<SpecializedNode>();
        try {
            // this may throw an exception
            CompilationUnit cu = StaticJavaParser.parse(classFile);
            List<Node> astNodes =
                    mapSpecializedNodeClassToASTNodeClass.get(specializedNodeClass).stream()
                            .map(astNodeClass -> cu.findAll(astNodeClass))
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

            boolean addParent = false;
            if (specializedNodeClass == SingleAssignmentStatementNode.class) {
                addParent = true;
                astNodes =
                        astNodes.stream()
                                .filter(
                                        node ->
                                                node.getParentNode().get().toString().endsWith(";")
                                                        && node.getParentNode().get().toString().contains("="))
                                .collect(Collectors.toList());
            } else if (specializedNodeClass == SingleMethodCallSequenceNode.class) {
                addParent = true;
                astNodes =
                        astNodes.stream()
                                .filter(
                                        node ->
                                                node.getParentNode().get() instanceof ExpressionStmt
                                                        && LexicalPreservingPrinter.print(
                                                                LexicalPreservingPrinter.setup(node.getParentNode().get()))
                                                        .endsWith(";"))
                                .collect(Collectors.toList());
            }

            for (Node astNode : astNodes) {
                if (addParent) {
                    astNode = astNode.getParentNode().get();
                }
                int lBegin = 0, lEnd = 0;
                if (astNode.getBegin().isPresent()) lBegin = astNode.getBegin().get().line;
                if (astNode.getEnd().isPresent()) lEnd = astNode.getEnd().get().line;
                Optional<List<InlineComment>> comments =
                        MethodInspector.MethodDataCollector.getJavaParserNodeComments(astNode);
                try {
                    nodes.add(
                            specializedNodeClass
                                    .getConstructor(new Class[]{Node.class, Optional.class, int.class, int.class})
                                    .newInstance(astNode, comments, lBegin, lEnd));
                } catch (InstantiationException
                         | IllegalAccessException
                         | IllegalArgumentException
                         | InvocationTargetException
                         | NoSuchMethodException
                         | SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("[javaparser]: method parsing failed for file " + classFile.getPath());
            e.printStackTrace();
        }
        return nodes;
    }


    public static MethodNode getSingleMethodNodeFromFile(String path) {
        Path fpath = Paths.get(path);

        List<MethodNode> blocks = new ArrayList<MethodNode>();

        try {
            // this may throw an exception
            String fileContents = Files.readString(fpath);
            BlockStmt bstmt = StaticJavaParser.parseMethodDeclaration(fileContents).getBody().get();
            MethodDataCollector mdc = new MethodDataCollector();
            mdc.visit(bstmt, blocks);

        } catch (IOException e) {
            System.err.println("[javaparser]: method parsing failed for file " + path);
            e.printStackTrace();
        }
        return blocks.get(0);
    }


//------------------------------------- INNER VISITOR CLASS --------------------------------------//

    /**
     * Code that extracts method AST nodes from a Java source file via a Visitor pattern and stores
     * them as {@code MethodNode}s.
     */
    public static class MethodDataCollector extends VoidVisitorAdapter<List<MethodNode>> {

        // list of AST node classes that should not be extracted as OtherNode
        private static final List<Class<?>> supportedNodeTypes =
                Arrays.asList(
                        new Class<?>[]{
                                MethodDeclaration.class,
                                ConstructorDeclaration.class,
                                IfStmt.class,
                                ForStmt.class,
                                ForEachStmt.class,
                                WhileStmt.class,
                                DoStmt.class,
                                TryStmt.class,
                                AssignExpr.class,
                                ReturnStmt.class,
                                MethodCallExpr.class,
                                VariableDeclarationExpr.class,
                                SwitchStmt.class
                        });

        private static boolean parseOnlyMethodsWithJavaDoc;

        // Way of defining a javaparser AST node class, after which the inspector shall not go
        // deeper in the AST (~= min granularity). See constructors for effect.
        private static Optional<Class<?>> maxDepthASTNodeClass;

        public MethodDataCollector() {
            MethodInspector.MethodDataCollector.maxDepthASTNodeClass = Optional.empty();
            MethodInspector.MethodDataCollector.parseOnlyMethodsWithJavaDoc = false;
        }

        public MethodDataCollector(boolean parseOnlyMethodsWithJavaDoc) {
            MethodInspector.MethodDataCollector.maxDepthASTNodeClass = Optional.empty();
            MethodInspector.MethodDataCollector.parseOnlyMethodsWithJavaDoc = parseOnlyMethodsWithJavaDoc;
        }

        public MethodDataCollector(Class<?> maxDepthASTNodeClass) {
            MethodInspector.MethodDataCollector.maxDepthASTNodeClass = Optional.of(maxDepthASTNodeClass);
            MethodInspector.MethodDataCollector.parseOnlyMethodsWithJavaDoc = false;
        }


        /**
         * Collect method signature, body, doc comment, and metadata (LOCs, char range).
         * <p>
         * Methods are ordered the same as they appear in the source file.
         * <p>
         * Annotations are not treated and not included into method signature
         */
        @Override
        public void visit(BlockStmt bsStmt, List<MethodNode> collector) {

            super.visit(bsStmt, collector);

            Node parent = bsStmt.getParentNode().get();

            // To avoid that a block of code inside {} below an if statement gets parsed
            if (parent instanceof MethodDeclaration || parent instanceof ConstructorDeclaration) {
                String methodName = "";
                boolean hasJavaDoc = false;
                NodeList<Parameter> params = null;
                Type returnType = null;
                NodeList<ReferenceType> thrownExceptions = null;
                CallableDeclaration wholeCallableDecl;
                SignatureNode signatureNode;
                Range<Integer> signatureLOCRange;


                // method signature processing code
                if (parent instanceof MethodDeclaration) {
                    methodName = ((MethodDeclaration) parent).getNameAsString();
                    hasJavaDoc = ((MethodDeclaration) parent).getJavadocComment().isPresent();
                    params = ((MethodDeclaration) parent).getParameters();
                    returnType = ((MethodDeclaration) parent).getType();
                    thrownExceptions = ((MethodDeclaration) parent).getThrownExceptions();
                    wholeCallableDecl = ((MethodDeclaration) parent).asCallableDeclaration();
                    // Signature LOCs go from the first line of the whole callable declaration
                    // till the first method body (bsStmt) line, i.e. the very first {
                    signatureLOCRange = Range.between(
                            extractLOCBeginFromPostion(wholeCallableDecl),
                            extractLOCBeginFromPostion(bsStmt)
                    );
                    signatureNode = new SignatureNode(wholeCallableDecl, signatureLOCRange);
                } else {
                    methodName = ((ConstructorDeclaration) parent).getNameAsString();
                    hasJavaDoc = ((ConstructorDeclaration) parent).getJavadocComment().isPresent();
                    params = ((ConstructorDeclaration) parent).getParameters();
                    thrownExceptions = ((ConstructorDeclaration) parent).getThrownExceptions();
                    wholeCallableDecl = ((ConstructorDeclaration) parent).asCallableDeclaration();
                    signatureLOCRange = Range.between(
                            extractLOCBeginFromPostion(wholeCallableDecl),
                            extractLOCBeginFromPostion(bsStmt)
                    );
                    signatureNode = new SignatureNode(wholeCallableDecl, signatureLOCRange);
                }

                if (hasJavaDoc || !parseOnlyMethodsWithJavaDoc) {

                    Optional<JavadocComment> maybeJavadocComment = ((CallableDeclaration<?>) parent).getJavadocComment();

                    List<AbstractNode> methodNodesSequence =
                            MethodInspector.MethodDataCollector.getNodesInsideMethodBody(bsStmt);

                    List<InlineComment> comments = null;
                    if (!bsStmt.getAllContainedComments().isEmpty()) {
                        comments =
                                bsStmt.getAllContainedComments().stream()
                                        .map(c -> c.toString())
                                        .map(s -> new InlineComment(s))
                                        .collect(Collectors.toList());
                    }


                    int lBegin = 0, lEnd = 0;
                    if (bsStmt.getBegin().isPresent()) {
                        // if a method has annotations (e.g. @Override), adjust signature LOC calculations
                        // signature fist line is line of last annotation +1 (next line)
                        if (((CallableDeclaration<?>) parent).getAnnotations().size() > 0) {
                            AnnotationExpr lastAnnotation =
                                    ((CallableDeclaration<?>) parent).getAnnotations().getLast().get();
                            lBegin = lastAnnotation.getEnd().get().line + 1;

                        } else {
                            lBegin = extractLOCBeginFromPostion(parent); // parent.getBegin().get().line;
                        }
                    }
                    if (bsStmt.getEnd().isPresent()) lEnd = parent.getEnd().get().line;
                    Range<Integer> lineRange = Range.between(lBegin, lEnd);


                    MethodNode mn =
                            new MethodNode(
                                    bsStmt,
                                    //
                                    methodName,
                                    params,
                                    returnType,
                                    thrownExceptions,
                                    maybeJavadocComment,
                                    //
                                    signatureNode,
                                    //
                                    methodNodesSequence,
                                    Optional.ofNullable(comments),
                                    //
                                    lineRange,
                                    "");

                    collector.add(mn);
                }
            }
        }

//------------------------------------- AUX PARSING METHODS --------------------------------------//

        /**
         * extract AST node LOC begin as an integer
         */
        private int extractLOCBeginFromPostion(Node node) {
            return node.getBegin().get().line;
        }

        /**
         * extract AST node LOC end as an integer
         */
        private int extractLOCendFromPostion(Node node) {
            return node.getEnd().get().line;
        }

        /**
         * Recursive function to be called on javaparser AST nodes to go deeper in the AST, to extract
         * nodes inside method body
         */
        private static List<AbstractNode> getNodesInsideMethodBody(Node node) {

            boolean supportedNodeType =
                    supportedNodeTypes.stream().anyMatch(type -> type.isInstance(node));

            List<AbstractNode> collected = new ArrayList<AbstractNode>();

            Optional<List<InlineComment>> comments = null;

            int lBegin = 0, lEnd = 0;
            if (node.getBegin().isPresent()) lBegin = node.getBegin().get().line;
            if (node.getEnd().isPresent()) lEnd = node.getEnd().get().line;

            Node parentNode = node.getParentNode().get();
            AbstractNode new_node = null;

            if (node instanceof IfStmt) {
                comments = getJavaParserNodeComments(node);
                new_node = new IfNode(node, comments, lBegin, lEnd);
            } else if (node instanceof ForStmt || node instanceof ForEachStmt) {
                comments = getJavaParserNodeComments(node);
                new_node = new ForNode(node, comments, lBegin, lEnd);
            } else if (node instanceof WhileStmt || node instanceof DoStmt) {
                comments = getJavaParserNodeComments(node);
                new_node = new WhileNode(node, comments, lBegin, lEnd);
            } else if (node instanceof TryStmt) {
                comments = getJavaParserNodeComments(node);
                new_node = new TryCatchNode(node, comments, lBegin, lEnd);
            } else if (node instanceof SwitchStmt) {
                comments = getJavaParserNodeComments(node);
                new_node = new SwitchNode(node, comments, lBegin, lEnd);
            } else if (node instanceof AssignExpr || node instanceof VariableDeclarationExpr) {
                if (parentNode.toString().endsWith(";") && parentNode.toString().contains("=")) {
                    comments = getJavaParserNodeComments(parentNode);
                    new_node = new SingleAssignmentStatementNode(parentNode, comments, lBegin, lEnd);
                } else {
                    supportedNodeType = false;
                }
            } else if (node instanceof ReturnStmt) {
                comments = getJavaParserNodeComments(node);
                new_node = new SingleMethodReturnStatementNode(node, comments, lBegin, lEnd);
            } else if (node instanceof MethodCallExpr) {
                /*
                 * need to use LexicalPreservingPrinter because by default if we print nested
                 * method calls, javaparser adds a semicolon to the end, and all the nested
                 * method calls have the same parent as the complete method call, checking if
                 * the printed parent node with LexicalPreservingPrinter has a semicolon at the
                 * end is the only way to discriminate between the full method call and the
                 * nested method calls
                 */
                if (parentNode instanceof ExpressionStmt
                        && LexicalPreservingPrinter.print(LexicalPreservingPrinter.setup(parentNode))
                        .endsWith(";")) {
                    comments = getJavaParserNodeComments(parentNode);
                    new_node = new SingleMethodCallSequenceNode(parentNode, comments, lBegin, lEnd);
                } else {
                    supportedNodeType = false;
                }
            }

            // it means that the current node should be of type OtherNode
            if (!supportedNodeType) {
                comments = getJavaParserNodeComments(node);
                new_node = new OtherNode(node, comments, lBegin, lEnd);
            }
            collected.add(new_node);

            if (!maxDepthASTNodeClass.isPresent()
                    || (maxDepthASTNodeClass.isPresent() && !maxDepthASTNodeClass.get().isInstance(node))) {
                node.getChildNodes()
                        .forEach(
                                c ->
                                        MethodInspector.MethodDataCollector.getNodesInsideMethodBody(c)
                                                .forEach(n -> collected.add(n)));
            }

            return collected;
        }

        /**
         * Collect any inline comments associated with a given AST node
         *
         * @param node AST node in parser (Javaparser) representation
         * @return possibly empty list of inline comments
         */
        public static Optional<List<InlineComment>> getJavaParserNodeComments(Node node) {
            Optional<List<InlineComment>> comments = Optional.empty();
            if (!node.getAllContainedComments().isEmpty()) {
                comments =
                        Optional.of(
                                node.getAllContainedComments().stream()
                                        .map(c -> c.toString())
                                        .map(s -> new InlineComment(s))
                                        .collect(Collectors.toList()));
            }
            if (node.getComment().isPresent()) {
                InlineComment comment = new InlineComment(node.getComment().get().toString());
                if (comments.isPresent()) {
                    comments.get().add(comment);
                } else {
                    comments = Optional.of(new ArrayList<InlineComment>(List.of(comment)));
                }
            }
            for (Comment comment : node.getOrphanComments()) {
                if (!comments.isPresent()) {
                    comments = Optional.of(new ArrayList<InlineComment>());
                }
                comments.get().add(new InlineComment(comment.toString()));
            }
            return comments;
        }

    }

}
