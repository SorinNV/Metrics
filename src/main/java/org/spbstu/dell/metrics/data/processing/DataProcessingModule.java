package org.spbstu.dell.metrics.data.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.spbstu.dell.metrics.data.saving.InputDataSavingModule;
import org.spbstu.dell.metrics.data.saving.InputData;
import org.spbstu.dell.metrics.data.saving.ResultData;
import org.spbstu.dell.metrics.data.saving.ResultDataSavingModule;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataProcessingModule {
    private static final Logger LOGGER =
            Logger.getLogger(String.valueOf(DataProcessingModule.class));
    private final String defaultPath;
    private static final String PATH = "config/dataProcessingConfig.yaml";

    private DataProcessingConfig config;

    public DataProcessingModule(String defaultPath) {
        this.defaultPath = defaultPath + "/src/main/resources/";
        this.readConfig();
        LOGGER.setLevel(Level.ALL);
    }

    public void calculateData() throws IOException {
        LOGGER.log(Level.WARNING, "Config:");
        LOGGER.log(Level.WARNING, "startDataProcessingTimeInMS: " + config.getStartDataProcessingTimeInMS());
        LOGGER.log(Level.WARNING, "endDataProcessingTimeInMS: " + config.getEndDataProcessingTimeInMS());

        if (config.getStartDataProcessingTimeInMS() < 0 || config.getEndDataProcessingTimeInMS() < 0) {
            throw new IOException("dataProcessingConfig.yaml: startDataProcessingTimeInMS and " +
                    "dataProcessingConfig.yaml: endDataProcessingTimeInMS must be more then 0");
        }

        InputDataSavingModule dsModule = new InputDataSavingModule(defaultPath);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        List<InputData> data = dsModule.get(new Timestamp(System.currentTimeMillis() - config.getStartDataProcessingTimeInMS()),
                calendar,
                new Timestamp(System.currentTimeMillis() - config.getEndDataProcessingTimeInMS()),
                calendar);
        if (data.isEmpty()) {
            throw new IOException("Can't read input data");
        }

        ResultDataSavingModule resultDataSavingModule = new ResultDataSavingModule(defaultPath);
        for (InputData inputData: data) {
            Optional<Integer> index = resultDataSavingModule.save(new ResultData(0,
                    inputData.getIdInputData(),
                    1.0 * inputData.getTotalFacilityPower() / inputData.getITEquipmentPower(),
                    1.0 * inputData.getITEquipmentPower() / inputData.getTotalFacilityPower() *
                            inputData.getiTEquipmentUtilization(), inputData.getTime(), inputData.getCalendar()));
            LOGGER.log(Level.INFO, "{0}", index);
        }
    }

    private void readConfig() {
        File file = new File(defaultPath + PATH);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            config = om.readValue(file, DataProcessingConfig.class);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can`t read config", e);
            //e.printStackTrace();
        }
    }
}