/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import entity.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utility.ConnectionManager;

/**
 * A java DAO that access and modifies the Student table in database
 * @author Aloysius, Cheryl
 */
public class StudentDAO {
    
    /**
     * 
     * Method to check student's username and password in the database
     * @param userId Student id
     * @param password Student password 
     * @return true if user id and password is found; false if not found;
     */
    public static boolean loginStudent(String userId,String password){
        Connection conn = null;
        PreparedStatement stmt = null;
        int numStudent = 0;
        try {
            conn = ConnectionManager.getConnection();


            stmt = conn.prepareStatement("SELECT COUNT(user_id) FROM student WHERE user_id = ? AND password = ?");
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.executeQuery();
            ResultSet results = stmt.getResultSet();
            //read first entry of the result set
            results.next();
            numStudent = results.getInt(1);
            if (numStudent==1){
                return true;
            }
            
        } catch (SQLException e){
            e.getStackTrace();
        }
        return false;
    }
    /**
     * Method to retrieve student object based on userid passed in
     * @param userId of student 
     * @return student object that the userid belongs to
     */
    public static Student getStudentById(String userId){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Student s = null;
        
        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT * from student where user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String studId = rs.getString("user_id");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String school = rs.getString("school");
                double eDollar = rs.getDouble("edollar");
                
                s = new Student(studId, password, name, school, eDollar);
            }  
        } catch (SQLException e){
            System.out.println(e);
        } finally{
            ConnectionManager.close(conn,stmt, rs);
        }
        return s;
    }
    /**
     * Method to retrieve all students
     * @return arraylist of students
     */
    public static ArrayList<Student> retrieveAllStudents(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        ArrayList<Student> sList = new ArrayList<Student>();
        Student s = null;
   
        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT * from student";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String studId = rs.getString("user_id");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String school = rs.getString("school");
                double eDollar = rs.getDouble("edollar");
                
                s = new Student(studId, password, name, school, eDollar);
                sList.add(s);
            }  
        } catch (SQLException e){
            System.out.println(e);
        } finally{
            ConnectionManager.close(conn,stmt, rs);
        }
        return sList;
    }
    /**
     * Method to upload e dollar balance of a particular student whose userid is passed in
     * @param userId of student whom we want to update his/her edollar amount
     * @param amt to be updated
     * @return edollar balance if successfully updated, -1 if unsuccessfully updated
     */
    public static double updateEdollar(String userId, double amt){
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        String checkSql = "";
        double updated = -1;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            sql = "UPDATE student SET eDollar=? where user_id= ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, amt);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            
            
        } catch (SQLException e){
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        try{
            conn = ConnectionManager.getConnection();
            sql = "SELECT edollar from STUDENT where user_id= ?";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.next();
            updated = rs.getDouble(1);
            rs.getDouble("edollar");
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        return updated;
    }
    
}
