package com.example.titancalculator.helper.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FlächenUmrechnung {

    private static HashMap<String, BigDecimal> Fläche = new LinkedHashMap<>();

    private static String[] units;


    public static String[] getUnits(){
        if(units == null)init();
        return units;
    }

    private static void init(){
        Fläche.put("Quadratmeter", new BigDecimal("1"));
        Fläche.put("Quadratzentimeter", new BigDecimal("10000"));
        Fläche.put("Quadratdezimeter", new BigDecimal("100"));
        Fläche.put("Ar", new BigDecimal("0.01"));
        Fläche.put("Hektar", new BigDecimal("0.0001"));
        Fläche.put("Quadratkilometer", new BigDecimal("0.000001"));
        Fläche.put("Quadratzoll", new BigDecimal("1550.0031000062"));
        Fläche.put("Quadratfuss", new BigDecimal("10.7639104167097"));
        Fläche.put("Quadratyard", new BigDecimal("13.1558905093119"));
        Fläche.put("Acre", new BigDecimal("0.000247105381467165"));
        Fläche.put("Quadratmeilen", new BigDecimal("0.000000386102158542446"));
        Fläche.put("Barn", new BigDecimal("1e+28"));
        Fläche.put("Rai", new BigDecimal("0.000625"));
        Fläche.put("Stremma", new BigDecimal("0.001"));

        units = (String[]) Fläche.keySet().toArray(new String[Fläche.size()]);
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Fläche.keySet().contains(source))return null;
        return a.divide(Fläche.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Fläche.keySet().contains(target))return null;
        return a.multiply(Fläche.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
        if(Fläche.keySet().size()==0)init();
        if(!Fläche.keySet().contains(source))return a.toString();
        if(!Fläche.keySet().contains(target))return a.toString();
        return baseToTarget(sourceToBase(a,source),target);
     }

     public static void main(String[] a){

        //System.out.println(Arrays.toString(Fläche.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

         System.out.println(convert(new BigDecimal("817.2"),"Kilometer/Stunde","Mach (SI Standard)"));
     }
}
