package mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbbreviationExpander {

    private static final Map<String, String> abbreviationMap = new HashMap<String, String>();
    private static final List<String> abbreviationFiles =
            List.of(
                    "ICSME_Abbreviation_Expansion_Artifact_Enscript.csv",
                    "ICSME_Abbreviation_Expansion_Artifact_KDevelop.csv",
                    "ICSME_Abbreviation_Expansion_Artifact_Open_Office.csv",
                    "ICSME_Abbreviation_Expansion_Artifact_Telegram.csv",
                    "ICSME_Abbreviation_Expansion_Artifact_Wycheproof.csv");
    private static final String abbrevation_folder = "abbreviation-lists/";

    public static String expandAbbreviationIdentifier(String abbreviation) {
        if (abbreviation.length() < 3) {
            return abbreviation;
        } else {
            if (abbreviationMap.isEmpty()) {
                buildAbbreviationMap();
            }
            if (abbreviationMap.containsKey(abbreviation)) {
                return abbreviationMap.get(abbreviation);
            } else {
                return abbreviation;
            }
        }
    }

    private static void buildAbbreviationMap() {
        for (String fileName : abbreviationFiles) {
            try {
                for (String line :
                        getResourceFileAsStringList(abbrevation_folder + fileName).stream()
                                .skip(1)
                                .collect(Collectors.toList())) {
                    //lines in csv file can be "commented" by prefixing the line with "#"
                    if (!line.startsWith("#")) {
                        String[] splitted_line = line.split(",");
                        String abbreviation_expansion = splitted_line[1];
                        for (String abbreviation : abbreviation_expansion.split("-")) {
                            abbreviation = abbreviation.replace("(", "").replace(")", "");
                            String[] splitted_abbreviation = abbreviation.split(":");
                            String abbrev = splitted_abbreviation[0].toLowerCase();
                            String expansion = splitted_abbreviation[1].toLowerCase();
              /* heuristics: ignore abbreviations shorter than 3 letters and expansions with more than
              1 word */
                            if (abbrev.length() >= 3 && expansion.split("\\s").length == 1) {
                                if (!abbreviationMap.containsKey(abbrev)) {
                                    abbreviationMap.put(abbrev, expansion);
                                } else if (!expansion.equals(abbreviationMap.get(abbrev))) {
                                    System.out.println(
                                            "Already exists: " + abbrev + " -> " + abbreviationMap.get(abbrev));
                                    System.out.println("Try to add: " + abbrev + " -> " + expansion);
                                    System.out.println(line);
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static List<String> getResourceFileAsStringList(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.toList());
            }
        }
    }
}
