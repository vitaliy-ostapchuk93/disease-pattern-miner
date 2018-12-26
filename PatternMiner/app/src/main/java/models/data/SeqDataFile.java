package models.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeqDataFile extends DataFile {
    public final static String DIR_PATH = "/resources/datasets/input/converted/";

    public SeqDataFile(String realPath) {
        super(realPath);
    }

    @Override
    public String getStats() {
        String separator = ", ";
        String stats = getFileStats();

        stats += separator + createStatistics(getSeqEntryCountList());

        return stats;
    }

    @Override
    public String getPathToChildren() {
        return ResultsDataFile.DIR_PATH;
    }

    @Override
    public DataFile createChildFile(String path) {
        return new ResultsDataFile(path);
    }

    private List<Integer> getSeqEntryCountList() {
        List<Integer> countList = new ArrayList<>();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(this, "UTF-8");

            while (it.hasNext()) {
                String[] p = it.nextLine().split(" ");
                countList.add(p.length);
            }
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countList;
    }
}
