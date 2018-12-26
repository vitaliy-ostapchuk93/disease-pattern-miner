package models.results;

import models.data.GroupDataFile;
import models.data.ResultsDataFile;
import models.data.SeqDataFile;

import java.io.Serializable;

public class ResultsEntry implements Comparable<ResultsEntry>, Serializable {

    private Integer supportValue;
    private ResultsDataFile fileOfResult;

    public ResultsEntry(int supportValue, ResultsDataFile file) {
        this.supportValue = supportValue;
        this.fileOfResult = file;
    }

    public int getSequenceCountOfInputFile() {
        return getSeqFileOfResult().getFileLineCount();
    }

    public int getAbsSupportValue() {
        return supportValue;
    }

    public ResultsDataFile getFileOfResult() {
        return fileOfResult;
    }

    public SeqDataFile getSeqFileOfResult() {
        return (SeqDataFile) this.fileOfResult.getParentDataFile();
    }

    public GroupDataFile getGroupFileOfResult() {
        return (GroupDataFile) getSeqFileOfResult().getParentDataFile();
    }


    public float getRelSupportValue() {
        return supportValue * 1.0f / getSequenceCountOfInputFile();
    }


    public String getTooltipStats() {
        StringBuilder tooltip = new StringBuilder();

        tooltip.append("<b>Pattern found in:</b><br>");
        tooltip.append(getFileOfResult().getName());
        tooltip.append(" with [ absolute support = ");
        tooltip.append(getAbsSupportValue());
        tooltip.append(", sequence-count = ");
        tooltip.append(getSequenceCountOfInputFile());
        tooltip.append(", relative support = ");
        tooltip.append(getRelSupportValue());
        tooltip.append(" ]");

        tooltip.append("<br><br>");

        return tooltip.toString();
    }

    @Override
    public int compareTo(ResultsEntry entry) {
        return nullSafeResultsEntryComparator(this, entry);
    }

    /**
     * Compares the given ResultEntries rel-support values in a null safe way.
     *
     * @param entry1
     * @param entry2
     * @return
     */
    private int nullSafeResultsEntryComparator(final ResultsEntry entry1, final ResultsEntry entry2) {
        if (entry1 == null ^ entry2 == null) {
            return (entry1 == null) ? -1 : 1;
        }

        if (entry1 == null && entry2 == null) {
            return 0;
        }

        return Float.compare(entry1.getRelSupportValue(), entry2.getRelSupportValue());
    }

}
