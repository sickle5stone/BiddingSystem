/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bootstrap_validation;

import controller.BootstrapController;
import entity.Student;

/**
 * Class to validate all student related bootstrap data
 * @author Cheryl and Huiyan
 */
public class ValidateStudent {    
    /**
     * checkStudent method to check for and consolidate all student related bootstrap errors
     * @param row Array of data for every row in student.csv
     * @return String of errors
     */
    public static String checkStudent (String [] row){
        boolean isValid = true;
        String errors = "";
        String userId = row[0];
        String password = row[1];
        String name = row[2];
        String school = row[3];
        String eDollar = row[4];
        
        if (school.length() > 5){
            errors += "invalid school, ";
        }
        
        if (!checkUserIdIsValid(userId)){
            errors += "invalid userid, ";             
        }
        if (checkDuplicateUserId(userId)){
            errors += "duplicate userid, ";
        }
        
        if (!checkPasswordIsValid(password)){
            errors += "invalid password, ";
        }
        
        if (!checkNameIsValid(name)){
            errors += "invalid name, ";
        }
        
        if (!checkEDollarIsValid(eDollar)){
            errors += "invalid e-dollar, ";
        }
        
        if (errors.isEmpty()){
            double dblEDollar = Double.parseDouble(eDollar);
            
            // check if number is more than what the database accepts
            if (dblEDollar > 9999.99){
                return errors += "invalid e-dollar";
            }
            
            BootstrapController.STUDENTLIST.add(new Student(userId, password, name, school, dblEDollar));
        }else{
            errors= errors.substring(0, errors.length()-2 );
        }        
        return errors;
    }
    /**
     * checkUserIdIsValid method to check if user id is valid
     * @param userId Student ID
     * @return true if student ID  is valid else return false
     */
    public static boolean checkUserIdIsValid(String userId){
               
        if (userId.length()== 0 || userId.length() > 128 ){
            return false;
        } 
        return true;
    }
    /**
     * checkDuplicateUserId method to check if there is duplicate user id 
     * @param userId Student id
     * @return true if there is duplicate user id else return false
     */
    public static boolean checkDuplicateUserId(String userId){
               
        for (Student stu: BootstrapController.STUDENTLIST){
            if (stu.getUserId().equals(userId)){
               return true;
            }
        }    
        return false;
    }
    /**
     * checkPasswordIsValid method to check password is valid
     * @param password Password
     * @return true if password is valid else return false
     */
    public static boolean checkPasswordIsValid(String password){
        if (password.length() == 0 || password.length() > 128){
            return false;
        }
        return true;
    }
    
    /**
     * checkNameIsValid method to check if name is valid
     * @param name Student name
     * @return true if name is valid else return false
     */          
    public static boolean checkNameIsValid(String name){
        if(name.length() == 0 || name.length() > 100){
            return false;
        }
        return true;
    }
    /**
     * checkEDollarIsValid method to check if eDollar amount is valid
     * @param eDollar eDollar
     * @return true if eDollar amount passes relevant validations else return false
     */
    public static boolean checkEDollarIsValid(String eDollar){
        int countDecimalSymbols = 0;
        int countDigits = 0;
        int posOfDecimal = eDollar.indexOf('.');
        
        if (posOfDecimal != -1){
            // concates from the '.' to the end. eg. 10.052 -> .052
            String checkDecimal = eDollar.substring(posOfDecimal);
            if (checkDecimal.length() > 3){
                return false;
            }
        }
        for (int i = 0; i < eDollar.length(); i++){
            char ch = eDollar.charAt(i);
            // assumption that .0 is a number 
            // don't need to check is less than 0.0 because '-' sign will be handled here
            if (!((ch >= '0' && ch <= '9'))) {
                if (ch == '.'){
                    countDecimalSymbols++;
                }else{
                    return false;                
                }
            }else{
                countDigits++;
            }            
        }
        if (countDecimalSymbols > 1 || countDigits==0 ||(countDecimalSymbols == 1 && eDollar.length() == 1)){
            return false;
        }
        return true;
    }
}
