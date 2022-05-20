package util;

import mapper.CommentSentence;
import mapper.MapBuilder;

import java.util.LinkedHashMap;

public class UtilStatsRepliComment {

    private String winnerMethod;
    private String loserMethod;
    private double firstSim;
    private double secndSim;
    private boolean bothLosers;
    private boolean bothVeryRelated;
    private double difference;

    private static final double HIGH_THRESHOLD = 0.15;

    public UtilStatsRepliComment() {

    }

    public double getFirstSim() {
        return firstSim;
    }

    public double getSecndSim() {
        return secndSim;
    }

    public boolean isBothVeryRelated() {
        return bothVeryRelated;
    }

    public String getWinnerMethod() {
        return winnerMethod;
    }

    public String getLoserMethod() {
        return loserMethod;
    }

    public boolean isBothLosers() {
        return bothLosers;
    }

    public double getDifference() {
        return difference;
    }

    public UtilStatsRepliComment isOriginalCommentIdentified(CommentSentence.CommentPart RELATED_PART,
                                                             MapBuilder actualMapping_o,
                                                             MapBuilder actualMapping_c,
                                                             double threshold) {

//        System.out.println("The cloned part is: " + RELATED_PART.toString());

        double original_sim = getSimilarityOfCommentPart(RELATED_PART, actualMapping_o);
        double clone_sim = getSimilarityOfCommentPart(RELATED_PART, actualMapping_c);

        this.firstSim = original_sim;
        this.secndSim = clone_sim;

        if (original_sim > clone_sim) {
            // FIXME Horrible, put something significant in these variables!
            this.winnerMethod = "original";
            this.loserMethod = "clone";
            this.difference = original_sim - clone_sim;

        } else {
            this.winnerMethod = "clone";
            this.loserMethod = "original";
            this.difference = clone_sim - original_sim;
        }

        this.bothLosers = original_sim < threshold && clone_sim < threshold;

        this.bothVeryRelated = original_sim > threshold + HIGH_THRESHOLD && clone_sim > threshold + HIGH_THRESHOLD;

        return this;
    }

    private double getSimilarityOfCommentPart(CommentSentence.CommentPart RELATED_PART,
                                              MapBuilder mapping) {
        double similarity = 0;
        double highestSimilarity = 0;
        LinkedHashMap<Integer, Double> sentencesAndSimilarity = mapping.getMapping().get(0);
        for (Integer sentenceId : sentencesAndSimilarity.keySet()) {
            // The related sentence in its original representation
            CommentSentence relatedSentence = mapping.getMethodComment().getSentenceByIndex(sentenceId);
            if (relatedSentence.getPart().equals(RELATED_PART)) {
                similarity = sentencesAndSimilarity.get(sentenceId);
                if (similarity > highestSimilarity) {
                    // If multiple DESC parts, only the last one would be saved;
                    // let's save the most similar one instead
                    highestSimilarity = similarity;
                }
            }
        }
        return highestSimilarity;
    }

    public UtilStatsRepliComment isOriginalCommentIdentified(MapBuilder actualMapping_o,
                                                             MapBuilder actualMapping_c,
                                                             double threshold) {

//        System.out.println("The cloned part is: the whole comment");

        double original_sim = getSimilarityOfWholeComment(actualMapping_o);
        double clone_sim = getSimilarityOfWholeComment(actualMapping_c);

        this.firstSim = original_sim;
        this.secndSim = clone_sim;

        if (original_sim > clone_sim) {
            // FIXME Horrible, put something significant in these variables!
            this.winnerMethod = "original";
            this.loserMethod = "clone";
            this.difference = original_sim - clone_sim;

        } else {
            this.winnerMethod = "clone";
            this.loserMethod = "original";
            this.difference = clone_sim - original_sim;
        }

        this.bothLosers = original_sim < threshold && clone_sim < threshold;

        this.bothVeryRelated = original_sim > threshold + HIGH_THRESHOLD && clone_sim > threshold + HIGH_THRESHOLD;

        return this;
    }

    private double getSimilarityOfWholeComment(MapBuilder mapping) {
        double cumulativeSimilarity = 0;
        LinkedHashMap<Integer, Double> sentencesAndSimilarity = mapping.getMapping().get(0);
        for (Integer sentenceId : sentencesAndSimilarity.keySet()) {
            cumulativeSimilarity += sentencesAndSimilarity.get(sentenceId);

        }
        return cumulativeSimilarity;
    }
}
