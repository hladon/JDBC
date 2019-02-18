package lesson4.hw1;


import java.sql.Connection;
import java.sql.DriverManager;

public class Demo {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.ctmtirr3ce0v.us-east-2.rds.amazonaws.com:1521:orcl";

    private static final String USER = "main";
    private static final String PASS = "QWer1234";

    public static void main(String[] args) throws Exception {
        File  file1 =new File(1,"Test","txt",1000,null);
        File  file2 =new File(2,"Test2","jpg",2500,null);
        File  file3 =new File(3,"Test3","doc",5000,null);
        File  file4 =new File(4,"Test4","txt",6000,null);
        String[] formats = {"txt","jpg"};
        Storage storage=new Storage((long)1,formats,"Ukraine",21000);
        String[] formats1 = {"txt","doc"};
        Storage storage1=new Storage((long)2,formats1,"Denmark",32000);

//        Controller.put(storage,file1);
        try(Connection connection=DriverManager.getConnection(DB_URL, USER, PASS)){
            FileDAO fileDAO=new FileDAO(connection);
            System.out.println(fileDAO.findById(1));
        }
    }




}
