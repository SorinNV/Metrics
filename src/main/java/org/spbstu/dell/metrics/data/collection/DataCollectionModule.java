package org.spbstu.dell.metrics.data.collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.spbstu.dell.metrics.data.saving.InputDataSavingModule;
import org.spbstu.dell.metrics.data.saving.InputData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataCollectionModule {
    private static final Logger LOGGER =
            Logger.getLogger(String.valueOf(DataCollectionModule.class));

    private final String defaultPath;
    private static final String PATH = "config/dataCollectionConfig.yaml";

    private DataCollectionConfig config;

    public DataCollectionModule(String defaultPath) {
        this.defaultPath = defaultPath + "/src/main/resources/";
        this.readConfig();
        LOGGER.setLevel(Level.ALL);
    }
    public List<InputData> readData(String fileName) {
        LOGGER.log(Level.WARNING, "Config:");
        LOGGER.log(Level.WARNING, "isReadFromMockTable: " + config.isReadFromMockTable());
        LOGGER.log(Level.WARNING, "readingDataDelayInMS: " + config.getReadingDataDelayInMS());

        String path = defaultPath + fileName;

        ArrayList<InputData> inputList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] nextLine;
            InputDataSavingModule savingModule = new InputDataSavingModule(defaultPath);

            while ((nextLine = reader.readNext()) != null) {
                long time = System.currentTimeMillis();
                long prevTime = 0;
                while (System.currentTimeMillis() - time < config.getReadingDataDelayInMS()) {
                    if ((System.currentTimeMillis() - time) % (config.getReadingDataDelayInMS() / 10) <
                            config.getReadingDataDelayInMS() / 100 &&
                            prevTime != System.currentTimeMillis() - time) {
                        LOGGER.log(Level.INFO,".");
                        prevTime = System.currentTimeMillis() - time;
                    }
                }
                LOGGER.log(Level.INFO, "\n{0}", Arrays.toString(nextLine));
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                InputData inputData = new InputData(0, Integer.parseInt(nextLine[0]),
                        Integer.parseInt(nextLine[1]), Integer.parseInt(nextLine[2]), timestamp, calendar);
                Optional<Integer> index = savingModule.save(inputData);
                inputList.add(inputData);
                LOGGER.log(Level.INFO, "{0}", index);
            }
        }
        catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return inputList;
    }

    private void readConfig() {
        File file = new File(defaultPath + PATH);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            config = om.readValue(file, DataCollectionConfig.class);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can`t read config", e);
        }
    }
}
