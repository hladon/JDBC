package lesson4.hw1;

import java.sql.*;

abstract class   DAO <T>  {
    protected static final String DB_URL = "****";
    protected static final String USER = "****";
    protected static final String PASS = "****";

    public void deleteFrom(long id ,String table) throws SQLException{
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement("DELETE FROM "+table+" WHERE ID=?")) {

            preparedStatement.setLong(1,id);

            preparedStatement.execute();

        }
    }

    public T findByIdFrom(long id,String table) throws SQLException{
        try (Connection connection = getConnection();
             Statement statement=connection.createStatement()) {

            ResultSet resultSet =statement.executeQuery("SELECT*FROM PRODUCT WHERE ID="+id);

            while (resultSet.next()){
                return getObject(resultSet);
            }

        }
        return null;
    }

    abstract T getObject(ResultSet resultSet) throws SQLException;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
