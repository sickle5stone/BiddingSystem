/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Course;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import utility.ConnectionManager;

/**
 *A java DAO that access and modifies the Course table in database
 * @author Haseena
 */
public class CourseDAO {

    public static Course getCourseByCourseCode(String courseCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Course course = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from course where course_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String courseID = rs.getString("course_id");
                String school = rs.getString("school");
                String title = rs.getString("title");
                String description = rs.getString("description");

                
                Date examDate = rs.getDate("exam_date");
                String examStart2 = rs.getString("exam_start");
                Date examStart=Time.valueOf(examStart2);
                
                String examEnd2 = rs.getString("exam_end");
                Date examEnd=Time.valueOf(examEnd2);

                //Create a course object
                course = new Course(courseID, school, title, description, examDate, examStart, examEnd);

            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return course;
    }

    public static ArrayList<Course> getPrereqListbyCourseCode(String courseCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> preReqCourseList = new ArrayList<String>();
        ArrayList<Course> preReqCourseCodes = new ArrayList<Course>();
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select prerequisite from  prerequisite where course_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String prerequisite = rs.getString("prerequisite");
                preReqCourseList.add(prerequisite);

            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);

        }
        if (preReqCourseList.size() > 0) {
            for (String preReqCourse : preReqCourseList) {
                Course preRequisteCourse = getCourseByCourseCode(preReqCourse);
                preReqCourseCodes.add(preRequisteCourse);
            }
        }
        return preReqCourseCodes;
    }

    public static ArrayList<Course> getCourseBySchool(String school) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> coursesInSchool = new ArrayList<Course>();
        try {
            conn = ConnectionManager.getConnection();
            String sql = "Select * from course where school=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, school);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String courseID = rs.getString("course_id");
                String schoolName = rs.getString("school");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Date examDate = rs.getDate("exam_date");
                
                String examStart2 = rs.getString("exam_start");
                Date examStart=Time.valueOf(examStart2);
                
                String examEnd2 = rs.getString("exam_end");
                Date examEnd=Time.valueOf(examEnd2);

                Course course = new Course(courseID, schoolName, title, description, examDate, examStart, examEnd);
                coursesInSchool.add(course);

            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
            return coursesInSchool;
        }
    }

    public static ArrayList<Course> getCompletedCoursesbyStudent(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> studentCompletedCourses = new ArrayList<Course>();
        ArrayList<String> completedCourses = new ArrayList<String>();
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select course_id from course_completed where user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                //Add the courseID into an string arraylist of completedCourses
                String courseID = rs.getString("course_id");
                completedCourses.add(courseID);
                //Cannot create a new connection without closing the pre connection & resultset
                // Course completedCourse=getCourseByCourseCode(courseID);
                // studentCompletedCourses.add(completedCourse);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        if (completedCourses.size() > 0) {
            for (String courseId : completedCourses) {
                Course completed = getCourseByCourseCode(courseId);
                studentCompletedCourses.add(completed);
            }
        }

        return studentCompletedCourses;

    }

    public static Course getCourseByCourseTitle(String courseTitle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Course course = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from course where title=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseTitle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String courseID = rs.getString("course_id");
                String school = rs.getString("school");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Date examDate = rs.getDate("exam_date");
                
                String examStart2 = rs.getString("exam_start");
                Date examStart=Time.valueOf(examStart2);
                
                String examEnd2 = rs.getString("exam_end");
                Date examEnd=Time.valueOf(examEnd2);

                course = new Course(courseID, school, title, description, examDate, examStart, examEnd);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
            return course;
        }
    }
    
    public static ArrayList<Course> getCoursesContainingTitle(String courseTitle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> courseList = new ArrayList<>();
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from course where title like ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, '%'+courseTitle+'%');
            rs = stmt.executeQuery();
            while (rs.next()) {
                String courseID = rs.getString("course_id");
                System.out.println(courseID+"courseID");
                String school = rs.getString("school");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Date examDate = rs.getDate("exam_date");
                
                String examStart2 = rs.getString("exam_start");
                Date examStart=Time.valueOf(examStart2);
                
                String examEnd2 = rs.getString("exam_end");
                Date examEnd=Time.valueOf(examEnd2);

                courseList.add(new Course(courseID, school, title, description, examDate, examStart, examEnd));
               // System.out.println(courseList.size());
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
           // System.out.println(courseList.size());
            return courseList;
        }
    }
     public static ArrayList<Course> searchCourse(String courseTitle, String courseCode, String schoolCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> courseList = new ArrayList<>();
        try {
            conn = ConnectionManager.getConnection();
            String sql = "select * from course where title like ? and school like ? and course_id like ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, '%'+courseTitle+'%');
            stmt.setString(2, '%'+schoolCode+'%');
            stmt.setString(3, '%'+courseCode+'%');
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                String courseID = rs.getString("course_id");
                System.out.println(courseID+"courseID");
                String school = rs.getString("school");
                String title = rs.getString("title");
                String description = rs.getString("description");

                Date examDate = rs.getDate("exam_date");
                
                String examStart2 = rs.getString("exam_start");
                Date examStart=Time.valueOf(examStart2);
                
                String examEnd2 = rs.getString("exam_end");
                Date examEnd=Time.valueOf(examEnd2);

                courseList.add(new Course(courseID, school, title, description, examDate, examStart, examEnd));
               // System.out.println(courseList.size());
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
           // System.out.println(courseList.size());
            return courseList;
        }
    }
    
    
}
