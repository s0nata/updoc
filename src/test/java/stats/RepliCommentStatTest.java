package stats;

import mapper.CommentSentence;
import mapper.MapBuilder;
import org.junit.AfterClass;
import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * All RepliComment examples' references are in:
 * doc-code-analysis/updoc/experiments/repo-mining/data-sources-manual/replicomment.md
 */
public class RepliCommentStatTest {

    /**
     * Where the source code of the examples are located
     */
    // TODO shouldn't these be tests' resources? They are in the root folder of upDoc for now
    private static final String TEST_FPATH = "replicomment/";
    /**
     * Total comment clones
     */
    private static float TOTAL = 0;
    /**
     * Total of distinguished clones
     */
    private static float HIT = 0;
    /**
     * Total of unrecognized clones
     */
    private static float MISS = 0;

    /**
     * Set the threshold to -1 to see all the computed similarities in the output (including the 0.0 ones)
     */
    private static final double SIMILARITY_THRESHOLD = 0.25;

    @AfterClass
    public static void tearDown() {
        DecimalFormat format = new DecimalFormat("0.00");
        float hitPerc = HIT / TOTAL * 100;
        float missPerc = MISS / TOTAL * 100;
        System.out.println();
        System.out.println("-- Tearing down --");
        System.out.println("Percentage of HITS: " + format.format(hitPerc) + "% (" + HIT + " over " + TOTAL + ")");
        System.out.println("Percentage of MISS: " + format.format(missPerc) + "% (" + MISS + " over " + TOTAL + ")");
        assertEquals(HIT, 35.0, 0);
    }

    private MapBuilder createTestMapping(String path, String fileName) {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(path + fileName);

        MapBuilder mapping = new MapBuilder(mn, SIMILARITY_THRESHOLD);

        System.out.println("\nMapping for " + fileName + " : ");
        System.out.println(mapping);

        return mapping;
    }

    @Test
    public void testMappingApacheCommon1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(CommentSentence.CommentPart.RETURN));

        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheCommon_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheCommon_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheCommon2() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN,
                CommentSentence.CommentPart.PARAM));

        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheCommon_2_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheCommon_2_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheCommon3() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheCommon_3_o.java");

        //FIXME why is there "java.util.NoSuchElementException: No value present"?--NS
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheCommon_3_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheCommon4() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheCommon_4_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheCommon_4_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheCommon5() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheCommon_5_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheCommon_5_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheCommon6() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheCommon_6_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheCommon_6_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheHadoop1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheHadoop_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheHadoop_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheHadoop2() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheHadoop_2_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheHadoop_2_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingApacheHadoop3() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ApacheHadoop_3_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ApacheHadoop_3_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch2() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_2_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_2_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch3() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_3_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_3_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch4() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_4_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_4_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch5() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_5_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_5_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch6() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC,
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_6_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_6_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch7() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_7_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_7_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch8() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC,
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_8_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_8_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch9() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_9_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_9_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingElasticSearch10() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC,
                CommentSentence.CommentPart.PARAM));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "ElasticSearch_10_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "ElasticSearch_10_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingGuava1() {
        // TODO It is interesting to note down that original and clone of Guava's example
        // TODO have exactly the same similarity: this is because the original method
        // TODO can be identified only via semantic matching (it was discovered via Jdoctor)

        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Guava_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Guava_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx2() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_2_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_2_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx3() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.PARAM));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_3_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_3_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx4() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_4_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_4_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx5() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_5_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_5_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx6() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_6_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_6_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx7() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_7_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_7_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx8() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_8_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_8_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx9() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_9_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_9_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingVertx10() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Vertx_10_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Vertx_10_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingSpring1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Spring_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Spring_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingSpring2() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Spring_2_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Spring_2_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingSpring3() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN,
                CommentSentence.CommentPart.DESC));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Spring_3_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Spring_3_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingLog4j1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Log4j_1_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Log4j_1_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }

    @Test
    public void testMappingLog4j2() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.RETURN));
        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_o = createTestMapping(TEST_FPATH, "Log4j_2_o.java");
        MapBuilder actualMapping_c = createTestMapping(TEST_FPATH, "Log4j_2_c.java");

        isOriginalCommentIdentified(RELATED_PARTS, actualMapping_o, actualMapping_c);
    }


    // FIXME Maybe move the following methods in another class e.g. "Statistics"
    private void isOriginalCommentIdentified(ArrayList<CommentSentence.CommentPart> RELATED_PARTS,
                                             MapBuilder actualMapping_o,
                                             MapBuilder actualMapping_c) {

        System.out.println("Related sentences should be: " + RELATED_PARTS.toString());

        for (CommentSentence.CommentPart expectedPart : RELATED_PARTS) {
            double original_sim = getSimilarityOfCommentPart(expectedPart, actualMapping_o);
            double clone_sim = getSimilarityOfCommentPart(expectedPart, actualMapping_c);
            if (original_sim > clone_sim) {
                System.out.println("Success");
                HIT++;
            } else {
                MISS++;
                System.out.println("Fail");
            }
        }
        System.out.println("---------------------------");
    }

    private double getSimilarityOfCommentPart(CommentSentence.CommentPart RELATED_PART,
                                              MapBuilder mapping) {
        double similarity = 0;
        double highestSimilarity = 0;
        LinkedHashMap<Integer, Double> sentencesAndSimilarity = mapping.getMapping().get(0);
        for (Integer sentenceId : sentencesAndSimilarity.keySet()) {
            // The related sentence in its original representation
            CommentSentence relatedSentence = mapping.getMethodComment().getSentenceByIndex(sentenceId);
            if (relatedSentence.getPart().equals(RELATED_PART)) {
                similarity = sentencesAndSimilarity.get(sentenceId);
                if (similarity > highestSimilarity) {
                    // If multiple DESC parts, only the last one would be saved;
                    // let's save the most similar one instead
                    highestSimilarity = similarity;
                }
            }
        }
        return highestSimilarity;
    }


}
