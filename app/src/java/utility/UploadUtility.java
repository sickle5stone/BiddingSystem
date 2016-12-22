/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import com.opencsv.CSVReader;
import com.oreilly.servlet.MultipartRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * A class which handles CSV-reader bootstrap file reading
 *
 * @author Cheryl , Aloysius
 */
public class UploadUtility {
    private static String token = null;

    /**
     *
     */
    public static HashMap<String, String[]> headerList = null;

    /**
     *
     */
    public static HashMap<String, List<String[]>> dataList = null;

    /**
     * Reads the zipFile content and checks if file is empty and is of the
     * correct file type
     *
     * @param request request of type HttpServletRequest
     * @return true if the file reading operation is successful, otherwise
     * returns false
     */
    public static boolean readZipFile(HttpServletRequest request) {
        
        //Initialize variables
        headerList = new HashMap<>();
        dataList = new HashMap<>();
        MultipartRequest fr = null;
        
        //System.out.println("Read zip file is called.");
        try {
            fr = new MultipartRequest(request, System.getProperty("java.io.tmpdir"));
            
            token = fr.getParameter("token");
            if (fr != null) {
                while (fr.getFileNames().hasMoreElements()) {

                    //System.out.print(fr.getFileNames().nextElement());
                    //name of the input type="file" from fileupload.jsp
                    String zipName = (String) fr.getFileNames().nextElement();

                    //check if extension is a zip file, return null if extension is not zip
                    String extension = fr.getFile(zipName).getName().substring(fr.getFile(zipName).getName().indexOf('.') + 1);
                    if (extension == null || !extension.equals("zip")) {
                        return false;
                    }

                    //open file in zip input stream
                    ZipInputStream zipStream = new ZipInputStream(new FileInputStream(fr.getFile(zipName)));

                    //check CSV file name
                    ZipEntry check = null;

                    //String[] s = null;
                    //check = file name (csv file)
                    while ((check = zipStream.getNextEntry()) != null) {
                        //traverse all folders detected
                        while (check.isDirectory()) {
                            check = zipStream.getNextEntry();
                        }

                        InputStreamReader isr = new InputStreamReader(zipStream, "UTF-8");
                        BufferedReader in = new BufferedReader(isr);
                        CSVReader reader = new CSVReader(in);

                        //Hashmap to return to servlet
                        String nameOfFile = check.getName();
                        //get name of file from last directory
                        int findSlash = nameOfFile.lastIndexOf("/");
                        nameOfFile = nameOfFile.substring(findSlash + 1);

                        String[] headersArray = null;
                        headersArray = in.readLine().split(",");
                        headerList.put(nameOfFile, headersArray);
                        dataList.put(nameOfFile, reader.readAll());

                    }
                    boolean test = !dataList.isEmpty() && !headerList.isEmpty();
                    return (test);
                }
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    /**
     * Get token of the multipart request to authenticate JSON bootstrap (Only used during JSON bootstrap)
     * @return token authentication token
     */
    public static String getToken(){
        return token;
    }
}
