package entry;

import parser.MethodInspector;
import parser.MethodNode;
import parser.nodes.AbstractNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * upDoc interface to the parsing component that creates the intermediary
 * representations of the source files under analysis.
 * <p>
 * Currently, reports code coverage by our coarse method body nodes parsing.
 */
public class Parser {

    // TODO here method to feed the IR into the mapper in th normal, non-demo upDoc pipeline
    // the demo upDoc pipeline is the method below, to be refactored out, see
    // https://gitlab.software.imdea.org/doc-code-analysis/updoc/updoc/-/issues/12

    public static void reportMethodBodyNodeCoverage(String dirPath, String fileName) {

        // get bodies of all methods in the class

        List<MethodNode> methodNodes =
                MethodInspector.getMethodNodesFromFile(new File(dirPath + fileName));

        // for each method get its node coverage by upDoc

        List<Double> coverages = new ArrayList<Double>();

        for (MethodNode currentMethodNode : methodNodes) {

            // print full method body
            System.out.println("\n\n[[Original method body:]]\n");
            System.out.println(currentMethodNode.getASTNode());

            // collect currentMethodCoverage stat for current method
            double currentMethodCoverage = currentMethodNode.getCodeCoverage() * 100.0; // in %
            coverages.add(currentMethodCoverage);

            // print method body node by node
            System.out.println("\n[[Parsed nodes ("
                    + String.format("%.2f", currentMethodCoverage) + " % currentMethodCoverage):]]");

            for (AbstractNode node : currentMethodNode.getMethodNodesSequence(false)) {
                System.out.println("\n\t" + node.toString().replace("\n", "\n\t"));
            }
        }

        // general coverage stats for this class
        double minCoverage = coverages.stream().mapToDouble(x -> x).min().getAsDouble();
        double maxCoverage = coverages.stream().mapToDouble(x -> x).max().getAsDouble();
        double avgCoverage = coverages.stream().mapToDouble(x -> x).average().getAsDouble();

        System.out.println("Min coverage: " + String.format("%.2f", minCoverage) + "%");
        System.out.println("Max coverage: " + String.format("%.2f", maxCoverage) + "%");
        System.out.println("Average coverage: " + String.format("%.2f", avgCoverage) + "%");
        System.out.println();
    }

}
