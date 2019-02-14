package lesson4.hw1;

import java.sql.*;
import java.util.Arrays;

public class StorageDAO extends DAO<Storage> {


    public Storage save(Storage storage){
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO STORAGE VALUES (?,?,?,?)")) {

            preparedStatement.setLong(1,storage.getId());
            preparedStatement.setString(2, Arrays.toString(storage.getFormatsSupported()));
            preparedStatement.setString(3,storage.getStorageCountry());
            preparedStatement.setLong(4,storage.getStorageMaxSize());

            preparedStatement.execute();

        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }

        return storage;
    }
    public void delete(long id){
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement("DELETE FROM STORAGE WHERE ID=?")) {

            preparedStatement.setLong(1,id);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }
    public void update(Object object){

    }

    protected Storage getObject(ResultSet resultSet) throws SQLException {
        return new Storage(resultSet.getLong(1),resultSet.getString(2).split(","),resultSet.getString(3),resultSet.getLong(4));
    }


}
