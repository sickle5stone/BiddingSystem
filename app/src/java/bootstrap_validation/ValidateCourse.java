/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bootstrap_validation;

import controller.BootstrapController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import entity.Course;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Class to validate all course related bootstrap data
 * @author ChenHuiYan and ReganSeah
 */
public class ValidateCourse {
     /**
     * Method to consolidate all the error messages resulting from calling the various course validation methods
     * @param row Array of data for every row in course.csv
     * @return String of errors
     */
    public static ArrayList<String> checkCourse(String[] row){
        ArrayList<String> errors = new ArrayList<>();
         if (row.length < 7){
            return errors;
        }
        
        String courseCode = row[0];
        String school = row[1];
        String title = row[2];
        String desc = row[3];
        String examDate = row[4];
        String examStart = row[5];
        String examEnd = row[6];
        
        if (courseCode.length() > 10){
            errors.add("invalid course code");
        }
        
        if (checkDuplicateCourseCode (BootstrapController.COURSELIST, courseCode)){
            errors.add("duplicate course code");
        } 

        if (!checkTitleIsValid(title)){
             errors.add("invalid title");
        }
        boolean temp = checkDescriptionIsValid(desc);
        if (!checkDescriptionIsValid(desc)){
            errors.add("invalid description");
        }
        
       
        Date examDateFormat=validDateFormat("yyyyMMdd", examDate);
        if (examDateFormat==null) {
            errors.add("invalid exam date");
        }
        
        Date examStartFormat=validDateFormat("H:mm", examStart);
        if (examStartFormat==null) {
            errors.add("invalid exam start");
        }
       
        Date examEndFormat=validDateFormat("H:mm", examEnd);
        if (examEndFormat==null) {
            errors.add("invalid exam end");
        }
        
        // to check if end time before start time
        if ((examEndFormat != null) && (examStartFormat != null) ){
            if ( examEndFormat.before(examStartFormat)){
                errors.add("invalid exam end");
            }            
        }
        
        
        if (errors.isEmpty()){
            BootstrapController.COURSELIST.add(new Course(courseCode, school, title, desc,examDateFormat,examStartFormat,examEndFormat));
        }     
       
        return errors;
    }
    /**
     * Method to check if there is a duplicate course code
     * @param courses ArrayList of courses
     * @param courseCode Course code
     * @return true if duplicate course found else return false
     */    
    public static boolean checkDuplicateCourseCode(ArrayList<Course> courses, String courseCode){
        if(courses!=null){
            for(int i=0; i<courses.size(); i++){
                if(courses.get(i).getCourseCode().equals(courseCode)){
                     return true;
                }
            }
        }        
        return false;   
    }
    
    /**
     * Method to check if course title is valid
     * @param title Course Title
     * @return true if course title is valid and 
     */    
    public static boolean checkTitleIsValid(String title){ 
        if (title.length()== 0 || title.length() > 100 ){
            return false;
        }            
        return true;
    
    }
    /**
     * Method to check if course description is valid
     * @param desc Course description
     * @return true if course description is valid else return false
     */    
     public static boolean checkDescriptionIsValid(String desc){ 
        int len = desc.length();
        boolean thisss = desc.length() > 1000;
        if (desc.length()== 0 || desc.length() > 1000 ){
            return false;
        }        
    
        return true;
    }
    
    /**
     * Method to check if date format is valid 
     * @param format Format to be used
     * @param dateToValdate Date to validate
     * @return Date object if date if valid else goes into exception
     */     
    public static Date validDateFormat(String format, String dateToValdate) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false); // for strict matching 
        Date returnDate=null;
        
        try {
            returnDate=formatter.parse(dateToValdate);
            
            

        } catch (ParseException e) {
            //Handle exception
           
        }
        return returnDate;
    }
}
