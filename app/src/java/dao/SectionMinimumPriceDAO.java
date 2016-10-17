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
 *Class that handles all database operations pertaining to the the minimum price of the section
 * @author Aloysius and Regan
 */
public class SectionMinimumPriceDAO {
    /**
     * Retrieve the minimum price of a particular section belonging to a course using courseId and sectionId
     * @param courseId courseId of section
     * @param sectionId sectionId
     * @return minimum bid amount for section
     */
    public static double getMinBidAmtByCourseSection(String courseId,String sectionId){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM section_minimal_price WHERE section_id = ? AND course_id = ? ");
            stmt.setString(1, sectionId);
            stmt.setString(2, courseId);
            rs = stmt.executeQuery();
            
            rs.next();
            return rs.getDouble("minimum_bid");
            
            
        }catch (SQLException e){
            return 0.0;
        }finally{
             ConnectionManager.close(conn, stmt, rs);
        }
    }
    
    public static void setMinBid(String courseCode,String sectionCode,double minBid){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("update section_minimal_price set minimum_bid=? where course_id=? and section_id=?");
            stmt.setDouble(1, minBid);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionCode);
            stmt.executeUpdate();
                        
        }catch (SQLException e){
            System.out.println(e);
        }finally{
             ConnectionManager.close(conn, stmt, rs);
        }
    }
    
    
}
