package com.example.titancalculator.helper.Math_String;

import android.net.http.SslCertificate;

import com.example.titancalculator.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    static String[] mathTokens= new String[]{
            "ASINH","ACOSH","ATANH",
            "MEAN",">A/B",">x\u207B\u00B9",">+/-","SINH","COSH","TANH",">DEG",">RAD",">BIN",">OCT",">DEC",">HEX","10^x","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC",
            "MIN","MAX","NCR","NPR","VAR",">M1",">M2",">M3",">M4",">M5",">M6","ANS","SIN","COS","TAN","COT","SEC","CSC","AND","OR","XOR","NOT","LOG","PFZ","GCD","LCM",
            ">%","LN","LB","Zn","Zb",
            "∑","∏","1","2","3","4","5","6","7","8","9","0",".",",","*","/","+","-","(",")","π","e","^","³√","√","³","²","!","S","M1","M2","M3","M4","M5","M6",

    };

    static String[] allTokens = StringUtils.concatenate(mathTokens,new String[]{"L","R"});


    /**
     * @param a
     * @param b
     * @param <T>
     * @return concatenate of array a and b (a+b)
     */
    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    /**
     * @param regex
     * @param s
     * @return finds longest match of regex in inputstring
     */
    public static String findLongestMatch(String regex, String s) {
        Pattern pattern = Pattern.compile("(" + regex + ")$");
        Matcher matcher = pattern.matcher(s);
        String longest = "";
        int longestLength = -1;
        for (int i = s.length(); i > longestLength; i--) {
            matcher.region(0, i);
            if (matcher.find() && longestLength < matcher.end() - matcher.start()) {
                longest = matcher.group();
                longestLength = longest.length();
            }
        }
        return longest;
    }

    /**
     * @param input
     * @param sub
     * @return #occurences of sub in input
     */
    public static int occurences(String input, String sub){
        String temp = input.replace(sub, "");
        return (input.length() - temp.length()) / sub.length();
    }

    /**
     * @param originalString
     * @param stringToBeInserted
     * @param index
     * @return inserts string at index
     */
    public static String insertString(String originalString,String stringToBeInserted,int index) {
        // Create a new string
        String newString = originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);

        // return the modified String
        return newString;
    }

    /**
     * @param input
     * @param replacement
     * @param start
     * @param end
     * @return replaces input[start..end] with replacement
     */
    public static String replace(String input, String replacement , int start, int end){
        if(start > end)return input;
        if(input == null || replacement == null)return input;
        if(replacement.isEmpty() || start < 0)return input;
        return input.substring(0,start)+replacement+input.substring(end);
    }

    /**
     * @param input
     * @param k
     * @return inputinput...input (k-times)
     */
    public static String repeat(String input, int k){
        if(k<0)return input;
        if(k==0)return "";
        if(k==1)return input;
        StringBuilder res = new StringBuilder("");
        for(int i=0; i<k; i++){
            res.append(input);
        }
        return res.toString();
    }


    /**
     * @param length
     * @return a random String with length
     */
    public static String randomString(int length){
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = -1;
            if((length - salt.length()) < 5){
                int dif = length - salt.length();
                while(index == -1 || mathTokens[index].length() != dif){
                    index = (int) (rnd.nextFloat() * mathTokens.length);
                }
            } else {
               index = (int) (rnd.nextFloat() * mathTokens.length);
            }
            salt.append(mathTokens[index]);
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String[] splitMathTokens(String input) {
        return split(input,mathTokens);
    }

    public static String[] splitTokens(String input) {
        return split(input,allTokens);
    }

     /**Splits the inputstring by an array of delimiters
     * @param input
     * @return
      * */
    private static String[] split(String input, String[] delimiters) {
        if (input.length() == 0) return new String[]{""};
        if(delimiters.length == 0) return new String[]{input};
        //System.out.println("split: \""+input+"\"");
        LinkedList<String> output = new LinkedList<>();


        while (input.length() > 0) {
            //System.out.println("   splitInput: "+input);
            //System.out.println("   splitArray: "+Arrays.toString(output.toArray(new String[output.size()])));
            for(String delimiter : delimiters) {
                //System.out.println("    "+input+" startsWith "+delimiter+": "+input.startsWith(delimiter));
                if (input.startsWith(delimiter)) {
                    output.add(delimiter);
                    input = input.substring(delimiter.length());
                    break;
                }
            }
        }
        return output.toArray(new String[output.size()]);
    }

    /**
     * @param input
     * @param start
     * @param end
     * @return input[0..start]+input[end,input.length]
     */
    public static String deleteSpan(String input, int start, int end){
        if(start < 0 || start > input.length() || end < 0 || end > input.length() || end < start){
            return input;
        }
        else{
            input = input.substring(0,start) + input.substring(end);
            return input;
        }
    }

    /**
     * Zähle kommas in richtiger Ebene
     * richtige Ebene klammer_offen - klammer_zu <= 1
     * @param eval: Input
     * @param posAnfang: star
     * @return
     */
	public static int getParameterNumber(String eval, int posAnfang){
	    if(occurences(eval,"(") != occurences(eval,")"))return -1;
	    int number_comma = 0;
	    int klammern_offen = 0; int klammern_geschlossen = 0;
	    while(posAnfang < eval.length()-2){
	        ++posAnfang;
	        char pos = eval.charAt(posAnfang);
	        if(pos == '(')++klammern_offen;
            if(pos == ')')++klammern_geschlossen;
            if(pos == ',' && klammern_offen - klammern_geschlossen <= 1)++number_comma;
        }
        return number_comma + 1;
    }
}
