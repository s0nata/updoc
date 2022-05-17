package parser;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains static operations to get method nodes from
 * different pointers to a Java source code (could be a file, a string path...)
 */

public class MethodInspector {

    /**
     * Code that extracts method AST nodes from a Java source file via a Visitor pattern and stores
     * them as {@code MethodNode}s.
     */
    private static class MethodDataCollector extends VoidVisitorAdapter<List<MethodNode>> {

        /**
         * Collect method name, parameters, doc comment, and lines of code (LOCs) where it is
         * implemented.
         */
        @Override
        public void visit(MethodDeclaration md, List<MethodNode> collector) {

            super.visit(md, collector);

            // only collect methods that already have non-empty doc comment
            // and let's also assume commits do not remove doc comments
            if (md.getJavadocComment().isPresent()) {
                JavadocComment jdoc = md.getJavadocComment().get();
                int LOCbegin = 0, LOCend = 0;
                if (md.getBegin().isPresent())
                    LOCbegin = md.getBegin().get().line;
                if (md.getEnd().isPresent())
                    LOCend = md.getEnd().get().line;

                collector.add(new MethodNode(
                        md.getNameAsString(),
                        md.getParameters(),
                        md.getType(),
                        md.getThrownExceptions(),
                        new StructuredComment(md.getJavadocComment()),
                        LOCbegin,
                        LOCend));
            }
        }
    }


    /**
     * Collect all methods implemented in a source file.
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


    /**
     * Parse a single method from a string.
     *
     * @param path an absolute Java source code file name, the file contains only a body of a
     *             single method (signature if interface method) and its doc comment
     * @return Returns a {@code MethodNode} representation of the method parsed
     */
    public static MethodNode getSingleMethodNodeFromFile(String path) {
        try {
            Path fpath = Paths.get(path);
            String fileContents = Files.readString(fpath); // IOException

            // FIXME keep this, not the parseMethod one
            BodyDeclaration<?> md = StaticJavaParser.parseBodyDeclaration(fileContents);
//      MethodDeclaration md = StaticJavaParser.parseMethodDeclaration(fileContents); // ParseProblemException

            if (md instanceof MethodDeclaration) {
                MethodNode mn = new MethodNode(
                        ((MethodDeclaration) md).getNameAsString(),
                        ((MethodDeclaration) md).getParameters(),
                        ((MethodDeclaration) md).getType(),
                        ((MethodDeclaration) md).getThrownExceptions(),

                        //FIXME if Javadoc is empty, here we throw exception.
                        //FIXME make the parameter of StructuredComment Optional and
                        //FIXME manage empty nodes there
                        new StructuredComment(((MethodDeclaration) md).getJavadocComment()),

                        0,
                        0);

                return mn;
            } else if (md instanceof ConstructorDeclaration) {
                MethodNode mn = new MethodNode(
                        ((ConstructorDeclaration) md).getNameAsString(),
                        ((ConstructorDeclaration) md).getParameters(),
                        null,
                        ((ConstructorDeclaration) md).getThrownExceptions(),
                        //FIXME if Javadoc is empty, here we throw exception.
                        //FIXME make the parameter of StructuredComment Optional and
                        //FIXME manage empty nodes there
                        new StructuredComment(((ConstructorDeclaration) md).getJavadocComment()),

                        0,
                        0);

                return mn;
            }

        } catch (ParseProblemException parsingException) {

            System.err.println("[javaparser]: could not parse file " + path);
            System.err.print(parsingException.getMessage());

        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }

        return null;
    }

}
