package lesson4.hw1;

import java.sql.*;
import java.util.List;

public class Controller {

    private static final String DB_URL = "";

    private static final String USER = "";
    private static final String PASS = "";

    public static void put(Storage storage, File file) throws Exception {

        init(storage, file);
        file.setStorage(storage);
        storage.setStorageMaxSize(storage.getStorageMaxSize() - file.getSize());
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            FileDAO fileDAO = new FileDAO(connection);
            fileDAO.update(file);
            StorageDAO storageDAO = new StorageDAO(connection);
            storageDAO.update(storage);
            connection.commit();
        }

    }

    public static void putAll(Storage storage, List<File> files) throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            FileDAO fileDAO = new FileDAO(connection);
            StorageDAO storageDAO = new StorageDAO(connection);

            for (File file : files) {
                init(storage, file);
                file.setStorage(storage);
                fileDAO.update(file);
                storage.setStorageMaxSize(storage.getStorageMaxSize() - file.getSize());
            }

            connection.commit();
        }

    }

    public static void delete(Storage storage, File file) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            remove(storage, file, connection);

            connection.commit();
        }
    }

    public void transferAll(Storage storageFrom, Storage storageTo) throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            FileDAO fileDAO = new FileDAO(connection);
            List<File> list = fileDAO.getFilesByStorage(storageFrom);

            for (File file : list) {
                remove(storageFrom, file, connection);
            }

            putAll(storageTo, list);

            connection.commit();
        }
    }

    public void transferFile(Storage storageFrom, Storage storageTo, long id) throws Exception {
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

    private static void remove(Storage storage, File file, Connection connection) throws SQLException {
        try {
            FileDAO fileDAO = new FileDAO(connection);
            StorageDAO storageDAO = new StorageDAO(connection);

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
