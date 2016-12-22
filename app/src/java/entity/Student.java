package entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** 
 * A Student with userId,password,name,school,eDollar.
 * @author ChenHuiYan and Haseena
 */

public class Student {
    private String userId;
    private String password;
    private String name;
    private String school;
    private double eDollar;

  /**
  * Create a Student object with the specified userId,password,name,school,eDollar.
  * 
  * @param userId (required) userId of student  
  * @param password (required) password of student
  * @param name (required) name of student
  * @param school (required) student's school 
  * @param eDollar (required) student's e-dollar balance
  * 
  */
    public Student(String userId, String password, String name, String school, double eDollar) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.school = school;
        this.eDollar = eDollar;
    }
    
    /**
     * Gets the userId of this student
     * @return the userId of this student
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Gets the password of this student
     * @return the password of this student
     */
    public String getPassword() {
        return password;
    }
    /**
     * Gets the name of this student
     * @return the name of this student
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the school of this student
     * @return the school of this student
     */
    public String getSchool() {
        return school;
    }
    /**
     * Gets the e-dollar of this student
     * @return the e-dollar of this student
     */
    public double geteDollar() {
        return eDollar;
    }
    /**
     * Sets the userId of this student
     * @param userId specifies the student's userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Sets the password of this student
     * @param password specifies the student's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Sets the name of this student
     * @param name specifies the student's name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the school of this student
     * @param school specifies the student's school
     */
    public void setSchool(String school) {
        this.school = school;
    }
    /**
     * Sets the e-dollar of this student
     * @param eDollar specifies the student's e-dollar
     */
    public void seteDollar(double eDollar) {
        this.eDollar = eDollar;
    }

    
    
    
    
}
