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
 *  A java DAO that access and modifies the Round table in database
 * @author ChenHuiYan and Regan
 */


public class RoundDAO {
   
    
    
     /**
     * Gets the round status from Round database.
     * @param round round number
     * @param status status of round
     */

    public static void setRoundAndStatus(int round, String status){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE round SET round_status = ? , round_num = ?");
            stmt.setString(1, status);
            stmt.setInt(2, round);
            stmt.executeUpdate();
        }   catch (SQLException e){
            System.out.println(e);
            return;
        }     
    }
    /**
     * Gets the status of the round
     * @return the string status
     */
    public static String getStatus(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String roundStatus="";
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from round";
            stmt = conn.prepareStatement(sql);
       

            rs = stmt.executeQuery();

            while (rs.next()) {
                roundStatus = rs.getString("round_status");

            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
           
        return roundStatus;
        
        
        
        
    }
    
     /**
     * Gets the round number from Round database
     * @return the round number  
     */
       public static int getRoundNum(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int roundRound=0;
        
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from round";
            stmt = conn.prepareStatement(sql);
       

            rs = stmt.executeQuery();

            while (rs.next()) {
                roundRound = rs.getInt("round_num");
        
                

            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
           
        return roundRound;
        
        
        
        
    }
    
    
     /**
     * Sets the round status from active to non-active or vice versa
     */
     public static void invertStatus(){    
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String status=getStatus();
   
        try {
            conn = ConnectionManager.getConnection();
            String sql = "update round set round_status=?";
            stmt = conn.prepareStatement(sql);
            String newStatus="";
            if(status.equals("active")){
                newStatus="inactive";
            
            }else if(status.equals("inactive")){
                newStatus="active";
            }
            
            
            stmt.setString(1, newStatus);
           

            stmt.executeUpdate(); 
 
        

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
           
        
        
        
        
        
    }
     
     /**
     * Method to increase the round number by 1 
     */
     
      public static void increaseRoundNum(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int roundNum=getRoundNum(); 
        if (roundNum == 2){
            roundNum = -1;
            invertStatus();
        }
        try {
            conn = ConnectionManager.getConnection();
            String sql = "update round set round_num=?";
            stmt = conn.prepareStatement(sql);
       
            
            stmt.setInt(1, ++roundNum);
            stmt.executeUpdate(); 
 
        

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
           
        
        
        
        
        
    }
     
     


    
    
    
    
    
}
