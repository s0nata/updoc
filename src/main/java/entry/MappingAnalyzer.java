package entry;

import mapper.MapBuilder;
import parser.MethodInspector;
import parser.MethodNode;

/**
 * upDoc interface for generating mapping between code and comments of single methods.
 * <p>
 * upDoc output's should not be a complex String. It is fine to print complete info
 * to the console, but the relevant information should be machine-readable -
 * thus return the mapping.
 */
public class MappingAnalyzer {

    /**
     * Given a path and a name of a file containing a method with its doc comment,
     * creates a mapping between the AST nodes of a method body and doc comment sentences
     * and prints the mapping to the standard output.
     *
     * @param path     folder where source code is located
     * @param fileName Java class (.java) file
     */
    public static MapBuilder reportMapping(String path, String fileName, String bowSimilarityThreshold) {

        try {

            MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(path + fileName);
            MapBuilder cm = new MapBuilder(mn, Double.parseDouble(bowSimilarityThreshold), false, "");

            return cm;

        } catch (NullPointerException ne) {

            String errorMsg = "[upDoc error] Unable to get method node from path: " + path + " and file: " + fileName;

            System.err.println(errorMsg);

            return null;
        }

    }

}
