package org.spbstu.dell.metrics.data.visualization;

public class DataVisualizationConfig {
    private int startDataVisualizationTimeInMS;
    private int endDataVisualizationTimeInMS;

    public DataVisualizationConfig() {
        //Class using for read yaml file, must have an empty constructor
    }

    public int getStartDataVisualizationTimeInMS() {
        return startDataVisualizationTimeInMS;
    }

    public void setStartDataVisualizationTimeInMS(int startDataVisualizationTimeInMS) {
        this.startDataVisualizationTimeInMS = startDataVisualizationTimeInMS;
    }

    public int getEndDataVisualizationTimeInMS() {
        return endDataVisualizationTimeInMS;
    }

    public void setEndDataVisualizationTimeInMS(int endDataVisualizationTimeInMS) {
        this.endDataVisualizationTimeInMS = endDataVisualizationTimeInMS;
    }
}
