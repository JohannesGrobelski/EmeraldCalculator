package com.example.calcitecalculator.geplanteFeatures.notUsedHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtils {
    public static boolean array_contains(String AR[], String S){
        for(String s: AR){
            if(s.equals(S))return true;
        }
        return false;
    }

    public static ArrayList<String> sublistLastN(ArrayList<String> list, int cap){
        if(list == null || cap > list.size())return list;
        ArrayList<String> res = new ArrayList<>();
        for(int i=list.size() - cap;i<list.size(); i++){
            res.add(list.get(i));
        }
        return res;
    }

    public static String listToString(List<String> array){
        if(array == null)return "";
        if(array.isEmpty())return"";
        String output="";
        for(int i=0; i<array.size()-1; i++)output+=array.get(i)+"_";
        output+=array.get(array.size()-1);
        return output;
    }

    public static ArrayList<String> stringToList(String array_string){
        if(array_string==null)return new ArrayList<>();
        if(array_string=="")return new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.addAll(new LinkedList<String>(Arrays.asList(array_string.split("_"))));
        return list;
    }

    public static String arrayToString(String[] array){
        if(array == null)return "";
        if(array.length == 0)return"";
        String output="";
        for(int i=0; i<array.length-1; i++)output+=array[i]+"_";
        output+=array[array.length-1];
        return output;
    }

    public static String[] stringToArray(String array_string){
        if(array_string==null)return new String[0];
        if(array_string=="")return new String[0];
        String[] array = (java.lang.String[]) Arrays.asList(array_string.split("_")).toArray();
        return array;
    }

    public static void main(String[] a){

        String[] ar = stringToArray("a_b");
        for(String l: ar){
            System.out.println(l);
        }

    }
}
