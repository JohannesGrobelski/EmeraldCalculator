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

    public static String replace(String source, String replacement , int start, int end){
        if(source == null || replacement == null)return source;
        if(replacement.isEmpty() || start < 0)return source;

        return source.substring(0,start)+replacement+source.substring(end);
    }

    public static void main(String[] a){
        String ges = "112233";
        String repl = "55555";
        System.out.println(replace(ges,repl,ges.length(),ges.length()));
    }
}
