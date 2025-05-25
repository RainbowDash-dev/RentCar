package dao;

import entity.UsersEntity;
import exceptions.DaoException;
import util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersDao {

    private static final UsersDao INSTANCE = new UsersDao();

    private static final String FIND_ALL_BY_USER_ID_SQL = """
                SELECT id,
                       first_name, 
                       last_name, 
                       birth_date,
                       driving_license,
                       money_balance,
                       login,
                       password
                FROM users
                WHERE id = ?
            """;

    private static final String INSERT_NEW_USER_SQL = """
            INSERT INTO users (first_name, last_name, birth_date, driving_license, money_balance, login, password)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String DELETE_USER_BY_ID = """
            DELETE FROM users
            WHERE id = ?;
            """;

    private static final String SIGN_IN_USER_SQL = """
            SELECT id
            FROM users
            WHERE login = ? AND password = ?
            LIMIT 1
            """;

    private static final String CHECK_LOGIN_SQL = """
            SELECT 1
            FROM users
            WHERE login = ?
            LIMIT 1
            """;

    private static final String GET_USER_BALANCE_BY_ID_SQL = """
            SELECT money_balance
            FROM users
            WHERE id = ?
            LIMIT 1
            """;

    private static final String UPDATE_USER_BALANCE_SQL = """
            UPDATE users
            SET money_balance = ?
            WHERE id = ?            
            """;


    private UsersDao() {
    }

    public static UsersDao getInstance() {
        return INSTANCE;
    }

    public List<UsersEntity> getUser(long id) {
        try (var connect = ConnectionPool.getPool();
             var prep = connect.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            prep.setLong(1, id);
            var rs = prep.executeQuery();
            List<UsersEntity> userEntityList = new ArrayList<>();
            while (rs.next()) {
                userEntityList.add(mapResultSetToUsersEntity(rs));
            }
            return userEntityList;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private UsersEntity mapResultSetToUsersEntity(ResultSet rs) throws SQLException {
        return new UsersEntity(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getObject("birth_date", LocalDate.class),
                rs.getString("driving_license"),
                rs.getObject("money_balance", BigDecimal.class),
                rs.getString("login"),
                rs.getString("password")

        );
    }

    public UsersEntity insertNewUser(UsersEntity userEntity) {
        try (var connection = ConnectionPool.getPool();
             var myPreparedStatement = connection.prepareStatement(INSERT_NEW_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            myPreparedStatement.setString(1, userEntity.getFirstName());
            myPreparedStatement.setString(2, userEntity.getLastName());
            myPreparedStatement.setObject(3, userEntity.getBirthDate());
            myPreparedStatement.setString(4, userEntity.getDrivingLicense());
            myPreparedStatement.setBigDecimal(5, userEntity.getMoneyBalance());
            myPreparedStatement.setString(6, userEntity.getLogin());
            myPreparedStatement.setString(7, userEntity.getPassword());
            myPreparedStatement.executeUpdate();

            var generatedKeys = myPreparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userEntity.setId(generatedKeys.getLong("id"));
            }
            return userEntity;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public boolean deleteUserById(Long id) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(DELETE_USER_BY_ID)) {

            prepSt.setLong(1, id);

            return prepSt.executeUpdate() == 1;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Long> isSignInUser(String login, String password) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(SIGN_IN_USER_SQL)) {

            prepSt.setString(1, login);
            prepSt.setString(2, password);
            Long userId = null;
            try (var resSet = prepSt.executeQuery()) {
                if (resSet.next()) {
                    userId = getUserIdResultSet(resSet);
                }
                return Optional.ofNullable(userId);
            }

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public boolean isLoginExists(String login) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(CHECK_LOGIN_SQL)) {

            prepSt.setString(1, login);

            try (var resultSet = prepSt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<BigDecimal> getUserBalance(Long id) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(GET_USER_BALANCE_BY_ID_SQL)) {
            prepSt.setLong(1, id);
            var resSet = prepSt.executeQuery();
            BigDecimal userBalance = null;
            if (resSet.next()) {
                userBalance = getUserBalanceResultSet(resSet);
            }
            return Optional.ofNullable(userBalance);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public BigDecimal getUserBalanceResultSet(ResultSet resSet) throws SQLException {
        return resSet.getBigDecimal("money_balance");
    }

    public Long getUserIdResultSet(ResultSet resSet) throws SQLException {
        return resSet.getLong("id");
    }

    public void updateUserBalance(Long userID, BigDecimal newBalance) {
        try (var connect = ConnectionPool.getPool();
             var prepSt = connect.prepareStatement(UPDATE_USER_BALANCE_SQL)) {
            prepSt.setBigDecimal(1, newBalance);
            prepSt.setLong(2, userID);
            var resSet = prepSt.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }
}
