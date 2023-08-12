/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafemanagement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class database {
    public static Connection connectDB(){
       try{
           Class.forName("com.mysql.jbdc.driver");
           Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/cafe","root","toor");
       }
       catch(Exception e){
           e.printStackTrace();
       }
       return null;
}
}
