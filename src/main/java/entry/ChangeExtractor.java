package entry;

import changextractor.Commit;


/**
 * upDoc interface for extracting AST changes, mostly for demo use
 */
public class ChangeExtractor {

    /**
     * Given paths of two revisions of a file pretty-print all detected changes.
     * <p>
     *
     * @param dir            folder of the file, must end with '/'
     * @param filenameBefore filename before change, must have extension
     * @param filenameAfter  filename after change, must have extension
     */
    public static void reportChangesTwoFiles(String dir, String filenameBefore, String filenameAfter) {

        Commit currentCommit = new Commit(dir + filenameBefore, dir + filenameAfter);
        currentCommit.prettyPrintChanges();

    }
}
