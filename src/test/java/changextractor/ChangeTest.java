package changextractor;

import org.apache.commons.lang3.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeTest {

    private static final String FILE_DIR_FIX = "src/test/resources/fix-suggest/";

    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    // TODO: use more of other example files?
    private final String testFileBefore =
            "0_AdaptiveIsomorphismInspectorFactory.java";
    private final String testFileAfter =
            "1_AdaptiveIsomorphismInspectorFactory.java";

    private final Commit testCommit = new Commit(FILE_DIR_CHANGE + testFileBefore, FILE_DIR_CHANGE + testFileAfter);
    private final Change testChange = testCommit.getChanges().get(0);

    @Test
    public void testCalculationOfLOCs() {
        assertEquals(Range.between(219, 219), testChange.getSrcNodeFineLOCs());
    }

    @Test
    public void testNodeCalculation() {
        assertEquals("SimpleName", testChange.getSrcNodeFineType());
        assertEquals("MethodSignature", testChange.getSrcNodeCoarseType());
    }

    // TODO: parent method detection?

    // TODO: parent of class field?

    /* change detected for a commented class field access modifier (out of scope for the mvp though) */
    @Test
    public void testClassFieldModifierChange() {
        String fileBeforePath = FILE_DIR_FIX + "0_CompilerConfiguration.java";
        String fileAfterPath = FILE_DIR_FIX + "1_CompilerConfiguration.java";
        Commit testCommit = new Commit(fileBeforePath, fileAfterPath);

        // for this file only one change should be detected, in specific LOC
        assertEquals(Range.between(59, 59), testCommit.getChanges().get(0).getSrcNodeFineLOCs());
        assertEquals(Range.between(60, 60), testCommit.getChanges().get(0).getDstNodeFineLOCs());
    }

    @Test
    public void testInconsistentChangeAllParts() {
        //DateTimeFunctions.java
    }

    @Test
    public void testConsistentChangeAllParts() {
        // DateTimeFunctions.java
    }

    @Test
    public void testAdditionChanges() {
        // ESClientFactory.java
    }

    @Test
    public void testInterfaceMethodChangeOne() {
        // FieldIndex.java
    }

    @Test
    public void testInterfaceMethodChangeTwo() {
        // GenericToken.java
    }
}
