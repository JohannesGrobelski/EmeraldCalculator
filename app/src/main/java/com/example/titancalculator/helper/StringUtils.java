package com.example.titancalculator.helper;

import java.lang.reflect.Array;

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

    public static void main(String[] a){
        String ges = "112233";
        String repl = "55555";
        System.out.println(replace(ges,repl,ges.length(),ges.length()));
    }
}
