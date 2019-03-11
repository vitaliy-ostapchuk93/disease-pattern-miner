package models.data;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;

public class ICDTransaction implements Serializable {

    private final String group;
    private final String id;
    private final ICDEntry entry;

    public ICDTransaction(String group, String id, String timestamp, String[] codes) {
        this.group = group;
        this.id = id;
        this.entry = new ICDEntry(timestamp, codes);
    }

    public ICDTransaction(String id, String timestamp, String[] codes) {
        this.group = null;
        this.id = id;
        this.entry = new ICDEntry(timestamp, codes);
    }

    public String getId() {
        return id;
    }

    public ICDEntry getEntry() {
        return entry;
    }

    public String getGroup() {
        return group;
    }

    public GenderAgeGroup getGenderAgeGroup() {
        return new GenderAgeGroup(getGender(), getAge());
    }

    public int getAge() {
        return Integer.parseInt(this.group, 1, 1, 10);
    }

    public Gender getGender() throws UnsupportedOperationException {
        if (group.startsWith("f")) {
            return Gender.FEMALE;
        }
        if (group.startsWith("m")) {
            return Gender.MALE;
        }
        throw new UnsupportedOperationException("Gender group string does not match!");
    }

    public DateTime getTimestamp() {
        return this.entry.getDate();
    }

    public static Comparator<ICDTransaction> getComporator() {
        Comparator<ICDTransaction> comparator = Comparator.comparing(ICDTransaction::getGender);
        comparator = comparator.thenComparing(ICDTransaction::getAge);
        comparator = comparator.thenComparing(ICDTransaction::getId);
        comparator = comparator.thenComparing(ICDTransaction::getTimestamp);

        return comparator;
    }


}
