/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bootstrap_validation;

import controller.BootstrapController;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;
import java.util.*;

/**
 * Class to validate all bootstrap data for bid related functions
 * @author Haseena , Cheryl 
 */
public class ValidateBid {
    /**
     * validateBid method to consolidate all the error messages resulting from calling the various bid validation methods
     * @param oneBid Array of data for every row in bid.csv
     * @return String of errors
     */
    public static ArrayList<String> validateBid(String[] oneBid) {
        ArrayList<String> errors = new ArrayList<>();
        String selectedUserId = oneBid[0];
        String selectedAmount = oneBid[1];
        String selectedCourseId = oneBid[2];
        String selectedSectionId = oneBid[3];
        // if need to update existing bid section and bid amount if bidding for the same course
        Bid existingBid = null;
        
        // get existing bid if any
        existingBid = getExistingBid(selectedUserId, selectedCourseId);
        
        //Check if userID is found in student arraylist from bootstrap controller
        if (!checkIfUserIdValid(selectedUserId)) {
            errors.add("invalid userid");
        }
        //Amount should be not more than 2 decimal places and be a positive number, else insert error Msg
        if (!checkIfAmountIsValid(selectedAmount)){
            errors.add("invalid amount");
        }
        //The course code must be found in the course list, else return errorMsg
        if (!checkIfCourseIsValid(selectedCourseId)) {
            errors.add("invalid code");            
            //checks sectionIsValid only if valid courseCode
        }else if(!checkIfSectionIsValid(selectedSectionId, selectedCourseId)){
            errors.add("invalid section");
        }
        
       
        // bootstrap always happens during round 1. Get student and check if it is round 1 always 
       if (!checkIfRoundBidIsOwnSchool(selectedUserId, selectedCourseId)){
            errors.add("not own school course");
       }
        
        if (!checkIfClassTimeTableClash(selectedUserId,selectedCourseId,selectedSectionId)){
           errors.add("class timetable clash");
        }
        
        // if student hasnt bidded for the course before, check for exam time table
        if (existingBid == null){
            if (!checkIfExamTimeTableClash(selectedUserId, selectedCourseId)){
                errors.add("exam timetable clash");            
            }
        }
        
        if (!checkPrereq(selectedUserId, selectedCourseId)){
            errors.add("incomplete prerequisites");            
        }
               
        if (checkCourseAlreadyCompleted(selectedUserId, selectedCourseId)){
            errors.add("already completed");
        }
        
        if (checkSectionLimitReached(selectedUserId, selectedSectionId)){
            errors.add("section limit reached");
        }        
        // check and deduct only if passes all validation
        if (errors.isEmpty()){
            double amount=Double.parseDouble(selectedAmount);
            if (existingBid != null){
                // there is an existing bid take into account balance after refund
                double existingBidAmt = existingBid.getBidAmount();
                if (!checkEDollarBalance(selectedUserId,amount-existingBidAmt)){
                    errors.add("not enough e-dollar");
                }else{
                    // sufficient edoller remove old bid, add new bid, update edollar
                    deleteBid(selectedUserId, selectedCourseId);  
                    BootstrapController.BIDLIST.add(new Bid(selectedUserId, selectedCourseId, selectedSectionId, amount, "PENDING"));
                    minusEDollar( selectedUserId, amount);
                } 
                    
                // when there is no existing bids
            } else if (!checkEDollarBalance(selectedUserId,amount)){
                errors.add("not enough e-dollar");
            } else{
                // not new bid & have sufficient edollar & update edollar
                BootstrapController.BIDLIST.add(new Bid(selectedUserId, selectedCourseId, selectedSectionId, amount, "PENDING"));
                minusEDollar( selectedUserId, amount);
            }           
        }              
        return errors;
    }
    /**
     * minusEDollar method to allow deduct of edollar
     * @param studentId student's ID
     * @param amount bid amount from bid.csv
     */    
    public static void minusEDollar(String studentId, double amount){
        for (Student s : BootstrapController.STUDENTLIST){
            if (s.getUserId().equals(studentId)){
                double amt = s.geteDollar();
                s.seteDollar(amt-amount);
                break;
            }
        }
    }
    
