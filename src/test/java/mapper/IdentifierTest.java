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
        expectedSplits.add(new ArrayList<>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "real", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "second", "real", "second", "world")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "second", "world", "twentieth")));
        expectedSplits.add(new ArrayList<>(List.of("hello", "world")));
        expectedSplits.add(new ArrayList<>(List.of("world", "eleventh")));

        // expectedSplits.add(new ArrayList<String>(List.of("jgraph", "tedg")));         // regex split results
        expectedSplits.add(new ArrayList<String>(List.of("j", "graph", "t", "edg")));    // intt split results
        // expectedSplits.add(new ArrayList<String>(List.of("jgraph", "txxx")));          // regex split results
        expectedSplits.add(new ArrayList<String>(List.of("j", "graph", "txxx")));         // intt split results
        expectedSplits.add(new ArrayList<String>(List.of("get", "uml", "diagram", "bodi")));
        // expectedSplits.add(new ArrayList<String>(List.of("abc", "foo", "bar")));  // regex split results
        expectedSplits.add(new ArrayList<String>(List.of("ab", "cfoo", "bar")));     // intt split results
        expectedSplits.add(new ArrayList<String>(List.of("my", "int32", "variabl")));

        ArrayList<ArrayList<String>> actualSplits = new ArrayList<ArrayList<String>>();
        actualSplits.add(new Identifier("helloRealWorld", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("HelloRealWorld", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("HELLO_REAL_WORLD", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("hello$real_World", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("Hello$$real$World", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("hello_real_world", KindOfID.VAR_NAME).split());

        actualSplits.add(new Identifier("hello2Real2World", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("HELLO2WORLD20", KindOfID.VAR_NAME).split());

        actualSplits.add(new Identifier("helloWORLD", KindOfID.VAR_NAME).split());

        actualSplits.add(new Identifier("WORLD_11", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("JGraphTEdge", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("JGraphTXXX", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("getUMLDiagramBody", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("ABCfooBar", KindOfID.VAR_NAME).split());
        actualSplits.add(new Identifier("MyInt32Var", KindOfID.VAR_NAME).split());

        for (int i = 0; i < actualSplits.size(); i++) {
            assertEquals(expectedSplits.get(i), actualSplits.get(i));
        }
    }
}
