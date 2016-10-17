/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.RoundController.getRound;
import static controller.RoundController.getStatus;
import dao.BidDAO;
import dao.RoundDAO;
import dao.StudentDAO;
import entity.Bid;
import entity.Section;
import java.util.ArrayList;

/**
 * Controller for all round related functions 
 * @author ChenHuiYan and Regan 
 */
/**
 * Class that handles all controller operations pertaining to the Round Controller
 */
public class RoundController {
    
    /**
     * setRoundAndStatus method to set the round number and status in the database to the specified arguments 
     * @param round round number
     * @param status status of round
     */
    public static void setRoundAndStatus(int round,String status){
        RoundDAO.setRoundAndStatus(round, status);
    }
     
    /**
     * getRound method to get round number from the database by calling roundDAO method
     * @return round number 
     */
    public static int getRound(){
        
        return RoundDAO.getRoundNum(); 
        
    }
    
    /**
     * getStatus method to get round status from the database by calling roundDAO method
     * @return round status 
     */
    public static String getStatus(){
        return  RoundDAO.getStatus();
    }
    /**
     * Start the End Round.
     */
    public static void startEndRound(){
        String roundStatus = getStatus();
        
        if(roundStatus.equals("inactive")){
            startRound();
        }else{
            stopRound();
        }
    }
    
    /**
     * Start the round 
     * @return the true value, if round is start. Otherwise, return false
     */
    public static boolean startRound(){
        String roundStatus=getStatus();
        int roundNum = getRound();
        if(roundStatus.equals("inactive")&& roundNum < 2){  // start the round 
            BidController.clearBidTable();
            RoundDAO.increaseRoundNum(); 
        }else{
            return false;
        }
        
        
        RoundDAO.invertStatus(); 
        return true;
     }
    
    /**
     * Stops the round
     * @return true if the round is started, otherwise returns false
     */
    public static boolean stopRound(){
        String roundStatus = getStatus();
        
        if(roundStatus.equals("inactive")){
            return false;
        } 
        
        int roundNum = getRound();
        
        if(roundNum == 1){
            clearRoundOne();
            ArrayList<Bid> successBids = BidController.getSuccessfulBids();
            for(Bid b: successBids){
                String userId = b.getUserId();
                String courseCode = b.getCourseCode();
                String sectionId = b.getSectionCode();
                double bidAmt = b.getBidAmount();
                
                SectionStudentController.insertSectionStudent(courseCode,sectionId,userId,bidAmt);
            }
            
        }
        
        if(roundNum == 2){
            clearRoundTwo();
            ArrayList<Bid> successBids = BidController.getSuccessfulBids();
            for(Bid b: successBids){
                String userId = b.getUserId();
                String courseCode = b.getCourseCode();
                String sectionId = b.getSectionCode();
                double bidAmt = b.getBidAmount();
                
                SectionStudentController.insertSectionStudent(courseCode,sectionId,userId,bidAmt);
            }
            
        }
        RoundDAO.invertStatus();
        return true;
    }
    
    
    /**
     * clearRoundOne method to proceed clear round one logic 
     */
    public static void clearRoundOne(){
        // get all sections in the bid table
        ArrayList<Section> sectionsInBidList = BidController.getAllSectionsInBidTable();
        // loop all sections in the list
        for (Section s : sectionsInBidList){
            String courseCode = s.getCourseCode();
            String sectionCode = s.getSection();
            Section sectionToCheck = CourseSectionController.getSection(courseCode, sectionCode);
            // get the vacancy of the specific section
            int sectionVacancy = sectionToCheck.getClassSize();
            int bidsForSection = BidController.getNumBidsForSection(courseCode, sectionCode);
            
            if (sectionVacancy > bidsForSection){
                // if there are more vacancies accept all bids above $10 for round 1 logic
                BidController.roundBidStatusUpdate(courseCode, sectionCode, 10);
            } else {
                
                // get the clearing price of the section
                double clearingPrice =BidController.getClearingPrice(courseCode, sectionCode, sectionVacancy);            
                // get number of bids at the clearing price
                int numBid =BidController.getNumBidsAtClearingPrice(courseCode, sectionCode, sectionVacancy);  

                // if more than one bid at qualifying price
                if (numBid > 1){
                    // find next higher bid amount
                    double nextBidAmount = BidController.getNextHigherBidAmount(courseCode, sectionCode, clearingPrice);
                    // clear 
                    BidController.roundBidStatusUpdate(courseCode, sectionCode, nextBidAmount);
                } else{
                    BidController.roundBidStatusUpdate(courseCode, sectionCode, clearingPrice);
                }        
            }
        }
        // refund Edollar for all failed bids
        ArrayList<Bid> failedBids = BidController.getBidsThatFailed();
        
        for (Bid bid : failedBids){
            String user = bid.getUserId();
            double amount = bid.getBidAmount();
            StudentDAO.updateEdollar(user, amount);
        }
        
       
    }
    
     /**
     * clearRoundTwo method to proceed clear round two logic 
     */
    public static void clearRoundTwo(){
        ArrayList<Bid> failedBids = BidController.getBidsThatFailed();
        for (Bid bid : failedBids){
            String user = bid.getUserId();
            double amount = bid.getBidAmount();
            StudentDAO.updateEdollar(user, amount);
        }
    }
    
    
   
    
}
