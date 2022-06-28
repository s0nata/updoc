package entry;

import mapper.MapBuilder;
import mapper.RelatedSentence;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class MapperTest {

    private static final String FILE_DIR = "src/test/resources/mapping/";

    private static final String CORECT_FILE_NAME = "AdaptiveIsomorphismInspectorFactory_c.java";
    //private static final String WRONG_FILE_NAME = "BadFile.java";

    private static final String SIMILARITY_THRESHOLD = "0.2";

    /**
     * To accept minor deviations in similarity: as the implementation of upDoc changes,
     * some new features may slightly deviate the final similarity computation --
     * this shouldn't bring to major breaks in previously expected similarities,
     * nor should be considered as a wrong similarity computation
     * (this test serves as regression testing)
     */
    private static final Double TOLERABLE_ERROR = 0.05;


    // FIXME why is the actual similarity score so much worse?
    @Ignore
    @Test
    public void testReportMappingExpectedFlow() {

        // FIXME separate important parts (for comparison) in static fields
//        String expectedOutput = ""
//            + "\tAST nodes:\n"
//            + "\tid:0\t[1:exception,1:argument,1:unsupported,1:void,1:assert,1:type,1:illegal,3:graph]\n"
//            + "\trelated comment sentences:\n"
//            + "\tid:0\tsim:0.56\tChecks if one of the graphs is from unsupported graph type and throws IllegalArgumentException if it is.\n"
//            + "\tid:1\tsim:0.47\tThe current unsupported types are graphs with multiple-edges.\n"
//            + "\tid:2\tsim:0.75\tgraph\n"
//            + "\tid:3\tsim:0.75\tgraph\n"
//            + "\tid:4\tsim:0.43\tillegal argument exception\n";

        List<RelatedSentence> expectedSimilarities = new ArrayList<>();
        expectedSimilarities.add(new RelatedSentence(0, "", 0.80));
        expectedSimilarities.add(new RelatedSentence(1, "", 0.50));
        expectedSimilarities.add(new RelatedSentence(2, "", 0.70));
        expectedSimilarities.add(new RelatedSentence(3, "", 0.70));
        expectedSimilarities.add(new RelatedSentence(4, "", 0.35));

        int expectedNodeID = 0;

        Hashtable<Integer, List<RelatedSentence>> expectedMapping = new Hashtable<>();
        expectedMapping.put(expectedNodeID, expectedSimilarities);

        MapBuilder actualOutput = Mapper.reportMapping(
                FILE_DIR, CORECT_FILE_NAME, SIMILARITY_THRESHOLD);

        Hashtable<Integer, List<RelatedSentence>> actualMapping = actualOutput.getMapping();
        Collection<List<RelatedSentence>> actualSimilaritiesView = actualMapping.values();

        // FIXME a proper comparator of mapping should be implemented, for now we go like this
        //assertEquals(expectedMapping, actualMapping);

        Iterator<List<RelatedSentence>> iteratorActual = actualSimilaritiesView.iterator();
        List<RelatedSentence> actualSimilarities = iteratorActual.next();

        for (RelatedSentence sentence : expectedSimilarities) {
            Double expectedSim = sentence.getSimilarityToNode();
            Optional<RelatedSentence> actualRelatedSentence = actualSimilarities
                    .stream()
                    .filter(x -> x.getSentenceID() == sentence.getSentenceID())
                    .findFirst();
            if (actualRelatedSentence.isPresent()) {
                double actualSim = actualRelatedSentence.get().getSimilarityToNode();
                assertTrue(actualSim >= expectedSim - TOLERABLE_ERROR &&
                        actualSim <= expectedSim + TOLERABLE_ERROR);

            }
        }

        // FIXME also add some asserts or other tests to verify that the bag of words are equal
        System.out.println(actualOutput);

    }

}
