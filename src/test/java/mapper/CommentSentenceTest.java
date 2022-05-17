package mapper;

import mapper.CommentSentence.CommentPart;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class CommentSentenceTest {

    @Test
    public void testToBagOfWords() {

        List<CommentSentence> sentenceList = new ArrayList<CommentSentence>();

        // lemmas
        sentenceList.add(new CommentSentence(0, CommentPart.DESC,
                "Returns an Image object that can then be painted on the screen."));
        // counts
        sentenceList.add(new CommentSentence(1, CommentPart.DESC,
                "The url argument must specify an absolute URL. "));
        sentenceList.add(new CommentSentence(2, CommentPart.DESC,
                "The name argument is a specifier that is relative to the url argument."));

        WordBag expectedBoW = new WordBag(
                List.of("imag",
                        "can",
                        "object",
                        "paint",
                        "return",
                        "screen"));


        assertEquals(expectedBoW.raw(), sentenceList.get(0).toBagOfWords().raw());

        assertEquals(6, sentenceList.get(1).toBagOfWords().size()); // url and URL are different lemmas
        assertEquals(2, sentenceList.get(2).toBagOfWords().getCount("argument"));

    }

}
