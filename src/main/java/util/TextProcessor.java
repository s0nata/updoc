package util;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import org.jsoup.Jsoup;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.util.ArrayList;

/**
 * This class collects NLP techniques for comment text processing. Target text unit granularity is sentence.
 */
public class TextProcessor {

    public static String preFormatString(String originalSentence) {

        // clean HTML markup
        String processedSentence = Jsoup.parse(originalSentence).text();

        // regex clean {@...} formatting around identifiers
        processedSentence =
                processedSentence
                        .replaceAll("[{@][a-zA-Z]*", "")
                        .replaceAll("}", "")
                        .replaceAll("\\s+", " ");

        return processedSentence;
    }

    public static ArrayList<Sentence> getSentencesFromText(String text) {

        // split description part of the doc comment into period-terminated sentences
        Document descriptionDoc = new Document(text);

        return new ArrayList<>(descriptionDoc.sentences());
    }

    /**
     * Given a word text, lemmatize it and then stem it.
     *
     * @param word to transform into lemma and then stem
     * @return the result of lemmatization+stemmating
     */
    public static String getStemmedLemma(String word) {
        //    String lemma = new Sentence(word).lemma(0);

        EnglishStemmer stemmer = new EnglishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        word = stemmer.getCurrent();
        return word;
    }

}
