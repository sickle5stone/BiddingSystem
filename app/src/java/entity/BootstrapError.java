/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;

/**
 * Class to store all attributes related to bootstrap errors
 * @author Cheryl, Aloysius
 */
public class BootstrapError implements Comparable<BootstrapError>{
    private String file;
    private int line;
    private ArrayList<String> errors;
    /**
     * Create a BootstrapError object with the specified file, line, errors 
     * @param file file
     * @param line line
     * @param errors ArrayList of errors
     */
    public BootstrapError(String file, int line, ArrayList<String> errors) {
        this.file = file;
        this.line = line;
        this.errors = errors;
    }
    /**
     * Get the file name required 
     * @return file name
     */
    public String getFile() {
        return file;
    }
    /**
     * Get the line of the file required
     * @return line
     */
    public int getLine() {
        return line;
    }
    /**
     * Get the ArrayList of errors 
     * @return arrayList of errors
     */
    public ArrayList<String> getErrors() {
        return errors;
    }
    /**
     * This method creates a textual representation of the object 
     * @return String representation of the object
     */
    public String toString(){
        return file + ":" + line +" " +errors;
    }
    
    public int compareTo(BootstrapError err){
        int diff = file.compareTo(err.getFile());
        if (diff == 0){
            return  line - err.getLine();
        }
        return file.compareTo(err.getFile());        
    }
    
}
