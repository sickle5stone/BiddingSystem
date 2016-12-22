/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Bid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utility.ConnectionManager;

/**
 *
 * @author Cheryl, Regan
 */
/**
 * SectionStudentDAO access database to grab data from the Student-Section Table
 *
 */
public class SectionStudentDAO {
    /**
     * Method to retrieve all enrolled students in a particular section of a course
     * @param courseCode course code
     * @param sectionCode section code of course
     * @return Arraylist of bids belonging to students that are enrolled
     */
    public static ArrayList<Bid> getStudentEnrolledBySection(String courseCode, String sectionCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> sectionList = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section_student where course_id =? and section_id=? ORDER BY user_id ASC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionId = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                String userId = rs.getString("user_id");
                Double amount = rs.getDouble("amount");
                // String status=rs.getString("status");
                bid = new Bid(userId, courseId, sectionCode, amount, "success");

                sectionList.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return sectionList;
    }
    
    /**
     * Method to retrieve all enrolled students in a particular section of a course for json ordering
     * @param courseCode course code
     * @param sectionCode section code of course
     * @return Arraylist of bids belonging to students that are enrolled
     */
    public static ArrayList<Bid> getStudentEnrolledBySectionJson(String courseCode, String sectionCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> sectionList = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section_student where course_id =? and section_id=? ORDER BY amount desc, user_id ASC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionId = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                String userId = rs.getString("user_id");
                Double amount = rs.getDouble("amount");
                // String status=rs.getString("status");
                bid = new Bid(userId, courseId, sectionCode, amount, "success");

                sectionList.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return sectionList;
    }

    /**
     * Method to retrieve all students who are enrolled
     * @return Arraylist of bids belonging to students who are enrolled
     */
    public static ArrayList<Bid> getAllSectionStudent(){
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        ArrayList<Bid> returnList = new ArrayList<>();

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT * FROM section_student ORDER BY `course_id` ASC, `user_id` ASC";
            stmt = conn.prepareStatement(sql);
            
            rs = stmt.executeQuery();
            while(rs.next()){
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                String userId = rs.getString("user_id");
                String status = rs.getString("status");
                returnList.add(new Bid(userId,courseId,sectionCode,amount,status));                
            }
            
        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return returnList;
    }
    /**
     * Gets the Bid object by using the parameters of sectionId,courseCode and
     * userId
     *
     * @param sectionId of section bid for
     * @param courseCode of course bid for
     * @param userId of the user who was logged in and bid
     * @return Bid Object
     */
    public static Bid getSectionStudent(String sectionId, String courseCode, String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section_student where user_id = ? and course_id =? and section_id=? ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                Double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                bid = new Bid(userId, courseCode, sectionId, amount, status);

            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            ConnectionManager.close(conn, stmt, rs);
            return bid;
        }

    }

    /**
     * Gets the object by using the parameter of userId
     *
     * @param userId of the user who bidded for
     * @return ArrayList of bid of the sections that user has bidded for
     */
    public static ArrayList<Bid> getSectionsByUserId(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> sectionListByuserId = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section_student where user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                // String status=rs.getString("status");
                bid = new Bid(userId, courseId, sectionCode, amount, "success");

                sectionListByuserId.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return sectionListByuserId;
    }

    /**
     * Method to delete section based on parameters of coursecode, sectionid and
     * userid
     *
     * @param courseCode of course to deleted
     * @param sectionId of section to be deleted
     * @param userId of student
     * @return true if successfully deleted and false if unsuccessfully deleted
     */
    public static boolean deleteSection(String courseCode, String sectionId, String userId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        boolean noError = false;

        Bid section = getSectionStudent(sectionId, courseCode, userId);
        System.out.println(noError);
        if (section != null) {
            try {
                String status = section.getBidStatus();
                conn = ConnectionManager.getConnection();
                sql = "DELETE from section_student where user_id=? and course_id=? and section_id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, userId);
                stmt.setString(2, courseCode);
                stmt.setString(3, sectionId);

                stmt.executeUpdate();
                noError = true;

            } catch (SQLException e) {
                System.out.println(e);
                return false;
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }
        return noError;
    }
    /**
     * Method to retrieve the number of students who are enrolled in a particular section of a course
     * @param courseCode course code
     * @param sectionCode section code belonging to a particular code
     * @return number of students who are enrolled in a particular section of a course
     */
    public static int getNumEnrolledInSection(String courseCode, String sectionCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        int numEnrolled = 0;

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT COUNT(*) FROM bios_database.section_student WHERE course_id = ? AND section_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            rs = stmt.executeQuery();

            rs.next();
            numEnrolled = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return numEnrolled;
    }
    /**
     * Method to insert all successful bids into section student table
     * @param courseCode course code
     * @param sectionId section id belonging to a particular course
     * @param userId userId of student
     * @param bidAmount bid amount
     */
    public static void insertSuccessfulBids(String courseCode, String sectionId, String userId, double bidAmount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {

            conn = ConnectionManager.getConnection();
            sql = "INSERT into section_student values(?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionId);
            stmt.setDouble(4, bidAmount);
            stmt.setString(5, "ENROLLED");

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

    }
    /**
     * Method to check if a student is enrolled in a specified course
     * @param userid userid of student
     * @param courseCode course code
     * @return true if student is enrolled in a course else return false
     */
    public static boolean isEnrolled(String userid,String courseCode){
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section_student where user_id = ? and course_id =?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userid);
            stmt.setString(2, courseCode);
            rs = stmt.executeQuery();

            while (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return false;

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }
    
    /**
     * Retrieve the lowest successful bid amount 
     * @param courseCode course code
     * @param sectionCode  section code of given course code
     * @return the lowest successful bid amount
     */
    public static double getLowestSuccessBidAmount(String courseCode, String sectionCode) {
        double lowestBidAmount=10;
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT amount FROM bios_database.section_student  WHERE course_id = ? AND section_id = ? ORDER BY amount ASC LIMIT 1";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            rs = stmt.executeQuery();
            rs.next();
            lowestBidAmount = rs.getDouble(1);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return lowestBidAmount;
    }
}

