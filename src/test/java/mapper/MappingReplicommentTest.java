package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class MappingReplicommentTest {

    static final String TEST_FPATH = "replicomment/";

    static final double SIMILARITY_THRESHOLD = 0.3;

    private MapBuilder createTestMapping(String path, String fileName) {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(path + fileName);

        MapBuilder mapping = new MapBuilder(mn, SIMILARITY_THRESHOLD);

        System.out.println("\nMapping for " + fileName + " : ");
        System.out.println(mapping);

        return mapping;
    }

    @Test
    public void testMappingApacheCommon1o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_1_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedOriginal = new ArrayList<>(List.of(0, 3, 4, 5));

        assertEquals(expectedOriginal, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingApacheCommon1c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_1_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1, 4, 5));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingApacheCommon2o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_2_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN
                + " "
                + CommentSentence.CommentPart.PARAM);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 2, 3, 4));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingApacheCommon2c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_2_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 4));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingApacheCommon3o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_3_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.DESC);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1, 2));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingApacheCommon3c() {
        //FIXME why is there "java.util.NoSuchElementException: No value present"?--NS

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_3_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1, 2, 3, 4));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingApacheCommon4o() {
        // TODO notice that here we want a match between an abbreviation and its full word!
        // FIXME also, id splitting seems off, double check

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_4_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingApacheCommon4c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheCommon_4_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingApacheHadoop1o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheHadoop_1_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingApacheHadoop1c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ApacheHadoop_1_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of());

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingElasticSearch1o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ElasticSearch_1_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.DESC);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingElasticSearch1c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ElasticSearch_1_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of());

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingElasticSearch2o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ElasticSearch_2_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingElasticSearch2c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "ElasticSearch_2_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 1, 2));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingGuava1o() {
        // TODO It is interesting to note down that original and clone of Guava's example
        // TODO have exactly the same similarity: this is because the original method
        // TODO can be identified only via semantic matching (it was discovered via Jdoctor)

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Guava_1_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 2, 3));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingGuava1c() {
        // TODO It is interesting to note down that original and clone of Guava's example
        // TODO have exactly the same similarity: this is because the original method
        // TODO can be identified only via semantic matching (it was discovered via Jdoctor)

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Guava_1_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 2, 4, 5));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingVertx1o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_1_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.DESC);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingVertx1c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_1_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of());

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingVertx2o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_2_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingVertx2c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_2_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of());

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingVertx3o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_3_o.java");

        System.out.print("\tRelated sentences should be: "
                + CommentSentence.CommentPart.PARAM);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingVertx3c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_3_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(1));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }

    @Test
    public void testMappingVertx4o() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_4_o.java");

        System.out.print("\tRelated  sentences should be: "
                + CommentSentence.CommentPart.RETURN);

        System.out.println("\n---------------------------");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 4));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));

    }

    @Test
    public void testMappingVertx4c() {

        MapBuilder actualMapping = createTestMapping(TEST_FPATH, "Vertx_4_c.java");

        ArrayList<Integer> expectedMappingSentences = new ArrayList<>(List.of(0, 3));

        assertEquals(expectedMappingSentences, actualMapping.getValuesAtNode(0));
    }
}
