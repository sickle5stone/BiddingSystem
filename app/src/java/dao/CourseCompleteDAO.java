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
import java.util.HashMap;
import utility.ConnectionManager;

/**
 * Class that handles all database operations pertaining to the course completed table
 * @author Cheryl, Regan
 */

public class CourseCompleteDAO {
    /**
     * 
     * @return HashMap of with course code as key, and ArrayList of student_id belong to students who have completed the course
     */
    public static HashMap<String,ArrayList<String>> getAllCourseCompleted(){
       Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        
        HashMap<String, ArrayList<String>> toReturn  = new HashMap<>();              
        try{
            conn = ConnectionManager.getConnection();
            sql = "select * from course_completed order by course_id, user_id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                String courseId = rs.getString("course_id");
                String userId = rs.getString("user_id");
                
                ArrayList<String> courseCom = toReturn.get(courseId);
                if (courseCom == null){
                    courseCom = new ArrayList<>();
                    courseCom.add(userId);
                    toReturn.put(courseId,courseCom);
                }else{
                    courseCom.add(userId);
                    toReturn.put(courseId,courseCom);
                }
            }
        } catch(SQLException e){
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return toReturn;
    }
    
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
