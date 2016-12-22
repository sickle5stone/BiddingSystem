/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.BidDAO;
import dao.CourseDAO;
import dao.SectionDAO;
import dao.SectionMinimumPriceDAO;
import dao.SectionStudentDAO;
import dao.StudentDAO;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Class that handles all controller operations pertaining to the Bid Controller
 * @author ChenHuiYan and Haseena
 */
public class BidController {

    /**
     * Adds a bid object into an arraylist of Bid with userId, amt, sctionCode
     * and courseCode after going through different validation methods which
     * will then return an arrayList of errors.
     *
     * @param userId the student's userId
     * @param amt the bid's amount
     * @param sectionCode the bid's sectionCode
     * @param courseCode the bid's courseCode
     * @return returns an arrayList of
     */
    public static ArrayList<String> addBid(String userId, double amt, String sectionCode, String courseCode) {
        ArrayList<String> errors = new ArrayList<>();
        int round = RoundController.getRound();

        // if Round 1, check if course bidded belongs to student's school
        if (round == 1 && !checkIfRoundBidIsOwnSchool(courseCode, userId)) {
            errors.add("Not own school course");
        }

        if (!checkIfAmountBalanceIsSufficient(userId, amt)) {
            errors.add("Insufficient EDollar Balance");
        }

        if (checkIfBiddedForCourse(courseCode, userId)) {
            errors.add("A bid for the course already exists");
        }

        if (checkIfSectionLimitReached(userId, courseCode, sectionCode)) {
            errors.add("Reached bid limit of 5 sections");
        }

        if (checkIfExamsClashes(courseCode, userId)) {
            errors.add("Exam date clashes with another bid");
        }

        if (checkIfClassTimeTableClashes(courseCode, sectionCode, userId, null)) {
            errors.add("Class timetable clashed with another bid");
        }

        if (!checkIfPreRequisiteCompleted(userId, courseCode)) {
            errors.add("Prerequisites not completed");
        }

        if (checkIfCourseAlreadyCompleted(userId, courseCode)) {
            errors.add("Course already completed");
        }

        if (!checkMinBidAmount(courseCode, sectionCode, amt)) {
            errors.add("Bid amount less than minimum bid");
        }
        
        if (checkIfCourseEnrolled(userId,courseCode)){
            errors.add("Already enrolled in course");
        }

        if (errors.isEmpty()) {
            // add the bid to bid table, decrease student edollar amount
            BidDAO.addBid(courseCode, sectionCode, userId, amt);
            StudentController.updateEdollar(userId, -amt);

            // update the status if bids if round 2
            if (round == 2) {
                double clearingPrice = getClearingPriceForRoundTwo(courseCode, sectionCode);
                BidDAO.roundBidStatusUpdate(courseCode, sectionCode, clearingPrice);
                double minBid = SectionMinimumPriceDAO.getMinBidAmtByCourseSection(courseCode, sectionCode);
                
                Section section = CourseSectionController.getSection(courseCode, sectionCode);
                int classSize = section.getClassSize();
                int numEnrolled = SectionStudentController.getNumEnrolledInSection(courseCode, sectionCode);
                int currentBids = BidDAO.getNumBidsForSection(courseCode, sectionCode);
                int vacancy = classSize - numEnrolled;
                int unfilledVacancies = classSize - numEnrolled - currentBids;
                //if the clearingprice+1 is more than minBid, sets the minimumbid to clearingPrice+1
                //unfilled less than or equals zero, means that there are same num or more bids than there are vacancy
                
                // unfilledVacancies less than 0: means more bids than slots: update min bid
                // clearing price + 1 bigger than min bid
                // 
                double nthBid = getClearingPrice(courseCode, sectionCode, vacancy);
                
                if (vacancy == 0 ){
                    BidDAO.setBidStatus(courseCode,sectionCode,userId,"fail");
                    //SectionMinimumPriceDAO.setMinBid(courseCode, sectionCode, clearingPrice - 100 );
                    } else if (unfilledVacancies <= 0 && nthBid + 1 > minBid && vacancy != 0) { 
                    SectionMinimumPriceDAO.setMinBid(courseCode, sectionCode, clearingPrice + 1);
                }    
            } 
        }

        return errors;
    }

