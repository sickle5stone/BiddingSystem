/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utility.ConnectionManager;

/**
 * A java DAO that access and modifies the Admin table in database.
 * @author Aloysius, Cheryl
 */
public class AdminDAO {
    
    /**
     * 
     * Method to check admin's username and password in the database
     * @param userId Admin id
     * @param password Admin password 
     * @return true if user id and password is found; false if not found;
     */
    public static boolean loginAdmin(String userId,String password){
        Connection conn = null;
        PreparedStatement stmt = null;
        int numAdmin = 0;
        ResultSet results = null;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(user_id) FROM admin WHERE user_id = ? AND password = ?");
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.executeQuery();
            results = stmt.getResultSet();
            //read first entry of the result set
            results.next();
            numAdmin = results.getInt(1);
            if (numAdmin==1){
                return true;
            }
            
        } catch (SQLException e){
            e.getStackTrace();
        } finally{
            ConnectionManager.close(conn, stmt, results);
        }
        return false;
    }
    
}
