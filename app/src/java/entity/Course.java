/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author aloysiuslim.2015 , cheryl.lim.2015
 */

/** 
 * A Course with courseCode, schoolTitle, courseTitle, description, examDate, examStart and examEnd 
 * @author ChenHuiYan and Haseena
 */
public class Course {
    private String courseCode;
    private String schoolTitle;
  //  private ArrayList<String> preReq;  there is no preReq in Course.csv
    private String courseTitle;
    private String description;
    private Date examDate;
    private Date examStart;
    private Date examEnd;

     /**
    * Creates a Course object with the specified courseCode, schoolTitle, courseTitle, description, examDate, examStart and examEnd
    * @param courseCode the course's  course code
    * @param schoolTitle the course's school title
    * @param courseTitle the course's course title
    * @param description the course's description
    * @param examDate the course's examDate
    * @param examStart the course's examStart
    * @param examEnd the course's examEnd
    * 
    */
    public Course(String courseCode, String schoolTitle, String courseTitle, String description, Date examDate, Date examStart, Date examEnd) {
        this.courseCode = courseCode;
        this.schoolTitle = schoolTitle;
      //  this.preReq = preReq;
        this.courseTitle = courseTitle;
        this.description = description;
        this.examDate = examDate;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }

    
     /**
     * Gets the courseCode of this Course
     * @return the courseCode of this Course
     */
    public String getCourseCode() {
        return courseCode;
    }

    
     /**
     * Gets the schoolTitle of this Course
     * @return the schoolTitle of this Course
     */
    public String getSchoolTitle() {
        return schoolTitle;
    }

 
    /**
     * Gets the courseTitle of this Course
     * @return the courseTitle of this Course
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
    * Gets the description of this Course
    * @return the description of this Course
    */
    public String getDescription() {
        return description;
    }
    /**
    * Gets the examDate of this Course
    * @return the examDate of this Course
    */
    public Date getExamDate() {
        return examDate;
    }

    /**
    * Gets the examStartTime of this Course
    * @return the examStartTime of this Course
    */
    public Date getExamStart() {
        return examStart;
    }
    
    /**
    * Gets the examEndTime of this Course
    * @return the examEndTime of this Course
    */
    public Date getExamEnd() {
        return examEnd;
    }
    
}