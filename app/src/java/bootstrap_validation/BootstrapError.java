/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bootstrap_validation;

import java.util.ArrayList;

/**
 *
 * @author Cheryl
 */
public class BootstrapError {
    private String file;
    private String line;
    private ArrayList<String> errors;

    public BootstrapError(String file, String line, ArrayList<String> errors) {
        this.file = file;
        this.line = line;
        this.errors = errors;
    }

    public String getFile() {
        return file;
    }

    public String getLine() {
        return line;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
    
    public String toString(){
        return file + ":" + line +" " +errors;
    }
    
}
