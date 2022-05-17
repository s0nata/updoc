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

import static junit.framework.TestCase.assertTrue;

/**
 * All ICPC'19 analyzed examples' references are at:
 * https://docs.google.com/spreadsheets/d/1maRH6YY0OVuKSB2ACDhrz1-CQCS7sRHHoaZJDvLWi2Y/edit#gid=0
 */
public class ICPC19StatsTest {

    /**
     * Where the source code of the examples are located
     */
    private static final String TEST_FPATH = "src/test/resources/icpc19/";
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
    private static final double SIMILARITY_THRESHOLD = -1;

    boolean useWMD = Boolean.valueOf(System.getProperty("useWMD"));

    @AfterClass
    public static void tearDown() {
        DecimalFormat format = new DecimalFormat("0.00");
        float hitPerc = HIT / TOTAL * 100;
        float missPerc = MISS / TOTAL * 100;
        System.out.println();
        System.out.println("-- Tearing down --");
        System.out.println("Percentage of HITS: " + format.format(hitPerc) + "% (" + HIT + " over " + TOTAL + ")");
        System.out.println("Percentage of MISS: " + format.format(missPerc) + "% (" + MISS + " over " + TOTAL + ")");
        assertTrue(hitPerc >= 80.00);
    }

    private MapBuilder createTestMapping(String path, String fileName) {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(path + fileName);

        MapBuilder mapping = new MapBuilder(mn, SIMILARITY_THRESHOLD, useWMD, "0.00");

        System.out.println("\nMapping for " + fileName + " : ");
        System.out.println(mapping);

        return mapping;
    }

    @Test
    public void testCommitID9_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));
        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

//        TOTAL += RELATED_PARTS.size();
        TOTAL += RELATED_IDS.size();
        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID9_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID9_1_a.java");

//        computeRelatedPartHits(RELATED_PARTS, actualMapping_a, actualMapping_b);
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID9_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));
        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID9_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID9_2_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID379() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.PARAM));
        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(2));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID379_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID379_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID523() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.PARAM,
//                        CommentSentence.CommentPart.DESC,
//                        CommentSentence.CommentPart.RETURN));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(1, 2, 3));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID523_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID523_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID279_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC,
//                        CommentSentence.CommentPart.PARAM,
//                        CommentSentence.CommentPart.RETURN));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 1, 2));
        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID279_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID279_1_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID279_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC,
//                        CommentSentence.CommentPart.PARAM,
//                        CommentSentence.CommentPart.RETURN));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 1, 2));
        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID279_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID279_2_a.java");

        // Auto-generated comments fixed
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID369() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC,
//                        CommentSentence.CommentPart.PARAM,
//                        CommentSentence.CommentPart.RETURN));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 3, 4));
        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID369_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID369_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID60_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.PARAM,
