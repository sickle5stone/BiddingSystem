/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author ChenHuiYan and Regan 
 */

/** 
 * A Bid with userId, courseCode, sectionCode, bidAmount and bidStatus
 * @author ChenHuiYan and Haseena
 */

public class Bid {
    private String userId;  
    private String courseCode;
    private String sectionCode;  
    private double bidAmount;
    private String bidStatus;
    
/**
  * Create a Bid object with the specified userId, courseCode, sectionCode, bidAmount and bidStatus
  * 
  * @param userId  bid's userId
  * @param courseCode bid's courseCode
  * @param sectionCode bid's sectionCode
  * @param bidAmount  bid's amount
  * @param bidStatus  bid's status
  * 
  */
    public Bid(String userId, String courseCode, String sectionCode, double bidAmount, String bidStatus) {
        this.userId = userId;
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.bidAmount = bidAmount;
        this.bidStatus = bidStatus;
    }
    
    /**
     * Gets the userId of this bid
     * @return the userId of this bid
     */

    public String getUserId() {
        return userId;
    }
    /**
     * Gets the courseCode of this bid 
     * @return the courseCode of this bid 
     */

    public String getCourseCode() {
        return courseCode;
    }
    /**
     * Gets the sectionCode of this bid 
     * @return the sectionCode of this bid
     */

    public String getSectionCode() {
        return sectionCode;
    }
    /**
     * Gets the bid amount of this bid 
     * @return the bid amount of this bid 
     */

    public double getBidAmount() {
        return bidAmount;
    }
    
    
    /**
     * Gets the bid status of this bid 
     * @return the bid status of this bid 
     */

    public String getBidStatus() {
        return bidStatus;
    }
    
    /**
     * Sets the id of this bid
     * @param userId specifies the bid's userId   
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * Sets the courseCode of this bid
     * @param courseCode specifies the bid's courseCode
     */

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    /**
     * Sets the sectionCode of this bid
     * @param sectionCode specifies the bid's sectionCode
     */
    

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }
    
    /**
     * Sets the bidAmount of this bid
     * @param bidAmount specifies the bid's bidAmount 
     */
    
    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
    
    
    
    /**
     * Sets the bidStatus of this bid
     * @param bidStatus specifies the bid's bidStatus 
     */

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

   
}
