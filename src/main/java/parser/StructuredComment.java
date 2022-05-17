package parser;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import edu.stanford.nlp.simple.Sentence;
import mapper.CommentSentence;
import mapper.Identifier;
import util.TextProcessor;

import java.util.ArrayList;
import java.util.Optional;

public class StructuredComment {

    /**
     * List of Javadoc Tags - backward compatibility:
     * in the 'analysis:commit' scenario we only treat @param tags
     */
    private final ArrayList<JavadocBlockTag> jDocTags;

    /**
     * Sentence index (its ID in the comment)
     */
    private int commentSentenceNumber;

    /**
     * Javadoc comment as extracted by JavaParser
     */
    private final JavadocComment javadocComment;


    public StructuredComment(Optional<JavadocComment> optJavadocComment) {
        if (optJavadocComment.isPresent()) {
            JavadocComment javadocComment = optJavadocComment.get();
            // backward compatibility: in the 'analysis:commit' scenario we only treat @param tags
            this.jDocTags = new ArrayList<>();
            for (JavadocBlockTag tag : javadocComment.parse().getBlockTags()) {
                if (tag.getName().isPresent()) {
                    switch (tag.getType()) {
                        case PARAM:
                            jDocTags.add(tag);
                            break;
                        default:
                            // ignore other possible sentences
                            break;
                    }
                }
            }
            this.javadocComment = javadocComment;
        } else {
            // Empty javadoc
            this.javadocComment = new JavadocComment();
            this.jDocTags = new ArrayList<>();
        }
        this.commentSentenceNumber = 0;
    }

    /**
     * Extracts comment part written as plain English text in sentences,
     * also if grouped in paragraphs.
     * <p>
     * Side-effect: updates the {@code this.commentSentenceNumber} value.
     *
     * @return individual period-terminated sentences
     */
    public ArrayList<CommentSentence> getDescriptiveSentences() {

        ArrayList<CommentSentence> extractedSentences = new ArrayList<>();

        JavadocDescription description = javadocComment.parse().getDescription();
        String descriptionText = TextProcessor.preFormatString(description.toText());

        for (Sentence sentenceCORE : TextProcessor.getSentencesFromText(descriptionText)) {
            extractedSentences
                    .add(new CommentSentence(commentSentenceNumber, CommentSentence.CommentPart.DESC, sentenceCORE.text()));
            commentSentenceNumber++;
        }

        return extractedSentences;
    }

    /**
     * Is Java doc comments specific. Extracts @-tag sentences from the doc comment,
     * one per tag.
     * <p>
     * Side-effect: updates the {@code this.commentSentenceNumber} value.
     * </p>
     * <p>
     * Empty @-tag sentences are not processed. In the future it might be interesting to generate filling
     * suggestions for such sentences.
     * </p>
     *
     * @return one English sentence for each non-empty tagged sentence encountered
     */
    public ArrayList<CommentSentence> getTaggedSentences() {

        ArrayList<CommentSentence> extractedSentences = new ArrayList<>();

        for (JavadocBlockTag tag : javadocComment.parse().getBlockTags()) {

            /* STEP 1: determine comment sentence part (limited subset of tags supported) */

            CommentSentence.CommentPart part;
            switch (tag.getType()) {
                case PARAM:
                    part = CommentSentence.CommentPart.PARAM;
                    break;
                case EXCEPTION:
                case THROWS:
                    part = CommentSentence.CommentPart.EXCEP;
                    break;
                case RETURN:
                    part = CommentSentence.CommentPart.RETURN;
                    break;
                default:
                    // ignore any other tag by skipping the rest of the for loop
                    continue;
            }

            /* STEP 2: extract and process the NL sentence parts */

            // Does the tag have a non-empty arameter/exception name?
            boolean tagHasName = tag.getName().isPresent() &&
                    tag.getName().get().length() > 0;

            // Is the tag content non-empty?
            boolean tagHasContent = tag.getContent().getElements().size() > 0;

            if (tagHasName || tagHasContent) {

                // There is at least one non-whitespace character after the @tag.
                // Construct the sentence:
                // 1) split that variable name (except for @return tag)
                // 2) add any description if present

                //FIXME what if multiple sentences per tag? Currently assuming one NL sentence in a tag
                String tagSentence = "";

                /* STEP 2.1: extract parameter/exception identifier (if applicable) */

                if (!tag.getType().equals(JavadocBlockTag.Type.RETURN)) {
                    if (tag.getName().isPresent()) {
                        Identifier id = new Identifier(tag.getName().get(), Identifier.KindOfID.VAR_NAME);
                        tagSentence += id.toString();
                    } else {
                        //FIXME we should not need this condition branch (please test)
                        System.err.println("[MAPPING ERROR] missing identifier name in a sentence:");
                        System.err.println(tag);
                    }
                }
//                else {
//                    tagSentence += "return "; //TODO: may lead to doubling number of 'return' lemmas
//                }

                /* STEP 2.2: extract the NL sentence part */

                if (!tag.getContent().isEmpty()) { // @-tag parameter has description
                    tagSentence += tag.getContent().toText();
                }

                /* STEP 2.3: preformat the sentence (cleanup, remove stopwords...) */

                tagSentence = TextProcessor.preFormatString(tagSentence);


                extractedSentences.add(new CommentSentence(commentSentenceNumber, part, tagSentence));
                commentSentenceNumber++;

            }
        }

        return extractedSentences;
    }

    public JavadocComment getJavadocComment() {
        return javadocComment;
    }

    public ArrayList<JavadocBlockTag> getJDocTags() {
        return this.jDocTags;
    }
}
