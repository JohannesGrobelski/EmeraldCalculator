package com.example.titancalculator.helper;

public class ArrayUtils {
    public static boolean array_contains(String AR[], String S){
        for(String s: AR){
            if(s.equals(S))return true;
        }
        return false;
    }
}
