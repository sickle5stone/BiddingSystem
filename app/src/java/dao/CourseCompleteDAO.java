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
import java.util.ArrayList;
import utility.ConnectionManager;

/**
 * Class that handles all database operations pertaining to the course completed table
 * @author Cheryl, Regan
 */

public class CourseCompleteDAO {
    
    /**
     * Gets the ArrayList of course codes which are of type String by using the parameter of userId
     * @param userId of the user who is currently logged in
     * @return ArrayList of courses codes the the user has completed
     */
    public static ArrayList<String> getListOfCourseCompleted(String userId){
       Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        
        ArrayList<String> courseCompByStud = new ArrayList();
        
        String course = "";
        
        try{
            conn = ConnectionManager.getConnection();
            sql = "select course_id from course_completed where user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            while(rs.next()){
                course = rs.getString("course_id");
                courseCompByStud.add(course);
            }
        } catch(SQLException e){
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return courseCompByStud;
    }
}
