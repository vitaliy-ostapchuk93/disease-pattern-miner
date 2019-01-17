package models.results;

import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import models.data.*;
import net.minidev.json.JSONObject;
import org.apache.commons.math3.stat.inference.TestUtils;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

public class ResultsMapper {

    private final static Logger LOGGER = Logger.getLogger(ResultsMapper.class.getName());

    private boolean initialized;

    private TreeBasedTable<String, GenderAgeGroup, ResultsEntry> resultsTable;

    private SortedSet<GenderAgeGroup> allGroups;
    private float highestRelSupValue;
    private int highestAbsSupValue;
    private int highestPatternLength;

    private List<ResultsFilter> filterList;

    private QueueLinkedMap<String, PatternScanner> patternScannerMap;

    public ResultsMapper() {
        LOGGER.setLevel(Level.INFO);

        this.resultsTable = TreeBasedTable.create();
        this.filterList = new ArrayList<>();
        this.patternScannerMap = new QueueLinkedMap<>(5);
    }

    public TreeBasedTable<String, GenderAgeGroup, ResultsEntry> getResultsTable() {
        return resultsTable;
    }

    public ImmutableTable<String, GenderAgeGroup, ResultsEntry> getFilteredResultsTable() {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : getTableCellComporator().reverse().sortedCopy(resultsTable.cellSet())) {
            sortedBuilder.put(cell);
        }

        ImmutableTable<String, GenderAgeGroup, ResultsEntry> filteredResults = sortedBuilder.build();

        //apply filter
        for (ResultsFilter filter : filterList) {
            ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> filteredBuilder = ImmutableTable.builder();
            filteredResults = filteredBuilder.putAll(applyFilter(filter, filteredResults)).build();
        }

        //update highest sup && highest pattern length
        if (filteredResults.cellSet().size() >= 2) {
            ResultsEntry maxRelEntry = Collections.max(filteredResults.cellSet(), Comparator.comparingDouble(o -> Objects.requireNonNull(o.getValue()).getRelSupportValue())).getValue();
            if (maxRelEntry != null) {
                setHighestRelSupValue(maxRelEntry.getRelSupportValue());
            }

            ResultsEntry maxAbsEntry = Collections.max(filteredResults.cellSet(), Comparator.comparingInt(o -> Objects.requireNonNull(o.getValue()).getAbsSupportValue())).getValue();
            if (maxAbsEntry != null) {
                setHighestAbsSupValue(maxAbsEntry.getAbsSupportValue());
            }

            String longestPattern = Collections.max(filteredResults.rowKeySet(), Comparator.comparingInt(o -> Objects.requireNonNull(o).split(" ").length));
            setHighestPatternLength(longestPattern.split(" ").length);
        }

        //update group keys
        setAllGroups(filteredResults.columnKeySet());

