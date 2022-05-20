package mapper;

import parser.StructuredComment;

import java.util.ArrayList;


/**
 * This class stores method-level comments according to our {@code mapping.CommentSentence}
 * representation.
 */
public class MethodComment {

    private final ArrayList<CommentSentence> commentSentences;

    public MethodComment(StructuredComment structuredComment) {
        this.commentSentences = new ArrayList<>();
        this.commentSentences.addAll(structuredComment.getDescriptiveSentences());
        this.commentSentences.addAll(structuredComment.getTaggedSentences());
    }

    /**
     * Special for Replicomment
     *
     * @param structuredComment
     * @param repliComment
     */
    public MethodComment(StructuredComment structuredComment, boolean repliComment) {
        this.commentSentences = new ArrayList<>();
        this.commentSentences.addAll(structuredComment.getDescriptiveSentences());
        this.commentSentences.addAll(structuredComment.getTaggedSentencesRepliComment());
    }

    public ArrayList<CommentSentence> getCommentSentences() {
        return commentSentences;
    }

    public CommentSentence getSentenceByIndex(int index) {
        return this.commentSentences.get(index);
    }

    public CommentSentence.CommentPart getJdocPartByIndex(int index) {
        return this.commentSentences.get(index).getPart();
    }

    /**
     * Get individual sentences of the doc comment as an array of lists of lemmas of the words of the
     * sentences.
     *
     * @return Returns an array of arrays of lemmas (bag-of-words representation).
     */
    public ArrayList<WordBag> getSentencesAsBoWs() {

        ArrayList<WordBag> sentenceBoWs = new ArrayList<WordBag>();

        for (CommentSentence cs : this.commentSentences) {
            sentenceBoWs.add(cs.toBagOfWords());
        }

        return sentenceBoWs;
    }


}
