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
    public static double getEdollarBalance(String userId){
        
        double edollarBal = -1;
        Student s = StudentDAO.getStudentById(userId);
        if(s != null){
            edollarBal = s.geteDollar();
        }
        return edollarBal;
    }
    
    //retrieve student's school based on userId
    public static String getSchoolById(String userId){
        String schoolName = "";
        
        Student s = StudentDAO.getStudentById(userId);
        if(s != null){
            schoolName = s.getSchool();
        }
        return schoolName;
    }
    
    //retrieve student's name based on userId
    public static String getNameById(String userId){
        String sName = "";
        
        Student s = StudentDAO.getStudentById(userId);
        if(s != null){
            sName = s.getName();
        }
        return sName;
    }
    
    //update student's edollar amount with the amount parameter being the difference from original amount, returns a false value if update operation is unsuccesful
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
    public static Student retrieveStudent(String userId){
        
        Student s = StudentDAO.getStudentById(userId);

        return s;
    }
    
    //check if courseCode has  been completed by student
    public static boolean checkCourseCompleted(String userId, String courseCode){
       
        ArrayList<String> courseCompletedList = CourseCompleteDAO.getListOfCourseCompleted(userId);
            
        for(String courseId : courseCompletedList){
            if(courseCode.equals(courseId)){
                return true;
            }
        }    
        return false;
    }
    
    //
    public static ArrayList<String> getCourseCompleted(String userId){
        
        ArrayList<String> courseCompletedList = null;
        courseCompletedList = CourseCompleteDAO.getListOfCourseCompleted(userId);
        return courseCompletedList;
        
    }
}
