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
import java.util.Collections;

/**
 * Class to handle all common validation for JSON
 * @author Aloysius and Cheryl
 */
public class JsonCommonValidation {

    /**
     * Does the common validation for the specified field in the method
     *
     * @param token Validation token
     * @param requiredFields A list of all required fields to check through
     * @param originalString String object passed in to checked
     * @return JSON Object of errors if there are errors, if not returns null;
     */
    public static JsonObject validate(String token, ArrayList<String> requiredFields, String originalString) {

        try {

            JsonObject toReturn = new JsonObject();
            JsonArray errorArray = new JsonArray();
            JsonParser jsonParser = new JsonParser();

            if (originalString == null) {
                toReturn.addProperty("status", "error");
                errorArray.add(new JsonPrimitive("invalid request object"));
                toReturn.add("message", errorArray);
                return toReturn;
            }

            JsonElement isNull = jsonParser.parse(originalString);

            if (isNull.isJsonNull()) {
                toReturn.addProperty("status", "error");
                errorArray.add(new JsonPrimitive("invalid request object"));
                toReturn.add("message", errorArray);
                return toReturn;
            }

            JsonObject value = (JsonObject) jsonParser.parse(originalString);

            for (String field : requiredFields) {
                if (value.get(field) == null) {
                    errorArray.add(new JsonPrimitive("missing " + field));
                } else if (value.get(field).getAsString().trim().isEmpty()) {
                    errorArray.add(new JsonPrimitive("blank " + field));
                }
            }

            if (!checkToken(token).isEmpty()) {
                errorArray.add(new JsonPrimitive((checkToken(token))));
            }

            if (errorArray.size() > 0) {
                toReturn.addProperty("status", "error");
                toReturn.add("message", errorArray);
                return toReturn;
            }
            //return null if successful
            return null;

        } catch (Exception e) {
            JsonArray errorArray = new JsonArray();
            JsonObject toReturn = new JsonObject();
            toReturn.addProperty("status", "error");
            errorArray.add(new JsonPrimitive("invalid request object"));
            toReturn.add("message", errorArray);
            return toReturn;
        }

    }

    /**
     * Check the validity of the token
     *
     * @param token the authentication token needed for json
     * @return return error string based on input token
     */
    public static String checkToken(String token) {

        String toReturn = "";

        if (token == null) {
            return "missing token";
        }
        if (token.trim().isEmpty()) {
            return "blank token";
        }

        try {
            String username = JWTUtility.verify(token, "TwoOStubbornCows");
            if (username == null) {
                return "invalid token";
            }

            if (!username.equals("admin")) {
                return "invalid token";
            }

        } catch (JWTException ex) {
            return "invalid token";
        }
        return toReturn;
    }
    /**
     * Method to sort JSON Array
     * @param o JSON Object to be sorted
     * @return sorted JSON Object
     */
    public static JsonObject sortJsonArray(JsonObject o){
        ArrayList<String> arrErr = new ArrayList<>();
        JsonArray needSort = (JsonArray) o.get("message");
        JsonArray sortedArray = new JsonArray();
        for (JsonElement s : needSort){
            arrErr.add(s.getAsString());
        }
        Collections.sort(arrErr,new StringComparator());
        for (String s : arrErr){
            sortedArray.add(new JsonPrimitive(s));
        }
        o.remove("message");
        o.add("message", sortedArray);
        return o;
    }
    
    /**
     * Method to sort JSON Bid Array
     * @param o JSON object to be sorted
     * @return sorted JSON object
     */
    public static JsonObject sortJsonBidArray(JsonObject o){
        ArrayList<String> arrErr = new ArrayList<>();

        JsonArray needSort = (JsonArray) o.get("bid");
        JsonArray sortedArray = new JsonArray();
        for (JsonElement s : needSort){
            arrErr.add(s.getAsString());
        }
        Collections.sort(arrErr);
        for (String s : arrErr){
            sortedArray.add(new JsonPrimitive(s));
        }
        o.remove("bid");
        o.add("bid", sortedArray);

       return o;

    }
    
    /**
     * Remove colon from time to match proper JSON output
     * @param time the to-be formatted time parameter
     * @return the re-formatted time
     */
    public static String formateTime(String time){
        int lastColon =time.lastIndexOf(":");
        time=time.substring(0,lastColon); // remove the part behind the last colon 
        time=time.replace(":","");
        if( time.length()==4 &&  time.charAt(0)=='0'){
             time= time.substring(1);
        }
        return time;
    }
       
}
