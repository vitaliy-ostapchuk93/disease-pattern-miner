package models.data;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.util.*;

public class ICDSequence implements Serializable {

    private final static int DAYS_IN_BETWEEN = 14;

    private String id;
    private Gender gender;
    private int ageGroup;

    private List<ICDEntry> diagnoses;

    public ICDSequence(String id) {
        this.id = id;
        this.diagnoses = new ArrayList<>();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setGender(String gender) {
        if (gender.equals("m")) {
            setGender(Gender.MALE);
        }
        if (gender.equals("f")) {
            setGender(Gender.FEMALE);
        }
    }

    public int getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        setAgeGroup(Integer.parseInt(ageGroup));
    }

    public void setAgeGroup(int ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void addDiagnoses(ICDEntry entry) {
        diagnoses.add(entry);
    }

    public void addDiagnoses(String time, String[] codes) {
        if (time.length() == 8 && codes.length > 0 && time.startsWith("20")) {
            addDiagnoses(new ICDEntry(time, codes));
        }
    }

    public int getDiagnosesCount() {
        int i = 0;
        for (ICDEntry entry : diagnoses) {
            i += entry.getIcdCodes().size();
        }
        return i;
    }

    public int getFilteredDiagnosesCount() {
        Set<DiagnosesGroup> filteredDiagnoses = new HashSet<>();
        for (ICDEntry entry : diagnoses) {
            filteredDiagnoses.addAll(entry.getFilteredDiagnoses());
        }
        return filteredDiagnoses.size();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormatedSeqSPMF() {
        return getFormatedSeqSPMF(DAYS_IN_BETWEEN);
    }

    public String getFormatedSeqSPMF(int daysInBetween) {
        StringBuilder seq = new StringBuilder();

        for (SortedSet<DiagnosesGroup> itemset : getSequenceOfItemsets(daysInBetween)) {
            for (DiagnosesGroup item : itemset) {
                seq.append(item.ordinal()).append(" ");
            }
            seq.append("-1 ");
        }
        seq.append("-2");
        return seq.toString();
    }

    public String getFormatedSeqMGSF() {
        return getFormatedSeqMGSF(DAYS_IN_BETWEEN);
    }

    public String getFormatedSeqMGSF(int daysInBetween) {
        StringBuilder seq = new StringBuilder(id + " ");

        for (SortedSet<DiagnosesGroup> itemset : getSequenceOfItemsets(daysInBetween)) {
            for (DiagnosesGroup item : itemset) {
                seq.append(item.ordinal()).append(" ");
            }
        }
        return seq.toString();
    }

    public String getFormatedSeqLASH() {
        return getFormatedSeqLASH(DAYS_IN_BETWEEN);
    }

    private String getFormatedSeqLASH(int daysInBetween) {
        StringBuilder seq = new StringBuilder(id + " ");

        for (SortedSet<ICDCode> itemset : getSortedICDCodeItemsets(daysInBetween)) {
            for (ICDCode code : itemset) {
                seq.append(code.getSmallCode()).append(" ");
            }
        }
        return seq.toString();
    }

    public List<SortedSet<DiagnosesGroup>> getSequenceOfItemsets() {
        return getSequenceOfItemsets(14);
    }

    public List<SortedSet<DiagnosesGroup>> getSequenceOfItemsets(int daysInBetween) {
        List<SortedSet<DiagnosesGroup>> sequeceItemsetList = new ArrayList<>();

        DateTime newD = null;
        DateTime oldD = null;

        SortedSet<DiagnosesGroup> itemset = new TreeSet<>(Comparator.comparingInt(Enum::ordinal));

        for (ICDEntry entry : diagnoses) {
            if (newD != null) {
                oldD = newD;
            }
            newD = entry.getDate();

            if (oldD != null && Days.daysBetween(oldD, newD).getDays() > daysInBetween) {
                sequeceItemsetList.add(itemset);
                itemset = new TreeSet<>(Comparator.comparingInt(Enum::ordinal));
            }
            itemset.addAll(entry.getFilteredDiagnoses());
        }

        if (!itemset.isEmpty()) {
            sequeceItemsetList.add(itemset);
        }

        //System.out.println(id + " : " + sequeceItemsetList);
        return sequeceItemsetList;
    }

    public String getFormatedCodes() {
        return getFormatedCodes(14);
    }

    public String getFormatedCodes(int daysInBetween) {
        StringBuilder seq = new StringBuilder();

        for (List<String> codesItemset : getSequenceOfItemsetCodes()) {
            for (String code : codesItemset) {
                seq.append(code).append(" - ");
            }
            seq.append(" | ");
        }

        return seq.toString();
    }

    public List<List<String>> getSequenceOfItemsetCodes() {
        return getSequenceOfItemsetCodes(14);
    }

    public List<List<String>> getSequenceOfItemsetCodes(int daysInBetween) {
        List<List<String>> sequeceCodesList = new ArrayList<>();

        DateTime newD = null;
        DateTime oldD = null;

        List<String> codesItemset = new ArrayList<>();

        for (ICDEntry entry : diagnoses) {
            if (newD != null) {
                oldD = newD;
            }
            newD = entry.getDate();

            if (oldD != null && Days.daysBetween(oldD, newD).getDays() > daysInBetween) {
                sequeceCodesList.add(codesItemset);
                codesItemset = new ArrayList<>();
            }

            for (ICDCode code : entry.getFilteredCodes()) {
                codesItemset.add(code.getSmallCode() + " [" + code.getGroup().ordinal() + "]");
            }
        }

        if (!codesItemset.isEmpty()) {
            sequeceCodesList.add(codesItemset);
        }

        return sequeceCodesList;
    }

    public Set<ICDCode> getCodesMatchingGroup(int key) {
        return getCodesMatchingGroup(key, 14);
    }

    public Set<ICDCode> getCodesMatchingGroup(int key, int daysInBetween) {
        List<SortedSet<ICDCode>> sequeceCodesList = getSortedICDCodeItemsets(daysInBetween);

        Set<ICDCode> codesSet = new HashSet<>();

        for (SortedSet<ICDCode> codeset : sequeceCodesList) {
            for (ICDCode code : codeset) {
                if (code.getGroup().ordinal() == key) {
                    codesSet.add(code);
                }
            }
        }

        return codesSet;
    }


    private List<SortedSet<ICDCode>> getSortedICDCodeItemsets(int daysInBetween) {
        List<SortedSet<ICDCode>> sequeceCodesList = new ArrayList<>();

        DateTime newD = null;
        DateTime oldD = null;

        SortedSet<ICDCode> codesItemset = new TreeSet<>(Comparator.comparingInt(o -> o.getGroup().ordinal()));

        for (ICDEntry entry : diagnoses) {
            if (newD != null) {
                oldD = newD;
            }
            newD = entry.getDate();

            if (oldD != null && Days.daysBetween(oldD, newD).getDays() > daysInBetween) {
                sequeceCodesList.add(codesItemset);
                codesItemset.clear();
            }

            for (ICDCode code : entry.getFilteredCodes()) {
                codesItemset.add(code);
            }
        }

        if (!codesItemset.isEmpty()) {
            sequeceCodesList.add(codesItemset);
        }

        return sequeceCodesList;
    }

    public Collection<? extends ICDCode> getAllIcdCodes() {
        Set<ICDCode> codes = new HashSet<>();

        for (ICDEntry entry : diagnoses) {
            codes.addAll(entry.getFilteredCodes());
        }

        return codes;
    }


    public String getAllIcdCodesString() {
        String codes = "";

        for (ICDCode code : getAllIcdCodes()) {
            codes += code.getSmallCode() + " ";
        }

        return codes.trim();
    }
}
