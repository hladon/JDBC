package lesson4.hw1;

import java.sql.*;

abstract class DAO<T> {


    protected Connection connection = null;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    public void deleteFrom(long id, String table) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table + " WHERE ID=?")) {

            preparedStatement.setLong(1, id);

            preparedStatement.execute();

        }catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }
    }

    public T findByIdFrom(long id, String table) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT*FROM PRODUCT WHERE ID=" + id);

            while (resultSet.next()) {
                return getObject(resultSet);
            }

        }catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }
        return null;
    }

    abstract T getObject(ResultSet resultSet) throws SQLException;


}
