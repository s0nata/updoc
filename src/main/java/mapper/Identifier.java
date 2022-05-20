package mapper;

import edu.stanford.nlp.simple.Sentence;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is a representation of program identifiers: variable, method, class and type names.
 */
public class Identifier {

    // FIXME Ari - what is the exact difference between Type_Name and Class_Name?
    public enum KindOfID {
        VAR_NAME, METHOD_NAME, CLASS_NAME, TYPE_NAME
    }

  private final String fullName;

    private final KindOfID kindOfID;

    private final ArrayList<String> splitName;

    public Identifier(String identifier, KindOfID kind) {

        fullName = identifier;

        kindOfID = kind;

        splitName = this.split();

    }

    public String getFullName() {
        return fullName;
    }

    public KindOfID getKindOfID() {
        return kindOfID;
    }

    /**
     * Split an identifier on case change ("camelCase" split), numbers or special characters legal in
     * Java identifier names and get a lemma of each constituent.
     *
     * <p>
     * Regex splits: (a) either between a character that is not a line begin or capital letter, and a
     * capital letter (b) or between a character that is not a line begin, and a capital followed by a
     * non-capital.
     *
     * <p>
     * After the splitting a lemma of each word is extracted and formatted to lowercase.
     *
     * @return Returns a list of strings that constitute the original identifier name.
     */
    public ArrayList<String> split() {

        ArrayList<String> nameParts = new ArrayList<String>();

        // String patternTORADOCU = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

        // match positions between letters of different case (camelCase)
        String patternLowerToUpper = "(?<!(^|[A-Z]))(?=[A-Z])";
        // match on Java-legal special characters and remove them
        // FIXME I am adding the dash too for now
        String patternOmitSpecial = "[#_$\\-]";
        // match on numbers and remove them
        // FIXME is this the best choice, though? Parameters may be distinguished from one another
        // FIXME by numbers (e.g., param1 and param2): maybe we need to be aware of that distinction
        String patternOmitNumber = "[0-9]+";

        // identifier splitting, order matters: matching is greedy
        // empty words appear when a character is removed (special one or number)
        String pattern = patternOmitSpecial + "|" + patternOmitNumber + "|" + patternLowerToUpper;

        String stopwordsContent = "";
        String abbrevContent = "";

        try (InputStream inputStream = getClass().getResourceAsStream("/stopwords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            stopwordsContent = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream inputStream = getClass().getResourceAsStream("/abbreviations.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            abbrevContent = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check whether word belongs to stopwords list: if so it won't be part of BoW
        List<String> stopwords = Arrays.asList(stopwordsContent.split("\n"));
        List<String> abbrevs = Arrays.asList(abbrevContent.split("\n"));
        extractAllLemmas(this.fullName, nameParts, pattern, stopwords, abbrevs);

        return nameParts;
    }

    private void extractAllLemmas(String fullName, ArrayList<String> nameParts, String pattern, List<String> stopwords, List<String> abbrevs) {
        for (String word : fullName.split(pattern)) {
            if (!word.isEmpty() && !stopwords.contains(word)) {
                // Word is not part of stopwords. Is it an abbreviation that must be expanded?
                word = word.toLowerCase();
                for (String abbr : abbrevs) {
                    //FIXME add init and enum in abbreviations.
                    String[] pair = abbr.split(":");
                    if (word.equals(pair[0])) {
                        word = pair[1];
                        if (word.contains("-")) {
                            extractAllLemmas(word, nameParts, pattern, stopwords, abbrevs);
                        }
                        break;
                    }
                }
                word = getStemmedLemma(word);
                // TODO check if lowercase is necessary, stemmer should already do this
                word = word.trim();
                if (!word.isEmpty()) {
                    nameParts.add(word.toLowerCase());
                }
            }
        }
    }

    /**
     * Given a word text, lemmatize it and then stem it.
     *
     * @param word to transform into lemma and then stem
     * @return the result of lemmatization+stemmating
     */
    private String getStemmedLemma(String word) {
        String lemma = new Sentence(word).lemma(0);

        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(lemma);
        stemmer.stem();
        word = stemmer.getCurrent();
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifier)) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(fullName, that.fullName) &&
                kindOfID == that.kindOfID &&
                Objects.equals(splitName, that.splitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, kindOfID, splitName);
    }

    @Override
    public String toString() {

        // can either return the original identifier name or its split into constituents, doing the
        // latter
        // FIXME: remove extra spaces before and after the splits
        String identfierString = " ";

        for (String constituent : this.splitName) {
            identfierString += constituent + " ";
        }

        return identfierString;
    }


}
