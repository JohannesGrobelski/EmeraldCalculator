package com.example.titancalculator.helper.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class KraftUmrechnung {

    private static HashMap<String, BigDecimal> Kraft = new LinkedHashMap<>();

    private static String[] units;


    public static String[] getUnits(){
        if(units == null)init();
        return units;
    }

    private static void init(){
        Kraft.put("Newton", new BigDecimal("1"));
        Kraft.put("Kilonewton", new BigDecimal("0.001"));
        Kraft.put("Millinewton", new BigDecimal("1000"));
        Kraft.put("Dyne", new BigDecimal("100000"));
        Kraft.put("Joule/Meter", new BigDecimal("1"));
        Kraft.put("Pond", new BigDecimal("101.971621298"));
        Kraft.put("Kilopond", new BigDecimal("0.101971621"));

        units = (String[]) Kraft.keySet().toArray(new String[Kraft.size()]);
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Kraft.keySet().contains(source))return null;
        return a.divide(Kraft.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Kraft.keySet().contains(target))return null;
        return a.multiply(Kraft.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
        if(Kraft.keySet().size()==0)init();
        if(!Kraft.keySet().contains(source))return a.toString();
        if(!Kraft.keySet().contains(target))return a.toString();
        return baseToTarget(sourceToBase(a,source),target);
     }

     public static void main(String[] a){

        //System.out.println(Arrays.toString(Kraft.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

     }
}
