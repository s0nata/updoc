import entry.ChangeAnalyzer;
import entry.ChangeExtractor;
import entry.Mapper;
import entry.Parser;

/**
 * Entry point of the upDoc tool. {@code upDoc.main} is automatically executed running the command:
 * {@code $ java -jar target/upDoc-1.0-SNAPSHOT-jar-with-dependencies.jar OPTIONS...}.
 */
public class upDoc {

    public static void main(String[] args) {

        if ("demo:parsing".equals(args[0])) {
            if (args.length == 3) {
                String path = args[1];
                String file = args[2];
                Parser.reportMethodBodyNodeCoverage(path, file);
            } else {
                System.out.println("[upDoc error]: wrong number of arguments for the parsing demo");
                System.out.println("[upDoc error]: expected arguments:");
                System.out.println("[upDoc error]: \tdemo:parsing PATH FILE");
            }
        } else if ("demo:mapping".equals(args[0])) {
            if (args.length == 4) {
                System.out.println(Mapper.reportMapping(args[1], args[2], args[3]));
            } else {
                System.out.println("[upDoc error]: wrong number of arguments for the mapping demo");
                System.out.println("[upDoc error]: expected arguments:");
                System.out.println("[upDoc error]: \tdemo:mapping PATH FILE SENSITIVITY");
            }
        } else if ("demo:change:extraction".equals(args[0])) {
            if (args.length == 4) {
                ChangeExtractor.reportChangesTwoFiles(args[1], args[2], args[3]);
            } else {
                System.out.println("[upDoc error]: wrong number of arguments for the change extraction demo");
                System.out.println("[upDoc error]: expected arguments:");
                System.out.println("[upDoc error]: \tdemo:change:extraction PATH FILE-BEFORE FILE-AFTER");
            }
        } else if ("demo:change:analysis".equals(args[0])) {
            if (args.length == 4) {
                ChangeAnalyzer.reportChangeAnalysisTwoFiles(args[1], args[2], args[3]);
            } else {
                System.out.println("[upDoc error]: wrong number of arguments for the change analysis demo");
                System.out.println("[upDoc error]: expected arguments:");
                System.out.println("[upDoc error]: \tdemo:change:extraction PATH FILE-BEFORE FILE-AFTER");
            }
        } else {
            System.out.println("[upDoc error]: could not process input, please re-enter data");
        }

    }
}
