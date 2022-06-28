package parser.nodes;

import com.github.javaparser.ast.Node;
import mapper.Identifier;
import mapper.WordBag;
import org.apache.commons.lang3.Range;
import util.TextProcessor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * upDoc's intermediate representation of a coarse-grained AST node within a method body.
 *
 * <p>The idea behind specializations was to allow for custom word bags depending on custom node
 * type.
 *
 * <p>TODO Maybe it is micromanagement at this point
 *
 * <p>
 *
 * @see parser.nodes.ForNode
 * @see parser.nodes.WhileNode
 * @see parser.nodes.IfNode
 * @see parser.nodes.SwitchNode
 * @see parser.nodes.TryCatchNode
 * @see parser.nodes.SingleAssignmentStatementNode
 * @see parser.nodes.SingleMethodCallSequenceNode
 * @see parser.nodes.SingleMethodReturnStatementNode
 */
public class SpecializedNode implements AbstractNode {

    /*
    private static List<String> sWordSynonymFileNames =
            List.of("commons_id_process.csv", "iReport_id_process.csv", "jajuk_id_process.csv",
            "javaHMO_id_process.csv", "jbidwatcher_id_process.csv");
    */
    private static final String SYNONYM_DIR = "synonym-lists/";
    private static final String KEYWORD_DIR = "keyword-lists/";
    /*
     * key: Node class name
     * value: exclude word list for that node class, the corresponding file for a given Node class
     *        will be read only the first time, then it will remain in memory
     */
    // TODO: can be part of a baseline performance comparison of different similarities
    private static final Map<String, List<String>> excludeLists = new HashMap<String, List<String>>();
    /*
     * key: Node class name
     * value: list of words to add in vocabulary for that node class, the corresponding file for a
     *        given Node class will be read only the first time, then it will remain in memory
     */
    // TODO: can be part of a baseline performance comparison of different similarities
    private static final Map<String, List<String>> includeLists = new HashMap<String, List<String>>();
    /*
     * key: word, value: list of synonyms for this word (e.g. "add: [append,plus]"
     * and there will also be "append: [add,plus]" and "plus: [add,append]") the
     * synonym files are read only once, then they remain in memory
     */
    // TODO: can be part of a baseline performance comparison of different similarities
    private static final Map<String, List<String>> synonymLists = new HashMap<String, List<String>>();
    private static final List<String> synonymFileNames =
            List.of("Howard13Synonyms.txt", "mined-pairs-97orig.csv", "Sridhara08Synonyms.txt");
    /*
     * key: Node class name
     * value: substitute words map for that node class (for the substitute map, the key string
     * is substituted by the value string), the corresponding file for a given Node class will be
     * read only the first time, then it will remain in memory
     */
    private static final Map<String, Map<String, String>> substituteMaps =
            new HashMap<String, Map<String, String>>();
    protected Range<Integer> LOC;
    protected Optional<List<InlineComment>> comments;
    private final Node astNode; // original node from the parser tool
    /* If there are multiple lines of comments above the node, they are not detected as orphan
    comments, therefore the MethodNode passes all the comments that it contains to every node
    in the methodNodesSequence so they can remove them in order to not be included in the
    identifierList*/
    private Optional<List<InlineComment>> potentialOrphanComments;

    public SpecializedNode(
            Node astNode, Optional<List<InlineComment>> comments, int lBegin, int lEnd) {
        this.comments = comments;
        this.astNode = astNode;
        this.LOC = Range.between(lBegin, lEnd);
        potentialOrphanComments = Optional.empty();
    }

