/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bootstrap_validation;

import controller.BootstrapController;
import entity.Course;
import java.util.ArrayList;

/**
 * Class to validate all prerequisite related bootstrap data
 * @author Cheryl and Aloysius
 */
public class ValidatePrereq {
    /**
     * checkCourse method to consolidate all the error messages resulting from calling the various pre requisite validation methods
     * @param row Array of data for every row in prerequisite.csv
     * @return String of error messages
     */
      public static ArrayList<String> checkPrereq (String [] row){
        ArrayList<String> errors = new ArrayList<>(); // to take in error msg
        // for blank row at the end
        if (row.length < 2){
            return errors;
        }
        
        
        String courseId = row[0];
        String prereqId = row[1];
         
        
        if (!checkValidCourseId(courseId)){
            errors.add("invalid course");
        }
        
        if (!checkValidPrereqId(prereqId)){
            errors.add("invalid prerequisite");
        }
        if (checkIsDuplicateRecord(courseId, prereqId)){
            errors.add("duplicate record");
        }
        if (!checkBothSameCourse(courseId, prereqId)){
            errors.add("invalid course & prerequisite");
        }
        
        if (errors.isEmpty()){
            if (recursiveCheck(courseId, prereqId)){
                errors.add("invalid recursive");
            }
        }
        
        if (errors.isEmpty()){ // store the course-prereq record if no errors

            ArrayList<String> prereqs = BootstrapController.PREREQLIST.get(courseId);
            if (prereqs == null){
                // if course does not exist in list add new key, value pair
                ArrayList<String> toStore = new ArrayList<>();
                toStore.add(prereqId);
                BootstrapController.PREREQLIST.put(courseId, toStore);                
            } else {
                prereqs.add(prereqId);
            }
        } 
        
        //System.out.println("MY ERROR IS HERE"+error);
        return errors;
    }
    /**
     * Method to check if Course Id is valid by checking against list of courses from course.csv
     * @param courseId Course Code
     * @return true if course is valid else return false 
     */    
    public static boolean checkValidCourseId(String courseId){
        for (Course c : BootstrapController.COURSELIST){
            String courseCode =  c.getCourseCode();
            //check if courseCode exists in the list of COURSELIST
            if (courseId.equals(courseCode)){
                return true;
            }
        }
        //return error when there courseCode does not exists
        return false;
    }
    /**
     * Check if prereq course ID is valid
     * @param prereqId Prereq course ID
     * @return true if prereq course ID is valid else return false
     */    
    public static boolean checkValidPrereqId(String prereqId){
        // loops through COURSELIST to check if prereqId exists
        for (Course c : BootstrapController.COURSELIST){
            String courseCode =  c.getCourseCode();
            if (prereqId.equals(courseCode)){
                return true;
            }
        }
        //return error when prerequisite does not exist in COURSELIST
        return false;
    }
    
    //check if the courseId is the same as preReqId
    /**
     * Method to check if both course and its prereq are the same
     * @param courseId Course code
     * @param prereqId Prerequisite course code
     * @return false both courses are the same else return true
     */
    public static boolean checkBothSameCourse(String courseId, String prereqId){
        return !courseId.equals(prereqId);
    }
    /**
     * Method to check if there are duplicate records
     * @param courseId Course code
     * @param prereqId Prereq course code
     * @return false if duplicate else return true
     */
    public static boolean checkIsDuplicateRecord(String courseId, String prereqId){
        ArrayList<String> prereqsOfCourse = BootstrapController.PREREQLIST.get(courseId);
        if (prereqsOfCourse == null){
            return false;
        }else{
            //check through the prerequisite of the course to check for duplicates
            for (String p: prereqsOfCourse){
                if(p.equals(prereqId)){
                    return true;
                }    
            }
        }
        return false;
    }
    
    //recusive check through all the prerequisite of the courseId
    //entry is invalid if the courseId exists in any of the prerequisite's prerequisite course
    /**
     * Method to perform recursive through all the prerequisite of the course id
     * @param courseId Course code
     * @param prereqId Prereq course id
     * @return true if check is valid else return false
     */
    public static boolean recursiveCheck(String courseId, String prereqId){
        ArrayList<String> prereqsOfCourse = BootstrapController.PREREQLIST.get(prereqId);
        if (prereqsOfCourse == null){
            return false;
        }
        //loop through the arrayList of prerequisites returned
        for (String p : prereqsOfCourse){
            if (p.equals(courseId)){
                return true;
            }
            //calls itself to check for any more prerequisite inside the prerequsite of the course
            recursiveCheck(courseId, p);
        }
        return false;
    }

}
