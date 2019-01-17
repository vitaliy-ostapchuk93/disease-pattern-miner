package models.results;

import com.google.common.collect.Table;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import models.data.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.uncommons.maths.statistics.EmptyDataSetException;

import javax.servlet.ServletContext;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    private List<ICDSequence> currentIcdSequences;
    private Map<String, Map<ICDLink, Long>> fullLinksMap;

    public PatternScanner(String seqKey) {
        this.seqKey = seqKey;
        createRegex();

        this.currentIcdSequences = new ArrayList<>();
        this.fullLinksMap = new ConcurrentHashMap<>();

        LOGGER.setLevel(Level.INFO);
    }


    public String scanForCommonICDCodes() {
        if (!currentIcdSequences.isEmpty()) {
            return buildPatternsResultString(currentIcdSequences);
        } else {
            throw new EmptyDataSetException();
        }
    }

    private String buildPatternsResultString(List<ICDSequence> matchingSequences) {
        StringBuilder patterns = new StringBuilder("<b>Pattern Code-Search [ICD-9 Code | SUP] :</b> <br>");

        for (String key : seqKey.split(" ")) {
            if (Integer.parseInt(key) != -1) {
                patterns.append("<div class=\"column\">");
                patterns.append("<div class=\"content\">");
                patterns.append("<p>Codes for <b>").append(key).append("</b> [").append(DiagnosesGroup.values()[Integer.parseInt(key)].name()).append("]</p>");
                patterns.append("</div>");
                patterns.append("<div class=\"field is-grouped is-grouped-multiline\">");


                Map<ICDCode, Integer> topTen = getMatchingCodesMap(matchingSequences).get(Integer.parseInt(key)).entrySet().stream()
                        .sorted(comparingByValue(Comparator.reverseOrder())).limit(10)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                for (Map.Entry<ICDCode, Integer> code : topTen.entrySet()) {
                    float p = code.getValue() * 100.0f / matchingSequences.size();
                    if (p >= 5) {
                        patterns.append("<div class=\"control\">");
                        patterns.append("<div class=\"tags has-addons\">");
                        patterns.append("<span class=\"tag\">").append(code.getKey().getSmallCode()).append("</span>");
                        patterns.append("<span class=\"tag is-info\">");

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);
                        patterns.append(df.format(p) + " %");

                        patterns.append("</span>");
                        patterns.append("</div>");
                        patterns.append("</div>");
                    }
                }


                patterns.append("</div>");
                patterns.append("</div>");
            }
        }
        return patterns.toString();
    }

    private Map<Integer, Map<ICDCode, Integer>> getMatchingCodesMap(List<ICDSequence> matchingSequences) {
        Map<Integer, Map<ICDCode, Integer>> matchingCodesMap = new HashMap<>();
        String[] keys = seqKey.split(" ");

        for (ICDSequence sequence : matchingSequences) {
            for (String key : keys) {
                int group = Integer.parseInt(key);
                if (!matchingCodesMap.containsKey(Integer.parseInt(key))) {
                    matchingCodesMap.put(group, new HashMap<>());
                }
                for (ICDCode code : sequence.getCodesMatchingGroup(group)) {
                    if (!matchingCodesMap.get(group).containsKey(code)) {
                        matchingCodesMap.get(group).put(code, 0);
                    }
                    matchingCodesMap.get(group).put(code, matchingCodesMap.get(group).get(code) + 1);
                }
            }
        }

        return matchingCodesMap;
    }

    public List<ICDSequence> scanForMatchingSequences(GroupDataFile dataFile) {
        List<ICDSequence> matchingSequences = new ArrayList<>();

        try {
            LineIterator it = FileUtils.lineIterator(dataFile, "UTF-8");

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
                            matchingSequences.add(sequence);
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

        return matchingSequences;
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

        this.currentIcdSequences = scanForMatchingSequences(entry.getGroupFileOfResult());

        String commonCodesFilePath = getCommonCodesFilePath(servletContext, entry.getGroupFileOfResult());
        if (!checkIfFileCreated(commonCodesFilePath)) {
            createCommonCodesJSON(commonCodesFilePath);
        }

        String fullIcdLinksFilePath = getFullIcdLinksFilePath(servletContext, entry.getGroupFileOfResult());
        if (!checkIfFileCreated(fullIcdLinksFilePath)) {
            createIcdLinksJSON(entry, fullIcdLinksFilePath);
        }
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

    private void createIcdLinksJSON(ResultsEntry entry, String fullIcdLinksFilePath) {
        List<ICDLink> icdLinks = new ArrayList<>();

        for (ICDSequence sequence : currentIcdSequences) {
            icdLinks.addAll(sequence.getIcdLinks());
        }

        Map<ICDLink, Long> fullLinks = icdLinks.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        fullLinksMap.put(entry.getGroupFileOfResult().getName(), fullLinks);

        JsonArray fullLinksJSON = buildLinksJSON(fullLinks);

        saveJson(fullLinksJSON, fullIcdLinksFilePath);
    }

    private void createCommonCodesJSON(String commonCodesFilePath) {
        JsonObject codes = new JsonObject();
        JsonArray commonCodes = new JsonArray();

        for (String key : seqKey.split(" ")) {
            if (Integer.parseInt(key) != -1) {
                JsonObject group = new JsonObject();
                group.addProperty("name", DiagnosesGroup.values()[Integer.parseInt(key)].name());
                group.addProperty("ordinal", DiagnosesGroup.values()[Integer.parseInt(key)].ordinal());

                Map<ICDCode, Integer> topTen = getMatchingCodesMap(currentIcdSequences).get(Integer.parseInt(key)).entrySet().stream()
                        .sorted(comparingByValue(Comparator.reverseOrder())).limit(10)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                JsonArray commonCodesGroup = new JsonArray();

                for (Map.Entry<ICDCode, Integer> topCode : topTen.entrySet()) {
                    JsonObject code = new JsonObject();
                    code.addProperty("code", topCode.getKey().getSmallCode());
                    code.addProperty("support", topCode.getValue());

                    commonCodesGroup.add(code);
                }

                group.add("commonCodes", commonCodesGroup);
                commonCodes.add(group);
            }
        }

        codes.add("commonCodes", commonCodes);

        saveJson(codes, commonCodesFilePath);
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
