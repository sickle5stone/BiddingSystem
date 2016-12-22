/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.BootstrapError;
import static bootstrap_validation.CommonValidation.CommonValidation;
import bootstrap_validation.ValidateBid;
import bootstrap_validation.ValidateCourse;
import bootstrap_validation.ValidateCourseCompleted;
import bootstrap_validation.ValidatePrereq;
import bootstrap_validation.ValidateSection;
import bootstrap_validation.ValidateStudent;
import dao.BootstrapDAO;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import utility.UploadUtility;

/**
 * BootstrapController
 *
 * @author Huiyan and Haseena
 */
/**
 * Class that handles all controller operations pertaining to the Bootstrap
 * Controller
 */
public class BootstrapController {


    /**
     * A static STUDENTLIST which contains all the students in the loaded bootstrap data
     */

    public static ArrayList<Student> STUDENTLIST = new ArrayList<>();

    /**
     * A static COURSELIST which contains all the arraylist of courseList.
     */
    public static ArrayList<Course> COURSELIST = new ArrayList<>();

    /**
     * A static COURSESECTION which contains the hashmap of course to sections.
     */
    public static HashMap<String, ArrayList<Section>> COURSESECTION = new HashMap<>(); // CourseID, Sections under the course

    /**
     * A static PREREQLIST which contains all the hashmap of course and
     * sections.
     */
    public static HashMap<String, ArrayList<String>> PREREQLIST = new HashMap<>(); // courseID, PrereqID to store

    /**
     * A static COURSECOMPLETE which contains all the hashmap of completed
     * courses to courses.
     */
    public static HashMap<String, ArrayList<String>> COURSECOMPLETE = new HashMap<>(); //StudentID, CourseID to store
    
    

    /**
     * A static ERRORLIST which contains all the errors in bootstrapping the
     * files.
     */
    public static ArrayList<BootstrapError> ERRORLIST = new ArrayList<>();

    /**
     * A static BIDLIST which contains all the bid objects.
     */
    public static ArrayList<Bid> BIDLIST = new ArrayList<>();
    
    /**
     * A static ArrayList of string containing the results of the loaded bootstrap elements
     */
    public static ArrayList<String> SUCCESSFULRECORDS = new ArrayList<>();


    /**
     * Counter for counting the number of bids processed from bootstrap
     */

    public static int COUNTBIDSPROCESSED = 0;
    

    /**
     * Reads all the CSVFiles uploaded in bootstrap and automatically increase
     * and sets the round to 1 and active
     *
     * @return an arraylist of errors
     */
    public static ArrayList<BootstrapError> readAllCsvFiles() {
        COUNTBIDSPROCESSED = 0; 

        STUDENTLIST = new ArrayList<>();
        COURSELIST = new ArrayList<>();
        COURSESECTION = new HashMap<>();
        PREREQLIST = new HashMap<>();
        COURSECOMPLETE = new HashMap<>();
        ERRORLIST = new ArrayList<>();
        BIDLIST = new ArrayList<>();
        SUCCESSFULRECORDS = new ArrayList<>();    
        
        //read in order
        readFiles("student.csv");
        readFiles("course.csv");
        readFiles("section.csv");
        readFiles("prerequisite.csv");
        readFiles("course_completed.csv");
        readFiles("bid.csv");

        if (BootstrapDAO.clearDatabase()) {
            BootstrapDAO.insertStudent(STUDENTLIST);
            BootstrapDAO.insertCourse(COURSELIST);
            BootstrapDAO.insertSection(COURSESECTION);
            BootstrapDAO.insertSectionMinBid(COURSESECTION);
            BootstrapDAO.insertPrerequisites(PREREQLIST);
            BootstrapDAO.insertCourseCompleted(COURSECOMPLETE);
            BootstrapDAO.insertBid(BIDLIST);
            //Set all bids to $10 mininum bid 
            BootstrapDAO.setDefaultBid(COURSESECTION);

        }
        
        
        // add total successful records to 
        SUCCESSFULRECORDS.add("student.csv: " + STUDENTLIST.size());
        SUCCESSFULRECORDS.add("course.csv: " + COURSELIST.size());
        SUCCESSFULRECORDS.add("section.csv: " + getNumElements(COURSESECTION));
        SUCCESSFULRECORDS.add("prerequisite.csv: " + getNumElements(PREREQLIST));
        SUCCESSFULRECORDS.add("course_complete.csv: " + getNumElements(COURSECOMPLETE));
        SUCCESSFULRECORDS.add("bid.csv: " + COUNTBIDSPROCESSED);
        
        //Start round 1 after bootstrap
        RoundController.setRoundAndStatus(1, "active");

        return ERRORLIST;

    }

    /**
     * Does validation of each column in CSV file and proceeds to check
     * bootstrap validation for respective file before returning an arraylist of
     * errors. S
     *
     * @param fileName the name of the file to be read
     * @return an arrayLsit of errors
     */
    public static ArrayList<BootstrapError> readFiles(String fileName) {

        String[] header = UploadUtility.headerList.get(fileName);
        List<String[]> data = UploadUtility.dataList.get(fileName);
        if (header == null || data == null) {
            ArrayList<String> err = new ArrayList<>();
            err.add(fileName + " is not found.");
            ERRORLIST.add(new BootstrapError(fileName, 0, err));
            return ERRORLIST;
        }

        String[] columns;

        int rowCount = 1;
        for (String[] t : data) {
            while ((columns = t) != null) {

                rowCount++;
                ArrayList<String> errors = CommonValidation(columns, header);
                // error.isEmpty => means it passes common validation( no blank field) && it is not an empty row
                if (errors.isEmpty()) {
                    switch (fileName) {
                        case "student.csv":
                            errors.addAll(ValidateStudent.checkStudent(columns));
                            break;
                        case "course.csv":
                            errors.addAll(ValidateCourse.checkCourse(columns));
                            break;
                        case "bid.csv":
                            errors.addAll(ValidateBid.validateBid(columns));
                            break;
                        case "prerequisite.csv":
                            errors.addAll(ValidatePrereq.checkPrereq(columns));
                            break;
                        case "section.csv":
                            errors.addAll(ValidateSection.checkSection(columns));
                            break;
                        case "course_completed.csv":
                            errors.addAll(ValidateCourseCompleted.checkCourseCompleted(columns));
                            break;
                    }
                }

                if (!errors.isEmpty()) {
                    ERRORLIST.add(new BootstrapError(fileName, rowCount, errors));
                }

                break;
            }
        }
        
        return ERRORLIST;
    }
    /**
     * Method to calculate the number of elements in the hashmap
     * @param hashmapObject Hashmapobject 
     * @return number of elements in the hashmap
     */
    public static int getNumElements(Object hashmapObject) {
        int toReturn = 0;

        if (hashmapObject instanceof HashMap) {
            HashMap<Object, Object> hashmap = (HashMap<Object, Object>) hashmapObject;
            Collection<Object> objectValues = hashmap.values();
            for (Object obj : objectValues) {
                if (obj instanceof ArrayList) {
                    ArrayList<Object> list = (ArrayList<Object>) obj;
                    toReturn += list.size();
                }
            }
        }

        return toReturn;
    }

}
