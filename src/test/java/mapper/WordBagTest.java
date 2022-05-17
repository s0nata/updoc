package mapper;

import mapper.CommentSentence.CommentPart;
import org.junit.Test;

import java.text.DecimalFormat;

import static junit.framework.TestCase.assertEquals;

public class WordBagTest {

    @Test
    public void testjaccardSim1() {

        CommentSentence descriptionSentence =
                new CommentSentence(0, CommentPart.DESC, "get input stream");
        CommentSentence tagSentence =
                new CommentSentence(1, CommentPart.EXCEP, "get output stream");

        WordBag bow1 = descriptionSentence.toBagOfWords();
        WordBag bow2 = tagSentence.toBagOfWords();

        assertEquals(0.5, bow1.jaccardSim(bow2)); // 1/2= 0.5
    }

    @Test
    public void testjaccardSim2() {

        CommentSentence descriptionSentence =
                new CommentSentence(0, CommentPart.DESC, "throws an exception if it is");
        CommentSentence tagSentence =
                new CommentSentence(1, CommentPart.EXCEP, "throw illegal argument exception");

        WordBag bow1 = descriptionSentence.toBagOfWords();
        WordBag bow2 = tagSentence.toBagOfWords();
        DecimalFormat df = new DecimalFormat("0.00");
        assertEquals(0.5, bow1.jaccardSim(bow2)); // 2/7 = 0.2857142857142857
    }

    @Test
    public void testCosineSim1() {

        CommentSentence descriptionSentence =
                new CommentSentence(0, CommentPart.DESC, "get input stream");
        CommentSentence tagSentence = new CommentSentence(1, CommentPart.EXCEP, "get output stream");

        WordBag bow1 = descriptionSentence.toBagOfWords();
        WordBag bow2 = tagSentence.toBagOfWords();

        assertEquals(0.6666666666666667, bow1.cosineSim(bow2)); // 2/3 = 0.6666666666666667
    }

    @Test
    public void testCosineSim2() {

        CommentSentence descriptionSentence =
                new CommentSentence(0, CommentPart.DESC, "throws an exception if it is");
        CommentSentence tagSentence =
                new CommentSentence(1, CommentPart.EXCEP, "throw illegal argument exception");

        WordBag bow1 = descriptionSentence.toBagOfWords();
        WordBag bow2 = tagSentence.toBagOfWords();

        DecimalFormat df = new DecimalFormat("0.00");
        assertEquals(df.format(0.71), df.format(bow1.cosineSim(bow2))); // 1/sqrt(5) = 0.4472135954999579

    }


}
