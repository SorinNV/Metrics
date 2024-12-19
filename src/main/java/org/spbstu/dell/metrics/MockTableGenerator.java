package org.spbstu.dell.metrics;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MockTableGenerator {
    private static final Logger LOGGER =
            Logger.getLogger(String.valueOf(MockTableGenerator.class));

    private static final Integer MAX_IT_EQUIPMENT_POWER = 400;
    private static final Integer MIN_IT_EQUIPMENT_POWER = 50;
    private static final Integer MAX_TOTAL_FACILITY_POWER = 1000;
    private static final Integer MIN_TOTAL_FACILITY_POWER = 400;
    private static final Integer MAX_IT_EQUIPMENT_UTILISATION = 20;
    private static final Integer MIN_IT_EQUIPMENT_UTILISATION = 0;
    private static final String COMMA = ",";
    private final String defaultPath;

    private final String fileName;

    public MockTableGenerator(String defaultPath, String fileName) {
        this.defaultPath = defaultPath + "/src/main/resources/";
        this.fileName = fileName;
        LOGGER.setLevel(Level.ALL);
    }
    public void generate(Integer recordsNumber) {
        String path = defaultPath + fileName;

        try (FileWriter writer = new FileWriter(path, false/*true*/);
             CSVWriter csvWriter = new CSVWriter(writer, ',', ICSVWriter.NO_QUOTE_CHARACTER,
                     ICSVWriter.NO_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END);
             CSVReader reader = new CSVReader(new FileReader(path))) {


            LOGGER.log(Level.INFO, "Start generation Mock Table");
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < recordsNumber; i++) {
                csvWriter.writeNext((
                        (MIN_IT_EQUIPMENT_POWER +
                                random.nextInt(MAX_IT_EQUIPMENT_POWER - MIN_IT_EQUIPMENT_POWER + 1)) + COMMA +
                                (MIN_TOTAL_FACILITY_POWER +
                                        random.nextInt(MAX_TOTAL_FACILITY_POWER - MIN_TOTAL_FACILITY_POWER + 1)) + COMMA +
                                (MIN_IT_EQUIPMENT_UTILISATION +
                                        random.nextInt(MAX_IT_EQUIPMENT_UTILISATION - MIN_IT_EQUIPMENT_UTILISATION + 1))
                ).split(COMMA));
            }

            LOGGER.log(Level.INFO, "Current Mock Table:");
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                LOGGER.log(Level.INFO, "{0}", Arrays.toString(nextLine));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
