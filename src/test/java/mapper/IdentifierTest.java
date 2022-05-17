package mapper;

import mapper.Identifier.KindOfID;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IdentifierTest {

    @Test
    public void testIdentifierSplitting() {
        // FIXME some examples here do not work as expected, check one by one.

        ArrayList<List<String>> expectedSplits = new ArrayList<List<String>>();
        expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
//    expectedSplits.add(new ArrayList<String>(List.of("hello", "real", "world")));
//    expectedSplits.add(new ArrayList<String>(List.of("hello", "world")));
        expectedSplits.add(new ArrayList<String>(List.of("hello", "world")));
//    expectedSplits.add(new ArrayList<String>(List.of("world")));

        // FIXME: "jGraphTEdge" -> "j Graph TEdge"
        expectedSplits.add(new ArrayList<String>(List.of("j", "graph", "tedg")));

        ArrayList<ArrayList<String>> actualSplits = new ArrayList<ArrayList<String>>();
        actualSplits.add(new Identifier("helloRealWorld", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("HelloRealWorld", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("HELLO_REAL_WORLD", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("hello$real_World", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("Hello$$real$World", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("hello_real_world", KindOfID.VAR_NAME).split());

        //TODO decide how we manage digits in identifiers: we may not want to split the following ones.
//    actualSplits.add(new Identifier("hello2real2world", KindOfID.VAR_NAME).split());
//    actualSplits.add(new Identifier("HELLO2WORLD33", KindOfID.VAR_NAME).split());

        actualSplits.add(new Identifier("helloWORLD", KindOfID.VAR_NAME).split());

        //TODO Same as above.
//    actualSplits.add(new Identifier("WORLD_11", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("jGraphTEdge", KindOfID.VAR_NAME).split());

        for (int i = 0; i < actualSplits.size(); i++) {
            assertEquals(expectedSplits.get(i), actualSplits.get(i));
        }
    }

}
