/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.BidController.getClearingPriceForRoundTwo;
import dao.BidDAO;
import dao.SectionStudentDAO;
import entity.Bid;
import entity.Section;
import java.util.ArrayList;

/**
 * Controller for all sectionstudent related functions
 *
 * @author Aloysius, Cheryl
 */
/**
 * Class that handles all controller operations pertaining to the SectionStudent
 * Controller
 */
public class SectionStudentController {

    /**
     * Method to retrieve all sections that the student has bidded for using
     * studentId
     *
     * @param studentId Student ID
     * @return ArrayList of bids by the student else return null;
     */
    public static ArrayList<Bid> getSectionsByStudentId(String studentId) {
        ArrayList<Bid> results = null;
        results = SectionStudentDAO.getSectionsByUserId(studentId);
        return results;
    }

    /**
     * deleteSectionFromStudent method to allow student to drop enrolled modules
     * using studentId, courseCode and sectionId
     *
     * @param studentId Student ID
     * @param courseCode Course code
     * @param sectionId Section code
     * @return true if module is successfully dropped else return false
     */
    public static boolean deleteSectionFromStudent(String studentId, String courseCode, String sectionId) {

        Bid section = SectionStudentDAO.getSectionStudent(sectionId, courseCode, studentId);
        if (section != null) {

            boolean success = SectionStudentDAO.deleteSection(courseCode, sectionId, studentId);
            if (success) {
                //gets clearingPrice and update bid status to success if equal and above clearing price
                double clearingPrice = getClearingPriceForRoundTwo(courseCode, sectionId);
                BidDAO.roundBidStatusUpdate(courseCode, sectionId, clearingPrice);
                //gets the refund amount and update e-dollars
                double refundAmount = section.getBidAmount();
                StudentController.updateEdollar(studentId, refundAmount);
                return success;
            }
        }
        return false;
    }

    /**
     * insertSectionStudent method to insert successful bids into section
     * student table using coureCode,sectionId,userId and bidAmount
     *
     * @param courseCode Course code
     * @param sectionId Section Id
     * @param userId Student ID
     * @param bidAmount Bid amount
     */
    public static void insertSectionStudent(String courseCode, String sectionId, String userId, double bidAmount) {
        SectionStudentDAO.insertSuccessfulBids(courseCode, sectionId, userId, bidAmount);
    }

    /**
     * getNumEnrolledinSection method to retrieve the number of students
     * enrolled in a particular section of a course
     *
     * @param courseCode Course code
     * @param sectionId Section Id
     * @return number of students enrolled in the particular section of a course
     */
    public static int getNumEnrolledInSection(String courseCode, String sectionId) {
        return SectionStudentDAO.getNumEnrolledInSection(courseCode, sectionId);
    }

}
