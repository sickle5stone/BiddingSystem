package bootstrap_validation;

/*
*@author Haseena,ChenHuiYan
*
 */
import controller.BootstrapController;
import entity.Course;
import entity.Section;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Class to validate all section related bootstrap data
 * @author Haseena,ChenHuiYan
 */
public class ValidateSection {
    /**
     * checkSection method to check for and consolidate all section related bootstrap errors
     * @param row Array of data for every row in section.csv
     * @return String of errors
     */
    public static ArrayList<String> checkSection(String[] row) {
        ArrayList<String> errors = new ArrayList<>();
        if (row.length < 8){
            return errors;
        }
        
        ArrayList<Course> courses = BootstrapController.COURSELIST;
        String courseCode = row[0];
        String section = row[1];
        String day = row[2];
        String examStartTime = row[3];
        String examEndTime = row[4];
        String instructorName = row[5];
        String venueName = row[6];
        String sectionSize = row[7];
        
        
        Date startTime = null;
        Date endTime = null;
        int dayOfWeek = 0;
        if (!checkIfInvalidCourse(courses, courseCode)) {
            errors.add("invalid course");
        }
        //check section only if course is valid
        if (errors.isEmpty()) {   
            String sectError = checkIfSectionValid(section);
            if(sectError != null){
                errors.add(sectError);
            }
        }
        dayOfWeek = checkIfInvalidDay(day);
        if (dayOfWeek == 0) {
            errors.add("invalid day");
        }

        //Check if startTime follows the correct format
        startTime = isTimeFormatValid("H:mm", examStartTime);
        if(startTime == null){
            errors.add("invalid start");
        }  // start time doesn't follow the format
        

        //check if the endTime is in the correct format
        
        endTime = isTimeFormatValid("H:mm", examEndTime);
        if ( endTime==null ){
            errors.add("invalid end");
        } else if (startTime != null && endTime.before(startTime)) {//check if exam end time is later than exam start time
            errors.add("invalid end");
        }
       
        String instruError = isValidInstructor(instructorName); // returns string error, or null object if no error
        String venueError = isValidVenue(venueName); // returns string error, or null object if no error
        
        if (instruError != null){
            errors.add(instruError);
        }
        
        if (venueError != null){
            errors.add(venueError);
        }
        
      /*  if (!venueIsNotOccupied(venueName, dayOfWeek, startTime, endTime)){
            errors.add("venue occupied");
        }*/
      
        String sizeError = isValidSize(sectionSize);
        if (sizeError!= null){
            errors.add(sizeError);
        }
        
        
        if (errors.isEmpty()){

            if (checkDuplicatedRecord(BootstrapController.COURSESECTION, courseCode, section)){
                errors.add("duplicate section");
            }
        }
        
        if(errors.isEmpty()){
            
            int sectionSizeNum = Integer.parseInt(sectionSize);
            HashMap<String, ArrayList<Section>> sectionList=BootstrapController.COURSESECTION;
            
            if(sectionList.get(courseCode)==null){                
                ArrayList<Section> sectionsToAdd = new ArrayList<>();
                sectionsToAdd.add(new Section(courseCode,section,dayOfWeek,startTime,endTime,instructorName,venueName,sectionSizeNum));
                BootstrapController.COURSESECTION.put(courseCode, sectionsToAdd);
                
            }else{
                ArrayList<Section> sections=sectionList.get(courseCode);
                sections.add(new Section(courseCode,section,dayOfWeek,startTime,endTime,instructorName,venueName,sectionSizeNum));
                BootstrapController.COURSESECTION.put(courseCode, sections);
            }
            
            
        }
        
        return errors;

    }   

