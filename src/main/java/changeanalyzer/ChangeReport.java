package changeanalyzer;

import changextractor.Change;
import changextractor.Commit;
import mapper.MapBuilder;
import mapper.RelatedSentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Class that takes input from the change extractor component
 * (in the form of Commit with Changes)
 * and from the mapper component
 * (in the form of mapping between AST nodes and related comment sentences).
 * <p>
 * From the SCAM paper:
 * <p>"According to previous research studies, it is more likely that a change
 * in the code should have a corresponding change in its documentation
 * rather than the reverse [1], [21].
 * To detect such inconsistencies we will combine a list of AST nodes
 * that are marked as modified or deleted in the source code diff,
 * and a list of related comment sentences for each AST node as present
 * in the mapping. Iterating through the related sentences of the node
 * under change, we will check if all related sentences are present in
 * the diff and warn the programmer if not. In case of code modification
 * (or addition) upDoc will issue a warning if the new code does not have
 * any relation to the comment text (e.g., common identifiers or domain terms).
 * In the case of code deletion upDoc will also report the lines in comment
 * that are most likely affected by the change".</p>
 */
public class ChangeReport {

    private StringBuilder changeReport;

    public ChangeReport(Commit commit,
                        List<MapBuilder> sourceMappings,
                        List<MapBuilder> targetMappings,
                        String reportFormat) {

        StringBuilder inconsistencyReport = new StringBuilder();
        boolean anyDetectedChange = false;

        List<Change> changes = commit.getChanges();
        if (changes.isEmpty()) {
            inconsistencyReport.append("\n");
            inconsistencyReport.append("---> NO CHANGES DETECTED AT ALL! <----");
            this.changeReport = inconsistencyReport;
        }
        for (Change c : changes) {
            int hashNodeSrc = Objects.hash(c.getSrcNodeCoarseLOCs());
            int hashNodeDst = Objects.hash(c.getDstNodeCoarseLOCs());
            Optional<MapBuilder> matchingSrcNodes = sourceMappings.stream()
                    .filter(x -> !x.getMapping().getOrDefault(hashNodeSrc, new ArrayList<>()).isEmpty())
                    .findFirst();

            Optional<MapBuilder> matchingDstNodes =
                    targetMappings.stream().filter(
                                    x -> !x.getMapping().getOrDefault(hashNodeDst, new ArrayList<>()).isEmpty())
                            .findFirst();

            if (matchingDstNodes.isPresent() && matchingSrcNodes.isPresent()) {
                anyDetectedChange = true;
                List<RelatedSentence> relatedSentencesSrc = matchingSrcNodes.get().getMapping().get(hashNodeSrc);
                List<RelatedSentence> relatedSentencesDst = matchingDstNodes.get().getMapping().get(hashNodeDst);

                if (reportFormat.equals("cumulative")) {
                    cumulativeReport(inconsistencyReport, c, relatedSentencesSrc, relatedSentencesDst);
                } else {
                    sentenceBySentenceReport(inconsistencyReport, relatedSentencesSrc, relatedSentencesDst);
                }
            }
        }

        if (!anyDetectedChange) {
            inconsistencyReport.append("\n");
            inconsistencyReport.append("----> No match between nodes was found in the mappings at all. <-----");
        }

        this.changeReport = inconsistencyReport;
    }

    private static void sentenceBySentenceReport(StringBuilder inconsistencyReport,
                                                 List<RelatedSentence> relatedSentencesSrc,
                                                 List<RelatedSentence> relatedSentencesDst) {
        for (int i = 0; i < relatedSentencesSrc.size(); i++) {
            RelatedSentence srcSentence = relatedSentencesSrc.get(i);
            RelatedSentence dstSentence = relatedSentencesDst.get(i);
            double srcSimilarity = srcSentence.getSimilarityToNode();
            double dstSimilarity = dstSentence.getSimilarityToNode();
            if (srcSimilarity > dstSimilarity) {
                inconsistencyReport.append("\nDestination sentence \n<");
                inconsistencyReport.append(dstSentence.getOriginalSentence());
                inconsistencyReport.append(">\n");
                inconsistencyReport.append("\nDescribing Destination node: \n<");
                inconsistencyReport.append(dstSentence.getTheNode());
                inconsistencyReport.append(">\n");
                inconsistencyReport.append("\n seems less relevant (")
                        .append(String.format("%.2f", dstSimilarity))
                        .append(") than original (")
                        .append(String.format("%.2f", srcSimilarity))
                        .append(")")
                        .append("\n<");
                inconsistencyReport.append(srcSentence.getOriginalSentence());
                inconsistencyReport.append(">\n");
                inconsistencyReport.append("\nDescribing original node: \n<");
                inconsistencyReport.append(srcSentence.getTheNode());
                inconsistencyReport.append(">\n");

            }
        }
    }

    private static void cumulativeReport(StringBuilder inconsistencyReport,
                                         Change c,
                                         List<RelatedSentence> relatedSentencesSrc,
                                         List<RelatedSentence> relatedSentencesDst) {
        double cumulativeSrcScore = 0;
        for (RelatedSentence s : relatedSentencesSrc) {
            cumulativeSrcScore += s.getSimilarityToNode();

        }

        double cumulativeDstScore = 0;
        for (RelatedSentence s : relatedSentencesDst) {
            cumulativeDstScore += s.getSimilarityToNode();
        }

        if (cumulativeSrcScore > cumulativeDstScore) {
            inconsistencyReport.append("\n");
            inconsistencyReport.append("----> Consistency between doc and code decreased! <----");
        } else {
            inconsistencyReport.append("\n");
            inconsistencyReport.append("----> Consistency between doc and code seems stable. <----");
        }
        inconsistencyReport.append("\n");
        inconsistencyReport.append("Original documentation:");
        inconsistencyReport.append("\n");
        for (RelatedSentence sentence : relatedSentencesSrc) {
            inconsistencyReport
                    .append(sentence.getOriginalSentence())
                    .append(" [")
                    .append(String.format("%.2f", sentence.getSimilarityToNode()))
                    .append("]")
                    .append("\n");
        }
        inconsistencyReport.append("\n");
        inconsistencyReport.append("Documenting node:");
        inconsistencyReport.append(c.sourceToString());

        inconsistencyReport.append("\nCurrent documentation:");
        inconsistencyReport.append("\n");
        for (RelatedSentence sentence : relatedSentencesDst) {
            inconsistencyReport
                    .append(sentence.getOriginalSentence())
                    .append(" [")
                    .append(String.format("%.2f", sentence.getSimilarityToNode()))
                    .append("]")
                    .append("\n");
        }

        inconsistencyReport.append("\n");
        inconsistencyReport.append("Documenting node:");
        inconsistencyReport.append(c.dstToString());

        // The following prints the whole change, above we break up original and source changes
//        inconsistencyReport.append(c);

    }

    public String getReport() {
        return this.changeReport.toString();
    }

}
