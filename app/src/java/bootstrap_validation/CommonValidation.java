package bootstrap_validation;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Class to validate all bootstrap data for common errors
 * @author ChenHuiYan, Haseena
 */
public class CommonValidation {
    /**
     * CommonValidation method to check for whitespace and empty rows and columns
     * @param row Array of data for every row 
     * @param header Header of column of data
     * @return String of errors
     */    
    public static ArrayList<String> CommonValidation(String[] row, String[] header){
        ArrayList<String> errors = new ArrayList<>();
        for(int i=0; i<row.length; i++){
            //To handle the extra spaces in the field
            String field=row[i];
            row[i] = row[i].trim();
            if(field.isEmpty()){
                errors.add("blank "+header[i]);                
            }           
        }
        
        return errors;
        
    }
}
