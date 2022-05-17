package parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Code that extracts class AST nodes from a Java source file via a
 * Visitor pattern and stores themas {@code ClassNode}s.
 */
public class ClassInspector {

    private static class ClassDataCollector extends VoidVisitorAdapter<List<ClassNode>> {

        /**
         * Collect class name and lines of code (LOCs) where it is implemented.
         */
        @Override
        public void visit(ClassOrInterfaceDeclaration coid, List<ClassNode> collector) {
            // future: see isInterface()

            super.visit(coid, collector);

            int LOCbegin = 0, LOCend = 0;
            if (coid.getBegin().isPresent()) {
                LOCbegin = coid.getBegin().get().line;
            }
            if (coid.getEnd().isPresent()) {
                LOCend = coid.getEnd().get().line;
            }

            collector.add(new ClassNode(coid.getNameAsString(), LOCbegin, LOCend));

        }
    }

    /**
     * Collect all classes implemented in a source file.
     *
     * @param classFile an absolute Java source code file name.
     * @return Returns a list of {@code ClassNode}s of a source code file.
     */
    public static ArrayList<ClassNode> getClassNodesFromFile(File classFile) {

        ArrayList<ClassNode> classes = new ArrayList<ClassNode>();

        try {

            CompilationUnit cu = StaticJavaParser.parse(classFile);

            ClassDataCollector cdc = new ClassDataCollector();
            cdc.visit(cu, classes);

        } catch (Exception parseException) {

            System.err.println("[javaparser]: class parsing failed for file " + classFile.getPath());
            System.err.println(parseException.getMessage().replaceAll("  [a-z].*\n", "")
                    .replaceAll("Problem stacktrace.*\n", ""));
        }

        return classes;
    }

}
