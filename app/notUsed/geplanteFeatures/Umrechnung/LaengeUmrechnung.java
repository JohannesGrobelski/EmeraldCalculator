package com.example.titancalculator.geplanteFeatures.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedHashMap;

public class LaengeUmrechnung {

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
        toHigher.put("Nanometer", new BigDecimal("1000"));
        toHigher.put("Mikrometer", new BigDecimal("1000"));
        toHigher.put("Millimeter", new BigDecimal("10"));
        toHigher.put("Centimeter", new BigDecimal("2.54"));
         toHigher.put("Zoll",  new BigDecimal("3.937"));
         toHigher.put("Dezimeter",  new BigDecimal("3.048"));
         toHigher.put("Fuß",  new BigDecimal("3.281"));
         toHigher.put("Meter",  new BigDecimal("1.094"));
         toHigher.put("Yard",  new BigDecimal("2025.372"));
         toHigher.put("Seemeile",  new BigDecimal("1.151"));
         toHigher.put("Meile",  null);


        toLower.put("Nanometer", null);
        toLower.put("Mikrometer", new BigDecimal("1000"));
        toLower.put("Millimeter", new BigDecimal("1000"));
        toLower.put("Centimeter", new BigDecimal("10"));
        toLower.put("Zoll",  new BigDecimal("2.54"));
        toLower.put("Dezimeter",  new BigDecimal("3.937"));
        toLower.put("Fuß",  new BigDecimal("3.048"));
        toLower.put("Meter",  new BigDecimal("3.281"));
        toLower.put("Yard",  new BigDecimal("1.094"));
        toLower.put("Seemeile",  new BigDecimal("2025.372"));
        toLower.put("Meile",  new BigDecimal("1.151"));

         unitsDE = (String[]) toHigher.keySet().toArray(new String[toHigher.size()]);
         unitsEN = new String[]{"nanometer","micrometer","millimeter","centimeter","inch","decimeter","foot","meter","yard","seamile","mile"};
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
        init();
     }
}
