package org.spbstu.dell.metrics.data.visualization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.spbstu.dell.metrics.data.saving.ResultData;
import org.spbstu.dell.metrics.data.saving.ResultDataSavingModule;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.*;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataVisualizationModule {
    private static final Logger LOGGER =
            Logger.getLogger(String.valueOf(DataVisualizationModule.class));
    private final String defaultPath;
    private static final String PATH = "config/dataVisualizationConfig.yaml";
    private static final Color BACKGROUND_COLOR = new Color(255,228,196);
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private DataVisualizationConfig config;
    private double averagePUE = 0;
    private double averageCPE = 0;

    public DataVisualizationModule(String defaultPath) {
        this.defaultPath = defaultPath + "/src/main/resources/";
        this.readConfig();
        LOGGER.setLevel(Level.ALL);
    }
    public List<ResultData> visualizeData() throws IOException {
        LOGGER.log(Level.WARNING, "Config:");
        LOGGER.log(Level.WARNING, "startDataProcessingTimeInMS: {0}", config.getStartDataVisualizationTimeInMS());
        LOGGER.log(Level.WARNING, "endDataProcessingTimeInMS: {0}", config.getEndDataVisualizationTimeInMS());
        if (config.getStartDataVisualizationTimeInMS() < 0 || config.getEndDataVisualizationTimeInMS() < 0) {
            throw new IOException("dataVisualizationConfig.yaml: startDataProcessingTimeInMS and " +
                    "dataVisualizationConfig.yaml: endDataProcessingTimeInMS must be more then 0");
        }

        ResultDataSavingModule dsModule = new ResultDataSavingModule(defaultPath);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        List<ResultData> data = dsModule.get(
                new Timestamp(System.currentTimeMillis() - config.getStartDataVisualizationTimeInMS()),
                calendar,
                new Timestamp(System.currentTimeMillis() - config.getEndDataVisualizationTimeInMS()),
                calendar);
        if (data.isEmpty()) {
            throw new IOException("Can't read input data");
        }

        return data;
    }

    private void readConfig() {
        File file = new File(defaultPath + PATH);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            config = om.readValue(file, DataVisualizationConfig.class);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can`t read config", e);
        }
    }
}
