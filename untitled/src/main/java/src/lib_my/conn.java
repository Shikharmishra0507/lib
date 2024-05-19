package src.lib_my;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

public class conn {
    int min=10,max=100000;
    private static conn sqlConnectionObj;
	public  Connection connection;
    public Statement statement;
    private  String StrUrl="jdbc:mysql://localhost:3306/db";
    private  String StrUid="root";
    private  String StrPwd= "gopirakesh";
   public conn(){  
       try{  
       	//System.out.println("debug 1");
           Class.forName("com.mysql.jdbc.Driver").newInstance();  
           //System.out.println("debug 2");
           //this.connection =DriverManager.getConnection("jdbc:mysql:///ums","root",""); 
           this.connection =DriverManager.getConnection(StrUrl,StrUid,StrPwd); 
           this.statement =connection.createStatement();  
           //System.out.println("debug 3");
           
          
       }catch(Exception e){ 
           System.out.println(e);
       }  
   }
   public static conn getInstance(){
       if(sqlConnectionObj==null){
           sqlConnectionObj=new conn();
           sqlConnectionObj.setupDatabaseConnection();
       }
       return sqlConnectionObj;
   }
   public void setupDatabaseConnection(){
       if(connection!=null)return ;
       try{
           connection=DriverManager.getConnection(StrUrl,StrUid,StrPwd);

           this.statement =connection.createStatement();
           System.out.println("connected to mysql database");
       }
       catch(Exception e){
           System.out.println("Failed to connect to Mysql Database");
       }
   }

   boolean checkIfUserExistsInDB(String username,String password) throws SQLException {
       try{
           String query="select * from studenttable";
           PreparedStatement preparedStatement=connection.prepareStatement(query);
           ResultSet resultSet=preparedStatement.executeQuery();
           while(resultSet.next()){
               if(username.equalsIgnoreCase(resultSet.getString("std_name")) && password.equals(resultSet.getString("std_pass")))return true;
           }
           return false;
       }
       catch(Exception e){
            throw e;
       }
   }
   boolean loginUser(String username,String password) throws SQLException {
       boolean successfulLogin=false;
       try{
           if(checkIfUserExistsInDB(username,password)){
               successfulLogin=true;
           }
       }
       catch(Exception e){
           System.out.println(e.getMessage());
           throw e;
       }
       return successfulLogin;
   }
   boolean registerUser(String username ,String password) throws SQLException {
       try{
          if(checkIfUserExistsInDB(username,password)){
              throw new Exception("This student is already registered!,try login");

       }
       String command="insert into studenttable(st_name,st_pass) values ('"+username+"','"+password+"');";
       statement.executeUpdate(command);
       return true;
       }
       catch (Exception e) {
           throw new RuntimeException(e);
       }

   }
    boolean addnewBook(String bookName,String author) throws SQLException {
       try{
           int randomNum=ThreadLocalRandom.current().nextInt(min,max+1);
           String query="insert into booktable values('"+randomNum+"','"+bookName+"','"+author+"',1,null,null,null,null,1);";
           statement.executeUpdate(query);
           return true;
       }
       catch (Exception e){
           throw e;
       }

    }
}
