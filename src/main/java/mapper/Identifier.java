package mapper;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import uk.ac.open.crc.intt.IdentifierNameTokeniserFactory;
import util.TextProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This is a representation of program identifiers: variable, method, class and type names.
 */
public class Identifier {

    public enum KindOfID {
        VAR_NAME,
        METHOD_NAME,
        CLASS_NAME,
        TYPE_NAME,
        GENERIC
    }

    // Separator between tokens when splitting (to avoid merging them in a unique word).
    //  final String SEPARATOR = " ";

    private final String fullName;

    private final KindOfID kindOfID;

    private final ArrayList<String> splitName;

    public Identifier(String identifier, KindOfID kind) {

        fullName = identifier;

        kindOfID = kind;

        splitName = this.split();
    }

    /**
     * Split an identifier on case change ("camelCase" split), numbers or special characters legal in
     * Java identifier names and get a lemma of each constituent.
     *
     * <p>Regex splits: (a) either between a character that is not a line begin or capital letter, and
     * a capital letter (b) or between a character that is not a line begin, and a capital followed by
     * a non-capital.
     *
     * <p>After the splitting a lemma of each word is extracted and formatted to lowercase.
     *
     * @return Returns a list of strings that constitute the original identifier name.
     */
    public ArrayList<String> split() {

        String fullNameCopy = fullName;

        ArrayList<String> nameParts = new ArrayList<String>(); // return this

        // STEP 0: in the identifier expand numbers to words

        // match on numbers to solve them
        String patternNumbers = "([0-9]+)";
        if (fullNameCopy.matches(".*[0-9]+.*")) {
            fullNameCopy = fromCardinalToOrdinal(fullNameCopy, patternNumbers);
        }

        // STEP 1: split the identifier, either with regex or with INTT

        // List<String> matches = splitWithRegex(fullNameCopy);
        List<String> matches = splitWithINTT(fullNameCopy);


        // STEP 2: filter out stop words, expand abbreviations

        // extract stop words from resource file and store them in a list
        String stopwordsContent = "";
        try (InputStream inputStream = getClass().getResourceAsStream("/stopwords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            stopwordsContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> stopwords = Arrays.asList(stopwordsContent.split("\n"));

        // extract our custom abbreviation expansions from resource file and store them in a list
        String abbrevContent = "";
        try (InputStream inputStream = getClass().getResourceAsStream("/abbreviations.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            abbrevContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] abbrevs = abbrevContent.split("\n");

        // Check whether word belongs to stopwords list: if so it won't be part of BoW
        for (String word : matches) {
            if (!word.isEmpty() && !stopwords.contains(word)) {
                // Word is not part of stopwords. Is it an abbreviation that must be expanded?
                word = word.toLowerCase();
                boolean replacedAbbr = false;
                for (String abbr : abbrevs) {
                    String[] pair = abbr.split(":");
                    if (word.equals(pair[0])) {
                        word = pair[1];
                        replacedAbbr = true;
                        break;
                    }
                }

                // if no abbreviation from abbreviations.txt was used
                if (!replacedAbbr) {
                    word = AbbreviationExpander.expandAbbreviationIdentifier(word);
                }

                // Here we get lemmas and stems.
                // Note that stemming or lemmatizating words
                // heavily affects cosine sim (obviously), while it is basically irrelevant for
                // semantic measures like WMD.
                word = TextProcessor.getStemmedLemma(word);

                // Trimming to avoid extra spaces before/after tokens.
                nameParts.add(word.toLowerCase().trim());
            }
        }

        return nameParts.stream()
                .filter(part -> !part.isBlank())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private String fromCardinalToOrdinal(String fullName, String patternNumbers) {
        Pattern pattern = Pattern.compile(patternNumbers);
        Matcher matcher = pattern.matcher(fullName);
        String numbersContent = "";
        try (InputStream inputStream = getClass().getResourceAsStream("/numbers.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            numbersContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check whether word belongs to stopwords list: if so it won't be part of BoW
        String[] ordinalNumbers = numbersContent.split("\n");

        while (matcher.find()) {
            String number = matcher.group(1);
            for (String abbr : ordinalNumbers) {
                String[] pair = abbr.split(":");
                if (number.equals(pair[0])) {
                    // TODO to be verified that replaceFirst is not too naive (for multiple occurrences of
                    // same number).
                    fullName = fullName.replaceFirst(pair[0], pair[1]);
                    break;
                }
            }
            //    for(String word: fullName.split(patternNumbers)){
            //      switch (word){
            //        case "1": fullName = fullName.replace("1", "First");
            //        break;
            //        case "2": fullName = fullName.replace("2", "Second");
            //        break;
            //      }
        }

        return fullName;
    }

    private List<String> splitWithRegex(String identifierName) {

        // String patternTORADOCU = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
        // match positions between letters of different case (camelCase)
        String camelCasePattern = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
        // match on Java-legal special characters and remove them
        String patternOmitSpecial = "[#_$\\-/'\"<>?!()]";

        Pattern p = Pattern.compile("((?<![A-Z])|^)[A-Z]{3}(?![A-Z])|(?<![A-Z])[A-Z]{2}[a-z]+");
        Matcher m = p.matcher(identifierName);

        List<String> matches = new ArrayList<String>();

        while (m.find()) {
            String stringMatch = m.group();
            matches.add(stringMatch);
            identifierName = identifierName.replace(stringMatch, "#");
        }

        // identifier splitting, order matters: matching is greedy

        // empty words appear when a character is removed (special one or number)
        String finalPattern = patternOmitSpecial + "|" + camelCasePattern;

        if (identifierName.length() > 0) {
            matches.addAll(Arrays.asList(identifierName.split(finalPattern)));
        }

        return matches;
    }

    private List<String> splitWithINTT(String identifierName) {

        IdentifierNameTokeniserFactory idFactory = new IdentifierNameTokeniserFactory();
        IdentifierNameTokeniser idTokenizer = idFactory.create();

        return idTokenizer.tokenise(identifierName);

    }
    /**/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifier)) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(fullName, that.fullName)
                && kindOfID == that.kindOfID
                && Objects.equals(splitName, that.splitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, kindOfID, splitName);
    }

    @Override
    public String toString() {
        // can either return the original identifier name or its split into constituents, doing the
        // latter
        StringBuilder identifierString = new StringBuilder(" ");

        for (String constituent : this.splitName) {
            identifierString.append(constituent).append(" ");
        }

        return identifierString.toString();
    }

    public List<String> getSplitName() {
        return this.splitName;
    }
}
