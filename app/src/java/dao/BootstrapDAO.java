/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controller.BootstrapController;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import utility.ConnectionManager;

/**
 * Class that handles all the database operations in relation to the bootstrap
 *
 * @author Haseena , Cheryl , Huiyan, Aloysius
 */
public class BootstrapDAO {
    private static final int BATCHSIZE = 3000;

    /**
     * Method to clear database of all previous data
     *
     * @return true if successfully cleared , false if met exception and failed
     * to successfully cleared database
     */
    public static boolean clearDatabase() {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE student");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE section");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE course");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE course_completed");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE prerequisite");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE bid");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE section_minimal_price");
            stmt.execute();
            stmt = conn.prepareStatement("TRUNCATE section_student");
            stmt.execute();
            stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }

    }

    /**
     * Method to insert completed courses using the hashmap of courseCompleted
     *
     * @param courseCompleted A hashmap of key of string and value of arraylist
     * of strings
     * @return true if coursecompleted was successfully inserted , false if
     * coursecompleted was not successfully inserted and exception was made
     */
    public static boolean insertCourseCompleted(HashMap<String, ArrayList<String>> courseCompleted) {
        Set<String> students = courseCompleted.keySet();
        Connection conn = null;
        PreparedStatement stmt = null;
        // Returns an arraylist of completed courseIDs returned by each student
        try {
            conn = ConnectionManager.getConnection();
            // count for batch processing
            int count = 0 ;
            String sql = "INSERT into course_completed values(?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);
            for (String studentId : students) {
                ArrayList<String> courseIDs = courseCompleted.get(studentId);
                for (String eachCourseID : courseIDs) {
                    
                    stmt.setString(1, studentId);
                    stmt.setString(2, eachCourseID);
                    //add batch 
                    stmt.addBatch();
                    count++;
                    //Execute the sql statement
                    if (count % BATCHSIZE == 0){
                        stmt.executeBatch();
                    }else if(count == BootstrapController.getNumElements(courseCompleted)){
                        stmt.executeBatch();
                    }
                }
            }
            
            //Close the statement and connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }

        return true;
    }

    /**
     * Method to insert students into the database using the arrayList of
     * studentList
     *
     * @param studentsList arraylist of students to be inserted into database
     * @return true if successfully inserted into database, false if
     * unsuccessfully inserted into database
     */
    public static boolean insertStudent(ArrayList<Student> studentsList) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            String sql = "INSERT into student values(?,?,?,?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);
            int count = 0;
            for (Student s : studentsList) {
                // Returns an arraylist of completed courseIDs returned by each student
                String studentId = s.getUserId();
                String password = s.getPassword();
                String name = s.getName();
                String school = s.getSchool();
                double eDollar = s.geteDollar();
                stmt.setString(1, studentId);
                stmt.setString(2, password);
                stmt.setString(3, name);
                stmt.setString(4, school);
                stmt.setDouble(5, eDollar);
                stmt.addBatch();
                count++;
                //Execute the sql statement
                if (count % BATCHSIZE == 0){
                    stmt.executeBatch();
                }else if(count == studentsList.size()){
                    stmt.executeBatch();
                }

            }
            //Close the statement and connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        return true;

    }

    /**
     * Method to insert course into database using the arrayList of coursesList
     *
     * @param coursesList arraylist of courses to be inserted into database
     * @return true if successfully inserted and false if unsuccessfully
     * inserted
     */
    public static boolean insertCourse(ArrayList<Course> coursesList) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            int count = 0 ;
            String sql = "INSERT into course values(?,?,?,?,?,?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);
            
            for (Course c : coursesList) {
                // Returns an arraylist of completed courseIDs returned by each student
                String courseCode = c.getCourseCode();
                String schoolTitle = c.getSchoolTitle();
                String courseTitle = c.getCourseTitle();
                String desc = c.getDescription();
                Date examDate = c.getExamDate();
                Date examStart = c.getExamStart();
                Date examEnd = c.getExamEnd();

                
                stmt.setString(1, courseCode);
                stmt.setString(2, schoolTitle);
                stmt.setString(3, courseTitle);
                stmt.setString(4, desc);
                java.sql.Date sqlExamDate = new java.sql.Date(examDate.getTime());
                stmt.setDate(5, sqlExamDate);
                java.sql.Time sqlExamStart = new java.sql.Time(examStart.getTime());
                stmt.setTime(6, sqlExamStart);
                java.sql.Time sqlExamEnd = new java.sql.Time(examEnd.getTime());
                stmt.setTime(7, sqlExamEnd);

                stmt.addBatch();
                count++;
                
                //Execute the sql statement
                if (count % BATCHSIZE == 0){
                    stmt.executeBatch();
                    }else if(count == coursesList.size()){
                        stmt.executeBatch();
                    }
            }
            //Close the statement and connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        return true;

    }

    /**
     * Method to insert section into database using the hashmap of sectionList
     *
     * @param sectionList A hashmap of key of string and value of arraylist of
     * sections
     * @return true if successfully inserted and false if unsuccessfully
     * inserted
     */
    public static boolean insertSection(HashMap<String, ArrayList<Section>> sectionList) {
        Set<String> courseIDs = sectionList.keySet();
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionManager.getConnection();
            String sql = "INSERT into section values(?,?,?,?,?,?,?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);
            
            int count = 0;
            for (String courseID : courseIDs) {
                // Returns an arraylist of sections under each course
                ArrayList<Section> sections = sectionList.get(courseID);

                for (Section section : sections) {
                    stmt.setString(1, section.getCourseCode());
                    stmt.setString(2, section.getSection());
                    stmt.setInt(3, section.getDayOfWeek());
                    Date startTime = section.getStartTime();
                    Date endTime = section.getEndTime();
                    java.sql.Time sqlStartTime = new java.sql.Time(startTime.getTime());
                    java.sql.Time sqlEndTime = new java.sql.Time(endTime.getTime());
                    stmt.setTime(4, sqlStartTime);
                    stmt.setTime(5, sqlEndTime);
                    stmt.setString(6, section.getInstructor());
                    stmt.setString(7, section.getVenue());
                    stmt.setInt(8, section.getClassSize());
                    // add to batch
                    stmt.addBatch();
                    count++;

                    //Execute the sql statement
                    if (count % BATCHSIZE == 0){
                        stmt.executeBatch();
                    }else if(count == BootstrapController.getNumElements(sectionList)){
                        stmt.executeBatch();
                    }
                }
            }
            //Close the statement and connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        return true;
    }
    /**
     * Inserts Sections into section_minimal_price table in database, setting default min price as $10
     * @param sectionList an arraylist of sections
     * @return true if all are successfully added,false otherwise
     */
    public static boolean insertSectionMinBid(HashMap<String, ArrayList<Section>> sectionList) {
        Set<String> courseIDs = sectionList.keySet();
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionManager.getConnection();
            int count = 0;
            String sql = "INSERT into section_minimal_price values(?,?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);
            
            for (String courseID : courseIDs) {
                // Returns an arraylist of sections under each course
                ArrayList<Section> sections = sectionList.get(courseID);

                for (Section section : sections) {
                    
                    stmt.setString(1, section.getCourseCode());
                    stmt.setString(2, section.getSection());
                    stmt.setDouble(3, 10);
                    
                    
                    // add to batch
                    stmt.addBatch();
                    count++;

                    //Execute the sql statement
                    if (count % BATCHSIZE == 0){
                        stmt.executeBatch();
                    }else if(count == BootstrapController.getNumElements(sectionList)){
                        stmt.executeBatch();
                    }
                }
            }
            //Close the statement and connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        return true;
    }

    /**
     * Method to insert prerequisites into database using the hashmap of
     * preReqList
     *
     * @param prereqsList A hashmap of key of string and value of arraylist of
     * strings
     * @return true if successfully inserted and false if unsuccessfully
     * inserted
     */
    public static boolean insertPrerequisites(HashMap<String, ArrayList<String>> prereqsList) {
        Set<String> courseIDs = prereqsList.keySet();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            int count = 0;
            String sql = "INSERT into prerequisite values(?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);
            for (String courseID : courseIDs) {
                // Returns an arraylist of prerequisites required by each course
                ArrayList<String> prereqIDs = prereqsList.get(courseID);
                for (String prereqID : prereqIDs) {
                    
                    stmt.setString(1, courseID);
                    stmt.setString(2, prereqID);
                    
                    // add to batch
                    stmt.addBatch();
                    count++;

                    //Execute the sql statement
                    if (count % BATCHSIZE == 0){
                        stmt.executeBatch();
                    }else if(count == BootstrapController.getNumElements(prereqsList)){
                        stmt.executeBatch();
                    }
                }
            }
            //Close the statement and connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }

        return true;

    }

    /**
     * Method to insert bid into database using the arrayList of BidList
     *
     * @param bidList arraylist of bids
     * @return true if successfully inserted into database and false if
     * unsuccessfully inserted into database
     */
    public static boolean insertBid(ArrayList<Bid> bidList) {

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "INSERT INTO bid values(?,?,?,?,?)";
            //Prepare statement to input respective parameter values
            stmt = conn.prepareStatement(sql);            
            int count = 0;
            for (Bid bid : bidList) {
                
                stmt.setString(1, bid.getUserId());
                stmt.setDouble(2, bid.getBidAmount());
                stmt.setString(3, bid.getCourseCode());
                stmt.setString(4, bid.getSectionCode());
                stmt.setString(5, "pending");
                
                // add to batch
                stmt.addBatch();
                count++;

                //Execute the sql statement
                if (count % BATCHSIZE == 0){
                        stmt.executeBatch();
                }else if(count == bidList.size()){
                    stmt.executeBatch();
                }
            }
            //Close the statement and connection
        } catch (SQLException e) {
            
            int i = 1;
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        return true;

    }

    /**
     * Sets the default bid value to $10
     *
     * @param courseSectionList Hashmap containing the map of course and
     * sections to set default bid value on
     */
    public static void setDefaultBid(HashMap<String, ArrayList<Section>> courseSectionList) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            int count = 0;
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO section_minimal_price VALUES(?,?,10)");            
            Set<String> listOfCourses = courseSectionList.keySet();
            for (String cId : listOfCourses) {
                ArrayList<Section> sectionList = courseSectionList.get(cId);
                for (Section s : sectionList) {
                    String courseId = s.getCourseCode();
                    String sectionId = s.getSection();

                    stmt.setString(1, courseId);
                    stmt.setString(2, sectionId);
                                        
                    // add to batch
                    stmt.addBatch();
                    count++;

                    //Execute the sql statement
                    if (count % BATCHSIZE == 0){
                        stmt.executeBatch();
                    }else if(count == BootstrapController.getNumElements(courseSectionList)){
                        stmt.executeBatch();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }

    }

}
