package mapper;

import mapper.CommentSentence.CommentPart;
import parser.StructuredComment;
import parser.nodes.InlineComment;

import java.util.ArrayList;
import java.util.List;

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

    public MethodComment(StructuredComment structuredComment, List<InlineComment> inlineComments) {
        this.commentSentences = new ArrayList<>();
        this.commentSentences.addAll(structuredComment.getDescriptiveSentences());
        this.commentSentences.addAll(structuredComment.getTaggedSentences());
        int comment_id = this.commentSentences.size();
        for (InlineComment comment : inlineComments) {
            this.commentSentences.add(
                    new CommentSentence(comment_id, CommentPart.INLINE, comment.toString()));
            comment_id++;
        }
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

    @Override
    public String toString() {

        String fullComment = "";

        for (CommentSentence cs : this.commentSentences) {
            fullComment += "[" + cs.getId() + "] " + cs + "\n";
        }

        return fullComment;
    }
}