    // delete the bid from the main list and refund amount to student
    /**
     * deleteBid method to delete bid from the main list and refund amount to student
     * @param userId Student's ID
     * @param courseId Course ID to retrieve to facilitate refund
     */
    public static void deleteBid(String userId, String courseId){
        for(int i = 0; i< BootstrapController.BIDLIST.size(); i++){
            Bid bid = BootstrapController.BIDLIST.get(i);
            String user = bid.getUserId();
            String course = bid.getCourseCode();
            double refund = bid.getBidAmount();
            
            if (user.equals(userId) && course.equals(courseId)){
                BootstrapController.BIDLIST.remove(i);
                for (Student stu : BootstrapController.STUDENTLIST){
                    String uid = stu.getUserId();
                    if (uid.equals(userId)){
                        double currEDollar = stu.geteDollar();
                        stu.seteDollar(refund+currEDollar);
                        break;
                    }
                }
                break;
            }
        }
    }
    /**
     * getExistingBid method to retrieve existing bid,else returns null 
     * @param userId Student's ID
     * @param courseId Course ID 
     * @return Bid object if bid exists in database else return null
     */    
    public static Bid getExistingBid (String userId, String courseId ){        
        for (Bid bid: BootstrapController.BIDLIST){
            String courseCode = bid.getCourseCode();
            String user = bid.getUserId();
            if (courseCode.equals(courseId) && user.equals(userId)){
                return bid;
            }
        }
        return null;
    }
    /**
     * checkIfUserIdValid method to check if user is valid, meaning if student exists in student table
     * @param selectedUserId UserID
     * @return true if Student is found else return false
     */
    public static boolean checkIfUserIdValid(String selectedUserId) {
        ArrayList<Student> studentList = BootstrapController.STUDENTLIST;
        for (Student eachStudent : studentList) {
            String eachUserId = eachStudent.getUserId();
            if (eachUserId.equals(selectedUserId)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Method to check if bid amount is of a valid format
     * @param selectedAmount Bid amount
     * @return true if bid amount is valid else return false
     */
    public static boolean checkIfAmountIsValid(String selectedAmount) {
        
        int posOfDecimal = selectedAmount.indexOf('.');
        
        if (posOfDecimal != -1){
            // concates from the '.' to the end. eg. 10.052 -> .052
            String checkDecimal = selectedAmount.substring(posOfDecimal);
            if (checkDecimal.length() > 3){
                return false;
            }
        }
        // check in case the string is not a double
        try {
            double amount = Double.parseDouble(selectedAmount);
            // reject if amount is less than min. bid of $10
            
             // check if number is more than what the database accepts
            if (amount > 9999.99){
                return false;
            }
            if (amount < 10.0 ) {
                return false;
            }
        } catch (NumberFormatException e) {
            // not double
            return false;
        }
        // return true if passes all
        return true;
    }
    /**
     * checkIfCourseIsValid method to check if course if valid
     * @param courseId Course code
     * @return true if course is valid else return false
     */
    public static boolean checkIfCourseIsValid(String courseId) {
        ArrayList<Course> totalCourses = BootstrapController.COURSELIST;
        for (Course eachCourse : totalCourses) {
            if (eachCourse.getCourseCode().equals(courseId)) {
                return true;
            }
        }
        return false;
    }
    /**
     * checkIfSectionIsValid method to check if section is valid
     * @param sectionId Section code
     * @param courseId Course code
     * @return true is section belongs to course else return false
     */
    public static boolean checkIfSectionIsValid(String sectionId, String courseId) {
        HashMap<String, ArrayList<Section>> validatedSections = BootstrapController.COURSESECTION;
        ArrayList<Section> sectionList = validatedSections.get(courseId);
        if(sectionList == null){
            return false;
        }
        for (Section section : sectionList) {
            String sid = section.getSection();
            if (sid.equals(sectionId)){
                return true;
            }
        }
        return false;
    }
    /**
     * checkIfRoundBidIsOwnSchool method to check if bid in the current round is for a module from the same school as the student
     * @param userId Student ID
     * @param courseId Course Code
     * @return true if bid in the current round is for a module from the same school as the student else return false
     */
    public static boolean checkIfRoundBidIsOwnSchool(String userId, String courseId) {
        //if it is round one, use the userId and get the Student. check the student's school
        /*  ArrayList<Student> studentList=BootstrapController.STUDENTLIST;
        for(Student eachStudent: studentList){
            String eachUserId=eachStudent.getUserId();
            if( eachUserId.equals(userId)){
                //get the school and then compare the bid.
                String selectedSchool=eachStudent.getSchool();
                if (selectedSchool.equals()){
                    
                }
            }
            
        }*/        
       
        Student checkStudent = null;
        Course checkCourse = null;
        for (Student s : BootstrapController.STUDENTLIST) {
            if (s.getUserId().equals(userId)) {
                checkStudent = s;
            }
        }
        for (Course c : BootstrapController.COURSELIST) {
            if (c.getCourseCode().equals(courseId)) {
                checkCourse = c;
            }
        }
        
        if (checkStudent == null || checkCourse == null) {
            return false;
        }
        
        return checkStudent.getSchool().equals(checkCourse.getSchoolTitle());
    }
    
    
    // return false if clash, true if there is no clash
    /**
     * checkIfClassTimeTableClash method to check if class time table clashes 
     * @param userId Student ID
     * @param courseCode Course Code
     * @param sectionId Section Code
     * @return true if class does not clash at the same time else return false
     */
    public static boolean checkIfClassTimeTableClash(String userId,String courseCode,String sectionId) {
    // get Section timing of new bid
        // get list of sections under that course
        Section sectionOfNewBid = getSectionFromCourseSectionList(courseCode, sectionId);
        // sectionOfNewBid sld never be null (set to true because assume checksection is valid will handle dont need to display clash class error)
        if (sectionOfNewBid==null){
            return true;
        }
        int newSectionDay = sectionOfNewBid.getDayOfWeek();
        Date newSectionStart = sectionOfNewBid.getStartTime();
        Date newSectionEnd = sectionOfNewBid.getEndTime();
    // loop through all sections student has bidded for 
        // get all bid by student
        for (Bid bid : BootstrapController.BIDLIST){
            // loop thu bidlist grab bids under student
            if (bid.getUserId().equals(userId)){
                String cid = bid.getCourseCode();
                String sid = bid.getSectionCode();                
                // get Section Object already bidded for
                Section currSection = getSectionFromCourseSectionList(cid, sid);
                // currSection sld never be null
                if (currSection == null){
                    return true;
                }
                int currSectionDay = currSection.getDayOfWeek();
                Date currSectionStart = currSection.getStartTime();
                Date currSectionEnd = currSection.getEndTime();
                // when (newStart is after currEnd  OR currStart is after new End) => qualified cases
                // NOT qualified "!" return false
                if (newSectionDay == currSectionDay){
                    if (!(newSectionStart.after(currSectionEnd)|| currSectionStart.after(newSectionEnd)) ){
                        return false;
                    }                    
                }
            }
        }
        return true;
    }
    /**
     * getSectionFromCourseSectionList method to retrieve section from course-section table
     * @param courseId Course code
     * @param sectionId Section code
     * @return Section object if successfully retrieved else return null
     */    
    public static Section getSectionFromCourseSectionList ( String courseId, String sectionId){
        ArrayList<Section> sectionList = BootstrapController.COURSESECTION.get(courseId);
        if (sectionList == null){
            return null;
        }
        for (Section s : sectionList){
            if (s.getSection().equals(sectionId)){
                return s;
            }
        }
        return null;
    }
    
    /**
     * checkIfExamTimeTableClash method to check if user's exam timetable has clashes
     * @param selectedUserId Student ID
     * @param selectedCourseId Course Code
     * @return true if no clashes found else return false
     */    
    public static boolean checkIfExamTimeTableClash(String selectedUserId, String selectedCourseId){
        String errorMsg="";
        boolean timeTableClash=false;
        
        HashMap<String,ArrayList<Section>> courseSection=BootstrapController.COURSESECTION;
        Date selectedStartTime=null;
        Date selectedEndTime=null;
        Date selectedExamDate=null;
        
        //based on the current bid
        for(Course course:BootstrapController.COURSELIST){
            if(course.getCourseCode().equals(selectedCourseId)){
                selectedStartTime=course.getExamStart();
                selectedEndTime=course.getExamEnd();
                selectedExamDate=course.getExamDate();
            }
        }
        
        for (Bid bid : BootstrapController.BIDLIST){
            // loop thu bidlist grab bids under student
            if (bid.getUserId().equals(selectedUserId)){
                String cid = bid.getCourseCode();              
                // get Course Object already bidded for           
                for(Course course:BootstrapController.COURSELIST){
                    // get specific course that they have bidded for in bid list
                    if(course.getCourseCode().equals(cid)){
                        Date currExamStart = course.getExamStart();
                        Date currExamEnd = course.getExamEnd();
                        Date currExamDate = course.getExamDate();
                        
                        if (currExamDate.equals(selectedExamDate)){
                            if (!(selectedStartTime.after(currExamEnd )|| currExamStart.after(selectedEndTime))){
                                return false;
                            }
                        }
                        // breaks out of for loop since matching courseid is found
                        break;
                    }
                }
            }
        }
        return true;
    }
    /**
     * checkEDollarBalance method to check edollar balance
     * @param studentId Student ID
     * @param amount Bid Amount
     * @return true if edollar balance is sufficient to handle amount passed in else return false
     */    
    public static boolean checkEDollarBalance(String studentId, double amount){
        for (Student s : BootstrapController.STUDENTLIST){
            if (s.getUserId().equals(studentId)){
                // get student edollar balance
                double eDollarAmt = s.geteDollar();
                
                // check if sufficient eDollar
                if (eDollarAmt < amount){
                    return false;
                }
                // already found matching student
                break;
            }
        }
        return true;                
    }
     
   /*  public static boolean checkBiddedForCourse(String studentId, String courseId){
        for (Bid bid : BootstrapController.BIDLIST){
            // loop thu bidlist grab bids under student
            if (bid.getUserId().equals(studentId)){
               if (bid.getCourseCode().equals(courseId)){
                   return false;
               }    
            }
        }
        return true;
     }*/
        
    /**
     * checkSectionLimit method to check if section limit has been reached
     * @param studentId Student ID
     * @param sectionId Section Code
     * @return true if section limit reached else return false
     */    
     public static boolean checkSectionLimitReached(String studentId, String sectionId){
        int numOfBids = 0;
          for (Bid bid : BootstrapController.BIDLIST){
            // loop thu bidlist grab bids under student
            if (bid.getUserId().equals(studentId)){
               numOfBids++;
            }
          }
        // reject bid already have 5 bids  
        return numOfBids == 5;
    }
    /**
      * checkCourseAlreadyCompleted method to check if course is completed
      * @param studentId Student ID
      * @param courseId Course Code
      * @return true if course has been completed else return false
      */
     public static boolean checkCourseAlreadyCompleted(String studentId, String courseId){
        ArrayList<String> checking = BootstrapController.COURSECOMPLETE.get(studentId);
        if (checking == null){
            return false;
        }
        for (String completedCourse : checking){
            if (courseId.equals(completedCourse)){
                // already bidded for the course
                //System.out.println(courseId+studentId);
                return true;
            }
        }
        return false;
    }
    /**
     * checkPrereq method if student has completed the prerequisites for the course
     * @param studentId Student ID
     * @param courseCode Course Code
     * @return true if prereq has not been completed else return false
     */     
    public static boolean checkPrereq(String studentId, String courseCode){
        if (studentId.equals("amy.ng.2009")){
            ArrayList<String> prereqs = BootstrapController.PREREQLIST.get(courseCode);
            ArrayList<String> coursesCompleted = BootstrapController.COURSECOMPLETE.get(studentId) ;
        }
        
        ArrayList<String> prereqs = BootstrapController.PREREQLIST.get(courseCode);
        ArrayList<String> coursesCompleted = BootstrapController.COURSECOMPLETE.get(studentId) ;
        if (prereqs == null){
            return true;
        }
        if (coursesCompleted == null){
            return false;
        }
        
        for (String p: prereqs){
            Boolean preReqCompleted = false; 
            for(String c: coursesCompleted){
              if( c.equals(p)){
                preReqCompleted=true;
                break;
              }
            }
            if(!preReqCompleted){
                return false;
            }
        }
        return true;
     }
}
