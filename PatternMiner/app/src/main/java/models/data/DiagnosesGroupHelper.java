package models.data;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import models.results.HSLColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DiagnosesGroupHelper {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    private static String ICD9_CSV_FILE_PATH = "/resources/datasets/input/csv/icd/icd9.csv";

    public static Set<ICDCode> filteredCodes = Collections.unmodifiableSet(Set.of(
            new ICDCode("460")                                                                         //common cold
    ));

    public static Set<DiagnosesGroup> filteredGroups = Collections.unmodifiableSet(Set.of(
            DiagnosesGroup.SupplementaryClassificationOfExternalCausesOfInjuryAndPoisoning,
            DiagnosesGroup.SupplementaryClassificationOfFactorsInfluencingHealthStatusAndContactWithHealthServices,
            DiagnosesGroup.CertainConditionsOriginatingInThePerinatalPeriod,
            DiagnosesGroup.UNKNOWN
    ));

    public Map<String, String> createIcdNamesMap(ServletContext ctx) {
        Map<String, String> icdNames = new HashMap<>();

        String icdPath = ctx.getRealPath(ICD9_CSV_FILE_PATH);
        File icdFile = new File(icdPath);

        try {
            LineIterator it = FileUtils.lineIterator(icdFile, "UTF-8");

            while (it.hasNext()) {
                List<String> line = parseLine(it.nextLine());

                String code = line.get(0).trim().replaceAll("\"", "");

                if (code.length() == 3) {
                    String name = line.get(2).trim().replaceAll("\"", "");
                    icdNames.put(code, name);
                }
            }

            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return icdNames;
    }

    public static String getColorByGroup(DiagnosesGroup group) {
        float hueValue;
        if (group == DiagnosesGroup.TIME_GAP) {
            hueValue = 0;
        } else {
            float h = 1.0f * (group.ordinal() + 1) / (getDiagnosesGroups().size() - 1);
            hueValue = h + (240.0f * h);
        }
        String hex = '#' + Integer.toHexString((HSLColor.toRGB(hueValue, 100, 80).getRGB() & 0xffffff) | 0x1000000).substring(1);
        return hex;
    }

    public static int lookupMin(DiagnosesGroup group) throws ICDLookupException {
        switch (group) {
            case InfectiousAndParasiticDiseases:
                return 1;
            case Neoplasms:
                return 140;
            case EndocrineNutritionalAndMetabolicDiseasesAndImmunityDisorders:
                return 240;
            case DiseasesOfTheBloodAndBloodFormingOrgans:
                return 280;
            case MentalDisorders:
                return 290;
            case DiseasesOfTheNervousSystemAndSenseOrgans:
                return 320;
            case DiseasesOfTheCirculatorySystem:
                return 390;
            case DiseasesOfTheRespiratorySystem:
                return 460;
            case DiseasesOfTheDigestiveSystem:
                return 520;
            case DiseasesOfTheGenitourinarySystem:
                return 580;
            case ComplicationsOfPregnancyChildbirthAndThePuerperium:
                return 630;
            case DiseasesOfTheSkinAndSubcutaneousTissue:
                return 680;
            case DiseasesOfTheMusculoskeletalSystemAndConnectiveTissue:
                return 710;
            case CongenitalAnomalies:
                return 740;
            case CertainConditionsOriginatingInThePerinatalPeriod:
                return 760;
            case SymptomsSignsAndIllDefinedConditions:
                return 780;
            case InjuryAndPoisoning:
                return 800;
            case SupplementaryClassificationOfFactorsInfluencingHealthStatusAndContactWithHealthServices:
                break;
            case SupplementaryClassificationOfExternalCausesOfInjuryAndPoisoning:
                break;
            case TIME_GAP:
                break;
            case UNKNOWN:
                break;
        }
        throw new ICDLookupException("ICD-Code min. lookup insufficient.");
    }

    public static int lookupMax(DiagnosesGroup group) throws ICDLookupException {
        switch (group) {
            case InfectiousAndParasiticDiseases:
                return 139;
            case Neoplasms:
                return 239;
            case EndocrineNutritionalAndMetabolicDiseasesAndImmunityDisorders:
                return 279;
            case DiseasesOfTheBloodAndBloodFormingOrgans:
                return 289;
            case MentalDisorders:
                return 319;
            case DiseasesOfTheNervousSystemAndSenseOrgans:
                return 389;
            case DiseasesOfTheCirculatorySystem:
                return 459;
            case DiseasesOfTheRespiratorySystem:
                return 519;
            case DiseasesOfTheDigestiveSystem:
                return 579;
            case DiseasesOfTheGenitourinarySystem:
                return 629;
            case ComplicationsOfPregnancyChildbirthAndThePuerperium:
                return 679;
            case DiseasesOfTheSkinAndSubcutaneousTissue:
                return 709;
            case DiseasesOfTheMusculoskeletalSystemAndConnectiveTissue:
                return 739;
            case CongenitalAnomalies:
                return 759;
            case CertainConditionsOriginatingInThePerinatalPeriod:
                return 779;
            case SymptomsSignsAndIllDefinedConditions:
                return 799;
            case InjuryAndPoisoning:
                return 999;
            case SupplementaryClassificationOfFactorsInfluencingHealthStatusAndContactWithHealthServices:
                break;
            case SupplementaryClassificationOfExternalCausesOfInjuryAndPoisoning:
                break;
            case TIME_GAP:
                break;
            case UNKNOWN:
                break;
        }
        throw new ICDLookupException("ICD-Code max. lookup insufficient.");
    }

    public static DiagnosesGroup parseCode(String code) {
        if (code.length() > 3) {
            code = code.substring(0, 3);                     //trim for just first 3 digits
        }

        if (code.startsWith("V")) {
            return DiagnosesGroup.SupplementaryClassificationOfFactorsInfluencingHealthStatusAndContactWithHealthServices;
        }
        if (code.startsWith("E")) {
            return DiagnosesGroup.SupplementaryClassificationOfExternalCausesOfInjuryAndPoisoning;
        }
        if (NumberUtils.isDigits(code)) {
            return parseCode(Integer.parseInt(code));
        } else {
            return DiagnosesGroup.UNKNOWN;
        }
    }

    public static DiagnosesGroup parseCode(int code) {
        for (DiagnosesGroup group : DiagnosesGroup.values()) {
            try {
                if (group == DiagnosesGroup.TIME_GAP) {
                    break;
                }
                if (code >= lookupMin(group) && code <= lookupMax(group)) {
                    return group;
                }
            } catch (ICDLookupException e) {
                continue;
            }
        }
        return DiagnosesGroup.UNKNOWN;
    }

    public static List<DiagnosesGroup> getDiagnosesGroups() {
        List<DiagnosesGroup> groups = new ArrayList<>();

        for (DiagnosesGroup group : DiagnosesGroup.values()) {
            if (!filteredGroups.contains(group)) {
                groups.add(group);
            }
        }

        return groups;
    }

    public static String getColorByGroup(int g) {
        DiagnosesGroup group = null;
        if (g == -1) {
            group = DiagnosesGroup.TIME_GAP;
        } else {
            group = DiagnosesGroup.values()[g];
        }
        return getColorByGroup(group);
    }

    public JsonObject createIcdNodesList(ServletContext ctx) {
        JsonObject nodes = new JsonObject();

        for (DiagnosesGroup group : DiagnosesGroup.values()) {
            JsonObject node = new JsonObject();

            node.addProperty("id", group.ordinal());
            node.addProperty("name", group.name());
            node.addProperty("type", group.name());
            node.add("parent", JsonNull.INSTANCE);

            nodes.add(String.valueOf(group.ordinal()), node);
        }


        String icdPath = ctx.getRealPath(ICD9_CSV_FILE_PATH);
        File icdFile = new File(icdPath);

        try {
            LineIterator it = FileUtils.lineIterator(icdFile, "UTF-8");

            while (it.hasNext()) {
                List<String> line = parseLine(it.nextLine());

                String code = line.get(0).trim().replaceAll("\"", "");

                if (code.length() == 3) {
                    String name = line.get(2).trim().replaceAll("\"", "");

                    JsonObject node = new JsonObject();

                    node.addProperty("id", code);
                    node.addProperty("name", name);
                    node.addProperty("type", parseCode(code).name());
                    node.addProperty("parent", parseCode(code).ordinal());

                    if (!name.isEmpty() && parseCode(code) != DiagnosesGroup.UNKNOWN) {
                        nodes.add(code, node);
                    }
                }
            }

            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodes;
    }


    public List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
}