   /* public static String venueClashWithOtherSections(String venue, Date startTime, Date endTime, int day) {
        HashMap<String, ArrayList<Section>> list = BootstrapController.COURSESECTION;
        Collection<ArrayList<Section>> sectionList = list.values();
        
        String toReturn = "";
        if (venue.length() > 100) {
            toReturn += "venue booked by other sections at that slot, ";
        }
        return toReturn;
    } */
    
    
    //Checks to see if the Course exist in the Course List
    /**
     * Method to check for duplicate record 
     * @param courseSections Hashmap of key:course code of type string and value of arraylist of section
     * @param courseCode Course code
     * @param sectionId Section ID
     * @return true duplicated record is found else return false
     */
    public static boolean checkDuplicatedRecord(HashMap<String, ArrayList<Section>> courseSections, String courseCode, String sectionId){
        ArrayList<Section> sectionsByCourseCode= courseSections.get(courseCode);
        if (sectionsByCourseCode!=null){
            for(int i=0; i<sectionsByCourseCode.size(); i++){
                Section eachSection = sectionsByCourseCode.get(i);
                if(eachSection.getSection().equals(sectionId)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Method to check if course is invalid
     * @param courses Arraylist of Courses
     * @param courseCode Course code
     * @return true if course is found in courses else return false
     */    
    public static boolean checkIfInvalidCourse(ArrayList<Course> courses, String courseCode) {
        String error = "";
        boolean courseMatched = false;
        //If a matching course is found, break out of your for loop, else continue
        for (Course eachCourse : courses) {
            String eachCourseCode = eachCourse.getCourseCode();
            if (eachCourseCode.equals(courseCode)) {
                courseMatched = true;
                break;
            }
        }
        return courseMatched;
    }
    /**
     * Method to check if day is within range of 1 to 7
     * @param day Day of the week
     * @return the day value if parameter is within range of 1 to 7 else return 0
     */
    public static int checkIfInvalidDay(String day) {
        //The day field should be a number between 1(inclusive) and 7 (inclusive).
        //1 - Monday, 2 - Tuesday, ... , 7 - Sunday
        Integer toReturn = 0;
        try {
            toReturn = Integer.parseInt(day);
            if (toReturn == null){
                return toReturn;
            }
            // return 0 if day is invalid
            if (toReturn < 1 || toReturn > 7) {
                return 0;
            }

        } catch (NumberFormatException e) {
           // returnValue = false;
           return 0;
        }
        return toReturn;
    }
    /**
     * Method to check if section is valid
     * @param sectionId Section ID
     * @return empty String if section is valid else return error message, "Invalid Section"
     */
    public static String checkIfSectionValid(String sectionId) {
        //check whether section input is more than what database can store
        if (sectionId.length() > 3){
            return "invalid section";
        }
        
        char firstChar = sectionId.charAt(0);
        if (firstChar == 'S') {
            String digits = sectionId.substring(1);
            //To get the remaining parts of section ID 
            try {
                //Check if remaining parts are digit, if failed, enter exception block
                int remainingNumbersInSectionId = Integer.parseInt(digits);
                if (remainingNumbersInSectionId < 1){
                    return "invalid section";
                }

            } catch (NumberFormatException e) {
                return "invalid section";
            }
            //If the char does not start with 'S'
        } else {
            return "invalid section";
        }

        return null;

    }
    /**
     * Method to check if time passed in is valid
     * @param format Format to match against
     * @param timeToValidate Time field to validate
     * @return date object if time passed in is valid
     */
    public static Date isTimeFormatValid(String format, String timeToValidate)  {
        if (timeToValidate.isEmpty() || timeToValidate.length()> 5){ // h:mm max hh:mm 5 char
            return null;
        }
        int colon = 0;
        for (int i = 0; i < timeToValidate.length(); i++){
            if (timeToValidate.charAt(i)==':' ){
                colon++;
            }
        }
        if (colon == 0 || colon > 1){
            return null;
        }
        String[] strs = timeToValidate.split(":");
        if (strs.length != 2){
            return null;
        }
        if (strs[0].length() > 2 || strs[0].length() < 1){ // check if first half of the string is valid
            return null;
        } 
        if (strs[1].length() != 2){ // has to be 2 char length
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false); // for strict matching 
        Date returnTime = null; //Format the Time to H:mm
        try {
            returnTime = formatter.parse(timeToValidate);
            
        }catch (ParseException e){
            
        }
        return returnTime;
    }

    //Check if instructor name's length is more than 100, considered as invalid
    /**
     * Method to check if instructor is valid
     * @param instructorName Instructor name
     * @return empty string if instructor is valid else return error message, "invalid instructor"
     */
    public static String isValidInstructor(String instructorName) {
        if (instructorName.length() > 100) {
            return "invalid instructor";
        }

        return null;
    }
    /**
     * Method to check if venue is valid
     * @param venue Venue
     * @return empty string if venue is valid else return error message, "invalid venue"
     */
    public static String isValidVenue(String venue) {
        if (venue.length() > 100) {
            return "invalid venue";
        }
        return null;
    }
    /**
     * Method to check if venue is not occupied by another section at the same time and date
     * @param venue Venue of the lesson
     * @param day Day of the week
     * @param startTime Start time of the lesson
     * @param endTime End time of the lesson
     * @return true if venue is not occupied else return false
     */
    public static boolean venueIsNotOccupied(String venue, int day, Date startTime, Date endTime){
         // loop through all sections get the venue and day time to see if they clash
        Set<String> courses  = BootstrapController.COURSESECTION.keySet();
        if (startTime != null && endTime != null){
            for (String sectInCourse : courses){
                ArrayList<Section> sections = BootstrapController.COURSESECTION.get(sectInCourse);
                for (Section sect : sections){
                    int sectionDay = sect.getDayOfWeek();
                    Date sectionStart = sect.getStartTime();
                    Date sectionEnd = sect.getEndTime();
                    String sectionVenue = sect.getVenue();
                    if (sectionDay == day){
                        if (sectionVenue.equals(venue)){
                            if (!(sectionEnd.before(startTime) || endTime.before(sectionStart))){
                                return false;
                            }
                        }
                    }
                }
            }            
        }
        return true;
    }
    
    //Assuming there is no plus sign in front of positive number, check if number is positive
    /**
     * Method to check if section is valid
     * @param size Size of section to validate
     * @return empty string if section size is valid else return error message, "invalid size"
     */    
    public static String isValidSize(String size) {
       /* if (size.length() > 2){
            return "invalid size";
        }  */
        boolean isValid = true;
        for (int i = 0; i < size.length(); i++) {
            char ch = size.charAt(i);
            //if '-' is present, set isValid=false
            if (!((ch >= '0' && ch <= '9'))) {
                isValid = false;
            }
        }
        
        // check if first number is zero it is invalid
        if(size.charAt(0) == '0'){  
            isValid = false;
        } 
   
        if (!isValid) {
            return "invalid size";
        }
        return null;
    }

}
