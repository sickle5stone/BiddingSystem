/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static bootstrap_validation.CommonValidation.CommonValidation;
import bootstrap_validation.ValidateBid;
import bootstrap_validation.ValidateCourse;
import bootstrap_validation.ValidateCourseCompleted;
import bootstrap_validation.ValidatePrereq;
import bootstrap_validation.ValidateSection;
import bootstrap_validation.ValidateStudent;
import com.opencsv.CSVReader;
import dao.BootstrapDAO;
import entity.Bid;
import entity.Course;
import entity.Section;
import entity.Student;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import utility.UploadUtility;

/**
 *
 * @author Cheryl, Aloysius
 */
/**
 * Class that handles all controller operations pertaining to the Bootstrap
 * Controller
 */
public class BootstrapController {

    /**
     * BootstrapController
     *
     * @author Haseena,Regan
     */
    public static ArrayList<Student> STUDENTLIST = new ArrayList<>();

    /**
     * A static COURSELIST which contains all the arraylist of courseList.
     */
    public static ArrayList<Course> COURSELIST = new ArrayList<>();
    // public static final HashMap<String, ArrayList<Section>> STUDENTSECTION = new HashMap(); //StudentId, Arraylist of Sections

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
    // public static final HashMap<String, Bid> BIDLIST = new HashMap<>(); //StudentID, Bid Object to store

    /**
     * A static ERRORLIST which contains all the errors in bootstrapping the
     * files.
     */
    public static ArrayList<String> ERRORLIST = new ArrayList<>();

    /**
     * A static BIDLIST which contains all the bid objects.
     */
    public static ArrayList<Bid> BIDLIST = new ArrayList<>();

    /**
     * Reads all the CSVFiles uploaded in bootstrap and automatically increase
     * and sets the round to 1 and active
     *
     * @return an arraylist of errors
     */
    public static ArrayList<String> readAllCsvFiles() {

        STUDENTLIST = new ArrayList<>();
        COURSELIST = new ArrayList<>();
        COURSESECTION = new HashMap<>();
        PREREQLIST = new HashMap<>();
        COURSECOMPLETE = new HashMap<>();
        ERRORLIST = new ArrayList<>();
        BIDLIST = new ArrayList<>();

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
        //Start round 1 after bootstrap
        RoundController.setRoundAndStatus(1, "active");

        return ERRORLIST;

    }

    /**
     * Does validation of each column in CSV file and proceeds to check
     * bootstrap validation for respective file before returning an arraylist of
     * errors.
     *
     * @param fileName the name of the file to be read
     * @return an arrayLsit of errors
     */
    public static ArrayList<String> readFiles(String fileName) {
        try {
            String[] header = UploadUtility.headerList.get(fileName);
            CSVReader CSVdata = UploadUtility.fileList.get(fileName);

            if (header == null || CSVdata == null) {
                String error = fileName + " is not found.";
                ERRORLIST.add(error);
                return ERRORLIST;
            }

            String[] columns;

            int rowCount = 1;

            while ((columns = CSVdata.readNext()) != null) {
                /*    for (String s : columns){
                    if (s.isEmpty()){
                System.out.println("outer loop"  + fileName);
                        break outer; 
                    }
                } */

                rowCount++;
                String errors = CommonValidation(columns, header);
                boolean isEmptyRow = false;
                if (errors.equals("skip")) {
                    errors = "";
                    isEmptyRow = true;
                }

                // error.isEmpty => means it passes common validation( no blank field) && it is not an empty row
                if (errors.isEmpty() && !isEmptyRow) {
                    switch (fileName) {
                        case "student.csv":
                            errors = ValidateStudent.checkStudent(columns);
                            break;
                        case "course.csv":
                            errors = ValidateCourse.checkCourse(columns);
                            break;
                        case "bid.csv":
                            errors = ValidateBid.validateBid(columns);
                            break;
                        case "prerequisite.csv":
                            errors = ValidatePrereq.checkPrereq(columns);
                            break;
                        case "section.csv":
                            errors = ValidateSection.checkSection(columns);
                            break;
                        case "course_completed.csv":
                            errors = ValidateCourseCompleted.checkCourseCompleted(columns);
                            break;
                    }
                }

                if (!errors.isEmpty()) {
                    errors = fileName + " row " + rowCount + " " + errors;
                    ERRORLIST.add(errors);
                }

            }

            CSVdata.close();

        } catch (IOException e) {
            System.out.print("ioexception> " + e.getMessage());
        }
        return ERRORLIST;
    }

}
