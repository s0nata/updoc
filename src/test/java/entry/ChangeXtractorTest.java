package entry;

import changextractor.Change;
import changextractor.Commit;
import org.apache.commons.lang3.Range;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Currently only tests for "update-node" change type are active.
 *
 * <p>Ignored tests are skeletons for testing the functionality planned to be added later (other
 * change types, e.g., update-tree, add/delete...).
 *
 * <p>Failing tests are for the functionality that should be already working.
 */
public class ChangeXtractorTest {

    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    @Test
    public void testParameterNameUpdateDetection() {
        String dirName = FILE_DIR_CHANGE;
        String fileNameBefore = "0_AdaptiveIsomorphismInspectorFactory.java";
        String fileNameAfter = "1_AdaptiveIsomorphismInspectorFactory.java";

        // expected
        int numUpdatesExpected = 1;

        // actual
        Commit testCommit = new Commit(dirName + fileNameBefore, dirName + fileNameAfter);
        int numUpdatesActual = testCommit.getChanges().size();

        // test
        assertEquals(numUpdatesExpected, numUpdatesActual);
    }

    @Test
    public void testMultilineSignatureLOCsCalculation() {
        String dirName = FILE_DIR_CHANGE;
        String fileNameBefore = "0_AdaptiveIsomorphismInspectorFactory.java";
        String fileNameAfter = "1_AdaptiveIsomorphismInspectorFactory.java";

        // expected
        Range<Integer> expectedLOCsBefore = Range.between(218, 222);
        Range<Integer> expectedLOCsAfter = Range.between(217, 219);

        // actual
        Commit testCommit = new Commit(dirName + fileNameBefore, dirName + fileNameAfter);
        Range<Integer> actualLOCsBefore = testCommit.getChanges().get(0).getSrcNodeCoarseLOCs();
        Range<Integer> actualLOCsAfter = testCommit.getChanges().get(0).getDstNodeCoarseLOCs();

        // test
        assertEquals(expectedLOCsBefore, actualLOCsBefore);
        assertEquals(expectedLOCsAfter, actualLOCsAfter);
    }

    // TODO do we have example of change in signature where signature is in single row? Test LOCs end calculation

    // TODO do we have example of signature with 2+ annotations? Test LOCs begin calculation

    /* consistent code-comment co-change */
    @Ignore
    @Test
    public void testParameterDeletionInConstructor() {
        //        String dirName = FILE_DIR_CHANGE;
        //        String fileNameBefore = "0_BhandariKDisjointShortestPaths.java";
        //        String fileNameAfter = "1_BhandariKDisjointShortestPaths.java";

        // parameter 'k' deleted in constructor in LOCs 65..99/63..88
    }

    /* inconsistent change: added code to match comment
    (but only in the lead sentence, respective @param missing) */
    @Ignore
    @Test
    public void testParameterAddition() {
        //        String dirName = FILE_DIR_CHANGE;
        //        String fileNameBefore = "0_BhandariKDisjointShortestPaths.java";
        //        String fileNameAfter = "1_BhandariKDisjointShortestPaths.java";

        // parameter 'k' added in getPaths() in LOCs 101..151/90..144
    }

    /* test class, degraded constructor comment */
    @Ignore
    @Test
    public void testCommentWorsening() {
        //        String dirName = FILE_DIR_CHANGE;
        //        String fileNameBefore = "0_DefaultDirectedWeightedEdgeTest.java";
        //        String fileNameAfter = "1_DefaultDirectedWeightedEdgeTest.java";

        // for constructor in LOC 52..58/53..56
    }

    /* test class, filled method comment */
    @Ignore
    @Test
    public void testCommentImprovement() {
        //        String dirName = FILE_DIR_CHANGE;
        //        String fileNameBefore = "0_DefaultDirectedWeightedEdgeTest.java";
        //        String fileNameAfter = "1_DefaultDirectedWeightedEdgeTest.java";

        // for testEqualsObject in LOC 61..82/60..80
    }

    // FIXME currently failing because a change in the body is detected
    //       and the target change in the signature is not detected
    //       see #20
    /* parameter type change should be detected */
    @Ignore
    @Test
    public void testParameterTypeChange() {
        String dirName = FILE_DIR_CHANGE;
        String fileNameBefore = "0_JGraphModelAdapter.java";
        String fileNameAfter = "1_JGraphModelAdapter.java";

        Commit testCommit = new Commit(dirName + fileNameBefore, dirName + fileNameAfter);

        List<Change> actualChanges = testCommit.getChanges();
        boolean changeInSignatureDetected = false;
        for (Change curChange : actualChanges) {
            if (curChange.getSrcNodeCoarseType().equals("MethodSignature")) {
                changeInSignatureDetected = true;
            }
        }

        assertEquals(true, changeInSignatureDetected);
    }
}
