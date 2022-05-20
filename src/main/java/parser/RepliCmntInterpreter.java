package parser;

import com.github.javaparser.ast.comments.JavadocComment;
import mapper.CommentSentence;
import mapper.MapBuilder;
import org.apache.commons.io.FileUtils;
import util.UtilStatsRepliComment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class RepliCmntInterpreter {

    private static final double SIMILARITY_THRESHOLD = 0.2;

    private static final double SIGNIFICANT_DIFFERENCE = 0.1;
    private static SEVERITY lastSeverity;

    // LOW is a likely FP
    // MILD is whole comment clone with overload or poor info
    // HIGH whole comment clone w/o overload is serious c&p
    private enum SEVERITY {
        LOW, MILD, HIGH
    }

//    private static boolean falsePositive = false;

    private static int issueCountPerRecord = 0;

    static DecimalFormat format = new DecimalFormat("0.000");

    private static UtilStatsRepliComment utilStats = new UtilStatsRepliComment();

    // TODO separate overloading cases? They are easier the analyze separately...
    private static FileWriter lowSeverityOutput;
    private static FileWriter mildSeverityOutput;
    private static FileWriter highSeverityOutput;

    private static FileWriter wmildSeverityOutput;
    private static FileWriter whighSeverityOutput;

    private static FileWriter specialIssues;

    public static void main(String[] args) {
        // Run with mvn exec:java -Dexec.mainClass="parser.RepliCmntInterpreter"
        List<File> files = (List<File>) FileUtils.listFiles(
                new File("replicomment-csv"), new String[]{"csv"}, false);

//        Path fpath = Paths.get("/Users/arianna/replicomment/2020_JavadocClones_elastic.csv");

        StringBuilder output;

        try {
            // Initialize output TXT files
            lowSeverityOutput = new FileWriter("JSS-outputs/pc_low_severity.txt", false);
            mildSeverityOutput = new FileWriter("JSS-outputs/pc_mild_severity.txt", false);
            highSeverityOutput = new FileWriter("JSS-outputs/pc_high_severity.txt", false);

            wmildSeverityOutput = new FileWriter("JSS-outputs/wc_mild_severity.txt", false);
            whighSeverityOutput = new FileWriter("JSS-outputs/wc_high_severity.txt", false);

            specialIssues = new FileWriter("JSS-outputs/special_issues.txt", false);
//
            // Start exploring input CSV files
            for (File csvFile : files) {
                List<String> fileContents = Files.readAllLines(Paths.get(csvFile.toURI()));

//                output.append(" ---------->  ").append(csvFile.getName()).append(" <----------------");
                System.out.println("Analyzing " + csvFile.getName() + "...");

                int recordCount = 1;
                for (String line : fileContents) {
//                    falsePositive = false;
                    output = new StringBuilder();
                    recordCount++;

                    // A record in the RepliComment output
                    String[] tokens = line.split(";");
                    //
                    // TOKENS:
                    // ----------------------
                    // 0 - class name
                    // 1 - method1
                    // 2 - method2
                    // 3 - comment part
                    // 4 - param1
                    // 5 - param2
                    // 6 - comment text
                    // 7 - legit
                    // ----------------------
                    //

                    if (tokens[7].equalsIgnoreCase("FALSE")) {
                        String recordInfo = "\n---- Record #" + recordCount + " " + csvFile.toURI() + " ----";
                        output.append(recordInfo);

                        issueCountPerRecord = 0;
                        // Non-legitimate clone
                        String fullyQualifiedClass = tokens[0];
                        String methodOne = tokens[1];
                        String methodTwo = tokens[2];
                        String typeOfClone = tokens[3];
                        String param1 = tokens[4];
                        String param2 = tokens[5];
                        String commentText = tokens[6].trim();

                        StructuredSignature ss1;
                        StructuredSignature ss2;

                        String simpleClassName = fullyQualifiedClass.
                                substring(fullyQualifiedClass.lastIndexOf(".") + 1);

                        if ("@param".equals(typeOfClone)) {
                            String paramName1 = param1.split(" ")[1].replaceAll(" ", "");
                            String paramName2 = param2.split(" ")[1].replaceAll(" ", "");

                            if (isSpecialIssue(recordInfo, commentText, paramName1, paramName2, methodOne, methodTwo)) {
                                continue;
                            }

                            // FIXME sometimes it is a Whole clone but only @param is present,
                            // FIXME should that case be treated like this?
                            ss1 = new StructuredSignature(paramName1, param1.split(" ")[0].trim());
                            ss2 = new StructuredSignature(paramName2, param2.split(" ")[0].trim());
                        } else {
                            ss1 = new StructuredSignature(methodOne);
                            ss2 = new StructuredSignature(methodTwo);
                        }

                        boolean isOverload = ss1.getMethodName().toString().equals(ss2.getMethodName().toString());

                        if ("Whole".equalsIgnoreCase(typeOfClone)) {
                            output.append(printWholeCloneOutput(isOverload, methodOne, methodTwo));
                        }
                        StructuredComment structuredComment1;
                        StructuredComment structuredComment2;

                        // THE following was commented out since adding to the param tag its
                        // param name would raise the similarity unfairly

//                        if ("@param".equals(typeOfClone)) {
//                            // Build a different comment for different parameter names
//                            String commentTextWithPart1 = typeOfClone + " " + param1 + " " + commentText;
//                            String commentTextWithPart2 = typeOfClone + " " + param2 + " " + commentText;
//                            Optional<JavadocComment> clonedComment1 =
//                                    Optional.of(new JavadocComment(commentTextWithPart1));
//                            Optional<JavadocComment> clonedComment2 =
//                                    Optional.of(new JavadocComment(commentTextWithPart2));
//                            structuredComment1 = new StructuredComment(clonedComment1);
//                            structuredComment2 = new StructuredComment(clonedComment2);
//                        } else {


                        String commentTextWithPart;
                        if (!"Whole".equalsIgnoreCase(typeOfClone)
                                && !"Free text".equalsIgnoreCase(typeOfClone)) {
                            // Report the tag before the cloned text, so that JavadocParser can parse the Javadoc
                            // FIXME BUT DO WE CARE??
                            commentTextWithPart = typeOfClone + " " + commentText;
                        } else {
                            commentTextWithPart = commentText;
                        }
                        Optional<JavadocComment> clonedComment = Optional.of(new JavadocComment(commentTextWithPart));
                        structuredComment1 = new StructuredComment(clonedComment, Optional.of(simpleClassName));
                        structuredComment2 = new StructuredComment(clonedComment, Optional.of(simpleClassName));
//                        }


                        MethodNode mn1 = new MethodNode(ss1, structuredComment1);
                        MethodNode mn2 = new MethodNode(ss2, structuredComment2);

                        CommentSentence.CommentPart part = identifyCommentPart(typeOfClone);

                        // Build the mapping....
                        MapBuilder mapping1 = new MapBuilder(mn1, 0.0, true, part);
                        MapBuilder mapping2 = new MapBuilder(mn2, 0.0, true, part);

                        // Find which method is more similar....
                        if ("Whole".equals(typeOfClone)) {
                            utilStats = utilStats.isOriginalCommentIdentified(mapping1, mapping2, SIMILARITY_THRESHOLD);
                        } else if (part != null) {
                            utilStats =
                                    utilStats.isOriginalCommentIdentified(
                                            part, mapping1, mapping2, SIMILARITY_THRESHOLD);
                        }

                        // Finally, analyze stats
                        similarityAnalysis(output, fullyQualifiedClass,
                                typeOfClone, methodOne, methodTwo, param1, param2, commentText);

                        output.append("\n");
//                        output.append("Severity:\t").append(lastSeverity);

                        if (!"Whole".equals(typeOfClone)) {
                            printRecordToRightFile(output);
                        } else {
                            printWCRecordToRightFile(output);
                        }
                    }
                }
            }

            // Close output streams
            lowSeverityOutput.close();
            mildSeverityOutput.close();
            highSeverityOutput.close();
            whighSeverityOutput.close();
            wmildSeverityOutput.close();
            specialIssues.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isSpecialIssue(String record, String comment,
                                          String param1, String param2, String method1, String method2) throws IOException {
        StringBuilder output = new StringBuilder();
        boolean specialIssue = false;
        if (param1.length() < 2 || param2.length() < 2) {
            output.append("\n\nYou cloned the comment ");
            output.append("\"").append(comment).append("\"");
            output.append("for two different parameters, \n");
            output.append(param1);
            output.append(" and ");
            output.append(param2);
            output.append("\nfrom methods \n");
            output.append(method1);
            output.append(" and ");
            output.append(method2);
            output.append("\n\nHowever one has a single-letter name: \n");
            output.append("it wouldn't be a fair comparison, so the suggestion is to\n");
            output.append("1) give more meaningful parameter names\n");
            output.append("2) asses the legitimacy of the clone manually\n");
            specialIssue = true;
        }
        if (param1.equals("nullParamName") || param2.equals("nullParamName")) {
            output.append("\n\nYou cloned the comment ");
            output.append("\"").append(comment).append("\"");
            output.append("\nfrom methods \n");
            output.append(method1);
            output.append(" and ");
            output.append(method2);
            output.append("\n\nHowever you didn't document the name of one of the two (nullParamName):\n");
            output.append(param1);
            output.append(" and ");
            output.append(param2);
            output.append("\n!Document the missing parameter name!");
            specialIssue = true;
        }
        if (specialIssue) {
            specialIssues.append("\n");
            specialIssues.append(record);
            specialIssues.append(output);
        }
        return specialIssue;
    }

    private static void printWCRecordToRightFile(StringBuilder output) throws IOException {
//        if(falsePositive){
//            fpOutput.append(output);
//            return;
//        }

        switch (lastSeverity) {
            case LOW:
                throw new IllegalStateException("You are trying to output a low-severity whole comment clone.");
            case MILD:
                wmildSeverityOutput.append(output);
                break;
            case HIGH:
                whighSeverityOutput.append(output);
                break;
        }
    }

    private static void printRecordToRightFile(StringBuilder output) throws IOException {
//        if(falsePositive){
//            fpOutput.append(output);
//            return;
//        }
        switch (lastSeverity) {
            case LOW:
                lowSeverityOutput.append(output);
                break;
            case MILD:
                mildSeverityOutput.append(output);
                break;
            case HIGH:
                highSeverityOutput.append(output);
                break;
        }
    }

    private static void similarityAnalysis(StringBuilder output, String className,
                                           String typeOfClone,
                                           String methodOne, String methodTwo, String param1, String param2,
                                           String commentText) {

        output.append(printClassHeader(className));

        if (utilStats.isBothLosers()) {
            // Both method have too low relatedness: very poor info
            if ("@param".equals(typeOfClone)) {
                output.append(printParamHeader(param1, param2));
                output.append(printPoorInfoUnraltedIssue(param1, param2, typeOfClone, commentText));
            } else {
                output.append(printPoorInfoUnraltedIssue(methodOne, methodTwo, typeOfClone, commentText));
            }
        } else {
            // One method is better than the other. Now we can face many cases...
            // Let's first retrieve which one is more similar
            if (!"Whole".equals(typeOfClone)) {
                if ("@param".equals(typeOfClone)) {
                    output.append(printParamHeader(param1, param2));
                    output.append(printMoreSimilarElement(param1, param2, typeOfClone, commentText));
                } else {
                    output.append(printMoreSimilarElement(methodOne, methodTwo, typeOfClone, commentText));
                }
            }

            // Then, reason about the similarity scores
            if (utilStats.getDifference() > SIGNIFICANT_DIFFERENCE && !utilStats.isBothVeryRelated()) {
                // One of the two elements is clearly more related than the other,
                // and the elements are not both very related to the comment: c&p
                lastSeverity = SEVERITY.HIGH;
            } else if (!"Whole".equals(typeOfClone)
                    && utilStats.getDifference() > SIGNIFICANT_DIFFERENCE
                    && utilStats.isBothVeryRelated()) {
                // Difference does exist, but both code elements seem quite related: false positive?
                // Unless it is a whole clone, which is never OK.
                output.append(twoGoodRelatednessPrint());
            } else if (!"Whole".equals(typeOfClone)
                    && utilStats.getDifference() < SIGNIFICANT_DIFFERENCE) {
                // ...Not so much better though: false positive?
                // Again, doesn't apply to whole clone.
                output.append(minimalDifferencePrint());
            }


            if ("Whole".equals(typeOfClone)) {
                if (utilStats.getDifference() < SIGNIFICANT_DIFFERENCE) {
                    output.append(printPoorInfoWholeIssue(methodOne, methodTwo, typeOfClone, commentText));
                } else {
                    output.append(printMoreSimilarElement(methodOne, methodTwo, typeOfClone, commentText));
                }
            }
        }
    }

    private static String printClassHeader(String classname) {
        return "\nIn class: " + classname + "\n";
    }

    private static String twoGoodRelatednessPrint() {
        StringBuilder output = new StringBuilder();
//        falsePositive = true;
        output.append("\n");
        output.append("\n| --------------------------------------------------------- |");
        output.append("\n|   Both elements quite related: this may be a false alarm  |");
        output.append("\n| \t\t\t\t\t\t ")
                .append(format.format(utilStats.getFirstSim()))
                .append(" vs ")
                .append(format.format(utilStats.getSecndSim()))
                .append(" \t\t\t\t\t\t|");
        output.append("\n| --------------------------------------------------------- |");
        lastSeverity = SEVERITY.LOW;
        return output.toString();
    }

    private static String minimalDifferencePrint() {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("\n| --------------------------------------------------------- |");
        output.append("\n|     Difference is minimal: this may be a false alarm      |");
        output.append("\n| \t\t\t\t\t\t ")
                .append(format.format(utilStats.getFirstSim()))
                .append(" vs ")
                .append(format.format(utilStats.getSecndSim()))
                .append(" \t\t\t\t\t\t|");
        output.append("\n| --------------------------------------------------------- |");
        lastSeverity = SEVERITY.LOW;
        return output.toString();
    }

    private static String printPoorInfoUnraltedIssue(String firstCodeElement,
                                                     String secondCodeElement,
                                                     String typeOfClone, String commentText) {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("\n");
        output.append(++issueCountPerRecord).append(") None of the code elements <")
                .append(firstCodeElement)
                .append("> and <")
                .append(secondCodeElement)
                .append(">");
        output.append("\nseem related to the comment you cloned:");
        output.append("\"")
                .append("(" + typeOfClone + ")")
                .append(commentText).append("\"");
        output.append("\n| \t\t\t\t\t\t ")
                .append(format.format(utilStats.getFirstSim()))
                .append(" and ")
                .append(format.format(utilStats.getSecndSim()))
                .append(" \t\t\t\t\t\t|");
        output.append("\nYour wording may be too generic (either in the comment or code elements' name).");
        lastSeverity = SEVERITY.MILD;
        return output.toString();
    }

    private static String printPoorInfoWholeIssue(String firstCodeElement,
                                                  String secondCodeElement,
                                                  String typeOfClone,
                                                  String commentText) {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("\n");
        output.append(++issueCountPerRecord).append(") Code elements <")
                .append(firstCodeElement)
                .append("> and <")
                .append(secondCodeElement)
                .append(">");
        output.append("\n seem similarly related to the comment:");
        output.append("\"")
                .append("(" + typeOfClone + ")")
                .append(commentText).append("\"");
        output.append("\nThe comment may be too generic: consider adding details.");

        // Don't lower severity from high to mild here, the correct severity for whole clones
        // MILD for overloading and HIGH for non overloading, should be already set
//        lastSeverity = SEVERITY.MILD;

        return output.toString();
    }

    private static String printParamHeader(String param1, String param2) {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("You documented in the same way 2 parameters with different name:");
        output.append("\n<").append(param1).append("> and <").append(param2).append(">");
        return output.toString();
    }

    private static String printMoreSimilarElement(String firstCodeElement, String secondCodeElement,
                                                  String typeOfClone, String commentText) {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        if ("original".equals(utilStats.getWinnerMethod())) {
            output.append("\n");
            output.append(++issueCountPerRecord).append(") The comment you cloned:");
            output.append("\"")
                    .append("(" + typeOfClone + ")")
                    .append(commentText).append("\"");
            output.append("\nseems more related to <").append(firstCodeElement).append("> than <").append(secondCodeElement).append(">");
        } else {
            output.append("\n");
            output.append(++issueCountPerRecord).append(") The comment you cloned:");
            output.append("\"")
                    .append("(" + typeOfClone + ")")
                    .append(commentText).append("\"");
            output.append("\nseems more related to <").append(secondCodeElement).append("> than <").append(firstCodeElement).append(">");
        }
        return output.toString();
    }

    private static String printWholeCloneOutput(boolean isOverload, String methodOne, String methodTwo) {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append(++issueCountPerRecord).append(") You cloned the whole comment for methods");
        output.append("\n<").append(methodOne).append("> and \n<").append(methodTwo).append(">");
        output.append("\n");
        if (isOverload) {
            output.append("\n");
            output.append("Even though this is a case of OVERLOADING, you should document the differences between " +
                    "the two methods in their @param tags.");
            lastSeverity = SEVERITY.MILD;
        } else {
            output.append("\n");
            output.append("This is not an overloading case. " +
                    "Check the differences among the two methods and document them.");
            lastSeverity = SEVERITY.HIGH;
        }
        return output.toString();
    }

    private static CommentSentence.CommentPart identifyCommentPart(String typeOfClone) {
        switch (typeOfClone) {
            case "Free text":
                return CommentSentence.CommentPart.DESC;
            case "@param":
                return CommentSentence.CommentPart.PARAM;
            case "@return":
                return CommentSentence.CommentPart.RETURN;
            case "@throws":
                return CommentSentence.CommentPart.EXCEP;
            default:
                return null;
        }
    }
}
