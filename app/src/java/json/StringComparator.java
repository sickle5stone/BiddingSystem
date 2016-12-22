/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.util.Comparator;

/**
 * Comparison of two strings
 * @author reganseah and Huiyan
 */
public class StringComparator implements Comparator<String> {
  /**
   * Comparison of two strings
   * @param s1 1st String
   * @param s2 2nd String
   * @return 0 if both strings are identical, -1 if 1st string is smaller than 2nd string, 1 if 1st string is larger than 2nd string
   */
  @Override
  public int compare(String s1, String s2) {

      String comS1 = s1.substring(s1.indexOf(" "), s1.length()-1);
      String comS2 = s2.substring(s2.indexOf(" "), s2.length()-1);
      
      return comS1.compareTo(comS2);
   }
}
