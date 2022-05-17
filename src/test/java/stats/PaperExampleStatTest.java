package stats;

import mapper.CommentSentence;
import mapper.MapBuilder;
import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PaperExampleStatTest {
    /**
     * Where the source code of the examples are located
     */
    private static final String TEST_FPATH = "src/test/resources/paper-example/";

    /**
     * Set the threshold to -1 to see all the computed similarities in the output (including the 0.0 ones)
     */
    private static final double SIMILARITY_THRESHOLD = 0.25;

    private MapBuilder createTestMapping(String path, String fileName, boolean semantic) {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(path + fileName);

        MapBuilder mapping = new MapBuilder(mn, SIMILARITY_THRESHOLD, semantic, "0.00");

        System.out.println("\nMapping for " + fileName + " : ");
        System.out.println(mapping);

        return mapping;
    }

    @Test
    public void testMappingAdaptiveIsomorphismInspectorFactory() {
        final ArrayList<CommentSentence.CommentPart> RELATED_PARTS = new ArrayList<>(List.of(
                CommentSentence.CommentPart.DESC));

        System.out.println("Compare mapping of inconsistent and consistent example, USING WMD:");
        // Inconsistent example
        MapBuilder inconsMapping = createTestMapping(TEST_FPATH, "AdaptiveIsomorphismInspectorFactory_i.java",
                true);
        // Consistent example
        MapBuilder consistMapping = createTestMapping(TEST_FPATH, "AdaptiveIsomorphismInspectorFactory_c.java",
                true);

        isOriginalCommentIdentified(RELATED_PARTS, consistMapping, inconsMapping);


        System.out.println("Compare mapping of inconsistent and consistent example, WITHOUT WMD (cosine sim):");
        // Inconsistent example
        inconsMapping = createTestMapping(TEST_FPATH, "AdaptiveIsomorphismInspectorFactory_i.java",
                false);
        // Consistent example
        consistMapping = createTestMapping(TEST_FPATH, "AdaptiveIsomorphismInspectorFactory_c.java",
                false);

        isOriginalCommentIdentified(RELATED_PARTS, consistMapping, inconsMapping);
    }

    // FIXME Maybe move the following methods in another class e.g. "Statistics"
    private void isOriginalCommentIdentified(ArrayList<CommentSentence.CommentPart> RELATED_PARTS,
                                             MapBuilder correctMapping,
                                             MapBuilder inconsistentMapping) {

        System.out.println("Related sentences should be: " + RELATED_PARTS.toString());

        for (CommentSentence.CommentPart expectedPart : RELATED_PARTS) {
            double correct_sim = getSimilarityOfCommentPart(expectedPart, correctMapping);
            double incosistent_sim = getSimilarityOfCommentPart(expectedPart, inconsistentMapping);
            if (correct_sim > (incosistent_sim + 0.01)) {
                System.out.println("Success");
            } else {
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
