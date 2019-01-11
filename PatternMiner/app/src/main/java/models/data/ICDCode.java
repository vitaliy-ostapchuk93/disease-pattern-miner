package models.data;

import java.io.Serializable;

public class ICDCode implements Comparable<ICDCode>, Serializable {

    private final DiagnosesGroup group;
    private final String code;

    public ICDCode(String code) {
        this.code = code;
        this.group = DiagnosesGroupHelper.parseCode(code);
    }

    public DiagnosesGroup getGroup() {
        return group;
    }

    public String getCode() {
        return code;
    }

    public String getSmallCode() {
        if (code.length() > 3) {
            return code.substring(0, 3);                     //trim for just first 3 digits
        }
        return code;
    }

    public boolean checkFiltered() {
        return !DiagnosesGroupHelper.filteredGroups.contains(group) && !DiagnosesGroupHelper.filteredCodes.contains(code);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ICDCode)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        ICDCode icdCode = (ICDCode) obj;
        return getSmallCode().equals(icdCode.getSmallCode());

    }

    @Override
    public int hashCode() {
        return getSmallCode().hashCode();
    }

    @Override
    public int compareTo(ICDCode icdCode) {
        return getSmallCode().compareTo(icdCode.getSmallCode());
    }
}