//                        CommentSentence.CommentPart.RETURN));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 1));
        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID60_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID60_1_a.java");

        // Comment with only format fixes
        // TODO Check for return (ID 1), semantically very close but not regarded as same (0.88 diff)
        computeSameIDs(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID60_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.PARAM,
//                        CommentSentence.CommentPart.RETURN));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 1));
        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID60_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID60_2_a.java");

        // Comment with only format fixes
        computeSameIDs(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID79_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));
        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID79_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID79_1_a.java");

        // Grammar
        computeSameIDs(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID530_1() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
                List.of(CommentSentence.CommentPart.DESC));

        TOTAL += RELATED_PARTS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID530_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID530_1_a.java");

        // Readability
        // FIXME sum of similarities here
        computeSameParts(RELATED_PARTS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID368_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC,
//                        CommentSentence.CommentPart.PARAM));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID368_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID368_1_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID368_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.PARAM));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(1));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID368_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID368_2_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID582_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(1));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID582_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID582_1_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID582_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(1));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID582_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID582_2_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID428_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID428_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID428_2_a.java");

        // TODO check - sim are very close, the new one get lower score probably bc
        // TODO it's longer (more noise in bow)
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID428_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID428_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID428_1_a.java");

        // TODO to be honest, this isn't a real comment fix, it pretty much says the same...
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test

    public void testCommitID428_3() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID428_3_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID428_3_a.java");

        // FIXME code+doc change: reather check that similarity does not significantly decrease,
        // FIXME e.g. does not go below -0.2 FOR EACH ID!
        computeSameIDs(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

//    @Test
//    public void testCommitID428_4() {
////        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
////                List.of(CommentSentence.CommentPart.DESC));
//
//        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));
//
//        TOTAL += RELATED_IDS.size();
//
//        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID428_4_b.java");
//        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID428_4_a.java");
//
//        // FIXME code+doc change: reather check that similarity does not significantly decrease,
//        // FIXME e.g. does not go below -0.2 FOR EACH ID!
//        computeSameIDs(RELATED_IDS, actualMapping_a, actualMapping_b);
//    }

    @Test
    public void testCommitID151() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(1));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID151_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID151_a.java");

        // TODO this example is basically a removed TODOcomment
        // TODO indeed similarity is only slighty higher (+ ~0.01)
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID166() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0, 1));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID166_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID166_a.java");

        // FIXME I don't know if I would include this example. Code changed significantly
        // FIXME and doc added a lot of details bc of it. We can just assume sim is still good,
        // FIXME like in the other doc+code changes?
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }


    @Test
    public void testCommitID391_1() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID391_1_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID391_1_a.java");

        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID391_2() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID391_2_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID391_2_a.java");

        // FIXME check this, I think after bow should be better
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }

    @Test
    public void testCommitID391_3() {
//        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(
//                List.of(CommentSentence.CommentPart.DESC));

        final ArrayList<Integer> RELATED_IDS = new ArrayList<>(List.of(0));

        TOTAL += RELATED_IDS.size();

        MapBuilder actualMapping_b = createTestMapping(TEST_FPATH, "CommitID391_3_b.java");
        MapBuilder actualMapping_a = createTestMapping(TEST_FPATH, "CommitID391_3_a.java");

        // TODO this is very similar to previous, yet this change is significantly better,
        // TODO while the previous is worse!
        computeRelatedIDsHits(RELATED_IDS, actualMapping_a, actualMapping_b);
    }


    // FIXME Maybe move the following methods in another class e.g. "Statistics"

    /**
     * Given a list of comment parts that should be classified as "related" and two mappings
     * (one legitimate, the other not) computes whether the legitimate mapping reports the
     * related parts with a higher similarity than the non-legitimate one (it calls
     * #getSimilarityOfCommentPart to retrieve the similarity of a given part)
     *
     * @param RELATED_PARTS   list of related comment parts
     * @param legitMapping    the code-comment mapping that is legitimate
     * @param nonLegitMapping the code-comment mapping that is not legitimate
     */
    private void computeRelatedPartHits(ArrayList<CommentSentence.CommentPart> RELATED_PARTS,
                                        MapBuilder legitMapping,
                                        MapBuilder nonLegitMapping) {

        System.out.println("Related sentences should be: " + RELATED_PARTS.toString());

        for (CommentSentence.CommentPart expectedPart : RELATED_PARTS) {
            double legitMappingSim = getSimilarityOfCommentPart(expectedPart, legitMapping);
            double nonLegitMappingSim = getSimilarityOfCommentPart(expectedPart, nonLegitMapping);
            if (legitMappingSim > nonLegitMappingSim) {
                System.out.println("For " + expectedPart + ":" + " Success");
                HIT++;
            } else {
                MISS++;
                System.out.println("For " + expectedPart + ":" + " Fail");
            }
        }
        System.out.println("---------------------------");
    }

    /**
     * //FIXME it's even more complex than that, IDs can correspond to different PARTS
     * //FIXME in one mapping and the other (lines that get deleted, added...)
     * Given a list of sentences IDs that should be classified as "related" and two mappings
     * (one legitimate, the other not) computes whether the legitimate mapping reports the
     * related sentences with a higher similarity than the non-legitimate one (it calls
     * #getSimilarityofIndex to retrieve the similarity of a given sentence)
     *
     * @param RELATED_IDS     list of related sentences IDs
     * @param legitMapping    the code-comment mapping that is legitimate
     * @param nonLegitMapping the code-comment mapping that is not legitimate
     */
    private void computeRelatedIDsHits(ArrayList<Integer> RELATED_IDS,
                                       MapBuilder legitMapping,
                                       MapBuilder nonLegitMapping) {

        System.out.println("Related sentences should be (ID): " + RELATED_IDS.toString());

        for (int expectedIndex : RELATED_IDS) {
            double legitMappingSim = getSimilarityOfIndex(expectedIndex, legitMapping);
            double nonLegitMappingSim = getSimilarityOfIndex(expectedIndex, nonLegitMapping);
            if (legitMappingSim > nonLegitMappingSim) {
                System.out.println("For " + expectedIndex + ":" + " Success");
                HIT++;
            } else {
                MISS++;
                System.out.println("For " + expectedIndex + ":" + " Fail");
            }
        }
        System.out.println("---------------------------");
    }

    private void computeSameIDs(ArrayList<Integer> RELATED_IDS,
                                MapBuilder legitMapping,
                                MapBuilder nonLegitMapping) {

        System.out.println("Related sentences should be: " + RELATED_IDS.toString());

        for (Integer expectedID : RELATED_IDS) {
            double legitMappingSim = getSimilarityOfIndex(expectedID, legitMapping);
            double nonLegitMappingSim = getSimilarityOfIndex(expectedID, nonLegitMapping);
            if (legitMappingSim == nonLegitMappingSim) {
                System.out.println("For " + expectedID + ":" + " Success");
                HIT++;
            } else {
                MISS++;
                System.out.println("For " + expectedID + ":" + " Fail");
            }
        }
        System.out.println("---------------------------");
    }

    // FIXME if it is the same doc, I expect the sum of EACH part to be equal.
    private void computeSameParts(ArrayList<CommentSentence.CommentPart> RELATED_PARTS,
                                  MapBuilder legitMapping,
                                  MapBuilder nonLegitMapping) {

        System.out.println("Related sentences should be: " + RELATED_PARTS.toString());

        for (CommentSentence.CommentPart expectedPart : RELATED_PARTS) {
            double legitMappingSim = getSimilarityOfCommentPart(expectedPart, legitMapping);
            double nonLegitMappingSim = getSimilarityOfCommentPart(expectedPart, nonLegitMapping);
            if (legitMappingSim == nonLegitMappingSim) {
                System.out.println("For " + expectedPart + ":" + " Success");
                HIT++;
            } else {
                MISS++;
                System.out.println("For " + expectedPart + ":" + " Fail");
            }
        }
        System.out.println("---------------------------");
    }

    /**
     * Given a specific comment part that should be related to the code and a mapping code-comment,
     * returns the similarity of such part in the given mapping
     *
     * @param RELATED_PART the comment part we are interested in knowing the similarity
     * @param mapping      the code-doc mapping
     * @return the similarity of the part in the mapping
     */
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
                    // FIXME but this is different than @params. Multiple params are different
                    // FIXME parameters. Multiple DESC is always the same DESC.
                    // FIXME Instead of passing "related part", pass the exact sentence index
                    //
                    // FIXME This could cost maintenance if for some text processing reasons
                    // FIXME sentences ID will be computed differently in the future, but
                    // FIXME there is not more clever solution atm
                    //
                    // FIXME see new getSimilarityOfSentenceIndexes
                    highestSimilarity = similarity;
                }
            }
        }
        return highestSimilarity;
    }


    private double getSimilarityOfIndex(int relatedIndex, MapBuilder mapping) {
        double similarity = 0;
        LinkedHashMap<Integer, Double> sentencesAndSimilarity = mapping.getMapping().get(0);
        for (Integer sentenceId : sentencesAndSimilarity.keySet()) {
            // The related sentence in its original representation
//            CommentSentence relatedSentence = mapping.getMethodComment().getSentenceByIndex(sentenceId);
            if (relatedIndex == sentenceId) {
                similarity = sentencesAndSimilarity.get(sentenceId);
            }
        }
        return similarity;
    }

}
