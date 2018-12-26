package models.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsDataFile extends DataFile {
    public final static String DIR_PATH = "/resources/datasets/output/";

    public ResultsDataFile(String realPath) {
        super(realPath);
    }

    @Override
    public String getStats() {
        String separator = ", ";
        String stats = getFileStats();

        stats += separator + createStatistics(getResultsEntryList());

        return stats;
    }

    @Override
    public String getPathToChildren() {
        return null;
    }

    @Override
    public DataFile createChildFile(String path) {
        return null;
    }

    private List<Integer> getResultsEntryList() {
        List<Integer> resultsEntryList = new ArrayList<>();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(this, "UTF-8");

            while (it.hasNext()) {
                String[] p = it.nextLine().split(" #SUP: ");
                resultsEntryList.add(Integer.parseInt(p[1]));
            }
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultsEntryList;
    }
}
