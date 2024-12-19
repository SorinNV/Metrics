package org.spbstu.dell.metrics.data.saving;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultDataSavingModule {
    private static final Logger LOGGER =
            Logger.getLogger(ResultDataSavingModule.class.getName());
    private final Connection connection;

    public ResultDataSavingModule(String defaultPath) {
        this.connection = JdbcConnection.getConnection();
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler(defaultPath + "logs/log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.addHandler(fileHandler);
    }

    public Optional<Integer> save(ResultData data) {
        String message = "The resultData to be added should not be null";
        ResultData nonNullResultData = Objects.requireNonNull(data, message);
        String sql = "INSERT INTO "
                + "resultData(idinputdata, pue, cpe) "
                + "VALUES(?, ?, ?)";


        Optional<Integer> generatedId = Optional.empty();

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, nonNullResultData.getIdInputData());
            statement.setDouble(2, nonNullResultData.getPue());
            statement.setDouble(3, nonNullResultData.getCpe());

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
                    new Object[]{nonNullResultData,
                            (numberOfInsertedRows > 0)});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return generatedId;
    }

    public List<ResultData> get(Timestamp startTime, Calendar startCal,
                                Timestamp endTime, Calendar endCal) {
        String sql = "SELECT * " +
                "FROM resultData, inputData " +
                "WHERE inputData.idInputData = resultData.idInputData " +
                "AND inputData.time > ? " +
                "AND inputData.time < ? ";
        List<ResultData> resultDataArrayList = new ArrayList<>();

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(1, startTime, startCal);
            statement.setTimestamp(2, endTime, endCal);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Integer idResultData = resultSet.getInt("idresultdata");
                Integer idInputData = resultSet.getInt("idinputdata");
                Double pue = resultSet.getDouble("pue");
                Double cpe = resultSet.getDouble("cpe");
                Timestamp time = resultSet.getTimestamp("time", endCal);

                resultDataArrayList.add(new ResultData(idResultData, idInputData, pue, cpe, time, endCal));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return resultDataArrayList;
    }
}
