/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Bid;
import entity.Section;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import utility.ConnectionManager;

/**
 * Class that handles all database operations pertaining to the Bid Table
 *
 * @author ChenHuiYan and Haseena
 */
/**
 * A java DAO that access and modifies the Bid table in database.
 * @author Aloysius, Cheryl
 */
public class BidDAO {

    /**
     * Method to retrieve bids by userid from the database
     *
     * @param userId student id
     * @return arraylist of bids by user
     */
    public static ArrayList<Bid> getBidsByUserId(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> bidListByuserId = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                bid = new Bid(userId, courseId, sectionCode, amount, status);

                bidListByuserId.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return bidListByuserId;
    }

    /**
     * Method to retrieve unsuccessful bids by user
     *
     * @param userId student id
     * @return Arraylist of unsuccessful bids by user
     */
    public static ArrayList<Bid> getUnsuccessfulBidsByUserId(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> unsuccessfulBidListByUserId = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where user_id = ? and status =? ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, "Fail"); // get unsuccessfulBid by checking the status
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                String status = "Fail";
                bid = new Bid(userId, courseId, sectionCode, amount, status);

                unsuccessfulBidListByUserId.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return unsuccessfulBidListByUserId;
    }

    /**
     * Method to add bid into database with parameters coursecode,
     * sectionid,userid,bidamount,status
     *
     * @param courseCode course that student is bidding for
     * @param sectionId section id of course that student is bidding for
     * @param userId student id
     * @param bidAmount bid amount
     */
    public static void addBid(String courseCode, String sectionId, String userId, double bidAmount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {

            conn = ConnectionManager.getConnection();
            sql = "INSERT into bid values(?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setDouble(2, bidAmount);
            stmt.setString(3, courseCode);
            stmt.setString(4, sectionId);
            stmt.setString(5, "PENDING");

            stmt.executeUpdate();

            /*  if(status.equals("Success")){
                PreparedStatement stmt2 = null;
                String sql2="";
                sql2 = "INSERT into section_student values(?,?,?)";
                stmt2 = conn.prepareStatement(sql2);
                stmt2.setString(1, userId);
                stmt2.setString(2, courseCode);
                stmt2.setString(3, sectionId);


                stmt.executeUpdate();   // if the status shows success, the data will be inserted into section_student


            }   */
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

    }

    /**
     * Method to deletebid based on parameters of course code,section id, user
     * id
     *
     * @param courseCode course id of course
     * @param sectionId section id of course
     * @param userId student id
     */
    public static void deleteBid(String courseCode, String sectionId, String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        Bid bid = getBid(sectionId, courseCode, userId);

        //if(bid!=null){
        try {
            String status = bid.getBidStatus();
            conn = ConnectionManager.getConnection();
            sql = "DELETE from bid where user_id=? and course_id=? and section_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionId);

            stmt.executeUpdate();

            /*    if(status.equals("Success")){
                    PreparedStatement stmt2 = null;
                    String sql2="";
                    sql2 = "DELETE from section_student where user_id=? and course_id=? and section_id=?";
                    stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, userId);
                    stmt2.setString(2, courseCode);
                    stmt2.setString(3, sectionId);


                stmt.executeUpdate();
                    
                    
                }  // if the status shows success, the data will be deleted from section_student
                
             */
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        //}
    }

    /**
     * Retrieves whether student has bidded for the specified course
     *
     * @param courseCode course that is being checked against
     * @param userId student id
     * @return bid if student has bidded for the course before
     */
    public static Bid getBidForCourse(String courseCode, String userId) { //check whether the user has bidded for the specified course
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where user_id = ? and course_id =? ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, courseCode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                Double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                bid = new Bid(userId, courseCode, sectionCode, amount, status);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return bid;
    }

    /**
     * Method to retrieve bid based on section id, course id and user id
     *
     * @param sectionId section id of course to be retrieved
     * @param courseCode course code of course to be retrieved
     * @param userId student id
     * @return bid based on the three parameters directly above
     */
    public static Bid getBid(String sectionId, String courseCode, String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where user_id = ? and course_id =? and section_id=? ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, courseCode);
            stmt.setString(3, sectionId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                Double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                bid = new Bid(userId, courseCode, sectionId, amount, status);

            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            ConnectionManager.close(conn, stmt, rs);
            return bid;
        }

    }

    /**
     * Method to update bid based on parameters of user id, coursecode, section
     * id, bid amount
     *
     * @param userId student id
     * @param courseCode course code of course which has the bid being updated
     * @param sectionId section code of course which has the bid being updated
     * @param amount bid amount
     */
    public static void updateBid(String userId, String courseCode, String sectionId, double amount) {
        deleteBid(courseCode, sectionId, userId);
        addBid(courseCode, sectionId, userId, amount);
    }

    /**
     * Method to clear bid table by truncating table
     *
     */
    public static void clearBidTable() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE bid");
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
    }

    /**
     * Method to retrieve bids that user has been enrolled for
     *
     * @param userId student id
     * @return arraylist of bids that student has been enrolled for
     */
    public static ArrayList<Bid> getEnrolled(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> enrolledBids = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where user_id = ? and status =? ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, "Success");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                String status = "Success";
                bid = new Bid(userId, courseId, sectionCode, amount, status);

                enrolledBids.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return enrolledBids;
    }

    // given numVacancy return number of bids at the clearing price
    /**
     * Method to get number of bids at clearing price
     *
     * @param courseCode Course code of course that has number of bids at
     * clearing price being retrieved
     * @param sectionCode section code of course that has number of bids at
     * clearing price being retrieved
     * @param numVacancy number of vacancies remaining for course
     * @return the number of bids at clearing price for section of the course
     */
    public static int getNumBidsAtClearingPrice(String courseCode, String sectionCode, int numVacancy) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        int numBids = 0;

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT COUNT(*) FROM bios_database.bid WHERE course_id = ? AND section_id = ? AND amount = (SELECT MIN(get_min_bid.amount) FROM (SELECT * FROM bios_database.bid WHERE course_id = 'IS100' AND section_id = 'S1' ORDER BY amount DESC LIMIT ? ) AS get_min_bid)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            stmt.setInt(3, numVacancy);
            rs = stmt.executeQuery();
            rs.next();
            numBids = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return numBids;
    }

    /**
     * Method to retrieve the next higher bid
     *
     * @param courseCode Coursecode of course that next higher bid amount should
     * be displayed
     * @param sectionCode Sectioncode of course that next higher bid amount
     * should be displayed
     * @param amount Bid amount that the retrieved bid amount should be greater
     * than
     * @return bid amount that is higher than amount passed in
     */
    public static double getNextHigherBidAmount(String courseCode, String sectionCode, double amount) {
        double nextBidAmt = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT amount FROM bios_database.bid WHERE course_id = ? AND section_id = ? AND amount > ? LIMIT 1";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            stmt.setDouble(3, amount);
            rs = stmt.executeQuery();
            rs.next();
            nextBidAmt = rs.getDouble(1);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return nextBidAmt;
    }

    // DECIDED NOT TO INCLUDE
    // returns the min bid at the cut off row
    /**
     * Method to retrieve clearing price of the section of the course
     *
     * @param courseCode Course code of the course
     * @param sectionCode Section code of the course
     * @param vacancies vacancies remaining for the section of the code
     * @return clearing price of the section of the course
     */
    public static double getClearingPrice(String courseCode, String sectionCode, int vacancies) {
        // set default min bid to $10
        double minBidAmt = 10;
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT MIN(get_min_bid.amount) FROM (SELECT * FROM bios_database.bid WHERE course_id = ? AND section_id = ? ORDER BY amount DESC LIMIT ?) AS get_min_bid";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            stmt.setInt(3, vacancies);
            rs = stmt.executeQuery();
            rs.next();
            minBidAmt = rs.getDouble(1);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return minBidAmt;
    }

    // updates the status of bids to successful and failed depending on minBid amount
    // everything bigger or equals to minBid is successful
    /**
     * Method to update the status of bids depending on minBid amount, if its
     * bigger or equal to minBid then it is successful
     *
     * @param courseCode course code of the course that has its bids' status
     * being updated
     * @param sectionCode section code of the course that has its bids being
     * updated
     * @param minBid minimum bid of section of course that has its bids being
     * updated
     */
    public static void roundBidStatusUpdate(String courseCode, String sectionCode, double minBid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "UPDATE bios_database.bid SET status = 'SUCCESS' WHERE course_id = ? AND section_id = ? AND amount >= ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            stmt.setDouble(3, minBid);
            stmt.executeUpdate();

            sql = "UPDATE bios_database.bid SET status = 'FAILED' WHERE course_id = ? AND section_id = ? AND amount < ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            stmt.setDouble(3, minBid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

    }

    /**
     * Method to get number of bids for the section of a course
     *
     * @param courseCode course code of course
     * @param sectionCode section code of course
     * @return number of bids for the section of the course
     */
    public static int getNumBidsForSection(String courseCode, String sectionCode) {
        int total = 0;

        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT COUNT(*) FROM bios_database.bid WHERE course_id = ? AND section_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setString(2, sectionCode);
            rs = stmt.executeQuery();
            rs.next();
            total = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return total;
    }

    /**
     * Method to get all sections in bid table
     *
     * @return ArrayList of sections that have been grouped by course id and
     * section id
     */
    public static ArrayList<Section> getAllSectionsInBidTable() {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        ArrayList<Section> sectionsInBidTable = new ArrayList<>();
        try {
            conn = ConnectionManager.getConnection();
            sql = "SELECT course_id, section_id FROM bios_database.bid GROUP BY course_id, section_id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String courseCode = rs.getString("course_id");
                String sectionCode = rs.getString("section_id");

                sectionsInBidTable.add(new Section(courseCode, sectionCode));
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return sectionsInBidTable;
    }

    /**
     * Method to retrieve the bids that have failed
     *
     * @return arraylist of bids that have failed
     */
    public static ArrayList<Bid> getBidsThatFailed() {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;

        ArrayList<Bid> failedBids = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where user_id = 'Failed'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                bid = new Bid(userId, courseId, sectionCode, amount, status);

                failedBids.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return failedBids;
    }
/**
 * Gets an arrayList of successful bids.
 * @return an arrayList of bid which contains the successful bids
 */
    public static ArrayList<Bid> getSuccessfulBids() {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "";
        ResultSet rs = null;
        ArrayList<Bid> successfulBids = new ArrayList();

        Bid bid = null;

        try {
            conn = ConnectionManager.getConnection();
            sql = "select * from bid where status = 'SUCCESS' ";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String sectionCode = rs.getString("section_id");
                String courseId = rs.getString("course_id");
                Double amount = rs.getDouble("amount");
                String status = "SUCCESS";
                bid = new Bid(userId, courseId, sectionCode, amount, status);

                successfulBids.add(bid);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return successfulBids;
    }
    // public static double getRoundTwoLowestSuccessfulBidPrice(String courseCode, String sectionCode, int vacancy){
    // }
}
