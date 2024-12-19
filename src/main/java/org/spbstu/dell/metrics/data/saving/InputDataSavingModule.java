package org.spbstu.dell.metrics.data.saving;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputDataSavingModule {
    private static final Logger LOGGER =
            Logger.getLogger(InputDataSavingModule.class.getName());
    private final Connection connection;

    public InputDataSavingModule(String defaultPath) {
        this.connection = JdbcConnection.getConnection();
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler(defaultPath + "logs/log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.addHandler(fileHandler);
    }

    public Optional<Integer> save(InputData data) {
        String message = "The inputData to be added should not be null";
        InputData nonNullInputData = Objects.requireNonNull(data, message);
        String sql = "INSERT INTO "
                + "inputData(itequipmentpower, totalfacilitypower, itequipmentutilization, time) "
                + "VALUES(?, ?, ?, ?)";


        Optional<Integer> generatedId = Optional.empty();

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, nonNullInputData.getITEquipmentPower());
            statement.setInt(2, nonNullInputData.getTotalFacilityPower());
            statement.setInt(3, nonNullInputData.getiTEquipmentUtilization());
            statement.setTimestamp(4, nonNullInputData.getTime(), nonNullInputData.getCalendar());

            int numberOfInsertedRows = statement.executeUpdate();

            // Retrieve the auto-generated id
            if (numberOfInsertedRows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = Optional.of(resultSet.getInt(1));
                    }
                }
            }

            LOGGER.log(
                    Level.INFO,
                    "{0} created successfully? {1}",
                    new Object[]{nonNullInputData,
                            (numberOfInsertedRows > 0)});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return generatedId;
    }

    public List<InputData> get(Timestamp startTime, Calendar startCal,
                               Timestamp endTime, Calendar endCal) {
        String sql = "SELECT * " +
                "FROM inputData " +
                "WHERE time > ? " +
                "AND time < ? ";
        List<InputData> inputDataArrayList = new ArrayList<>();

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(1, startTime, startCal);
            statement.setTimestamp(2, endTime, endCal);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Integer idInputData = resultSet.getInt("idinputdata");
                Integer iTEquipmentPower = resultSet.getInt("itequipmentpower");
                Integer totalFacilityPower = resultSet.getInt("totalfacilitypower");
                Integer iTEquipmentUtilization = resultSet.getInt("itequipmentutilization");
                Timestamp time = resultSet.getTimestamp("time", endCal);
                inputDataArrayList.add(new InputData(idInputData, iTEquipmentPower, totalFacilityPower,
                        iTEquipmentUtilization, time, endCal));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, null, ex);
        }
        return inputDataArrayList;
    }
}
