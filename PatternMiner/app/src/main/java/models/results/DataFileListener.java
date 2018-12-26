package models.results;

import models.data.DataFile;

public interface DataFileListener {
    void newFileAvailable(DataFile file);
}
