package models.results;

import com.google.common.collect.Table;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import models.data.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class PatternScanner {

    private final static Logger LOGGER = Logger.getLogger(PatternScanner.class.getName());

    private String seqKey;
    private Pattern pattern;


    public PatternScanner(String seqKey) {
        this.seqKey = seqKey;
        createRegex();
        LOGGER.setLevel(Level.INFO);
    }

    private void createRegex() {
        String sortedSeqKey = sortedPattern();

        // build regex
        StringBuilder ex = new StringBuilder();
        ex.append("(^| )");

        for (String code : sortedSeqKey.split(" ")) {
            if (code.equals("-2")) {
                break;
            } else if (code.equals("-1")) {
                ex.append(code + "(.*)");
            } else {
                ex.append(code + " ([^-]*)");
            }
        }

        LOGGER.info("Builded regex pattern > " + ex.toString());
        this.pattern = Pattern.compile(ex.toString());
    }

    public String sortedPattern() {
        String expanded = seqKey + " -1 -2";
        String[] codes = expanded.split(" ");
        SortedSet<String> itemset = new TreeSet<>(Comparator.comparingInt(Integer::parseInt));
        StringBuilder sortedCodes = new StringBuilder();

        for (String code : codes) {
            if (code.equals("-1")) {
                for (String item : itemset) {
                    sortedCodes.append(item).append(" ");
                }
                itemset.clear();
            }
            if (code.equals("-2")) {
                sortedCodes.append("-1 -2");
            } else {
                itemset.add(code);
            }
        }
        String sorted = sortedCodes.toString();

        return sorted;
    }

    private JsonArray buildLinksJSON(Map<ICDLink, Long> linksMap) {

        Map<ICDLink, Long> filteredLinkMap = linksMap.entrySet().stream()
                .filter(icdLinkEntry -> icdLinkEntry.getValue() >= 10)
                .sorted(comparingByValue(Comparator.reverseOrder()))
                .limit(250)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return createLinks(filteredLinkMap);
    }


    public JsonArray getCommonCodesJSON(ServletContext servletContext, GroupDataFile groupFileOfResult) {
        return (JsonArray) loadJson(getCommonCodesFilePath(servletContext, groupFileOfResult));
    }

    private JsonArray createLinks(Map<ICDLink, Long> linksMap) {
        JsonArray links = new JsonArray();

        for (Map.Entry<ICDLink, Long> icdLink : linksMap.entrySet()) {
            JsonObject link = new JsonObject();

            link.addProperty("source", icdLink.getKey().getSource().getSmallCode());
            link.addProperty("target", icdLink.getKey().getTarget().getSmallCode());
            link.addProperty("value", icdLink.getValue());

            links.add(link);
        }

        return links;
    }

    public JsonObject getCompleteIcdLinksJSON(SortedMap<GenderAgeGroup, ResultsEntry> pattern, ServletContext servletContext) {
        JsonObject obj = new JsonObject();

        for (Map.Entry<GenderAgeGroup, ResultsEntry> entry : pattern.entrySet()) {
            JsonArray fullIcdLinksGroup = getIcdLinksJSON(servletContext, entry.getValue().getGroupFileOfResult());
            obj.add(entry.getKey().toString(), fullIcdLinksGroup);
        }

        return obj;
    }

    private JsonArray getIcdLinksJSON(ServletContext servletContext, GroupDataFile groupFileOfResult) {
        return (JsonArray) loadJson(getFullIcdLinksFilePath(servletContext, groupFileOfResult));
    }

    public void createInverseSearchFiles(ServletContext servletContext, Table.Cell<String, GenderAgeGroup, ResultsEntry> cell) {
        createInverseSearchFiles(servletContext, cell.getValue());
    }

    public void createInverseSearchFiles(ServletContext servletContext, ResultsEntry entry) {
        File dir = new File(servletContext.getRealPath(getScanDir()));
        dir.mkdirs();

        Map<ICDLink, Long> fullLinks = new HashMap<>();
        Map<ICDCode, Long> fullCommonCodes = new HashMap<>();

        String commonCodesFilePath = getCommonCodesFilePath(servletContext, entry.getGroupFileOfResult());
        String icdLinksFilePath = getFullIcdLinksFilePath(servletContext, entry.getGroupFileOfResult());

        boolean checkCommonCodes = checkIfFileCreated(commonCodesFilePath);
        boolean checkIcdLinks = checkIfFileCreated(icdLinksFilePath);


        if (!checkCommonCodes || !checkIcdLinks) {
            try {
                LineIterator it = FileUtils.lineIterator(entry.getGroupFileOfResult(), "UTF-8");

                ICDSequence sequence = null;
                while (it.hasNext()) {
                    String[] p = it.nextLine().split(",");

                    //first sequence
                    if (sequence == null) {
                        sequence = new ICDSequence(p[0]);
                    }

                    //different sequence id
                    if (!p[0].equals(sequence.getId())) {
                        if (sequence.getFilteredDiagnosesCount() > 2) {
                            String formatedSeq = sequence.getFormatedSeqSPMF(14);

                            Matcher m = pattern.matcher(formatedSeq);
                            if (m.find()) {
                                //commonCodes
                                if (!checkCommonCodes) {
                                    Map<ICDCode, Long> icdCommonCodes = sequence.getAllIcdCodes().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                                    icdCommonCodes.forEach((k, v) -> fullCommonCodes.merge(k, v, (v1, v2) -> v1 + v2));
                                }

                                // icdLinks
                                if (!checkIcdLinks) {
                                    Map<ICDLink, Long> icdLinks = sequence.getIcdLinks().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                                    icdLinks.forEach((k, v) -> fullLinks.merge(k, v, (v1, v2) -> v1 + v2));
                                }
                            }
                        }

                        sequence = new ICDSequence(p[0]);
                    }

                    //same sequence
                    if (p.length >= 2) {
                        sequence.addDiagnoses(p[1], Arrays.copyOfRange(p, 2, p.length));
                    }

                }
                it.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (!checkCommonCodes) {
                saveJson(buildCommonCodesJSON(fullCommonCodes), commonCodesFilePath);
            }

            if (!checkIfFileCreated(icdLinksFilePath)) {
                saveJson(buildLinksJSON(fullLinks), icdLinksFilePath);
            }
        }
    }

    private JsonElement buildCommonCodesJSON(Map<ICDCode, Long> fullCommonCodes) {
        Map<Integer, Map<ICDCode, Double>> commonCodes = new HashMap<>();

        String[] keys = seqKey.split(" ");
        for (String key : keys) {
            int group = Integer.parseInt(key);

            if (group == -1) {
                continue;
            }

            if (!commonCodes.containsKey(Integer.parseInt(key))) {
                commonCodes.put(group, new HashMap<>());
            }

            Map<ICDCode, Long> commonCodesInGroup = fullCommonCodes.entrySet().stream()
                    .filter(entry -> entry.getKey().getGroup().ordinal() == group)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            int sum = commonCodesInGroup.values().stream().mapToInt(entry -> entry.intValue()).sum();

            Map<ICDCode, Double> topTen = commonCodesInGroup.entrySet().stream()
                    .filter(entry -> entry.getValue() >= 0.05 * sum)
                    .sorted(comparingByValue(Comparator.reverseOrder())).limit(10)
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> round(entry.getValue() / (1.0 * sum))));

            commonCodes.put(group, topTen);
        }

        return createCommonCodes(commonCodes);
    }

    private double round(double val) {
        return Math.round(val * 10000) / 100.0;
    }

    private JsonElement createCommonCodes(Map<Integer, Map<ICDCode, Double>> commonCodes) {
        JsonArray fullCommonCodes = new JsonArray();

        for (Map.Entry<Integer, Map<ICDCode, Double>> topGroupsCodes : commonCodes.entrySet()) {
            JsonObject groupCommonCodes = new JsonObject();

            groupCommonCodes.addProperty("groupOrdinal", topGroupsCodes.getKey());
            groupCommonCodes.addProperty("groupName", DiagnosesGroup.values()[topGroupsCodes.getKey()].name());

            JsonArray topCodes = new JsonArray();

            for (Map.Entry<ICDCode, Double> topCode : topGroupsCodes.getValue().entrySet()) {
                JsonObject code = new JsonObject();
                code.addProperty("code", topCode.getKey().getSmallCode());
                code.addProperty("count", topCode.getValue());

                topCodes.add(code);
            }

            groupCommonCodes.add("topCodes", topCodes);

            fullCommonCodes.add(groupCommonCodes);
        }
        return fullCommonCodes;
    }

    private String getFullIcdLinksFilePath(ServletContext servletContext, GroupDataFile file) {
        String scanName = file.getName().replace(".csv", "_fl.json").replace("_sorted", "");
        return servletContext.getRealPath(getScanDir() + File.separator + scanName);
    }

    private String getCommonCodesFilePath(ServletContext servletContext, GroupDataFile file) {
        String scanName = file.getName().replace(".csv", "_cc.json").replace("_sorted", "");
        return servletContext.getRealPath(getScanDir() + File.separator + scanName);
    }


    private String getScanDir() {
        return ResultsDataFile.DIR_PATH + File.separator + "scan_ " + seqKey;
    }

    private boolean checkIfFileCreated(String path) {
        File file = new File(path);
        return file.exists();
    }


    private void saveJson(JsonElement object, String filePath) {
        File file = new File(filePath);
        OutputStream outputStream = null;
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        try {
            outputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            gson.toJson(object, bufferedWriter);
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private JsonElement loadJson(String filePath) {
        JsonElement jsonElement = null;

        File file = new File(filePath);
        InputStream inputStream = null;


        try {
            inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
            JsonReader reader = new JsonReader(streamReader);
            JsonParser parser = new JsonParser();

            jsonElement = parser.parse(reader);

            streamReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonElement;
    }
}
