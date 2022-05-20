package mapper;

import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class MapBuilderTest {

    static final String TEST_FPATH = "mapping/";

    static final String TEST_FILE = "AdaptiveIsomorphismInspectorFactory.java";

    static final double SIMILARITY_THRESHOLD = 0.2;

    private MapBuilder createTestMapping() {

        MethodNode mn = MethodInspector.getSingleMethodNodeFromFile(TEST_FPATH + TEST_FILE);

        return new MapBuilder(mn, SIMILARITY_THRESHOLD);
    }

    @Test
    public void shouldMapToAllSentences() {

        MapBuilder actualOutput = createTestMapping();

        // Shallow comparison to verify that all sentences map (without looking at
        // precise similarities or anything else)
        LinkedHashSet<Integer> expectedMapping = new LinkedHashSet<>(List.of(0, 1, 2, 3, 4));

        Hashtable<Integer, LinkedHashMap<Integer, Double>> actualMapping = actualOutput.getMapping();
        Collection<LinkedHashMap<Integer, Double>> similaritiesView = actualMapping.values();
        LinkedHashMap<Integer, Double> actualSim = similaritiesView.iterator().next();
        assertEquals(expectedMapping, actualSim.keySet());
    }

}