    /**
     * Used in round 1 bidding which checks if the round bidded is offered by
     * the user's school using the courseCode and userId
     *
     * @param courseCode bid's courseCode
     * @param userId student's userId
     * @return true if the course bidded belongs to the user's own school,
     * otherwise returns false
     */
    public static boolean checkIfRoundBidIsOwnSchool(String courseCode, String userId) {
        //Retrieve the user's school
        //boolean biddingIsFromOwnSchool=false;
        Student stu = StudentDAO.getStudentById(userId);
        String userSchool = stu.getSchool();
        //retrieve the bid's school

        Course course = CourseDAO.getCourseByCourseCode(courseCode);
        String courseSchool = course.getSchoolTitle();

        //check if it is the same.
        if (userSchool.equals(courseSchool)) {
            return true;
        }
        return false;
    }

    /**
     * Checks to see if the student has sufficient e-dollars to bid using the
     * userId and bidAmount
     *
     * @param userId the student's userId
     * @param bidAmount the bid's amount
     * @return true if the balance is more than the bidAmount for bidding,
     * otherwise return false.
     */
    public static boolean checkIfAmountBalanceIsSufficient(String userId, double bidAmount) {
        //retrieve the student's balance and see if the value is more than that
        // boolean amountInsufficient=true;
        Student s = StudentDAO.getStudentById(userId);
        double balance = s.geteDollar();
        if (bidAmount > balance) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the student has already bidded for the sections in the course
     *
     * @param courseCode the bid's courseCode
     * @param userId student's userId
     * @return true if the student has bidded for the course, otherwise returns
     * false
     */
    public static boolean checkIfBiddedForCourse(String courseCode, String userId) {
        //Get an arraylist of all bids for the courseCode
        Bid b = BidDAO.getBidForCourse(courseCode, userId);
        return (b != null);
    }

    /**
     * Checks if the student has already bidded for a max of 5sections using
     * userId,courseCode and sectionCode
     *
     * @param userId student's userId
     * @param courseCode courseCode of section
     * @param sectionCode the sectionCode
     * @return true if the total number of bids is less than 5, otherwise
     * returns false
     */
    public static boolean checkIfSectionLimitReached(String userId, String courseCode, String sectionCode) {
        ArrayList<Bid> bids = BidDAO.getBidsByUserId(userId);
        int totalBids = 0;

        int currentBids = bids.size();
        // to call studentSection controller
        
        int enrolledBids = 0;
        ArrayList<Bid> sectionsEnrolled = SectionStudentController.getSectionsByStudentId(userId);
        if (sectionsEnrolled != null){
            enrolledBids = sectionsEnrolled.size();
        }
        
        totalBids = currentBids + enrolledBids;

        // return true if total Bids is less than 5
        return totalBids >= 5;
    }

    /**
     * Checks if the exam timing of intended bid clashes with the list of
     * current bids using the courseCode and userId
     *
     * @param courseCode courseCode of section
     * @param userId student's userId
     * @return true if the exams clashes, otherwise returns false
     */
    public static boolean checkIfExamsClashes(String courseCode, String userId) {
        // get the exam details of the course student is currently bidding for
        Course c = CourseDAO.getCourseByCourseCode(courseCode);
        Date examDate = c.getExamDate();
        Date examStart = c.getExamStart();
        Date examEnd = c.getExamEnd();

        // get list of bids by student 
        ArrayList<Bid> bids = BidDAO.getBidsByUserId(userId);

        for (Bid eachBid : bids) {
            String eachCourseCode = eachBid.getCourseCode();
            Course eachCourse = CourseDAO.getCourseByCourseCode(eachCourseCode);

            Date eachExamDate = eachCourse.getExamDate();
            Date eachExamStart = eachCourse.getExamStart();
            Date eachExamEnd = eachCourse.getExamEnd();

            if (eachExamDate.equals(examDate)) {
                if (!(eachExamStart.after(examEnd) || examStart.after(eachExamEnd))) {
                    // return true if there is a clash
                    return true;
                }
            }
        }
        // get bid type sections for enrolled
        ArrayList<Bid> sectionEnrolled = SectionStudentDAO.getSectionsByUserId(userId); 

        for (Bid eachBid : sectionEnrolled) {
            String eachCourseCode = eachBid.getCourseCode();
            Course eachCourse = CourseDAO.getCourseByCourseCode(eachCourseCode);

            Date eachExamDate = eachCourse.getExamDate();
            Date eachExamStart = eachCourse.getExamStart();
            Date eachExamEnd = eachCourse.getExamEnd();

            if (eachExamDate.equals(examDate)) {
                if (!(eachExamStart.after(examEnd) || examStart.after(eachExamEnd))) {
                    // return true if there is a clash
                    return true;
                }
            }
        }
        // return false if no clash
        return false;
    }

    /**
     * Checks if the intended section to bid clashes with the classTimeTable of
     * all current bids using courseCode,StringId and userId
     *
     * @param courseCode courseCode of section
     * @param sectionId the sectionId of section
     * @param userId the student's userid
     * @param existingBid the existing bid if this is an update of bid
     * @return true if the classTimeTable clashes with the ones present in the
     * current bid list
     */
    public static boolean checkIfClassTimeTableClashes(String courseCode, String sectionId, String userId, Bid existingBid) {
        Section s = SectionDAO.getSpecificSection(courseCode, sectionId);
        int sectionDay = s.getDayOfWeek();
        //Date sectionStart=s.getStartTime();
        //Date sectionEnd=s.getEndTime();
        Date sectionStart = s.getStartTime();
        // java.sql.Time sqlSectionStart = new java.sql.Time(sectionStart.getTime()); 

        Date sectionEnd = s.getEndTime();
        
        ArrayList<Bid> bids = BidDAO.getBidsByUserId(userId);

        if (existingBid != null){
            String crs = existingBid.getCourseCode();
            for (int i = 0; i < bids.size() ; i++){
                Bid b = bids.get(i);
                if (b.getCourseCode().equals(crs)){
                    bids.remove(i);
                    break;
                }
            }
            
        }
        
        for (Bid eachBid : bids) {
            String eachSectionId = eachBid.getSectionCode();
            String eachCourseCode = eachBid.getCourseCode();

            Section eachSe = SectionDAO.getSpecificSection(eachCourseCode, eachSectionId);
            int eachSectionDay = eachSe.getDayOfWeek();
            Date eachSectionStart = eachSe.getStartTime();
            //java.sql.Time sqlStartTime = new java.sql.Time(eachSectionStart.getTime());

            Date eachSectionEnd = eachSe.getEndTime();
            //java.sql.Time sqlEndTime = new java.sql.Time(eachSectionEnd.getTime());

            if (eachSectionDay == sectionDay) {
                if (!(sectionStart.after(eachSectionEnd) || eachSectionStart.after(sectionEnd))) {
                    // returns true if time table clash
                    return true;
                }
            }
        }
        
        ArrayList<Bid> enrolledBids = SectionStudentDAO.getSectionsByUserId(userId);

        for (Bid eachBid : enrolledBids) {
            String eachSectionId = eachBid.getSectionCode();
            String eachCourseCode = eachBid.getCourseCode();

            Section eachSe = SectionDAO.getSpecificSection(eachCourseCode, eachSectionId);
            int eachSectionDay = eachSe.getDayOfWeek();
            Date eachSectionStart = eachSe.getStartTime();
            //java.sql.Time sqlStartTime = new java.sql.Time(eachSectionStart.getTime());

            Date eachSectionEnd = eachSe.getEndTime();
            //java.sql.Time sqlEndTime = new java.sql.Time(eachSectionEnd.getTime());

            if (eachSectionDay == sectionDay) {
                if (!(sectionStart.after(eachSectionEnd) || eachSectionStart.after(sectionEnd))) {
                    // returns true if time table clash
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Checks to see if the prerequisite is completed using userId and
     * courseCode
     *
     * @param userId student's userId
     * @param courseCode courseCode of section
     * @return true if the student has completed the preRequisite course for the
     * intended bid
     */
    public static boolean checkIfPreRequisiteCompleted(String userId, String courseCode) {
        ArrayList<Course> completedList = CourseDAO.getCompletedCoursesbyStudent(userId);
        ArrayList<Course> prereqList = CourseDAO.getPrereqListbyCourseCode(courseCode);

        // return false if number of completed course is less the number of prereq
        if (completedList.size() < prereqList.size()) {
            return false;
        }

        for (Course eachPreReq : prereqList) {
            boolean checkIfEachpreReqFulfilled = false;
            for (Course completedCourse : completedList) {

                if (eachPreReq.getCourseCode().equals(completedCourse.getCourseCode())) {
                    //If it is true, it continues looping to the next pre-req after setting it to true
                    checkIfEachpreReqFulfilled = true;
                    break;
                }
                //After checking one pre-req to see if it is fulfilled, return false

            }

            if (checkIfEachpreReqFulfilled == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if the student has completed the course using userId and
     * courseCode
     *
     * @param userId student's userId
     * @param courseCode courseCode of section
     * @return true if the student has completed the course, otherwise returns
     * false
     */
    public static boolean checkIfCourseAlreadyCompleted(String userId, String courseCode) {
        ArrayList<Course> completedCourse = CourseDAO.getCompletedCoursesbyStudent(userId);
        for (Course course : completedCourse) {
            String eachCourse = course.getCourseCode();
            if (eachCourse.equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if the amount bidded is more than or equal to the minimum
     * amount using the bid's courseCode, sectionCode and amount
     *
     * @param courseCode courseCode of new bid
     * @param sectionCode sectionCode of new bid
     * @param amount amount bidded
     * @return true if amount is more than the minimum bid for each round,
     * otherwise return false
     */
    public static boolean checkMinBidAmount(String courseCode, String sectionCode, double amount) {
        double minBid = BidController.getMinimumBid(courseCode, sectionCode);
        return amount >= minBid;

    }

    /**
     * Gets the minimum bid for round two using courseCode and sectionCode
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @return the minimum bid amount for round two
     */
    public static double getClearingPriceForRoundTwo(String courseCode, String sectionCode) {
        double clearingPrice = 10.0;
        Section section = CourseSectionController.getSection(courseCode, sectionCode);
        int classSize = section.getClassSize();
        int numEnrolled = SectionStudentController.getNumEnrolledInSection(courseCode, sectionCode);
        int currentBids = BidDAO.getNumBidsForSection(courseCode, sectionCode);
        int vacancy = classSize - numEnrolled;
        int unfilledVacancies = classSize - numEnrolled - currentBids;

        // at least 1 slot return 10
        if (unfilledVacancies > 0) {
            return 10;
        }

        if (unfilledVacancies == 0) {
            return BidDAO.getClearingPrice(courseCode, sectionCode, vacancy);
        }

        if (unfilledVacancies < 0) {
            //retrieve the bid amount at vacancy +1
            double bidAfterClearingPrice = BidDAO.getClearingPrice(courseCode, sectionCode, vacancy + 1);
            //retrieve the next highest bid 
            double bidAtClearingPrice = BidDAO.getNextHigherBidAmount(courseCode, sectionCode, bidAfterClearingPrice);
            if (bidAtClearingPrice == 0){
                return bidAfterClearingPrice + 1;
            }else{
                return bidAtClearingPrice;
            }
        }
        // should not reach here
        return 10;
    }

    /**
     * Deletes the student's bid from record and updates the e-dollar balance by
     * returning amount for previous userid's bid and deduct amount from new
     * user's bid using userId,courseCode and sectionCode
     *
     * @param userId userId of Student
     * @param courseCode courseCode of Section
     * @param sectionCode sectionCode
     * @return true if user is found and bid exist in database, otherwise return
     * false
     */
    public static boolean deleteBidFromStudent(String userId, String courseCode, String sectionCode) {
        //Delete the bid from the bid table
        Student user = StudentDAO.getStudentById(userId);
        // return false if no such user
        if (user == null) {
            return false;
        }

        // check if bid exists
        Bid bidInDB = BidDAO.getBid(sectionCode, courseCode, userId);
        // return false if no such bid in database
        if (bidInDB == null) {
            return false;
        }

        // delete bid from database
        BidDAO.deleteBid(courseCode, sectionCode, userId);

        // refund bid amount to user
        double refundAmount = bidInDB.getBidAmount();
        StudentController.updateEdollar(userId, refundAmount);
        // Update bid status for Round 2 drop bid logic 
        if (RoundController.getRound() == 2) {
            //gets clearingPrice and update bid status to success if equal and above clearing price
            double clearingPrice = getClearingPriceForRoundTwo(courseCode, sectionCode);
            BidDAO.roundBidStatusUpdate(courseCode, sectionCode, clearingPrice);
         
        }

        return true;
    }

    /**
     * Updates the new value of the bid into the database using
     * userId,amt,sectionCode and courseCode
     *
     * @param userId student's userId
     * @param amt bid amount
     * @param sectionCode the sectionCode
     * @param courseCode courseCode of section
     * @return true upon successful delete and add new value of bid into record,
     * otherwise returns false
     */
    public static boolean updateBid(String userId, double amt, String sectionCode, String courseCode) {
        //Needs to delete then add new bid.
        deleteBidFromStudent(userId, courseCode, sectionCode);
        addBid(userId, amt, sectionCode, courseCode);
        return true;
    }

    /**
     * Method to facilitate the updating of bid through JSON
     * @param userId student's userId
     * @param amt bid amount
     * @param sectionCode the section
     * @param previousSectionCode previous section code that student bidded
     * @param courseCode courseCode of section
     * @return true upon successful delete and add new value of bid into record, otherwise return false
     */
    public static boolean jsonUpdateBid(String userId, double amt, String sectionCode, String previousSectionCode ,String courseCode) {
        //Needs to delete then add new bid.
        deleteBidFromStudent(userId, courseCode, previousSectionCode);
        addBid(userId, amt, sectionCode, courseCode);
        return true;
    }
    
    /**
     * Gets all bids from student using their userId
     *
     * @param userId student's userId
     * @return an arraylist of bids which belongs to student
     */
    public static ArrayList<Bid> getBidsByStudent(String userId) {
        ArrayList<Bid> bidsResult = new ArrayList<Bid>();

        bidsResult = BidDAO.getBidsByUserId(userId);
        return bidsResult;
    }

    /**
     * Gets the bid amount for a section using the userId,sectionCode and
     * courseCode
     *
     * @param userId student's userId
     * @param sectionCode sectionCode
     * @param courseCode courseCode of section
     * @return the bid amount for a section
     */
    public static double getBidAmount(String userId, String sectionCode, String courseCode) {
        Bid bid = BidDAO.getBidForCourse(courseCode, userId);
        return bid.getBidAmount();
    }

    // processes Round One logic, update status of all bid, refund failed bids to students 
    /**
     * Gets all the sections present in the bid table
     *
     * @return an arrayList of Sections present in the bid table
     */
    public static ArrayList<Section> getAllSectionsInBidTable() {
        return BidDAO.getAllSectionsInBidTable();
    }

    /**
     * Gets the number of bids present for each section in the course using
     * courseCode and sectionCode
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @return total count of bids present for a course section
     */
    public static int getNumBidsForSection(String courseCode, String sectionCode) {
        return BidDAO.getNumBidsForSection(courseCode, sectionCode);
    }

    /**
     * Updates the roundBidStatus in the database to "SUCCESS" or "FAILED"
     * according to minValue using the courseCode,sectionCode and minAmount
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @param minAmount the minimum amount of bid
     */
    public static void roundBidStatusUpdate(String courseCode, String sectionCode, double minAmount) {
        BidDAO.roundBidStatusUpdate(courseCode, sectionCode, minAmount);
    }

    /**
     * Gets the clearing price for the round by sorting the number of bids and
     * providing vacant slots to the top few bids using courseCode,sectionCode
     * and sectionVacancy
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @param sectionVacancy number of vacant seats in the section
     * @return the clearing price of that round
     */
    public static double getClearingPrice(String courseCode, String sectionCode, int sectionVacancy) {
        return BidDAO.getClearingPrice(courseCode, sectionCode, sectionVacancy);
    }

    /**
     * Gets the number of bids at clearing price according to the vacant slots
     * using courseCode,sectionCode and sectionVacancy
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @param sectionVacancy vacant seat available for the section
     * @return the number of bids at clearing price
     */
    public static int getNumBidsAtClearingPrice(String courseCode, String sectionCode, int sectionVacancy) {
        return BidDAO.getNumBidsAtClearingPrice(courseCode, sectionCode, sectionVacancy);
    }

    /**
     * Gets the next higher bid amount for that section using courseCode,
     * sectionCode and clearingPrice
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @param clearingPrice the clearingPrice for the section
     * @return the next higher bid amount for the section
     */
    public static double getNextHigherBidAmount(String courseCode, String sectionCode, double clearingPrice) {
        return BidDAO.getNextHigherBidAmount(courseCode, sectionCode, clearingPrice);
    }

    /**
     * Gets the list of failed bids from the bid record
     *
     * @return an arraylist of failed bids
     */
    public static ArrayList<Bid> getBidsThatFailed() {
        // refund bids failed in Round 2
        return BidDAO.getBidsThatFailed();
    }

    /**
     * Gets the list of successful bids from the bid record
     *
     * @return an arraylist of successful bids
     */
    public static ArrayList<Bid> getSuccessfulBids() {
        return BidDAO.getSuccessfulBids();
    }

    /**
     * Clears the table of bids after each round end
     */
    public static void clearBidTable() {
        BidDAO.clearBidTable();
    }

    /**
     * Gets the minimum bid of a particular section of a course using the
     * courseId and sectionId
     *
     * @param courseId courseId of section
     * @param sectionId sectionId
     * @return minimum bid amount of the section
     */
    public static double getMinimumBid(String courseId, String sectionId) {
        return SectionMinimumPriceDAO.getMinBidAmtByCourseSection(courseId, sectionId);
    }

    /**
     * Sets the minimum bid amount for given section
     *
     * @param courseCode courseCode of section
     * @param sectionCode sectionCode
     * @param minBid minimum bid amount
     */
    public static void setMinimumBid(String courseCode, String sectionCode, double minBid) {
        SectionMinimumPriceDAO.setMinBid(courseCode, sectionCode, minBid);
    }
    
    /**
     * Check whether user is already enrolled in the course
     * 
     * @param userId User Id of student
     * @param courseCode Course of bid student is bidding for
     * 
     * @return true if student has already enrolled in the course specified
     */
    
    public static boolean checkIfCourseEnrolled(String userId, String courseCode){
        ArrayList<Bid> sectionList = SectionStudentController.getSectionsByStudentId(userId);
        //check through the enrolled sections
        if (sectionList == null || sectionList.isEmpty()){
            return false;
        }
        for (Bid section : sectionList){
            if (section.getCourseCode().equals(courseCode)){
                return true;
            }
        }
        return false;
    }

    /**
     * Get lowest bid amount in database through DAO
     * @param courseId course code
     * @param sectionCode section code of the specific course
     * @return lowest bid amount
     */
    public static double getLowestBidAmount(String courseId, String sectionCode){
        return BidDAO.getLowestBidAmount(courseId, sectionCode);
    }   
    
    /**
     * Get all records in database through DAO
     * @param courseId course code
     * @param sectionId section code of the specific course
     * @return list of bids by section and course
     */
    public static ArrayList<Bid> getAllBidBySectionCourseId(String courseId, String sectionId){
        ArrayList<Bid> bids = BidDAO.getAllBidBySectionCourseId(courseId, sectionId);
        return bids;

    }
}

