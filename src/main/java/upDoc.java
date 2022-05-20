import entry.MappingAnalyzer;

/**
 * Entry point of the upDoc tool. {@code upDoc.main} is automatically executed running the command:
 * {@code $ java -jar target/upDoc-1.0-SNAPSHOT-jar-with-dependencies.jar OPTIONS...}.
 */
public class upDoc {

  public static void main(String args[]) {

    // create mapping between an AST node and a (doc) comment
    if ("analysis:mapping".equals(args[0])) {
      if (args.length == 4) {
        System.out.println("\n" + args[1] + args[2]);
        System.out.println(MappingAnalyzer.reportMapping(args[1], args[2], args[3]));
      } else {
        System.err.println("[upDoc error]: not enough arguments for mapping analysis");
        System.err.println("[upDoc error]: expected arguments: analysis:commit");
        System.err.println("[upDoc error]: \tanalysis:mapping PATH FILE SENSITIVITY");
      }
    }
  }

}
