package bootstrap_validation;


import controller.BootstrapController;
import entity.Course;
import entity.Student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Class to validate all course completed related bootstrap data
 * @author ChenHuiYan and Regan 
 */
public class ValidateCourseCompleted {
    /**
     * Method to consolidate all the error messages resulting from calling the various course completed validation methods
     * @param row Array of data for every row in coursecompleted.csv
     * @return String of errors
     */
    public static String checkCourseCompleted(String[] row){
        String errors = "";
        String userId=row[0];
        String courseCode=row[1];
      
        if (!checkValidStudent(BootstrapController.STUDENTLIST, userId)){
            errors += "invalid userid, ";            
        }
        if (!checkValidCourse(BootstrapController.COURSELIST, courseCode)){
            errors += "invalid code, ";            
        }

        if (errors.isEmpty()) {
            
            if (!completedPrereq(userId, courseCode)){
                // add error message if student did not complete prereq for the course
                errors += "invalid course completed, ";
            }else {
                // if student complete prereq of the course, add course into coursecomplete list
                
                addIntoCourseCompleteList(userId, courseCode);
            }
        } 
        
        if(!errors.isEmpty()){
            errors= errors.substring(0, errors.length()-2 );        
        }

        return errors;
    
    }        
          
    /**
     * Method to check if student is valid 
     * @param users Arraylist of students
     * @param studentId Student ID
     * @return true if student is valid else return false
     */        
    public static boolean checkValidStudent(ArrayList<Student> users, String studentId){
        for(int s=0; s<users.size(); s++){
               if(users.get(s).getUserId().equals(studentId)){
                    return true;
               }
        }
        return false;
    }
    
    /**
     * Method to check if check is valid
     * @param courses Arraylist of courses
     * @param courseCode Course code
     * @return true if course is valid else return false
     */           
    public static boolean checkValidCourse(ArrayList<Course> courses, String courseCode){        
        for(int c=0; c<courses.size(); c++){
               if(courses.get(c).getCourseCode().equals(courseCode)){
                   return true;
               }
        }
        return false;
    }
    
    /**
     * Method to check if student has completed all the prereq for the course passed in as a parameter
     * @param userId Student ID
     * @param courseCode Course code
     * @return true if all the prereq are completed are return false
     */    
    public static boolean completedPrereq (String userId, String courseCode){
        HashMap<String, ArrayList<String>> preReqs = BootstrapController.PREREQLIST; // courseId, list of prereIds
        HashMap<String, ArrayList<String>> coursesCompleted = BootstrapController.COURSECOMPLETE; // studentId, list of courseId completed
    
        Set<String> keys = preReqs.keySet(); // get the courses with pre-req
        boolean hasPrereq = false;
        boolean existingUser=false;
        boolean addIntoCourseCompleted=false;

        if (!preReqs.containsKey(courseCode)) {
            return true;
        }   // check whether the course need prereq

       
        ArrayList<String> preReqsByCourse = preReqs.get(courseCode); // get all preReqs for the course
        ArrayList<String> courseCompletedByStudent = coursesCompleted.get(userId);
        if (courseCompletedByStudent == null){
            return false;
        }
        for (String prereqId : preReqsByCourse) {
            boolean completePrereq = false;
            for (String courseId : courseCompletedByStudent) {
                if (prereqId.equals(courseId)) {
                    completePrereq = true;
                    break;
                }
            }    

            if (!completePrereq) {
                return false;
            }     // the preReq is not completed 

        }  
        return true;
    }    
    /**
     * Method to add course into course completed list
     * @param userId Student ID
     * @param courseCode Course code
     */
    public static void addIntoCourseCompleteList(String userId, String courseCode){
        
        HashMap<String, ArrayList<String>> coursesCompleted = BootstrapController.COURSECOMPLETE; // studentId, list of courseId completed
         ArrayList<String> courses;
        if(coursesCompleted.containsKey(userId)){
            
            // retrieve existing student record, add course code to existing arraylist of courses completed
           courses= coursesCompleted.get(userId);
           courses.add(courseCode);
           BootstrapController.COURSECOMPLETE.remove(userId);
           BootstrapController.COURSECOMPLETE.put(userId, courses);
        } else {
           // adds record of new student and the completed course
           courses = new ArrayList<String>();
           courses.add(courseCode);
           //System.out.println(userId);
           BootstrapController.COURSECOMPLETE.put(userId, courses);
        }
    }     
}
