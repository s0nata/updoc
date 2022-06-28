package entry;

import mapper.MapBuilder;
import parser.MethodInspector;
import parser.MethodNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * upDoc interface for generating mapping between code and doc comments of
 * single methods.
 */
public class Mapper {

    /**
     * Given a path and a name of a file containing a method with its doc
     * comment, creates a mapping between the AST nodes of a method body and doc
     * comment sentences
     *
     * @param path                   folder where source code is located
     * @param fileName               Java class (.java) file
     * @param bowSimilarityThreshold minimal similarity value at which code and
     *                               comment units are considered related
     */
    public static MapBuilder reportMapping(String path, String fileName,
                                           String bowSimilarityThreshold) {

        try {

            MethodNode mn =
                    MethodInspector.getSingleMethodNodeFromFile(path + fileName);
            MapBuilder cm =
                    new MapBuilder(mn, Double.parseDouble(bowSimilarityThreshold),
                            false, "");

            return cm;

        } catch (NullPointerException ne) {

            String errorMsg =
                    "[upDoc error] Unable to get method node from path: " + path
                            + " and file: " + fileName;

            System.err.println(errorMsg);

            return null;
        }
    }

    /**
     * Given a path and a name of a file containing a class with methods with their doc or inline
     * comments, creates a mapping between the AST nodes of the method bodies and comment sentences
     * and returns a List of MapBuilder
     *
     * @param path     folder where source code is located
     * @param fileName Java class (.java) file
     * @param semantic
     */
    public static List<MapBuilder> reportMappingsFromSource(
            String path, String fileName, String bowSimilarityThreshold, boolean semantic) {

        try {

            List<MethodNode> mbnList =
                    MethodInspector.getMethodNodesFromFile(new File(path + fileName));

            List<MapBuilder> mapBuilders = new ArrayList<MapBuilder>();

            for (int i = 0; i < mbnList.size(); i++) {
                mapBuilders.add(
                        new MapBuilder(mbnList.get(i), Double.parseDouble(bowSimilarityThreshold), semantic, ""));
            }

            return mapBuilders;

        } catch (NullPointerException ne) {

            String errorMsg =
                    "[upDoc error] Unable to get method nodes from path: " + path + " and file: " + fileName;

            System.err.println(errorMsg);

            return null;
        }
    }
}
