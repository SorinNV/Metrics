package org.spbstu.dell.metrics.data.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataCollectionConfig {
    private boolean isReadFromMockTable;
    private int readingDataDelayInMS;

    public DataCollectionConfig() {
        //Class using for read yaml file, must have an empty constructor
    }

    public boolean isReadFromMockTable() {
        return isReadFromMockTable;
    }

    public void setReadFromMockTable(boolean readFromMockTable) {
        isReadFromMockTable = readFromMockTable;
    }

    public int getReadingDataDelayInMS() {
        return readingDataDelayInMS;
    }

    public void setReadingDataDelayInMS(int readingDataDelayInMS) {
        this.readingDataDelayInMS = readingDataDelayInMS;
    }
}