        return filteredResults;
    }

    public String getTableAsJSON() {
        JSONObject table = new JSONObject();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeHierarchyAdapter(Table.class, new TableTypeHierarchyAdapter())
                .create();

        gson.toJson(getFilteredResultsTable());

        table.put("table", gson.toString());
        table.put("icdGroups", getIcdCodeGroups());

        return table.toJSONString();
    }


    private JSONObject getIcdCodeGroups() {
        JSONObject icdGroups = new JSONObject();

        for (DiagnosesGroup group : DiagnosesGroup.values()) {
            if (group != DiagnosesGroup.TIME_GAP) {
                icdGroups.put(String.valueOf(group.ordinal()), group.name());
            } else {
                icdGroups.put("-1", group.name());
            }
        }

        return icdGroups;
    }

    public Ordering<Table.Cell<String, GenderAgeGroup, ResultsEntry>> getTableCellComporator() {
        return new Ordering<>() {
            public int compare(Table.Cell<String, GenderAgeGroup, ResultsEntry> cell1, Table.Cell<String, GenderAgeGroup, ResultsEntry> cell2) {
                return Objects.requireNonNull(cell1.getValue()).compareTo(cell2.getValue());
            }
        };
    }


    public ImmutableMap<String, Map<GenderAgeGroup, ResultsEntry>> getFilteredResultsAsSortedMap() {
        return getFilteredResultsTable().rowMap();
    }

    public ResultsEntry getResultsEntry(String seq, String group) {
        return resultsTable.get(seq, new GenderAgeGroup(group));
    }


    public ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyFilter(ResultsFilter filter, ImmutableTable<String, GenderAgeGroup, ResultsEntry> input) {
        switch (filter.getFilterType()) {
            case GROUPS_COUNT:
                return applyGroupsCountFilter(input, filter.getFilterGroupsCount());
            case TOP_N_SUP_GROUP:
                return applyTopNSupGroupFilter(input, filter.getFilterTopNGroups());
            case NOT_SELECTED:
                break;
            case GENDER:
                return applyGenderFilter(input, filter.getFilterGender());
            case AGE:
                return applyAgeFilter(input, filter.getFilterAgeGroups());
            case PATTERN:
                return applyPatternFilter(input, filter.getFilterPattern());
            case REL_SUP:
                return applyRelSupFilter(input, filter.getFilterRelSup());
            case ABS_SUP:
                return applyAbsSupFilter(input, filter.getFilterAbsSup());
            case TOP_N_SUP_ALL:
                return applyTopNSupAllFilter(input, filter.getFilterTopNAll());
            case TOP_PERCENT_ALL:
                return applyTopPercentAllFilter(input, filter.getFilterTopPAll());
            case TOP_PERCENT_GROUP:
                return applyTopPercentGroupFilter(input, filter.getFilterTopPGroups());
            case SEQ_LENGTH:
                return applySeqLenthFilter(input, filter.getFilterSeqLength());
        }
        return input;
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applySeqLenthFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, int filterSeqLength) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (cell.getRowKey() != null && cell.getRowKey().split(" ").length >= filterSeqLength) {
                sortedBuilder.put(cell);
            }
        }
        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyPatternFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, String filterPattern) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            try {
                if (cell.getRowKey() != null && cell.getRowKey().matches("(.*)" + filterPattern + "(.*)")) {
                    sortedBuilder.put(cell);
                }
            } catch (PatternSyntaxException e) {
                LOGGER.warning("Something wrong with the entered pattern!");
            }
        }
        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyTopPercentGroupFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, double filterTopPGroup) {
        Set<String> topEntrySet = new HashSet<>();
        for (GenderAgeGroup group : table.columnKeySet()) {
            int p_size = (int) (table.column(group).size() * filterTopPGroup / 100.0f);
            if (p_size == 0) {
                p_size = 1;
            }
            topEntrySet.addAll(createGroupKeySet(table.column(group).keySet(), p_size));
        }
        return createTableByEntryKeys(table, topEntrySet);
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> createTableByEntryKeys(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, Set<String> topEntrySet) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (topEntrySet.contains(cell.getRowKey())) {
                sortedBuilder.put(cell);
            }
        }
        return sortedBuilder.build();
    }

    private Collection<? extends String> createGroupKeySet(ImmutableSet<String> keySet, int p_size) {
        int counter = 0;
        Set<String> topEntrySet = new HashSet<>();

        for (String key : keySet) {
            topEntrySet.add(key);
            counter++;

            if (counter == p_size) {
                break;
            }
        }
        return topEntrySet;
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyTopPercentAllFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, double filterTopPAll) {
        int p_size = (int) (table.rowKeySet().size() * filterTopPAll / 100.0f);
        return applyTopNSupAllFilter(table, p_size);
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyRelSupFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, double filterRelSup) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (cell.getValue() != null && cell.getValue().getRelSupportValue() >= filterRelSup) {
                sortedBuilder.put(cell);
            }
        }

        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyAbsSupFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, int filterAbsSup) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (cell.getValue() != null && cell.getValue().getAbsSupportValue() >= filterAbsSup) {
                sortedBuilder.put(cell);
            }
        }

        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyAgeFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, Map<Integer, Boolean> filterAgeGroups) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (cell.getColumnKey() != null && !filterAgeGroups.get(cell.getColumnKey().getAgeGroup())) {
                sortedBuilder.put(cell);
            }
        }

        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyGenderFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, Map<Gender, Boolean> filterGender) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (cell.getValue() != null && cell.getColumnKey() != null && !filterGender.get(cell.getColumnKey().getGender())) {
                sortedBuilder.put(cell);
            }
        }

        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyGroupsCountFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, int filterGroupsCount) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (cell.getRowKey() != null && table.row(cell.getRowKey()).size() >= filterGroupsCount) {
                sortedBuilder.put(cell);
            }
        }

        return sortedBuilder.build();
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyTopNSupGroupFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, int filterTopNGroups) {
        Set<String> topEntrySet = new HashSet<>();
        for (GenderAgeGroup group : table.columnKeySet()) {
            topEntrySet.addAll(createGroupKeySet(table.column(group).keySet(), filterTopNGroups));
        }

        return createTableByEntryKeys(table, topEntrySet);
    }

    private ImmutableTable<String, GenderAgeGroup, ResultsEntry> applyTopNSupAllFilter(ImmutableTable<String, GenderAgeGroup, ResultsEntry> table, int filterTopNAll) {
        ImmutableTable.Builder<String, GenderAgeGroup, ResultsEntry> sortedBuilder = ImmutableTable.builder();

        Set<String> topEntrySet = new HashSet<>();
        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : table.cellSet()) {
            if (topEntrySet.size() < filterTopNAll) {
                sortedBuilder.put(cell);
                topEntrySet.add(cell.getRowKey());
            } else {
                break;
            }
        }

        return sortedBuilder.build();
    }

    public List<String> getAllGroups() {
        List<String> groupsAsList = new ArrayList<>();
        for (GenderAgeGroup group : allGroups) {
            groupsAsList.add(group.toString());
        }
        return groupsAsList;
    }

    private void setAllGroups(ImmutableSet<GenderAgeGroup> groups) {
        this.allGroups = new TreeSet<>(groups);
    }

    public float getHighestRelSupValue() {
        return highestRelSupValue;
    }

    public void setHighestRelSupValue(float highestRelSupValue) {
        this.highestRelSupValue = highestRelSupValue;
    }

    public float getHighestAbsSupValue() {
        return highestAbsSupValue;
    }

    public void setHighestAbsSupValue(int highestAbsSupValue) {
        this.highestAbsSupValue = highestAbsSupValue;
    }

    /**
     * Add a new sequence entry to the result set.
     *
     * @param seqence   Pattern as String used as Key of the result map.
     * @param gender    Gender of the Entry.
     * @param ageGroup  AgeGroup of the Entry.
     * @param file      File where the Entry was found.
     * @param suppValue Absolute support value of the Pattern.
     */
    public void addEntry(String seqence, Gender gender, int ageGroup, ResultsDataFile file, int suppValue) {
        GenderAgeGroup group = new GenderAgeGroup(gender, ageGroup);
        ResultsEntry newEntry = new ResultsEntry(suppValue, file);

        if (resultsTable.contains(seqence, group)) {
            ResultsEntry entry = resultsTable.get(seqence, group);
            if (entry.getSequenceCountOfInputFile() < newEntry.getSequenceCountOfInputFile()) {
                resultsTable.put(seqence, group, newEntry);
            }
        } else {
            resultsTable.put(seqence, group, newEntry);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Get filters as sorted List to display them in order.
     *
     * @return
     */
    public List<ResultsFilter> getFilterList() {
        return filterList;
    }

    /**
     * get single filter by its ID.
     *
     * @param id ID of the filter.
     * @return
     */
    public ResultsFilter getFilter(int id) {
        for (ResultsFilter filter : filterList) {
            if (filter.getId() == id) {
                return filter;
            }
        }
        return null;
    }

    /**
     * Get the count of all the groups in the current result set.
     *
     * @return
     */
    public int getMaxGroupsCount() {
        return getAllGroups().size();
    }

    public String getPatternFormatted(String pattern) {
        String formattedSeq = "";
        for (String key : getPatternList(pattern)) {
            if (Integer.parseInt(key) == -1) {
                formattedSeq += DiagnosesGroup.TIME_GAP.name() + " | ";
            } else {
                formattedSeq += DiagnosesGroup.values()[Integer.parseInt(key)].name() + " | ";
            }
        }
        return formattedSeq;
    }

    public String[] getPatternList(String pattern) {
        return pattern.split(" ");

    }

    public String getPatternColor(String key) {
        return DiagnosesGroupHelper.getColorByGroup(Integer.parseInt(key));
    }

    public ResultsEntry getCell(String seqKey, String groupKey) {
        return resultsTable.get(seqKey, new GenderAgeGroup(groupKey));
    }

    private List<String> getIcdColors(LinkedHashSet<ICDCode> codes) {
        List<String> colors = new ArrayList<>();
        LOGGER.info("Creating response object for the link-colors.");
        for (ICDCode code : codes) {
            colors.add(DiagnosesGroupHelper.getColorByGroup(code.getGroup().ordinal()));
        }
        return colors;
    }

    private int getColumnValue(int ageLowerGroupValue, Gender gender) {
        int column = ageLowerGroupValue * 2;
        if (gender == Gender.FEMALE) {
            column++;
        }
        return column;
    }

    private int interpolate(double entryLower, double entryHigher, float v) {
        return (int) (entryLower + v * (entryHigher - entryLower));
    }

    public double tTestGenderDifference(String seqKey) {
        SortedMap<GenderAgeGroup, ResultsEntry> row = resultsTable.row(seqKey);

        if (row.size() >= 6) {
            double[] males = new double[10];
            double[] females = new double[10];

            Map<Gender, double[]> genderValuesMap = new HashMap<>();
            genderValuesMap.put(Gender.MALE, males);
            genderValuesMap.put(Gender.FEMALE, females);

            for (int age = 0; age < 10; age++) {
                for (Gender gender : Gender.values()) {
                    GenderAgeGroup group = new GenderAgeGroup(gender, age);

                    double value;
                    if (row.containsKey(group)) {
                        value = row.get(group).getRelSupportValue();
                    } else {
                        value = 0;
                    }

                    genderValuesMap.get(gender)[age] = value;
                }
            }

            return Math.round(TestUtils.tTest(males, females) * 100.0) / 100.0;
            //return "<b>tTest for difference in gender:</b> " + (Math.round(TestUtils.tTest(males, females) * 100.0) / 100.0) + "<br><br><br>";
        }
        //return "<b>Too less columns to perform tTest.</b><br><br><br>";
        return -1;
    }

    public int getHighestPatternLength() {
        return highestPatternLength;
    }

    public void setHighestPatternLength(int highestPatternLength) {
        this.highestPatternLength = highestPatternLength;
    }

    public Map<String, PatternScanner> getPatternScannerMap() {
        return patternScannerMap;
    }

    public PatternScanner getPatternScanner(String patternKey) {
        if (!patternScannerMap.containsKey(patternKey)) {
            patternScannerMap.put(patternKey, new PatternScanner(patternKey));

            if (patternScannerMap.size() > patternScannerMap.getMaxSize()) {
                patternScannerMap.removeHead();
            }

        }
        return patternScannerMap.get(patternKey);
    }


    public JsonObject getIcdLinksData(String patternKey, ServletContext servletContext) {
        PatternScanner patternScanner = getPatternScanner(patternKey);

        for (ResultsEntry entry : resultsTable.row(patternKey).values()) {
            patternScanner.createInverseSearchFiles(servletContext, entry);
        }

        return patternScanner.getCompleteIcdLinksJSON(resultsTable.row(patternKey), servletContext);
    }

    public void createFullICDLinksFiles(ServletContext servletContext) {
        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : applyTopPercentGroupFilter(getFilteredResultsTable(), 3.0).cellSet()) {
            PatternScanner patternScanner = getPatternScanner(cell.getRowKey());
            patternScanner.createInverseSearchFiles(servletContext, cell);
        }
    }
}
