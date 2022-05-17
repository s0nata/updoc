package util;

import edu.stanford.nlp.simple.Sentence;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TextProcessorTest {

    @Test
    public void testPreFormatString() {
        String commentText = ""
                + "If no corresponding cell found, returns <code>null</code>. \n"
                + "Returns the <b>JGraph</b> edge {@code cell} that corresponds to the specified JGraphT edge.\n"
                + "See the <a href=\"{@docRoot}/copyright.html\">Copyright</a>. \n";

        String expectedString = ""
                + "If no corresponding cell found, returns null. "
                + "Returns the JGraph edge cell that corresponds to the specified JGraphT edge. "
                + "See the Copyright.";

        String actualString = TextProcessor.preFormatString(commentText);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetSentences() {

        String commentText = ""
                + "Returns an Image object that can then be painted on the screen. \n"
                + "The url argument must specify an absolute {@link URL}. The name\n"
                + "argument is a specifier that is relative to the url argument. \n" + "<p>\n"
                + "This method always returns immediately, whether or not the \n"
                + "image exists. When this applet attempts to draw the image on\n"
                + "the screen, the data will be loaded. The graphics primitives \n"
                + "that draw the image will incrementally paint on the screen. \n";

        ArrayList<String> expectedSentences = new ArrayList<>();
        expectedSentences.add("Returns an Image object that can then be painted on the screen.");
        expectedSentences.add("The url argument must specify an absolute URL.");
        expectedSentences.add("The name argument is a specifier that is relative to the url argument.");
        expectedSentences
                .add("This method always returns immediately, whether or not the image exists.");
        expectedSentences
                .add("When this applet attempts to draw the image on the screen, the data will be loaded.");
        expectedSentences
                .add("The graphics primitives that draw the image will incrementally paint on the screen.");

        ArrayList<Sentence> actualSentences = TextProcessor.getSentencesFromText(TextProcessor.preFormatString(commentText));

        for (int i = 0; i < actualSentences.size(); i++) {
            assertEquals(expectedSentences.get(i), actualSentences.get(i).toString());
        }

    }

}
