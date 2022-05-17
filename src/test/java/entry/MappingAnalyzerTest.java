package entry;

import mapper.MapBuilder;
import org.junit.Test;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertTrue;

public class MappingAnalyzerTest {

    private static final String FILE_DIR = "src/test/resources/paper-example/";

    private static final String CORECT_FILE_NAME = "AdaptiveIsomorphismInspectorFactory_c.java";

    private static final String SIMILARITY_THRESHOLD = "0.2";

    /**
     * To accept minor deviations in similarity: as the implementation of upDoc changes,
     * some new features may slightly deviate the final similarity computation --
     * this shouldn't bring to major breaks in previously expected similarities,
     * nor should be considered as a wrong similarity computation
     * (this test serves as regression testing)
     */
    private static final Double TOLERABLE_ERROR = 0.05;


    @Test
    public void testReportMappingExpectedFlow() {

        LinkedHashMap<Integer, Double> expectedSimilarities = new LinkedHashMap<>();
        expectedSimilarities.put(0, 0.80);
        expectedSimilarities.put(1, 0.50);
        expectedSimilarities.put(2, 0.70);
        expectedSimilarities.put(3, 0.70);
        expectedSimilarities.put(4, 0.35);
        int expectedNodeID = 0;
        Hashtable<Integer, LinkedHashMap<Integer, Double>> expectedMapping = new Hashtable<>();
        expectedMapping.put(expectedNodeID, expectedSimilarities);

        MapBuilder actualOutput = MappingAnalyzer.reportMapping(
                FILE_DIR, CORECT_FILE_NAME, SIMILARITY_THRESHOLD);

        Hashtable<Integer, LinkedHashMap<Integer, Double>> actualMapping = actualOutput.getMapping();
        Collection<LinkedHashMap<Integer, Double>> actualSimilaritiesView = actualMapping.values();

        Iterator<LinkedHashMap<Integer, Double>> iterator = actualSimilaritiesView.iterator();
        LinkedHashMap<Integer, Double> actualSimilarities = iterator.next();

        for (Integer sentenceID : expectedSimilarities.keySet()) {
            Double expectedSim = expectedSimilarities.get(sentenceID);
            Double actualSim = actualSimilarities.get(sentenceID);
            assertTrue(actualSim >= expectedSim - TOLERABLE_ERROR &&
                    actualSim <= expectedSim + TOLERABLE_ERROR);
        }

        // FIXME also add some asserts or other tests to verify that the bag of words are equal
        System.out.println(actualOutput);

    }

}
