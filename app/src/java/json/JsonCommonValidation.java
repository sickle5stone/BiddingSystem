/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import is203.JWTException;
import is203.JWTUtility;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aloysius & Cheryl
 */
public class JsonCommonValidation {
    
    /**
     * Does the common validation for the specified field in the method
     * @param token Validation token 
     * @param requiredFields A list of all required fields to check through
     * @param value The object containing all the values of the fields
     * @return JSON Object of errors if there are errors, if not returns null;
     */
    public static JsonObject validate(String token, ArrayList<String> requiredFields, String originalString){
        
        try{
        
        JsonObject toReturn = new JsonObject();
        JsonArray errorArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();
        
        if (originalString == null){
            toReturn.addProperty("status", "error");
            toReturn.addProperty("message", "invalid request object");
            return toReturn;
        }
        
        JsonElement isNull = jsonParser.parse(originalString);
        
        if (isNull.isJsonNull()){
            toReturn.addProperty("status", "error");
            toReturn.addProperty("message", "invalid request object");
            return toReturn;
        }
        
        JsonObject value = (JsonObject) jsonParser.parse(originalString);
        
        for (String field : requiredFields){
            if (value.get(field) == null){
                errorArray.add(new JsonPrimitive(field + " is missing"));
            }else{
                if(value.get(field).getAsString().trim().isEmpty()){
                    errorArray.add(new JsonPrimitive(field + " is blank"));
                }
            }
        }
        
        if (!checkToken(token).isEmpty()){
            errorArray.add(new JsonPrimitive((checkToken(token))));
        }
        
        if(errorArray.size() > 0){
            toReturn.addProperty("status", "error");
            toReturn.add("message",errorArray);
            return toReturn;
        }
        
        //return null if successful
        return null;
        
        }catch (Exception e){
            JsonObject toReturn = new JsonObject();
            toReturn.addProperty("status", "error");
            toReturn.addProperty("message", "invalid request object");
            return toReturn;
        }
        
    }
    
    /**
     * Check the validity of the token
     * @param token
     * @return return error string based on input token
     */
    public static String checkToken(String token){
       
       String toReturn = "";
       
       if (token == null){
           return "missing token";
       }
       if (token.trim().isEmpty()){
           return "blank token";
       }
              
       try {
           String username = JWTUtility.verify(token, "TwoOStubbornCows");
           if (username == null){
               return "invalid token";
           }
           
           if (!username.equals("admin")){
               return "invalid token";
           }
       
       } catch (JWTException ex) {
           return "invalid token";
       }
       
       return toReturn;
   }
    
}
