package dao;

import entity.AvailableCarsEntity;
import exceptions.DaoException;
import util.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvailableCarsDao {

    private static final AvailableCarsDao INSTANCE = new AvailableCarsDao();

    private static final String FIND_BY_ID = """
                SELECT id, car_type_id, plate_number, vin_code, is_available
                FROM available_cars
                WHERE id = ?
            """;

    private static final String FIND_BY_CAR_TYPE_ID_SQL = """
                SELECT id, car_type_id, plate_number, vin_code, is_available
                FROM available_cars
                WHERE car_type_id = ?
                AND is_available = true
                ORDER BY id ASC
            """;

    private static final String UPDATE_IS_AVAILABLE_BY_ID_SQL = """
            UPDATE available_cars
            SET is_available = NOT is_available
            WHERE id = ?
            """;



    private AvailableCarsDao() {
    }

    public static AvailableCarsDao getInstance() {
        return INSTANCE;
    }


    public List<AvailableCarsEntity> findById(long id) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(FIND_BY_ID)) {

            prepSt.setLong(1, id);
            var resSet = prepSt.executeQuery();
            List<AvailableCarsEntity> availableListID = new ArrayList<>();
            while (resSet.next()) {
                availableListID.add(buildAvailableCarsEntity(resSet));
            }
            return availableListID;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<AvailableCarsEntity> findByCarTypeId(Long carTypeId) {
        try (var connection = ConnectionPool.getPool();
             var preparedStatement = connection.prepareStatement(FIND_BY_CAR_TYPE_ID_SQL)) {
            preparedStatement.setLong(1, carTypeId);
            var resultSet = preparedStatement.executeQuery();

            List<AvailableCarsEntity> availableCarsEntityList = new ArrayList<>();
            while (resultSet.next()) {
                availableCarsEntityList.add(buildAvailableCarsEntity(resultSet));
            }
            return availableCarsEntityList;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private AvailableCarsEntity buildAvailableCarsEntity(ResultSet resultSet) throws SQLException {
        return new AvailableCarsEntity(
                resultSet.getLong("id"),
                resultSet.getLong("car_type_id"),
                resultSet.getString("plate_number"),
                resultSet.getString("vin_code"),
                resultSet.getBoolean("is_available")
        );
    }

    public void updateIsAvailableByID(Long id) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(UPDATE_IS_AVAILABLE_BY_ID_SQL)) {
            prepSt.setLong(1, id);
            prepSt.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

}
