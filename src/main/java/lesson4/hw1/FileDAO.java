package lesson4.hw1;



import java.sql.*;


public class FileDAO extends DAO<File> {


    public File save(File file) throws SQLException{
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO FILES VALUES (?,?,?,?,?)")) {

            preparedStatement.setLong(1,file.getId());
            preparedStatement.setString(2,file.getName());
            preparedStatement.setString(3,file.getFormat());
            preparedStatement.setLong(4,file.getSize());
            preparedStatement.setLong(5,file.getStorage().getId());

            preparedStatement.execute();

        }
        return file;
    }



    public File update(File file) throws SQLException{
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement("UPDATE FILES SET NAME=? ," +
                     " FORMAT=?,FILE_SIZE=?,STORAGE=? WHERE ID=?")) {

            preparedStatement.setString(1,file.getName());
            preparedStatement.setString(2,file.getFormat());
            preparedStatement.setLong(3,file.getSize());
            preparedStatement.setLong(4,file.getStorage().getId());
            preparedStatement.setLong(5,file.getId());

            preparedStatement.execute();

        }
        return file;
    }


    protected File getObject(ResultSet resultSet) throws SQLException {
        StorageDAO storageDAO =new StorageDAO();
        return new File(resultSet.getLong(1),resultSet.getString(2),resultSet.getString(3),resultSet.getLong(4),
                storageDAO.findById(resultSet.getLong(5)));
    }



}
