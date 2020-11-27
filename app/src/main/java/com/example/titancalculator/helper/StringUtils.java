package com.example.titancalculator.helper;

import android.net.http.SslCertificate;

import com.example.titancalculator.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
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
     * Splits the inputstring by an array of delimiters
     * @param input
     * @return
     */
    public static String[] split(String input, String[] delimiters){
        //sorts delimiters by decreasing length
        Arrays.sort(delimiters, new java.util.Comparator<String>() {
            @Override public int compare(String a, String b) {return -(a.length()-b.length());}
        });
        if(input.length() < 1)return new String[]{input};
        //System.out.println("split: \""+input+"\"");
        LinkedList<String> output = new LinkedList<>(); output.add(input);

        for(String delimiter: delimiters){
            LinkedList<String> step = new LinkedList<>();
            LinkedList<String> toRemove = new LinkedList<>();

            for(String candidate: output){
                ArrayList<String> split = new ArrayList<>(Arrays.asList(candidate.split(delimiter)));
                split.remove(candidate); split.remove("");
                if(!split.isEmpty())toRemove.add(candidate);
                step.addAll(split);
            }
            step.remove(delimiter);
            if(!step.isEmpty())output.remove(input);
            output.addAll(step);
            output.removeAll(toRemove);
            //System.out.println("output["+delimiter+"]: "+Arrays.toString(output.toArray()));
        }
        output.remove(input);
        System.out.println("end result: "+output.toString());
        return output.toArray(new String[output.size()]);


    }

    /**
     * @param length
     * @return a random String with length
     */
    public static String randomString(int length){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * alphabet.length());
            salt.append(alphabet.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
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

}
