package mapper;

import parser.MethodNode;

import java.text.DecimalFormat;
import java.util.*;

/**
 * This class implements a mapping between identifiers of AST nodes and doc comment sentences,
 * based on respective bag of words representations' similarity. Essentially, a mapping goes from
 * an AST Node to the IDs of the sentences considered relevant for it (i.e., similar to the AST node).
 */
public class MapBuilder {

    /**
     * The original method comment, composed of CommentSentences
     */
    private final MethodComment methodComment;

    /**
     * The original method body, composed of ASTNodes
     */
    private final MethodBody methodBody;

    /**
     * The similarity threshold above which we consider a relatedness
     * FIXME it should be a public static parameter of the upDoc configuration,
     * FIXME there would be no need to save it also in this class and
     * FIXME also we would be sure to have only one single reference
     */
    private final double similarityThreshold;

    /**
     * The mapping between a node ID and related sentences (with similarities)
     */
    // TODO This data structure may look a bit convoluted, introducing
    // TODO a new class (e.g. RelatedSentence) could be an alternative
    private final Hashtable<Integer, LinkedHashMap<Integer, Double>> mapping;


    /**
     * Special for Replicomment
     *
     * @param mn
     * @param bowSimilarity
     * @param replicomment
     */
    public MapBuilder(MethodNode mn, double bowSimilarity, boolean replicomment, CommentSentence.CommentPart part) {
        this.similarityThreshold = bowSimilarity;

        this.methodComment = new MethodComment(mn.getDocComment(), replicomment);

        this.methodBody = new MethodBody(mn);

        // As soon as a MapBuilder is instantiated,
        // the mapping between code and comment is created and stored
        this.mapping = mapCommentToCode(part);

    }


    public MapBuilder(MethodNode mn, double bowSimilarity) {
        this.similarityThreshold = bowSimilarity;

        this.methodComment = new MethodComment(mn.getDocComment());

        this.methodBody = new MethodBody(mn);

        // As soon as a MapBuilder is instantiated,
        // the mapping between code and comment is created and stored
        this.mapping = mapCommentToCode();

    }


    /**
     * Creates a mapping between each AST node of a method body and all relevant sentences
     * from the doc comment. The map ties the AST Node ID (an Integer) to multiples sentences
     * IDs (a list of Integers).
     *
     * @return Returns the mapping
     */
    private Hashtable<Integer, LinkedHashMap<Integer, Double>> mapCommentToCode() {

        // The final code-doc mapping to be returned
        Hashtable<Integer, LinkedHashMap<Integer, Double>> mapping = new Hashtable<>();

        for (ASTNode methodNode : methodBody.getBodyNodes()) {
            WordBag methodBoW = methodNode.toBagOfWords();

            // Related sentences, with their ID and similarity
            LinkedHashMap<Integer, Double> relatedSentences = new LinkedHashMap<>();

            for (CommentSentence sentence : methodComment.getCommentSentences()) {
                WordBag sentenceBoW = sentence.toBagOfWords();

                // FIXME this computation is repeated in toString() method.
                // FIXME Save instead this computation result.
                // FIXME currently this method only saves the related sentence ID
                double cosineSim = methodBoW.cosineSim(sentenceBoW);
                if (cosineSim > this.similarityThreshold) {
                    relatedSentences.put(sentence.getId(), cosineSim);
                }

            }

            mapping.put(methodNode.getNodeId(), relatedSentences);
        }

        return mapping;
    }

    /**
     * Creates a mapping between each AST node of a method body and all relevant sentences
     * from the doc comment. The map ties the AST Node ID (an Integer) to multiples sentences
     * IDs (a list of Integers).
     *
     * @return Returns the mapping
     */
    private Hashtable<Integer, LinkedHashMap<Integer, Double>> mapCommentToCode(CommentSentence.CommentPart part) {

        // The final code-doc mapping to be returned
        Hashtable<Integer, LinkedHashMap<Integer, Double>> mapping = new Hashtable<>();

        for (ASTNode methodNode : methodBody.getBodyNodes()) {
            WordBag methodBoW = methodNode.toBagOfWords(part);

            // Related sentences, with their ID and similarity
            LinkedHashMap<Integer, Double> relatedSentences = new LinkedHashMap<>();

            for (CommentSentence sentence : methodComment.getCommentSentences()) {
                WordBag sentenceBoW = sentence.toBagOfWords();

                // FIXME this computation is repeated in toString() method.
                // FIXME Save instead this computation result.
                // FIXME currently this method only saves the related sentence ID
                double cosineSim = methodBoW.cosineSim(sentenceBoW);
                if (cosineSim > this.similarityThreshold) {
                    relatedSentences.put(sentence.getId(), cosineSim);
                }

            }

            mapping.put(methodNode.getNodeId(), relatedSentences);
        }

        return mapping;
    }

    public Hashtable<Integer, LinkedHashMap<Integer, Double>> getMapping() {
        return this.mapping;
    }

    /**
     * Returns the list with related sentence's IDs in the mapping given a nodeID
     * <p>
     * FIXME slightly convoluted
     *
     * @param nodeID the node from which to retrieve related sentences
     * @return list of related sentences' IDs
     */
    public List getValuesAtNode(int nodeID) {
        List<Integer> mappingList = new ArrayList<>(this.mapping.get(nodeID).keySet());
        Collections.sort(mappingList);
        return mappingList;
    }


    public MethodComment getMethodComment() {
        return methodComment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof MapBuilder)) {
            return false;
        }

        MapBuilder other = (MapBuilder) obj;

        boolean sameElements = false;

        for (Integer key : this.mapping.keySet()) {
            sameElements = this.mapping.get(key).equals(other.mapping.get(key));
        }

        return sameElements;
    }


    @Override
    public String toString() {

        DecimalFormat format = new DecimalFormat("0.000");

        StringBuilder mappingAsString = new StringBuilder();

        // Iterate over nodeIDs in the mapping
        for (Integer nodeId : this.mapping.keySet()) {

            // FIXME For each nodeID we iterate over method body's nodes: why?
            for (ASTNode currentNode : this.methodBody.getBodyNodes()) {

                mappingAsString
                        .append("\tAST nodes:\n" + "\tid:")
                        .append(nodeId)
                        .append("\t")
                        .append(currentNode.toBagOfWords())
                        .append("\n");

                mappingAsString.append("\tRelated comment sentences:\n");

                // Iterate over the sentence IDs related to the current nodeID
                LinkedHashMap<Integer, Double> sentencesAndSimilarity = this.mapping.get(nodeId);
                if (!sentencesAndSimilarity.isEmpty()) {
                    for (Integer sentenceId : sentencesAndSimilarity.keySet()) {
                        // The related sentence in its original representation
                        CommentSentence relatedSentence = this.methodComment.getSentenceByIndex(sentenceId);
                        double cosineSim = sentencesAndSimilarity.get(sentenceId);

                        mappingAsString
                                .append("\t" + relatedSentence.getPart())
                                .append("\tid:")
                                .append(relatedSentence.getId())
                                .append("\tsim:")
                                .append(format.format(cosineSim))
                                .append("\t")
                                .append(relatedSentence)
                                .append(/*"\n\t" + relatedSentence.toBagOfWords()+ */ "\n");
                    }
                } else {
                    mappingAsString.append("\t<None>");
                }
            }
        }

        return mappingAsString.toString();
    }


}
