package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class MapBuilderTest {

    static final String TEST_FPATH = "src/test/resources/mapping/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory_c.java";

    static final double SIMILARITY_THRESHOLD = 0.2;

    private MapBuilder createTestMapping() {
        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);
        return new MapBuilder(mn, SIMILARITY_THRESHOLD, false, "");
    }

    @Test
    public void shouldMapToAllSentences() {
        MapBuilder actualOutput = createTestMapping();
        // Shallow comparison to verify that all sentences map (without looking at
        // precise similarities or anything else)
        LinkedHashSet<Integer> expectedMapping = new LinkedHashSet<>(List.of(0, 1, 2, 3, 4));
        Hashtable<Integer, List<RelatedSentence>> actualMapping = actualOutput.getMapping();
        Collection<List<RelatedSentence>> similaritiesView = actualMapping.values();
        List<RelatedSentence> actualRelSent = similaritiesView.iterator().next();
        assertTrue(actualRelSent.stream().allMatch(s -> expectedMapping.contains(s.getSentenceID())));
    }

}
