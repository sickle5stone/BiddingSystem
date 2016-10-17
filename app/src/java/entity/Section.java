/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;

/**
 *
 * @author Cheryl & Haseena
 */

/** 
 * A Section with courseCode, sectionCode,dayOfWeek,startTime,endTime,instructor,venue,classSize
 * @author ChenHuiYan and Haseena
 */
public class Section {
    private String courseCode;
    private String sectionCode;
    private int dayOfWeek; // use enum ?
    private Date startTime;
    private Date endTime;
    private String instructor;
    private String venue;
    private int classSize;
    
 /**
  * Create a Section object with specified courseCode and sectionCode
  * 
  * @param courseCode (required) userId of student  
  * @param sectionCode (required) password of student
  * 
  */
    public Section(String courseCode, String sectionCode){
        this(courseCode, sectionCode, 0, null , null, null, null, 0);
    }
    
 /**
  * Create a Section object with the specified courseCode,dayOfWeek,startTime,endTime,instructor,venue,classSize
  * 
  * @param courseCode (required) courseCode of section  
  * @param sectionCode (required) sectionCode of section
  * @param dayOfWeek (required) dayOfWeek of section
  * @param startTime (required) startTime of section
  * @param endTime (required) courseCode of section
  * @param instructor (required) instructor for section
  * @param venue (required) venue of section
  * @param classSize  (required) student's e-dollar balance
  * 
  */
    public Section(String courseCode, String sectionCode, int dayOfWeek, Date startTime, Date endTime, String instructor, String venue, int classSize) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.instructor = instructor;
        this.venue = venue;
        this.classSize = classSize;
    }
    /**
     * Sets the courseCode for the section
     * @param courseCode specifies the section's courseCode
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    /**
     * Sets the sectionCode for the section
     * @param sectionCode specifies the section's sectionCode
     */
    public void setSection(String sectionCode) {
        this.sectionCode = sectionCode;
    }
    /**
     * Sets the dayOfWeek for the section
     * @param dayOfWeek specifies the section's dayOfWeek
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Sets the startTime for the section
     * @param startTime specifies the section's startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the endTime for the section
     * @param endTime specifies the section's endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the instructor for the section
     * @param instructor specifies the section's instructor
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    /**
     * Sets the venue for the section
     * @param venue specifies the section's venue
     */

    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Sets the classSize for the section
     * @param classSize specifies the section's classSize
     */
    public void setClassSize(int classSize) {
        this.classSize = classSize;
    }
    /**
     * Gets the courseCode of this section
     * @return the courseCode of this section
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Gets the sectionCode of this section
     * @return the sectionCode of this section
     */
    public String getSection() {
        return sectionCode;
    }

    /**
     * Gets the dayOfWeek of this section
     * @return the dayOfWeek of this section
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Gets the startTime of this section
     * @return the startTime of this section
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Gets the endTime of this section
     * @return the endTime of this section
     */
    public Date getEndTime() {
        return endTime;
    }
    /**
     * Gets the instructor of this section
     * @return the instructor of this section
     */

    public String getInstructor() {
        return instructor;
    }

    /**
     * Gets the venue of this section
     * @return the venue of this section
     */
    public String getVenue() {
        return venue;
    }
    /**
     * Gets the classSize of this section
     * @return the classSize of this section
     */

    public int getClassSize() {
        return classSize;
    }
    
    
    
    
}
