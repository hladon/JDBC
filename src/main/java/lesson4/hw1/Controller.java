package lesson4.hw1;

import java.sql.SQLException;
import java.util.List;

public class Controller {
    private static FileDAO fileDAO=new FileDAO();
    private static StorageDAO storageDAO=new StorageDAO();

    public static void put(Storage storage,File file) throws Exception{
        boolean allowed=false;
        if (storage==null||file==null){
            throw new Exception("Wrong input");
        }
        if (storage.getStorageMaxSize()<file.getSize()){
            System.out.println("File to big for this storage");
            throw new Exception("File "+file.getId()+" to big for storage "+storage.getId());
        }
        for (String type:storage.getFormatsSupported()){
            if(file.getFormat().equalsIgnoreCase(type)){
                allowed=true;
                break;
            }
        }
        if (!allowed){
            throw new Exception("Storage "+storage.getId()+"don`t support type of file "+file.getId());
        }

        file.setStorage(storage);
        try {
            fileDAO.update(file);
        }catch (SQLException sql){
            System.err.println("File "+file.getId()+"impossible to update");
            sql.printStackTrace();
        }

    }
    public void putAll(Storage storage, List<File> files){

    }
    public void delete(Storage storage,File file){

    }
    public void transferAll(Storage storageFrom,Storage storageTo){

    }
    public void transferFile(Storage storageFrom,Storage storageTo, long id){

    }

}
