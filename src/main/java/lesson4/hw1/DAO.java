package lesson4.hw1;

import java.sql.*;

abstract class DAO<T> {


    protected Connection connection = null;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    public void deleteFrom(long id, String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ? WHERE ID=?")) {
            statement.setString(1,table);
            statement.setLong(2, id);

            statement.execute();

        }catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }
    }

    public T findByIdFrom(long id, String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT*FROM ? WHERE ID=?")) {
            statement.setString(1,table);
            statement.setLong(2, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return getObject(resultSet);
            }

        }catch (SQLException sql) {
              throw sql;
        }
        return null;
    }

    abstract T getObject(ResultSet resultSet) throws SQLException;


}
