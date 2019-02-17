package lesson4.hw1;

import java.sql.*;
import java.util.Arrays;

public class StorageDAO extends DAO<Storage> {



    public StorageDAO(Connection connection) {
        super(connection);
    }

    public Storage save(Storage storage) throws SQLException{
        try (PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO STORAGE VALUES (?,?,?,?)")) {

            preparedStatement.setLong(1,storage.getId());
            preparedStatement.setString(2, Arrays.toString(storage.getFormatsSupported()));
            preparedStatement.setString(3,storage.getStorageCountry());
            preparedStatement.setLong(4,storage.getStorageMaxSize());

            preparedStatement.execute();

        } catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }

        return storage;
    }

    public Storage update(Storage storage) throws SQLException{
        try (    PreparedStatement preparedStatement=connection.prepareStatement("UPDATE STORAGE SET " +
                     " FORMATS_SUPPORTED=?,STORAGE_COUNTRY=?,STORAGE_MAX_SIZE=? WHERE ID=?")) {

            preparedStatement.setString(1,Arrays.toString(storage.getFormatsSupported()));
            preparedStatement.setString(2,storage.getStorageCountry());
            preparedStatement.setLong(3,storage.getStorageMaxSize());
            preparedStatement.setLong(4,storage.getId());

            preparedStatement.execute();

        }catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }
        return storage;
    }

    public void delete (long id)throws SQLException{
        deleteFrom(id,"STORAGE");
    }

    public Storage findById(long id) throws SQLException{
        return findByIdFrom(id,"STORAGE");
    }

    protected Storage getObject(ResultSet resultSet) throws SQLException {
        return new Storage(resultSet.getLong(1),resultSet.getString(2).split(","),resultSet.getString(3),resultSet.getLong(4));
    }


}
