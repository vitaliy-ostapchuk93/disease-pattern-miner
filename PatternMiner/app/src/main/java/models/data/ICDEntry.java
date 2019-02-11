package models.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class ICDEntry implements Serializable, Comparable<ICDEntry> {
    private final DateTime date;
    private final Set<ICDCode> icdCodes;

    public ICDEntry(String time, String[] codes) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        this.date = formatter.parseDateTime(time);
        this.icdCodes = Arrays.stream(codes).map(ICDCode::new).collect(Collectors.toSet());
    }

    public DateTime getDate() {
        return date;
    }

    public Set<ICDCode> getIcdCodes() {
        return icdCodes;
    }

    public Set<DiagnosesGroup> getFilteredDiagnoses() {
        return icdCodes.stream().filter(ICDCode::checkFiltered).map(ICDCode::getGroup).collect(Collectors.toSet());
    }

    public Set<ICDCode> getFilteredCodes() {
        return icdCodes.stream().filter(ICDCode::checkFiltered).collect(Collectors.toSet());
    }

    @Override
    public int compareTo(ICDEntry entry) {
        return this.date.compareTo(entry.date);
    }
}
