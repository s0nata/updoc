package mapper;

import edu.stanford.nlp.simple.Sentence;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a sentence in a code comment (which may be composed of multiple sentences).
 * A sentence is identified by its own id w.r.t the whole comment: the first sentence of the comment
 * has ID==0, the 2nd sentence has ID==1, and so on. For each sentence, we also know what part of
 * the Javadoc it belonged to (@param, @return, etc.). All this information will be used by
 * MapBuilder.
 *
 * <p>This class takes care of performing all the NLP operations needed on sentences from Javadoc
 * comments (represented as strings), such as: removing HTML markup, filtering stop words,
 * lemmatization etc.
 *
 * <p>Functionality can be used both for strings containing multiple and single sentences.
 *
 * <p>Stanford CORE NLP functionality is used.
 */
public class CommentSentence {

    public enum CommentPart {
        DESC,
        PARAM,
        EXCEP,
        RETURN,
        INLINE,
        OTHER
    }

    private final Integer id;

    private final CommentPart part;

    private final String originalString;

    private boolean hasSentenceStructure;

    private Sentence sentenceStructure; // CoreNLP representation

    public CommentSentence(int id, CommentPart part, String originalText) {

        this.id = id;
        this.part = part;
        this.originalString = originalText;
        this.hasSentenceStructure = false;

        try {

            this.sentenceStructure = new Sentence(originalText);
            this.hasSentenceStructure = true;

        } catch (IllegalStateException ilsException) {

            System.err.println(ilsException.getMessage());
        }
    }

    public Integer getId() {

        return this.id;
    }

    public CommentPart getPart() {
        return this.part;
    }

    /**
     * Note that CamelCase IDs get split in comment just as they are for AST nodes.
     */
    public WordBag toBagOfWords() {

        WordBag sentenceBoW = new WordBag();

        if (this.hasSentenceStructure) {
            for (String word :
                    this.sentenceStructure.words().stream()
                            .filter(word -> !word.equals("``"))
                            .collect(Collectors.toList())) {
                // treat every lemma as an identifier
                Identifier id = new Identifier(word, Identifier.KindOfID.VAR_NAME);
                for (String lemmaFromId : id.split()) {
                    sentenceBoW.add(lemmaFromId.toLowerCase());
                }
            }
            removePunctuation(sentenceBoW);

            removeStopWords(sentenceBoW);
        }

        return sentenceBoW;
    }

    private void removePunctuation(WordBag stringBag) {

        List<String> punctuationSymbols = Arrays.asList(".", ",", "lrb", "rrb");

        for (String symbol : punctuationSymbols) {
            if (stringBag.contains(symbol)) {
                stringBag.remove(symbol);
            }
        }
    }

    private void removeStopWords(WordBag stringBag) {

        // TODO: this is ad-hoc stop word filtering, use Lucene or a more flexible solution, e.g., see
        // https://github.com/stanfordnlp/CoreNLP/blob/master/data/edu/stanford/nlp/patterns/surface/stopwords.txt

        List<String> stopWords = Arrays.asList("a", "an", "the", "this", "that");

        for (String symbol : stopWords) {
            if (stringBag.contains(symbol)) {
                stringBag.remove(symbol);
            }
        }
    }

    @Override
    public String toString() {

        return this.originalString;
    }
}
