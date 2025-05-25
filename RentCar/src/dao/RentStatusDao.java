package dao;

import entity.RentStatusEntity;
import exceptions.DaoException;
import util.ConnectionPool;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class RentStatusDao {

    private static final RentStatusDao INSTANCE = new RentStatusDao();

    private static final String INSERT_DATA_SQL = """
            INSERT INTO rent_status (user_id, available_car_id, start_date, update_date, status_code, status_description)
            VALUES (?, ?, ?, ?, ?, ?)            
            """;

    private static final String FIND_LATEST_BY_USER_ID_AND_CAR_ID = """
            SELECT user_id, available_car_id, start_date, update_date, status_code, status_description
            FROM rent_status 
            WHERE user_id = ? AND available_car_id = ?
            ORDER BY update_date DESC
            LIMIT 1
            """;

    public static RentStatusDao getInstance() {
        return INSTANCE;
    }

    private RentStatusDao() {
    }

    public RentStatusEntity insertNewStatus(RentStatusEntity rentStatusEntity) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(INSERT_DATA_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepSt.setLong(1, rentStatusEntity.getUserId());
            prepSt.setLong(2, rentStatusEntity.getAvailableCarId());
            prepSt.setObject(3, rentStatusEntity.getStartDate());
            prepSt.setObject(4, rentStatusEntity.getUpdateDate());
            prepSt.setString(5, rentStatusEntity.getStatusCode());
            prepSt.setString(6, rentStatusEntity.getStatusDescription());

            prepSt.executeUpdate();

            var generatedKeys = prepSt.getGeneratedKeys();
            if (generatedKeys.next()) {
                rentStatusEntity.setId(generatedKeys.getLong("id"));
            }
            return rentStatusEntity;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public RentStatusEntity findLatestByUserIdAndCarId(long userId, long carId) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(FIND_LATEST_BY_USER_ID_AND_CAR_ID)) {

            prepSt.setLong(1, userId);
            prepSt.setLong(2, carId);

            try (var rs = prepSt.executeQuery()) {
                if (rs.next()) {
                    RentStatusEntity entity = new RentStatusEntity();
                    entity.setUserId(rs.getLong("user_id"));
                    entity.setAvailableCarId(rs.getLong("available_car_id"));
                    entity.setStartDate(rs.getObject("start_date", LocalDateTime.class));
                    entity.setUpdateDate(rs.getObject("update_date", LocalDateTime.class));
                    entity.setStatusCode(rs.getString("status_code"));
                    entity.setStatusDescription(rs.getString("status_description"));
                    return entity;
                }
                return null;
            }
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }
}
