package org.spbstu.dell.metrics.data.processing;

public class DataProcessingConfig {
    private int startDataProcessingTimeInMS;
    private int endDataProcessingTimeInMS;
    public DataProcessingConfig() {
        //Class using for read yaml file, must have an empty constructor
    }

    public int getStartDataProcessingTimeInMS() {
        return startDataProcessingTimeInMS;
    }

    public void setStartDataProcessingTimeInMS(int startDataProcessingTimeInMS) {
        this.startDataProcessingTimeInMS = startDataProcessingTimeInMS;
    }

    public int getEndDataProcessingTimeInMS() {
        return endDataProcessingTimeInMS;
    }

    public void setEndDataProcessingTimeInMS(int endDataProcessingTimeInMS) {
        this.endDataProcessingTimeInMS = endDataProcessingTimeInMS;
    }
}