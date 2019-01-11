package models.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ICDEntry implements Serializable {
    private final DateTime date;
    private final List<ICDCode> icdCodes;

    public ICDEntry(String time, String[] codes) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        this.date = formatter.parseDateTime(time);

        List<ICDCode> icdCodeList = new ArrayList<>();
        for (String code : codes) {
            icdCodeList.add(new ICDCode(code));
        }
        this.icdCodes = icdCodeList;
    }

    public DateTime getDate() {
        return date;
    }

    public List<ICDCode> getIcdCodes() {
        return icdCodes;
    }

    public Set<DiagnosesGroup> getFilteredDiagnoses() {
        Set<DiagnosesGroup> groups = new HashSet<>();
        for (ICDCode code : icdCodes) {
            if (code.checkFiltered()) {
                groups.add(code.getGroup());
            }
        }
        return groups;
    }

    public Set<ICDCode> getFilteredCodes() {
        Set<ICDCode> codes = new HashSet<>();
        for (ICDCode code : icdCodes) {
            if (code.checkFiltered()) {
                codes.add(code);
            }
        }
        return codes;
    }
}
