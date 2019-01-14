package models.results;

import models.data.Gender;

import java.util.HashMap;
import java.util.Map;

public class ResultsFilter {

    private ResultsFilterType filterType;
    private boolean active;

    private int id;

    private Map<Gender, Boolean> filterGender;
    private Map<Integer, Boolean> filterAgeGroups;
    private double filterRelSup;
    private int filterAbsSup;
    private int filterTopNGroups;
    private int filterTopNAll;
    private float filterTopPGroups;
    private float filterTopPAll;
    private int filterGroupsCount;
    private String filterPattern;
    private int filterSeqLength;

    public ResultsFilter(int id) {
        this.id = id;
        initFilter();
    }

    private void initFilter() {
        this.filterType = ResultsFilterType.NOT_SELECTED;
        this.active = true;

        this.filterGender = new HashMap<>();
        for (Gender g : Gender.values()) {
            filterGender.put(g, false);
        }

        this.filterAgeGroups = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            filterAgeGroups.put(i, false);
        }

        this.filterRelSup = 0.0;
        this.filterAbsSup = 0;

        this.filterTopNGroups = 20;
        this.filterTopNAll = 100;

        this.filterTopPGroups = 3.0f;
        this.filterTopPAll = 5.0f;

        this.filterGroupsCount = 1;

        this.filterPattern = "";

        this.filterSeqLength = 3;
    }

    public ResultsFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(ResultsFilterType filterType) {
        this.filterType = filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = ResultsFilterType.valueOf(filterType);
    }

    public String getFilterName() {
        return filterType.name();
    }

    public ResultsFilterType[] getFilterTypes() {
        return ResultsFilterType.values();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Gender, Boolean> getFilterGender() {
        return filterGender;
    }

    public void setFilterGender(Map<Gender, Boolean> filterGender) {
        this.filterGender = filterGender;
    }

    public Map<Integer, Boolean> getFilterAgeGroups() {
        return filterAgeGroups;
    }

    public void setFilterAgeGroups(Map<Integer, Boolean> filterAgeGroups) {
        this.filterAgeGroups = filterAgeGroups;
    }

    public double getFilterRelSup() {
        return filterRelSup;
    }

    public void setFilterRelSup(double filterRelSup) {
        this.filterRelSup = filterRelSup;
    }

    public int getFilterAbsSup() {
        return filterAbsSup;
    }

    public void setFilterAbsSup(int filterAbsSup) {
        this.filterAbsSup = filterAbsSup;
    }

    public int getFilterTopNGroups() {
        return filterTopNGroups;
    }

    public void setFilterTopNGroups(int filterTopNGroups) {
        this.filterTopNGroups = filterTopNGroups;
    }

    public int getFilterTopNAll() {
        return filterTopNAll;
    }

    public void setFilterTopNAll(int filterTopNAll) {
        this.filterTopNAll = filterTopNAll;
    }

    public int getFilterGroupsCount() {
        return filterGroupsCount;
    }

    public void setFilterGroupsCount(int filterGroupsCount) {
        this.filterGroupsCount = filterGroupsCount;
    }

    public float getFilterTopPGroups() {
        return filterTopPGroups;
    }

    public void setFilterTopPGroups(float filterTopPGroups) {
        this.filterTopPGroups = filterTopPGroups;
    }

    public double getFilterTopPAll() {
        return filterTopPAll;
    }

    public void setFilterTopPAll(float filterTopPAll) {
        this.filterTopPAll = filterTopPAll;
    }

    public String getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public int getFilterSeqLength() {
        return filterSeqLength;
    }

    public void setFilterSeqLength(int filterSeqLength) {
        this.filterSeqLength = filterSeqLength;
    }
}
