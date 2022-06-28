package mapper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AbbreviationExpanderTest {

    // Tests if the abbreviation expansion expands abbreviations like supposed
    @Test
    public void testAbbreviationExpansion() {
        List<Identifier> idList = new ArrayList<Identifier>();
        idList.add(new Identifier("cmd", Identifier.KindOfID.GENERIC));
        idList.add(new Identifier("func", Identifier.KindOfID.GENERIC));
        idList.add(new Identifier("pwd", Identifier.KindOfID.GENERIC));
        idList.add(new Identifier("fib", Identifier.KindOfID.GENERIC));
        idList.add(new Identifier("hdr", Identifier.KindOfID.GENERIC));

        List<String> expectedExpansions = new ArrayList<String>();
        expectedExpansions.add("command");
        expectedExpansions.add("function");
        expectedExpansions.add("password");
        expectedExpansions.add("fibonacci");
        expectedExpansions.add("header");

        for (int i = 0; i < idList.size(); i++) {
            Assert.assertTrue(idList.get(i).getSplitName().contains(expectedExpansions.get(i)));
        }
    }
}
