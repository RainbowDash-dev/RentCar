package dao;

import entity.CarTypeEntity;
import exceptions.DaoException;
import util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarTypeDao {

    private static final CarTypeDao INSTANCE = new CarTypeDao();

    private static final String FIND_BY_ID_SQL = """
            SELECT availableCarId, mark, model, year, cost, max_speed
            FROM car_type
            WHERE availableCarId = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, mark, model, year, cost, max_speed 
            FROM car_type;
            """;

    private static final String DELETE_SQL = """
            DELETE FROM car_type
            WHERE availableCarId = ?;
            """;

    private static final String INSERT_SQL = """
            INSERT INTO car_type (mark, model, year, cost, max_speed)
            VALUES (?, ?, ?, ?, ?);            
            """;

    private static final String UPDATE_SQL = """
            UPDATE car_type
            SET mark = ?, model = ?, year = ?, cost = ?, max_speed = ?
            WHERE availableCarId = ?;
            """;

    private static final String GET_COST_BY_ID_SQL = """
            SELECT cost
            FROM car_type
            WHERE id = ?
            """;

    private CarTypeDao() {
    }

    public static CarTypeDao getInstance() {
        return INSTANCE;
    }

    public List<CarTypeEntity> findAllCarType() {
        try (Connection connection = ConnectionPool.getPool();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<CarTypeEntity> carTypeEntityList = new ArrayList<>();
            while (resultSet.next()) {
                carTypeEntityList.add(buildCarTypeEntity(resultSet));
            }
            return carTypeEntityList;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private CarTypeEntity buildCarTypeEntity(ResultSet resultSet) throws SQLException {
        return new CarTypeEntity(
                resultSet.getLong("id"),
                resultSet.getString("mark"),
                resultSet.getString("model"),
                resultSet.getInt("year"),
                resultSet.getBigDecimal("cost"),
                resultSet.getInt("max_speed")
        );
    }

    public Optional<Long> getCost(Long id) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(GET_COST_BY_ID_SQL)) {
            prepSt.setLong(1, id);
            ResultSet resultSet = prepSt.executeQuery();
            Long cost = null;
            if (resultSet.next()) {
                cost = findCost(resultSet);
            }
            return Optional.ofNullable(cost);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Long findCost (ResultSet rs) throws SQLException{
        return rs.getLong("cost");
    }
}
