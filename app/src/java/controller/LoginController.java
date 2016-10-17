/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.AdminDAO;
import dao.StudentDAO;

/**
 * Controller for all login related functions 
 * @author Cheryl, Aloysius
 */
/**
 * Class that handles all controller operations pertaining to the Login Controller
 */
public class LoginController {

    /**
     * Login method to check the user's username and password against the database
     * @param userId Logging in user's userId
     * @param password User's password
     * @param userType Student or Staff
     * @return true if the login validation is successful, otherwise return false
     */
    public static boolean processLogin(String userId, String password, String userType){
        if (userType.equals("admin")){
            System.out.print(">>>>processes admin login");
            return AdminDAO.loginAdmin(userId, password);
        } else{
            System.out.print(">>>>processes student login");
            return StudentDAO.loginStudent(userId, password);
        } 
    }
}
