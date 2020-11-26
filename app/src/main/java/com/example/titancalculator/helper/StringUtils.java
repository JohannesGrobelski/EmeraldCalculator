package com.example.titancalculator.helper;

import android.net.http.SslCertificate;

import com.example.titancalculator.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

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

    public static int occurences(String s, String sub){
        String temp = s.replace(sub, "");
        return (s.length() - temp.length()) / sub.length();
    }

    // Function to insert string
    public static String insertString(String originalString,String stringToBeInserted,int index) {
        // Create a new string
        String newString = originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);

        // return the modified String
        return newString;
    }

    public static String replace(String source, String replacement , int start, int end){
        if(source == null || replacement == null)return source;
        if(replacement.isEmpty() || start < 0)return source;

        return source.substring(0,start)+replacement+source.substring(end);
    }

    public static String repeat(String s, int k){
        if(k<0)return "";
        StringBuilder res = new StringBuilder("");
        for(int i=0; i<k; i++){
            res.append(s);
        }
        return res.toString();
    }
    /**
     * Splits the inputstring by delimiters
     * @param input
     * @return
     */
    public static String[] split(String input, String[] delimiters){
        //TODO: if delimiter does not exist => infinite loop
        if(input.length() < 1)return new String[]{input};
        //System.out.println("split: \""+input+"\"");
        LinkedList<String> output = new LinkedList<>();
        while(input.length() > 0){
            //System.out.println(" splitInput: "+input);
            //System.out.println(" splitArray: "+Arrays.toString(output.toArray(new String[output.size()])));
            for(String delimiter: delimiters){
                if(input.startsWith(delimiter)){
                    output.add(delimiter);
                    input = input.substring(delimiter.length());
                }
            }
        }
        return output.toArray(new String[output.size()]);
    }

    public static String randomString(int length){
        StringBuilder result = new StringBuilder();
        while(length > 0){
            String add = String.valueOf(Character.valueOf((char) (Math.random()*256)));
            result.append(add);
            length--;
        }
        return result.toString();
    }

}
