package semantic;

import mapper.WordBag;
import semantic.wmd4j.WordMovers;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main component. Contains all the methods to compute the {@code SemantichMatch}es for a given
 * class. This implements the "basic" semantic semantic, i.e. the one that uses plain vector sums.
 * Other kinds of matcher will extend this class.
 */
public class SemanticMatcher {

    /**
     * Tells whether the semantic matching is enabled or not according to configuration parameters.
     */
//  private static boolean enabled;

    /**
     * List of words to be ignored in the comment and code element name when performing semantic
     * matching.
     */
//  private final ArrayList<String> stopwords;

    /**
     * Threshold up to which a similarity distance is considered acceptable. Zero is perfect
     * similarity.
     */
    private float wmdThreshold;

    public SemanticMatcher(boolean stopWordsRemoval, float distanceThreshold, float wmdThreshold) {
        this.wmdThreshold = wmdThreshold;
//
//    // TODO can this naive list be improved?
//    stopwords =
//        new ArrayList<>(
//            Arrays.asList(
//                "true",
//                "false",
//                "the",
//                "a",
//                "and",
//                "or",
//                "to",
//                "if",
//                "either",
//                "whether",
//                "else",
//                "otherwise",
//                "for",
//                "be",
//                "have",
//                "this",
//                "do",
//                "not",
//                "of",
//                "in",
//                "null",
//                "only",
//                "already",
//                "specify"));
    }

//  public static boolean isEnabled() {
//    return enabled;
//  }

//  public static void setEnabled(boolean enabled) {
//    SemanticMatcher.enabled = enabled;
//  }

    /**
     * Entry point to run the semantic matching through vector sums. Takes the list of candidates
     * involved in the matching, the method for which calculating the matching, the comment to match
     * and the corresponding proposition. Returns the best matches.
     *
     * @param codeElements list of potentially candidate {@code CodeElement}s
     * @param method the method which the comment to match belongs
     * @param subject the subject {@code CodeElement}
     * @param proposition the {@code Proposition} extracted from the comment
     * @param comment comment text @return a map containing the best matches together with the
     *     distance computed in respect to the comment
     * @return the best matches
     * @throws IOException if there were problems reading the vector model
     */
//  public LinkedHashMap<CodeElement<?>, Double> runSemanticMatch(
//      List<CodeElement<?>> codeElements,
//      DocumentedExecutable method,
//      CodeElement<?> subject,
//      Proposition proposition,
//      String comment)
//      throws IOException {
//
////    stopwords.add(method.getDeclaringClass().getSimpleName().toLowerCase());
//    return wmdMatch(comment, proposition, subject, method, codeElements);
//  }

    /**
     * Returns true if the {@code DocumentedExecutable} is a setter and the possible candidate is the
     * symmetric getter
     *
     * @param candidate the possible candidate {@code MethodCodeElement}
     * @param method the {@code DocumentedExecutable}
     * @return true if candidate and method are respectively the setter and the getter
     */
//  private boolean areComplementary(MethodCodeElement candidate, DocumentedExecutable method) {
//    String candidateName = candidate.getJavaCodeElement().getName();
//    if (candidateName.matches("(.*)get[A-Z](.*)")) {
//      String property = candidateName.split("get")[1];
//      return method.getName().equals("set" + property);
//    }
//
//    return false;
//  }

    /**
     * Parse the original tag comment. Special characters are removed. Then the comment is normalized
     * to lower case and lemmatization is applied. As a last step, stopwords are removed.
     *
     * @return the parsed comment in form of array of strings (words retained from the original
     *     comment)
     */
//  private List<String> parseComment(String comment) {
//    comment = comment.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
//
//    ArrayList<String> wordComment = new ArrayList<String>(Arrays.asList(comment.split(" ")));
//    int index = 0;
//    List<CoreLabel> lemmas = StanfordParser.lemmatize(comment);
//    for (CoreLabel lemma : lemmas) {
//      if (lemma != null) {
//        if (index < wordComment.size()) {
//          wordComment.remove(index);
//        }
//        wordComment.add(index, lemma.lemma());
//      }
//      index++;
//    }
//
//    return this.removeStopWords(wordComment);
//  }

    /**
     * Compute semantic distance through Word Mover's Distance.
     */
    public static double wmdMatch(WordBag one, WordBag two){
        try {
            WordMovers wm = WordMovers.Builder()
                            .wordVectors(GloveModelWrapper.getInstance().getGloveTxtVectors())
                            .build();

            double dist = 10;
            try {
                dist = wm.distance(one.asSentence(), two.asSentence());
            } catch (Exception e) {
                // do nothing
            } return dist;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return -1.0;
    }

    /**
     * Split code element name according to camel case
     *
     * @param name code element name
     * @return list of words composing the code element name
     */
//  private List<String> parseCodeElementName(String name) {
//    ArrayList<String> camelId = new ArrayList<>(Arrays.asList(name.split("(?<!^)(?=[A-Z])")));
//    String joinedId = String.join(" ", camelId).replaceAll("\\s+", " ").trim().toLowerCase();
//    int index = 0;
//    for (CoreLabel lemma : StanfordParser.lemmatize(joinedId)) {
//      if (lemma != null) {
//        if (index < camelId.size()) {
//          camelId.remove(index);
//        }
//        camelId.add(index, lemma.lemma());
//      }
//      index++;
//    }
//    return camelId;
//  }

    /**
     * Compute and instantiate the {@code SemantiMatch} computed for a tag.
     *
     * @param methodName name of the method the tag belongs to
     * @param distances  the computed distance, for every possible code element candidate, from the
     *                   comment
//     */
//    private LinkedHashMap<CodeElement<?>, Double> retainMatches(
//            List<String> commentWords, String methodName, Map<CodeElement<?>, Double> distances) {
//        if (commentWords.size() > 8) {
//            wmdThreshold = 5.96f;
//        }
//
//        // Select as candidates only code elements that have a semantic distance below the chosen
//        // threshold.
//        LinkedHashMap<CodeElement<?>, Double> orderedDistances;
//
//        if (!distances.isEmpty()) {
//            distances.values().removeIf(aDouble -> aDouble > wmdThreshold);
//        }
//
//        // Order the retained distances from the lowest (best one) to the highest (worst one).
//        orderedDistances =
//                distances
//                        .entrySet()
//                        .stream()
//                        .sorted(Map.Entry.comparingByValue())
//                        .collect(
//                                Collectors.toMap(
//                                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//
//        return orderedDistances;
//    }

    /**
     * Remove stopwords from given list of {@code String}s
     *
     * @param words list of {@code String}s to be cleaned
     * @return the cleaned list of words
     */
//    private List<String> removeStopWords(List<String> words) {
//        for (int i = 0; i < words.size(); i++) {
//            String word = words.get(i).toLowerCase();
//            if (this.stopwords.contains(word)) {
//                words.remove(i);
//                words.add(i, "");
//            }
//        }
//
//        List<String> wordList = new ArrayList<>(words);
//        wordList.removeAll(Collections.singletonList(""));
//        return wordList;
//    }
}
