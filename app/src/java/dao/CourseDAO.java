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
import java.util.HashMap;
import utility.ConnectionManager;

/**
 *A java DAO that access and modifies the Course table in database
 * @author Haseena and Huiyan
 */
public class CourseDAO {
    
    /**
     * Return pre-requisite order in Hashmap format
     * @return hashmap format of the course section 
     */
    public static HashMap<String, ArrayList<String>> getCourseSectionHashMap(){
        HashMap<String, ArrayList<String>> toReturn = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionManager.getConnection(); 
            String sql = "Select * from prerequisite order by course_id, prerequisite";
            stmt = conn.prepareStatement(sql);  
            rs = stmt.executeQuery();

            while (rs.next()) {
                String courseId = rs.getString("course_id");
                String sectionId = rs.getString("prerequisite");
                ArrayList<String> sections = toReturn.get(courseId);
                if (sections == null){
                    sections = new ArrayList<>();
                    sections.add(sectionId);
                    toReturn.put(courseId,sections);
                }else{
                    sections.add(sectionId);
                    toReturn.put(courseId,sections);
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
            return toReturn;
        }
        
        
    }
    
    
    /**
     * Get all courses in database
     * @return ArrayList of Course objects
     */
    public static ArrayList<Course> getAllCourses(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> toReturn = new ArrayList<>();
        
        try {
            conn = ConnectionManager.getConnection();
            String sql = "Select * from course order by course_id";
            stmt = conn.prepareStatement(sql);
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
                toReturn.add(course);

            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
            return toReturn;
        }
    }
    /**
     * Method to retrieve course object by course code
     * @param courseCode courseCode 
     * @return Course object if courseCode is valid otherwise return false
     */
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
    /**
     * Method to retrieve all prerequisites for a course
     * @param courseCode courseCode
     * @return ArrayList of prerequisites for a particular course
     */
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
    /**
     * Method to retrieve all courses belonging to a school
     * @param school school name
     * @return Arraylist of courses belonging to a school
     */
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
    /**
     * Method to retrieve all courses that a student has completed
     * @param userId student's userId
     * @return ArrayList of courses that student has completed
     */
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
    /**
     * Method to retrieve Course object using course title
     * @param courseTitle course title of course
     * @return Course object if courseTitle is valid otherwise return null
     */
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
    /**
     * Method to retrieve all courses containing specified course title
     * @param courseTitle Title of course
     * @return ArrayList of courses containing specified course title
     */
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
    /**
     * Method to search course based on parameters of course title, course code and school code
     * @param courseTitle Title of course
     * @param courseCode Course code
     * @param schoolCode School Code
     * @return ArrayList of courses containing the three specified parameters
     */
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