    private static List<String> getResourceFileAsStringList(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) throw new IOException("Resource file not found");
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().filter(line -> !line.isBlank()).collect(Collectors.toList());
            }
        }
    }

    private static void addWordPairToSynonymList(String wordA, String wordB) {
        if (synonymLists.containsKey(wordA) && !synonymLists.get(wordA).contains(wordB)) {
            synonymLists.get(wordA).add(wordB);
        } else if (!synonymLists.containsKey(wordA)) {
            synonymLists.put(wordA, new ArrayList<String>());
            synonymLists.get(wordA).add(wordB);
        }
    }

    public static Map<String, List<String>> getSynonymLists() {
        if (synonymLists.isEmpty()) {
            buildSynonymList();
        }
        return synonymLists;
    }

    private static void buildSynonymList() {
        for (String file_name : synonymFileNames) {
            try {
                for (String line : getResourceFileAsStringList(SYNONYM_DIR + file_name)) {
                    String[] splitted_line = line.split(",");
                    String wordA = splitted_line[0].trim();
                    String wordB = splitted_line[1].trim();
                    addWordPairToSynonymList(wordA, wordB);
                    addWordPairToSynonymList(wordB, wordA);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            for (String line :
                    getResourceFileAsStringList(SYNONYM_DIR + "mined-java-large.csv").stream()
                            .skip(1)
                            .collect(Collectors.toList())) {
                String[] splitted_line = line.split("\\s");
                int projectCount = Integer.parseInt(splitted_line[3]);
                if (projectCount > 3) {
                    String wordA = splitted_line[0].trim();
                    String wordB = splitted_line[1].trim();
                    addWordPairToSynonymList(wordA, wordB);
                    addWordPairToSynonymList(wordB, wordA);
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Those synonyms seemed too specific for me, and would in my opinion add noise more than help:

        // try {
        //   for (String file : sWordSynonymFileNames) {
        //     for (String line : getResourceFileAsStringList(SYNONYM_DIR + "SWordNet/" + file)) {
        //       String[] splitted_line = line.split(",");

        //       // if synonym, 4th column == 1 (because the dataset contains also antonyms)
        //       if (Integer.valueOf(splitted_line[3].trim()) == 1) {
        //         String wordA = splitted_line[0].trim();
        //         String wordB = splitted_line[1].trim();
        //         addWordPairToSynonymList(wordA, wordB);
        //         addWordPairToSynonymList(wordB, wordA);
        //       }
        //     }
        //   }
        // } catch (IOException e) {
        //   // TODO Auto-generated catch block
        //   e.printStackTrace();
        // }
    }

    // TODO: any reason this was protected?
    public void addPotentialOrphanComments(Optional<List<InlineComment>> potentialOrphanComments) {
        this.potentialOrphanComments = potentialOrphanComments;
    }

    /**
     * Creates a BoW representation from non-split code identifiers of this AST node
     *
     * @return a BoW of this AST node
     */
    public WordBag toIdentifierList() {

        List<String> keywordsToExclude = getKeywordsToExclude();
        List<String> keywordsToInclude = getKeywordsToInclude();
        Map<String, String> keywordsToSubstitue = getKeywordsToSubstitute();

        String raw_code = astNode.toString();

        // remove inline comments that could be contained in the current AST node

        if (comments.isPresent()) {
            for (InlineComment comment : comments.get()) {
                raw_code = raw_code.replace(comment.toString(), "");
            }
        }

        if (potentialOrphanComments.isPresent()) {
            for (InlineComment comment : potentialOrphanComments.get()) {
                raw_code = raw_code.replace(comment.toString(), "");
            }
        }

        // extract keywords delimited by non-alphanumerical symbols
        List<String> identifierList =
                List.of(raw_code.split("\\W+")).stream().collect(Collectors.toList());

        // remove words contained in keywordsToExclude
        identifierList =
                identifierList.stream()
                        .filter(word -> !keywordsToExclude.contains(word))
                        .collect(Collectors.toList());

        // add words contained in keywordsToInclude that are not already in identifierList
        for (String keyword : keywordsToInclude) {
            if (!identifierList.contains(keyword)) {
                identifierList.add(keyword);
            }
        }

        // substitute words that are contained as key in keywordsToSubstitue by the
        // corresponding value
        identifierList =
                identifierList.stream()
                        .map(
                                word ->
                                        keywordsToSubstitue.containsKey(word)
                                                ? keywordsToSubstitue.get(word)
                                                : word)
                        .collect(Collectors.toList());

        return new WordBag(identifierList);
    }

    /**
     * Creates a BoW representation from non-split identifiers of this node, enriched with synonyms if
     * specified so
     *
     * @param addSynonyms flag to specify is synonyms are added to the node's BoW
     * @return a BoW of the current AST node
     */
    @Override
    public WordBag toIdentifierList(boolean addSynonyms) {
        WordBag identifierList = toIdentifierList();
        if (!addSynonyms) {
            return identifierList;
        } else {
            Map<String, List<String>> synonymList = getSynonymLists();
            identifierList.toList().stream()
                    .filter(word -> synonymList.containsKey(word))
                    .map(word -> synonymList.get(word))
                    .flatMap(List::stream)
                    .forEach(word -> identifierList.add(word));
            return identifierList;
        }
    }

    /**
     * Creates a BoW representation from split code identifiers of this AST node. Identifiers are
     * split into stems of full English words
     *
     * @return a BoW of this AST node
     */
    @Override
    public WordBag toExpandedIdentifierList() {
        return new WordBag(
                toIdentifierList().toList().stream()
                        .map(
                                identifier ->
                                        new Identifier(identifier, Identifier.KindOfID.GENERIC).getSplitName())
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
    }

    /**
     * Creates a BoW representation from split code identifiers of this AST node, enriched with
     * synonyms if specified so.
     *
     * <p>Identifiers are split into stems of full English words, synonyms are also stems
     *
     * @return a BoW of this AST node
     */
    @Override
    public WordBag toExpandedIdentifierList(boolean synonyms) {

        WordBag expandedIdentifierList = toExpandedIdentifierList();
        if (!synonyms) {
            return expandedIdentifierList;
        } else {
      /* The words in the expandedIdentifierList are stemmed, therefore we need to apply the
      same stemmer to the keys of the synonymList map */
            Map<String, List<String>> synonymList = getSynonymLists();
            Map<String, List<String>> synonymListWithStemmedKeys = new HashMap<String, List<String>>();

            for (Map.Entry<String, List<String>> e : synonymList.entrySet()) {
                String key = TextProcessor.getStemmedLemma(e.getKey());
                List<String> value = e.getValue();
                if (!synonymListWithStemmedKeys.containsKey(key)) {
                    List<String> listCopy = new ArrayList<String>();
                    listCopy.addAll(value);
                    synonymListWithStemmedKeys.put(key, listCopy);
                } else {
                    for (String word : value) {
                        if (!synonymListWithStemmedKeys.get(key).contains(word)) {
                            synonymListWithStemmedKeys.get(key).add(word);
                        }
                    }
                }
            }

      /*We also apply the stemmer to the synonyms because the expanded identifier list
      is stemmed */
            expandedIdentifierList.toList().stream()
                    .filter(word -> synonymListWithStemmedKeys.containsKey(word))
                    .map(word -> synonymListWithStemmedKeys.get(word))
                    .flatMap(List::stream)
                    .map(word -> TextProcessor.getStemmedLemma(word))
                    .forEach(word -> expandedIdentifierList.add(word));
        }
        return expandedIdentifierList;
    }

    /*
     * if node class name key not in excludeLists map, read file, else get value
     * corresponding to key
     */
    private List<String> getKeywordsToExclude() {
        // className can be any child class of SpecializedNode
        String className = this.getClass().getSimpleName();
        if (!excludeLists.containsKey(className)) {
            // read file and add to excludeLists
            String fileName = KEYWORD_DIR + "exclude/" + className + "-exclude.txt";
            try {
                excludeLists.put(className, getResourceFileAsStringList(fileName));
            } catch (IOException e) {
                excludeLists.put(className, new ArrayList<String>());
                createFile(fileName);
            }
        }
        return excludeLists.get(className);
    }

    /*
     * if node class name key not in includeLists map, read file, else get value
     * corresponding to key
     */
    private List<String> getKeywordsToInclude() {
        // className can be any child class of SpecializedNode
        String className = this.getClass().getSimpleName();
        if (!includeLists.containsKey(className)) {
            // read file and add to excludeLists
            String fileName = KEYWORD_DIR + "include/" + className + "-include.txt";
            try {
                includeLists.put(className, getResourceFileAsStringList(fileName));
            } catch (IOException e) {
                includeLists.put(className, new ArrayList<String>());
                createFile(fileName);
            }
        }
        return includeLists.get(className);
    }

    /*
     * if node class name key not in substituteMaps map, read file, else get value
     * corresponding to key
     */
    private Map<String, String> getKeywordsToSubstitute() {
        // className can be any child class of SpecializedNode
        String className = this.getClass().getSimpleName();
        if (!substituteMaps.containsKey(className)) {
            // read file and add to substituteMaps
            String fileName = KEYWORD_DIR + "substitute/" + className + "-substitute.txt";
            try {
                substituteMaps.put(
                        className,
                        getResourceFileAsStringList(fileName).stream()
                                .filter(line -> line.length() >= 4)
                                .map(line -> line.split("->"))
                                .collect(
                                        Collectors.toMap(
                                                splitted_line -> splitted_line[0].trim(),
                                                splitted_line -> splitted_line[1].trim())));
            } catch (IOException e) {
                substituteMaps.put(className, new HashMap<String, String>());
                createFile(fileName);
            }
        }
        return substituteMaps.get(className);
    }

    private void createFile(String fileName) {
        System.err.println(fileName + " does not exists, it will be created automatically");
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("\n");

            writer.close();
        } catch (IOException e) {
            System.err.println("Could not create automatically file: " + fileName);
            e.printStackTrace();
        }
    }

    public Optional<List<InlineComment>> getComments() {
        return this.comments;
    }

    public String printASTNode(boolean withComments) {
        if (withComments) {
            return this.astNode.toString();
        } else {
            String raw_code = this.astNode.toString();
            // remove comments that could be contained in ast node
            if (comments.isPresent()) {
                for (InlineComment comment : comments.get()) {
                    raw_code = raw_code.replace(comment.toString(), "");
                }
            }

            if (potentialOrphanComments.isPresent()) {
                for (InlineComment comment : potentialOrphanComments.get()) {
                    raw_code = raw_code.replace(comment.toString(), "");
                }
            }
            return raw_code;
        }
    }

    @Override
    public Node getASTNode() {
        return this.astNode;
    }

    @Override
    public Range<Integer> getLOCs() {
        return this.LOC;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpecializedNode) {
            return this.astNode.equals(((SpecializedNode) obj).getASTNode());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String out = "";
        out += "[Original AST node:]\n";
        out += astNode.toString();
        out += "\n[SpecializedNode subclass: " + this.getClass().getSimpleName() + "]";
        if (comments.isPresent()) {
            out += "\n[Comments:]\n";
            for (InlineComment comment : comments.get()) {
                out += comment;
            }
        }
        out += "\n[Unexpanded identifier list:]\n";
        out += toIdentifierList();
        out += "\n[Unexpanded identifier list with synonyms:]\n";
        out += toIdentifierList(true);
        out += "\n[Expanded identifier list:]\n";
        out += toExpandedIdentifierList();
        out += "\n[Expanded identifier list with synonyms:]\n";
        out += toExpandedIdentifierList(true);
        return out;
    }

    @Override
    public int hashCode() {
        return Objects.hash(LOC);
    }
}
