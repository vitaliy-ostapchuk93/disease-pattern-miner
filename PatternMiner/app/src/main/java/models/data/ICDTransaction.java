package models.data;

import java.util.Arrays;

public class ICDTransaction {
    private String id;
    private ICDEntry entry;

    public ICDTransaction(String[] transaction) {
        this.id = transaction[0];
        this.entry = new ICDEntry(transaction[1], Arrays.copyOfRange(transaction, 2, transaction.length));
    }

    public String getId() {
        return id;
    }

    public ICDEntry getEntry() {
        return entry;
    }
}
