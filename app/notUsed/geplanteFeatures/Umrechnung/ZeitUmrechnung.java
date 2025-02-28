package com.example.titancalculator.geplanteFeatures.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedHashMap;

public class ZeitUmrechnung {

    private static LinkedHashMap<String, BigDecimal> toHigher = new LinkedHashMap<>();
    private static LinkedHashMap<String, BigDecimal> toLower = new LinkedHashMap<>();

    private static String[] unitsDE,unitsEN;


    public static String[] getunitsDE(){
        if(unitsDE == null)init();
        return unitsDE;
    }

    public static String[] getunitsEN(){
        if(unitsEN == null)init();
        return unitsEN;
    }

    private static void init(){
         toHigher.put("Nanosekunde", new BigDecimal("1000"));
         toHigher.put("Mikrosekunde", new BigDecimal("1000"));
         toHigher.put("Millisekunde", new BigDecimal("1000"));
         toHigher.put("Sekunde", new BigDecimal("60"));
         toHigher.put("Minute",  new BigDecimal("60"));
         toHigher.put("Stunde",  new BigDecimal("24"));
         toHigher.put("Tag",  new BigDecimal("7"));
         toHigher.put("Woche",  new BigDecimal("30"));
         toHigher.put("Monate",  new BigDecimal("12"));
         toHigher.put("Jahr",  new BigDecimal("10"));
         toHigher.put("Dekade",  new BigDecimal("10"));
         toHigher.put("Jahrhunderte", new BigDecimal("10"));
         toHigher.put("Jahrtausende", null);

         toLower.put("Nanosekunde", null);
         toLower.put("Mikrosekunde", new BigDecimal("1000"));
         toLower.put("Millisekunde", new BigDecimal("1000"));
         toLower.put("Sekunde", new BigDecimal("1000"));
         toLower.put("Minute",  new BigDecimal("60"));
         toLower.put("Stunde",  new BigDecimal("60"));
         toLower.put("Tag",  new BigDecimal("24"));
         toLower.put("Woche",  new BigDecimal("7"));
         toLower.put("Monate",  new BigDecimal("12"));
         toLower.put("Jahr",  new BigDecimal("10"));
         toLower.put("Dekade",  new BigDecimal("10"));
         toLower.put("Jahrhunderte", new BigDecimal("10"));
         toLower.put("Jahrtausende", new BigDecimal("10"));

         unitsDE = (String[]) toHigher.keySet().toArray(new String[toHigher.size()]);
         unitsEN = new String[]{"nanosecond","microsecond","millisecond","second","minute","hour","day","week","month","year","decade","century","millennial"};
     }

     private static int searchUnit(String s){
        for(int i=0; i<unitsDE.length; i++){
            if(s.equals(unitsDE[i]))return i;
        }
         for(int i=0; i<unitsEN.length; i++){
             if(s.equals(unitsEN[i]))return i;
         }
        return -1;
     }

     public static BigDecimal convert(BigDecimal a, String source, String target){
        if(toLower.size() == 0)init();
        int sourceInd = searchUnit(source);
        int targetInd = searchUnit(target);
        System.out.println(unitsDE[targetInd]);
        System.out.println(sourceInd+" "+targetInd);
         if(sourceInd == -1 || targetInd == -1 )return a;
        else if(targetInd > sourceInd){
            int measInd = sourceInd;
            while(measInd != targetInd){
                //System.out.println(unitsDE[measInd]);

                a = a.divide(toHigher.get(unitsDE[measInd]), MathContext.DECIMAL128);
                ++measInd;
            }
        } else if (targetInd < sourceInd){
            int measInd = sourceInd;
            while(measInd != targetInd){
                a = a.multiply(toLower.get(unitsDE[measInd]), MathContext.DECIMAL128);
                --measInd;
            }
        }
        return a;
     }

     public static void main(String[] a){
         System.out.println(convert(new BigDecimal("300"),"Sekunde","Tag"));
         System.out.println(convert(new BigDecimal("22"),"Monate","Nanosekunde"));

     }
}
