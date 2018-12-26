package models.data;

import java.io.Serializable;

public class GenderAgeGroup implements Comparable<GenderAgeGroup>, Serializable {

    private Gender gender;
    private Integer ageGroup;

    public GenderAgeGroup(Gender gender, int ageGroup) {
        this.gender = gender;
        this.ageGroup = ageGroup;
    }

    public GenderAgeGroup(String group) {
        if (group.endsWith("M")) {
            this.gender = Gender.MALE;
        }
        if (group.endsWith("F")) {
            this.gender = Gender.FEMALE;
        }

        this.ageGroup = Integer.parseInt(group.substring(0, 1));
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(int ageGroup) {
        this.ageGroup = ageGroup;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GenderAgeGroup))
            return false;
        GenderAgeGroup ref = (GenderAgeGroup) obj;
        return this.gender == ref.gender && this.ageGroup == ref.ageGroup;
    }

    @Override
    public int hashCode() {
        return this.gender.hashCode() ^ Integer.valueOf(this.ageGroup).hashCode();
    }

    @Override
    public String toString() {
        if (gender == Gender.MALE) {
            return ageGroup + "M";
        }
        if (gender == Gender.FEMALE) {
            return ageGroup + "F";
        } else {
            return ageGroup + "U";
        }
    }

    @Override
    public int compareTo(GenderAgeGroup group) {
        int i = Integer.compare(ageGroup, group.ageGroup);
        if (i != 0) {
            return i;
        }

        int j = gender.compareTo(group.gender);
        if (j != 0) {
            return j;
        }

        return 0;
    }
}