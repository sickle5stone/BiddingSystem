/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.CourseDAO;
import dao.SectionDAO;
import entity.Course;
import entity.Section;
import java.util.ArrayList;

/**
 * Class that handles all controller operations pertaining to the course section table
 * @author reganseah and Haseena
 */
public class CourseSectionController {
    /**
     * getCoursesContainingTitle method retrieves all courses that contains the paramater of courseTitle
     * @param courseTitle Course title
     * @return Arraylist of courses that contain the course title
     */
    public static ArrayList<Course> getCoursesContainingTitle(String courseTitle){
        ArrayList<Course> courseList = CourseDAO.getCoursesContainingTitle(courseTitle);
        
        return courseList;
    }
    /**
     * Method to retrieve all courses containing the parameters passed in through courseTitle,courseCode,school
     * @param courseTitle Course Title
     * @param courseCode Course Code
     * @param school School
     * @return Arraylist of courses that contain the parameters
     */
    public static ArrayList<Course> getCourses(String courseTitle, String courseCode, String school){
        ArrayList<Course> courseList = CourseDAO.searchCourse(courseTitle, courseCode, school); 
        
        return courseList;
    }
    /**
     * Method to retrieve all sections by course code
     * @param courseCode Course code
     * @return Arraylist of sections belonging to the course code passed in
     */
    public static ArrayList<Section> getSectionsByCourse(String courseCode){
        ArrayList<Section> returnSection = new ArrayList<>();
        
        
        returnSection = SectionDAO.getSectionByCourseCode(courseCode);
        
        
        return returnSection;
    }
    /**
     * Method to retrieve course object by course code
     * @param courseCode Course code
     * @return Course object which contains the course code else return null
     */
    public static Course getCourseByCourseCode(String courseCode){
        Course course = null;
        
        
        course = CourseDAO.getCourseByCourseCode(courseCode);
        
        
        return course;
    }
    /**
     * Method to retrieve section object based on course code and section code
     * @param courseCode Course code
     * @param sectionCode Section code
     * @return Section object which contains both the course code and section code else return null
     */
    public static Section getSection(String courseCode, String sectionCode){
        Section section = null;
        
        section = SectionDAO.getSpecificSection(courseCode, sectionCode);
        
        return section;
        
    }
    

}
