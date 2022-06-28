package mapper;

public class RelatedSentence {
    private final int sentenceID;
    private final String originalSentence;
    private final double similarityToNode;
    private ASTNode theNode;


    public RelatedSentence(int sentenceID, String originalSentence, double similarityToNode) {
        this.sentenceID = sentenceID;
        this.originalSentence = originalSentence;
        this.similarityToNode = similarityToNode;
    }

    public RelatedSentence(int sentenceID, String originalSentence, double similarityToNode, ASTNode theNode) {
        this.sentenceID = sentenceID;
        this.originalSentence = originalSentence;
        this.similarityToNode = similarityToNode;
        this.theNode = theNode;
    }

    public int getSentenceID() {
        return sentenceID;
    }

    public double getSimilarityToNode() {
        return similarityToNode;
    }

    public String getOriginalSentence() {
        return originalSentence;
    }

    public ASTNode getTheNode() {
        return theNode;
    }

    @Override
    public String toString() {
        String sentence = " ";

        sentence += String.format("%.2f similarity of sentence [%d] to node [%d]", similarityToNode, sentenceID, theNode.getNodeId());

        return sentence;
    }
}
