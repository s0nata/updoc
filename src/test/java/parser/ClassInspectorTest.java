package parser;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ClassInspectorTest {

    private static final String FILE_DIR = "src/test/resources/change/";

    @Test
    public void testGetClass2Strings() {

        // test files
        ArrayList<String> fileNames = new ArrayList<String>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory");
        fileNames.add("BhandariKDisjointShortestPaths");
        fileNames.add("DefaultDirectedWeightedEdgeTest");
        fileNames.add("JGraphModelAdapter");

        // test file toString() representations
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("AdaptiveIsomorphismInspectorFactory has LOC range [63..268]");
        expected.add("BhandariKDisjointShortestPaths has LOC range [53..302]");
        expected.add("DefaultDirectedWeightedEdgeTest has LOC range [52..104]");
        expected
                .add("MyGraphListener has LOC range [391..422]JGraphModelAdapter has LOC range [87..423]");

        for (int i = 0; i < expected.size(); i++) {
            ArrayList<ClassNode> testClasses = ClassInspector
                    .getClassNodesFromFile(new File(FILE_DIR + "0_" + fileNames.get(i) + ".java"));
            String actual = "";
            for (int j = 0; j < testClasses.size(); j++) {
                actual += testClasses.get(j).toString();
            }
            assertEquals(expected.get(i), actual);
        }

    }

}
