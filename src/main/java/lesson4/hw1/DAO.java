package lesson4.hw1;

import java.sql.*;
import java.util.List;

abstract class DAO<T> {


    protected Connection connection = null;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    public void deleteQuery( String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            statement.execute(query);

        }catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }
    }

    public T qetResult(String query) throws SQLException{
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

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
