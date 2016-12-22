/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Class that handles all database operations pertaining to the Bid Controller
 */
/**
 * controller is a group of controller classes which operates on the logic of app
 */
package controller;

import dao.CourseCompleteDAO;
import dao.StudentDAO;
import entity.Student;
import java.util.ArrayList;

/**
 *
 * @author reganseah and cheryl
 */
/**
 * Class that handles all controller operations pertaining to the student Controller
 */
public class StudentController {
    
    //retrieve student's edollar balance based on userId

    /**
     * Method to retrieve e-dollar balance of student
     * @param userId userId of student
     * @return e-dollar balance
     */
    public static double getEdollarBalance(String userId){
        
        double edollarBal = -1;
        Student s = StudentDAO.getStudentById(userId);
        if(s != null){
            edollarBal = s.geteDollar();
        }
        return edollarBal;
    }
    
    //retrieve student's school based on userId
    /**
     * Method to retrieve school of student by userId
     * @param userId userId of student
     * @return student's school
     */
    public static String getSchoolById(String userId){
        String schoolName = "";
        
        Student s = StudentDAO.getStudentById(userId);
        if(s != null){
            schoolName = s.getSchool();
        }
        return schoolName;
    }
    
    //retrieve student's name based on userId
    /**
     * Method to retrieve name of student by user id
     * @param userId userId of student
     * @return student's name
     */
    public static String getNameById(String userId){
        String sName = "";
        
        Student s = StudentDAO.getStudentById(userId);
        if(s != null){
            sName = s.getName();
        }
        return sName;
    }
    
    //update student's edollar amount with the amount parameter being the difference from original amount, returns a false value if update operation is unsuccesful
    /**
     * Method to student's edollar amount
     * @param userId userId of student
     * @param amt amount to be updated
     * @return true if updated otherwise return false
     */
    public static boolean updateEdollar(String userId, double amt){
        boolean updateStatus = false;
        
        Student s = StudentDAO.getStudentById(userId);
        
        if(s != null){
            double newAmount = s.geteDollar()+ amt;
            double updated = StudentDAO.updateEdollar(userId,newAmount);
            if(updated != -1){
                updateStatus = true;
            }
        }
        return updateStatus;
    }
    
    //retrieve Student object based on userId
    /**
     * Method to retrieve Student object based on userId
     * @param userId userId of student
     * @return Student object if userId is valid else return false
     */
    public static Student retrieveStudent(String userId){
        
        Student s = StudentDAO.getStudentById(userId);

        return s;
    }
    
    //check if courseCode has  been completed by student
    /**
     * Method to check if student has completed a particular course
     * @param userId userId of student
     * @param courseCode courseCode to be checked against
     * @return true if course has been completed before else return false
     */
    public static boolean checkCourseCompleted(String userId, String courseCode){
       
        ArrayList<String> courseCompletedList = CourseCompleteDAO.getListOfCourseCompleted(userId);
            
        for(String courseId : courseCompletedList){
            if(courseCode.equals(courseId)){
                return true;
            }
        }    
        return false;
    }
    
    /**
     * Method to all courses completed by a student
     * @param userId userId of student
     * @return Arraylist of courses that the student has completed before
     */
    public static ArrayList<String> getCourseCompleted(String userId){
        
        ArrayList<String> courseCompletedList = null;
        courseCompletedList = CourseCompleteDAO.getListOfCourseCompleted(userId);
        return courseCompletedList;
        
    }
}
