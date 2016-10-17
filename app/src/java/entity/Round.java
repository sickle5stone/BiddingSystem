/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Haseena
 */

/** 
 * A Round with roundNumber and status 
 * @author ChenHuiYan and Haseena
 */
public class Round {
    private int roundNumber;
    private String status;

    
     /**
     * Gets the RoundNumber of this Round
     * @return the RoundNumber of this Round
     */
    public int getRoundNumber() {
        return roundNumber;
    }
    
    /**
     * Sets the RoundNumber of this Round
     * @param roundNumber specifies the round number 
     */
    
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
    
    /**
     * Gets the Status of this Round
     * @return the status of this Round
     */
    public String getStatus() {
        return status;
    }

     /**
     * Sets the status of this Round
     * @param status specifies the round status 
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
}