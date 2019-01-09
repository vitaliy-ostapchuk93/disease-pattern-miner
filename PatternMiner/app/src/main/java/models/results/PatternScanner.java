package models.results;

import models.data.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatternScanner {

    private final static Logger LOGGER = Logger.getLogger(PatternScanner.class.getName());
    private String seqKey;
    private Pattern pattern;

    private Map<DataFile, List<ICDSequence>> matchingSequences;
    private Map<DataFile, Set<ICDLink>> icdLinksMap;


    public PatternScanner(String seqKey) {
        this.seqKey = seqKey;
        this.matchingSequences = new ConcurrentHashMap<>();
        this.icdLinksMap = new ConcurrentHashMap<>();

        LOGGER.setLevel(Level.INFO);
    }


    public String scanForPattern(GroupDataFile dataFile) {
        LOGGER.info("Scanning " + dataFile.getName() + " for pattern ( " + seqKey + " )");

        if (pattern == null) {
            createRegex();
        }
        if (!matchingSequences.containsKey(dataFile)) {
            scanForMatchingSequences(dataFile);
            LOGGER.info("Found " + matchingSequences.get(dataFile).size() + " sequences mathching the pattern ( " + seqKey + " )");
        }

        if (!getMatchingCodesMap(dataFile).isEmpty()) {
            return buildPatternsResultString(dataFile);
        }
        return "Error! No matching patterns found...";
    }

    private String buildPatternsResultString(GroupDataFile dataFile) {
        StringBuilder patterns = new StringBuilder("<b>Pattern Code-Search [ICD-9 Code | SUP] :</b> <br>");

        for (String key : seqKey.split(" ")) {
            if (Integer.parseInt(key) != -1) {
                patterns.append("<div class=\"column\">");
                patterns.append("<div class=\"content\">");
                patterns.append("<p>Codes for <b>").append(key).append("</b> [").append(DiagnosesGroup.values()[Integer.parseInt(key)].name()).append("]</p>");
                patterns.append("</div>");
                patterns.append("<div class=\"field is-grouped is-grouped-multiline\">");

                Map<ICDCode, Integer> topTen = getMatchingCodesMap(dataFile).get(Integer.parseInt(key)).entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                for (Map.Entry<ICDCode, Integer> code : topTen.entrySet()) {
                    float p = code.getValue() * 100.0f / matchingSequences.get(dataFile).size();
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

    private Map<Integer, Map<ICDCode, Integer>> getMatchingCodesMap(GroupDataFile dataFile) {
        Map<Integer, Map<ICDCode, Integer>> matchingCodesMap = new HashMap<>();

        Iterator<ICDSequence> iterator = matchingSequences.get(dataFile).iterator();

        while (iterator.hasNext()) {
            ICDSequence sequence = iterator.next();
            for (String key : seqKey.split(" ")) {
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

    public void scanForMatchingSequences(GroupDataFile dataFile) {
        matchingSequences.put(dataFile, new ArrayList<>());

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
                            matchingSequences.get(dataFile).add(sequence);
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

    private String sortedPattern() {
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


    public Set<ICDLink> getIcdLinks(GroupDataFile dataFile) {
        if (pattern == null) {
            createRegex();
        }
        if (!matchingSequences.containsKey(dataFile)) {
            scanForMatchingSequences(dataFile);
            LOGGER.info("Found " + matchingSequences.get(dataFile).size() + " sequences mathching the pattern ( " + seqKey + " )");
        }
        if (!icdLinksMap.containsKey(dataFile)) {
            createIcdLinks(dataFile);
        }

        return icdLinksMap.get(dataFile);
    }

    private void createIcdLinks(GroupDataFile dataFile) {
        String[] keys = seqKey.replace("-1", "").replace("  ", " ").split(" ");
        Iterator<ICDSequence> icdSequenceIterator = matchingSequences.get(dataFile).iterator();
        List<ICDLink> icdLinks = new ArrayList<>();

        while (icdSequenceIterator.hasNext()) {
            ICDSequence sequence = icdSequenceIterator.next();

            for (int i = 0; i < keys.length - 1; i++) {
                int groupSource = Integer.parseInt(keys[i]);
                int groupTarget = Integer.parseInt(keys[i + 1]);

                Iterator<ICDCode> iteratorSource = sequence.getCodesMatchingGroup(groupSource).iterator();
                while (iteratorSource.hasNext()) {
                    ICDCode source = iteratorSource.next();
                    Iterator<ICDCode> iteratorTarget = sequence.getCodesMatchingGroup(groupTarget).iterator();
                    while (iteratorTarget.hasNext()) {
                        ICDCode target = iteratorTarget.next();
                        ICDLink icdLink = new ICDLink(source, target);

                        if (!icdLinks.contains(icdLink)) {
                            icdLinks.add(icdLink);
                        } else {
                            icdLinks.get(icdLinks.indexOf(icdLink)).addCount();
                        }
                    }
                }
            }
        }

        icdLinksMap.put(dataFile, new HashSet<>(icdLinks));
    }
}
