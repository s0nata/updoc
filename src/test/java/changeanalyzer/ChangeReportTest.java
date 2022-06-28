package changeanalyzer;

import changextractor.Change;
import changextractor.Commit;
import entry.Mapper;
import mapper.MapBuilder;
import org.junit.Test;
import parser.MethodInspector;
import parser.MethodNode;
import parser.nodes.AbstractNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangeReportTest {

    private static final String FILE_DIR_CHANGE = "src/test/resources/change/";

    private static final String FILE_DIR_CHANGECONS = "src/test/resources/change-consistent/";

    private static final String FILE_DIR_FIX = "src/test/resources/fix-suggest/";
    private static final String SRC = "0_AdaptiveIsomorphismInspectorFactory.java";
    private static final String DST = "1_AdaptiveIsomorphismInspectorFactory.java";

    // FIXME here there's no mapping/no Node LOCs in the mapping?
//    private static final String SRC = "0_JGraphModelAdapter.java";
//    private static final String DST = "1_JGraphModelAdapter.java";

    private static final String SIMILARITY_THRESHOLD = "0.2";

    @Test
    public void testNodeMatchingNoSemantic() {
        Commit dummyCommit = new Commit(FILE_DIR_CHANGE + SRC, FILE_DIR_CHANGE + DST);
        List<MapBuilder> mappingsFromSrcFile = Mapper.reportMappingsFromSource(
                FILE_DIR_CHANGE, SRC, SIMILARITY_THRESHOLD, false);
        List<MapBuilder> mappingsFromDstFile = Mapper.reportMappingsFromSource(
                FILE_DIR_CHANGE, DST, SIMILARITY_THRESHOLD, false);

        ChangeReport chrep = new ChangeReport(dummyCommit, mappingsFromSrcFile, mappingsFromDstFile, "");

        String inconsistencyReport = chrep.getReport();

        System.out.println(inconsistencyReport);
    }

    @Test
    public void testNodeMatchingSemantic() {
        Commit dummyCommit = new Commit(FILE_DIR_CHANGE + SRC, FILE_DIR_CHANGE + DST);
        List<MapBuilder> mappingsFromSrcFile = Mapper.reportMappingsFromSource(
                FILE_DIR_CHANGE, SRC, SIMILARITY_THRESHOLD, true);
        List<MapBuilder> mappingsFromDstFile = Mapper.reportMappingsFromSource(
                FILE_DIR_CHANGE, DST, SIMILARITY_THRESHOLD, true);

        ChangeReport chrep = new ChangeReport(dummyCommit, mappingsFromSrcFile, mappingsFromDstFile, "");

        String inconsistencyReport = chrep.getReport();
        System.out.println(inconsistencyReport);
    }


    // LOCs of methods extracted by parser and changextractor should match for same sources
    // logic: for each coarse node extracted by GTD there should be exactly one respective node
    //        extracted with JavaParser (LOC match and type match (for latter see #18))
    // FIXME at the moment only printing no checking
    // FIXME missing signature ranges comparison
    // FIXME missing actual range checking for body nodes
    @Test
    public void testLOCMatching() {

        // extract nodes with parser

        ArrayList<MethodNode> srcMethods = MethodInspector.getMethodNodesFromFile(new File(FILE_DIR_CHANGE + SRC));
//        ArrayList<MethodNode> dstMethods = MethodInspector.getMethodNodesFromFile(new File(FILE_DIR + DST));

        // extract nodes with changextractor

        Commit testCommit = new Commit(FILE_DIR_CHANGE + SRC, FILE_DIR_CHANGE + DST);

        for (Change chg : testCommit.getChanges()) {

            System.out.println("SRC"); // TODO also do for DST

            System.out.println("> node from the change (by GTD)");
            System.out.println(chg.getSrcNodeCoarseType() + chg.getSrcNodeCoarseLOCs());
            System.out.println("> should appear among all method body nodes of the respective method (by JavaParser)");
            for (MethodNode mn : srcMethods) {
                if (mn.getLOCs().containsRange(chg.getSrcNodeCoarseLOCs())) {
                    System.out.println("> enclosing method:");
                    System.out.println(mn.getMethodName() + mn.getLOCs());
                    // it is either signature or one of the body nodes
                    //
                    // signature should begin with method on the same line and end before first body node
                    // TODO: StructuredSignature does not have LOCs, but we have a "MethodSignature" node
                    //       (as NodeInfo created in Change) <- this should be aligned with StructuredSignature
                    //       this can also go as part of fixes in #18
                    if (mn.getLOCs().getMinimum().equals(chg.getSrcNodeCoarseLOCs().getMinimum())) {
                        // signature LOC comparison
                        System.out.println("> here should have been signature LOC comparison (change in the signature)");

                        System.out.println("NEW: MethodSignature from JP LOCs: " + mn.getSignatureNode().getLOCs());

                    } else {
                        System.out.println("> here should have been body node LOC comparison (change in the body)");

                        for (AbstractNode an : mn.getBodyNodesSequence()) {
                            // only select coarse-grained nodes
                            // FIXME can we compare not by string matching?
                            if (!(an.getClass().getTypeName().equals(parser.nodes.OtherNode.class.getName()))) {
                                System.out.println(an.getClass().getTypeName() + an.getLOCs());
                            }
                        }
                    }
                }
            }

        }
    }

    @Test
    public void fixSuggestsFoldrTest() {
        loopThroughFiles(FILE_DIR_FIX, false);
    }


    @Test
    public void fixSuggestsFoldrSemanticTest() {
        loopThroughFiles(FILE_DIR_FIX, true);
    }

    @Test
    public void changeFoldrTest() {
        loopThroughFiles(FILE_DIR_CHANGE, false);
    }


    @Test
    public void changeFoldrSemanticTest() {
        loopThroughFiles(FILE_DIR_CHANGE, true);
    }


    @Test
    public void changeConsistentFoldrTest() {
        loopThroughFiles(FILE_DIR_CHANGECONS, false);
    }

    @Test
    public void changeConsistentFoldrSemanticTest() {
        loopThroughFiles(FILE_DIR_CHANGECONS, true);
    }


    private void loopThroughFiles(String fixSuggestSrc, boolean semantic) {
        String prefix = "0_";
        String twinPrefix = "1_";

        Set<String> alreadySeen = new HashSet<>();
        File dir = new File(fixSuggestSrc);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (!alreadySeen.contains(child.getName()) && child.getName().startsWith("0_")) {
                    alreadySeen.add(child.getName());
                    String twinFile = child.getName().replace(prefix, twinPrefix);
                    alreadySeen.add(twinFile);

                    System.out.println("++++++++++++++++++ SEEING FILE: ++++++++++++++++++");
                    System.out.println("----" + child.getName() + "-----");
                    Commit dummyCommit = new Commit(fixSuggestSrc + child.getName(),
                            fixSuggestSrc + twinFile);
                    List<MapBuilder> mappingsFromSrcFile = Mapper.reportMappingsFromSource(
                            fixSuggestSrc, child.getName(), SIMILARITY_THRESHOLD, semantic);
                    List<MapBuilder> mappingsFromDstFile = Mapper.reportMappingsFromSource(
                            fixSuggestSrc, twinFile, SIMILARITY_THRESHOLD, semantic);

                    ChangeReport chrep = new ChangeReport(dummyCommit, mappingsFromSrcFile, mappingsFromDstFile, "cumulative");

                    String inconsistencyReport = chrep.getReport();

                    System.out.println(inconsistencyReport);
                }
            }
        }
    }
}
