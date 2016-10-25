/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Section;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import utility.ConnectionManager;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Class that handles all database operations pertaining to the Section Table
 *
 * @author reganseah and haseena
 */
public class SectionDAO {
    /**
     * Retrieves all sections in the database
     * @param courseCode
     * @return ArrayList of Section objects
     */
     public static ArrayList<Section> getAllSections() {
        ArrayList<Section> sectionList = new ArrayList<Section>();

        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        Section s = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section order by course_id, section_id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                int day = rs.getInt("day");
                Date startTime = rs.getTime("start_time");
                Date endTime = rs.getTime("end_time");
                String instructor = rs.getString("instructor");
                String venue = rs.getString("venue");
                int size = rs.getInt("size");

                s = new Section(courseId, sectionCode, day, startTime, endTime, instructor, venue, size);
                sectionList.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return sectionList;
    }
    
    
    /**
     * Method to retrieve specific section based on parameters of course code
     * and section id being passed in
     *
     * @param courseCode courseCode of Section
     * @param sectionID SectionIF
     * @return section object which contains both the coursecode and sectionid being
     * passed in
     */
    public static Section getSpecificSection(String courseCode, String sectionID) {

        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        Section s = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section where course_id = ? and section_id = ?";
            //prepare statement to get respective values
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionID);
            System.out.println(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                int day=rs.getInt("day");
                String startTime2 = rs.getString("start_time");
                Date startTime = Time.valueOf(startTime2);
                String endTime2 = rs.getString("end_time");
                Date endTime = Time.valueOf(endTime2);
                String instructor = rs.getString("instructor");
                String venue = rs.getString("venue");
                int size = rs.getInt("size");

                s = new Section(sectionCode, courseId, day, startTime, endTime, instructor, venue, size);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return s;
    }

    /**
     * Method to retrieve sections belonging to the course code being passed in
     *
     * @param courseCode courseCode of section
     * @return an arraylist of sections in the course
     */
    public static ArrayList<Section> getSectionByCourseCode(String courseCode) {
        ArrayList<Section> sectionList = new ArrayList<Section>();

        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        Section s = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from section where course_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                int day = rs.getInt("day");
                Date startTime = rs.getTime("start_time");
                Date endTime = rs.getTime("end_time");
                String instructor = rs.getString("instructor");
                String venue = rs.getString("venue");
                int size = rs.getInt("size");

                s = new Section(courseId, sectionCode, day, startTime, endTime, instructor, venue, size);
                sectionList.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return sectionList;
    }
}
