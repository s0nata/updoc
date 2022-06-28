package changextractor;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CommitTest {

    // TODO would be good to have numbers of changes per change type

    private ArrayList<Integer> computeNumChangesForFilesInDir(String dirPath, ArrayList<String> fileNames) {
        ArrayList<Integer> changesPerFile = new ArrayList<>();

        for (String fileName : fileNames) {
            Commit testCommit = new Commit(dirPath + "0_" + fileName, dirPath + "1_" + fileName);
            changesPerFile.add(testCommit.getNumberOfChanges());
        }

        return changesPerFile;
    }

    @Test
    public void testNumberOfSourceNodeUpdateChangesOne() {

        // expected
        ArrayList<Integer> expectedNumChanges = new ArrayList<>();
        expectedNumChanges.add(1);
        expectedNumChanges.add(1);
        expectedNumChanges.add(0);
        expectedNumChanges.add(0);
        expectedNumChanges.add(1);

        // actual
        String dirName = "src/test/resources/change/";
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add("AdaptiveIsomorphismInspectorFactory.java");
        fileNames.add("AdaptiveIsomorphismInspectorFactory_Reduced.java");
        fileNames.add("BhandariKDisjointShortestPaths.java");
        fileNames.add("DefaultDirectedWeightedEdgeTest.java");
        fileNames.add("JGraphModelAdapter.java");

        ArrayList<Integer> actualNumChanges = computeNumChangesForFilesInDir(dirName, fileNames);

        assertEquals(expectedNumChanges.size(), actualNumChanges.size());
        for (int i = 0; i < expectedNumChanges.size(); i++) {
            assertEquals(expectedNumChanges.get(i), actualNumChanges.get(i));
        }

    }

    @Test
    public void testNumberOfSourceNodeUpdateChangesTwo() {

        // expected
        ArrayList<Integer> expectedNumChanges = new ArrayList<>();
        expectedNumChanges.add(7); // DateTimeFunctions
        expectedNumChanges.add(0); // ESClientFactory
        expectedNumChanges.add(112); // ExecutionTime
        expectedNumChanges.add(2); // FieldIndex
        expectedNumChanges.add(1); // GenericToken
        expectedNumChanges.add(5); // VirtualConsole
        expectedNumChanges.add(0); // VirtualMachine

        // actual
        String dirName = "src/test/resources/fix-suggest/";
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add("DateTimeFunctions.java");
        fileNames.add("ESClientFactory.java");
        fileNames.add("ExecutionTime.java");
        fileNames.add("FieldIndex.java");
        fileNames.add("GenericToken.java");
        fileNames.add("VirtualConsole.java");
        fileNames.add("VirtualMachine.java");
        ArrayList<Integer> actualNumChanges = computeNumChangesForFilesInDir(dirName, fileNames);

        assertEquals(expectedNumChanges.size(), actualNumChanges.size());
        for (int i = 0; i < expectedNumChanges.size(); i++) {
            assertEquals(expectedNumChanges.get(i), actualNumChanges.get(i));
        }

    }

}
