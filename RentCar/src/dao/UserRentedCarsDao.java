package dao;

import dto.UserRentedCarsDto;
import entity.UserRentedCarsEntity;
import exceptions.DaoException;
import util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRentedCarsDao {

    private static final UserRentedCarsDao INSTANCE = new UserRentedCarsDao();

    private static final String INSERT_SQL = """
            INSERT INTO user_rented_cars (user_id, available_car_id, start_date)
            VALUES (?, ?, ?)
            """;

    private static final String CHECK_USER_CARS_SQL = """
            SELECT available_car_id
            FROM user_rented_cars
            WHERE user_id = ?
            """;

    private static final String GET_USER_RENTED_CARS = """
            SELECT ac.id, 
                   ct.mark, 
                   ct.model, 
                   ct.year, 
                   ct.cost, 
                   ct.max_speed,
                   ac.car_type_id, 
                   ac.plate_number, 
                   ac.vin_code, 
                   ac.is_available,
                   urc.start_date
            FROM available_cars ac
            JOIN user_rented_cars urc ON ac.id = urc.available_car_id
            JOIN car_type ct ON ac.car_type_id = ct.id
            WHERE urc.user_id = ?
            ORDER BY ac.id ASC, ac.car_type_id ASC
            """;

    private static final String DELETE_SQL = """
            DELETE FROM user_rented_cars
            WHERE user_id = ? AND available_car_id = ?
            """;

    public static UserRentedCarsDao getInstance() {
        return INSTANCE;
    }

    public UserRentedCarsEntity insertUserAndCar(UserRentedCarsEntity entity) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(INSERT_SQL)) {
            prepSt.setLong(1, entity.getUserId());
            prepSt.setLong(2, entity.getAvailableCarId());
            prepSt.setObject(3, entity.getStartDate());

            prepSt.executeUpdate();
            return entity;

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<UserRentedCarsDto> getRentedCarsByUserId(long userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(GET_USER_RENTED_CARS)) {

            prepSt.setLong(1, userId);

            List<UserRentedCarsDto> rentedDtoList = new ArrayList<>();

            try (var rsSet = prepSt.executeQuery()) {

                while (rsSet.next()) {

                    rentedDtoList.add(mapResultSetToUserRentedCarsDto(rsSet));
                }
            }
            return rentedDtoList;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private UserRentedCarsDto mapResultSetToUserRentedCarsDto(ResultSet rs) throws SQLException {
        return new UserRentedCarsDto(
                rs.getLong("id"),
                rs.getString("mark"),
                rs.getString("model"),
                rs.getInt("year"),
                rs.getBigDecimal("cost"), // rs.getObject("cost", BigDecimal.class) - Проверка на null для BigDecimal
                rs.getInt("max_speed"),
                rs.getLong("car_type_id"),
                rs.getString("plate_number"),
                rs.getString("vin_code"),
                rs.getBoolean("is_available"),
                rs.getObject("start_date", LocalDateTime.class)
        );
    }

    public boolean delete(long userId, long carId) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(DELETE_SQL)) {
            prepSt.setLong(1, userId);
            prepSt.setLong(2, carId);
            return prepSt.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }


}
