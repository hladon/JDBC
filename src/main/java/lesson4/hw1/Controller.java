package lesson4.hw1;

import java.sql.*;
import java.util.List;

public class Controller {




    public static void put(Storage storage, File file) throws Exception {

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            putFile(storage,file,connection);
            connection.commit();
        }

    }

    public static void putAll(Storage storage, List<File> files) throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            putList(storage,files,connection);

            connection.commit();
        }

    }

    public static void delete(Storage storage, File file) throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            remove(storage, file, connection);

            connection.commit();
        }
    }

    public static void transferAll(Storage storageFrom, Storage storageTo) throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            FileDAO fileDAO = new FileDAO(connection);
            List<File> list = fileDAO.getFilesByStorage(storageFrom);

            for (File file : list) {
                remove(storageFrom, file, connection);
            }

            putList(storageTo,list,connection);

            connection.commit();
        }
    }

    public static void transferFile(Storage storageFrom, Storage storageTo, long id) throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            FileDAO fileDAO = new FileDAO(connection);
            StorageDAO storageDAO = new StorageDAO(connection);
            File file = fileDAO.findById(id);
            remove(storageFrom, file, connection);
            put(storageTo, file);


            connection.commit();
        }
    }

    public static File findFileById(long id) throws SQLException {
        FileDAO fileDAO = new FileDAO(getConnection());
        return fileDAO.findById(id);
    }

    public static Storage findStorageById(long id)throws SQLException {
        StorageDAO storageDAO=new StorageDAO(getConnection());
        return storageDAO.findById(id);
    }

    public static void update(File file) throws SQLException{
        FileDAO fileDAO = new FileDAO(getConnection());
        fileDAO.update(file);
    }
    public static void update(Storage storage) throws SQLException{
        StorageDAO storageDAO = new StorageDAO(getConnection());
        storageDAO.update(storage);
    }

    private static File putFile(Storage storage,File file,Connection connection) throws Exception{
        init(storage, file);
        file.setStorage(storage);
        storage.setStorageMaxSize(storage.getStorageMaxSize() - file.getSize());

            FileDAO fileDAO = new FileDAO(connection);
            fileDAO.update(file);
            StorageDAO storageDAO = new StorageDAO(connection);
            storageDAO.update(storage);
        return file;
    }

    private static void putList(Storage storage, List<File> files,Connection connection) throws Exception{
        FileDAO fileDAO = new FileDAO(connection);
        StorageDAO storageDAO = new StorageDAO(connection);

        for (File file : files) {
            init(storage, file);
            if (file.getStorage()!=null)
                remove(file.getStorage(),file,connection);
            file.setStorage(storage);
            fileDAO.update(file);

            storage.setStorageMaxSize(storage.getStorageMaxSize() - file.getSize());
            storageDAO.update(storage);
        }
    }

    private static void remove(Storage storage, File file, Connection connection) throws Exception {
        try {
            FileDAO fileDAO = new FileDAO(connection);
            StorageDAO storageDAO = new StorageDAO(connection);
            if (file.getStorage()==null|| !file.getStorage().equals(storage)){
                throw new Exception("File "+file.getId()+" don`t belong to storage "+storage.getId());

            }

            storage.setStorageMaxSize(storage.getStorageMaxSize() + file.getSize());
            file.setStorage(null);
            fileDAO.update(file);
            storageDAO.update(storage);
        } catch (SQLException sql) {
            connection.rollback();
            throw sql;
        }

    }

    private static void init(Storage storage, File file) throws Exception {
        boolean allowed = false;
        if (storage == null || file == null) {
            throw new Exception("Wrong input");
        }
        if (storage.getStorageMaxSize() < file.getSize()) {
            System.out.println("File to big for this storage");
            throw new Exception("File " + file.getId() + " to big for storage " + storage.getId());
        }
        for (String type : storage.getFormatsSupported()) {
            if (file.getFormat().equalsIgnoreCase(type)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            throw new Exception("Storage " + storage.getId() + "don`t support type of file " + file.getId());
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
