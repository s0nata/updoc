package entry;

import changeanalyzer.ChangeReport;
import changextractor.Commit;
import mapper.MapBuilder;

import java.util.List;

/**
 * upDoc interface to the change analysis component.
 */
public class ChangeAnalyzer {

    public static void reportChangeAnalysisTwoFiles(String dir, String filenameBefore, String filenameAfter) {

        Commit cmt = new Commit(dir + filenameBefore, dir + filenameAfter);

        Double similarityThreshold = 0.2;

        boolean semanticSimilarity = false;

        List<MapBuilder> mappingsFromSrcFile = Mapper.reportMappingsFromSource(
                dir, filenameBefore,
                Double.toString(similarityThreshold), semanticSimilarity);

        List<MapBuilder> mappingsFromDstFile = Mapper.reportMappingsFromSource(
                dir, filenameAfter,
                Double.toString(similarityThreshold), semanticSimilarity);

        String reportFormat = "";

        ChangeReport cr = new ChangeReport(cmt, mappingsFromSrcFile, mappingsFromDstFile, reportFormat);

        System.out.println(cr.getReport());

    }
}
