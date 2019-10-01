package com.example.titancalculator.helper.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class StromstärkeUmrechnung {

    private static HashMap<String, BigDecimal> Volumen = new LinkedHashMap<>();

    private static String[] units;


    public static String[] getUnits(){
        if(units == null)init();
        return units;
    }

    private static void init(){
        Volumen.put("Joule", new BigDecimal("1"));
        Volumen.put("Kilojoule", new BigDecimal("0.001"));
        Volumen.put("Gramkalorien", new BigDecimal("0.239006"));
        Volumen.put("Kilokalorien", new BigDecimal("0.000239006"));
        Volumen.put("Wattstunden", new BigDecimal("0.000277778"));
        Volumen.put("Kilowattstunden", new BigDecimal("2.7778e-7"));
        Volumen.put("Elektronenvolt", new BigDecimal("6.242e+18"));


        units = (String[]) Volumen.keySet().toArray(new String[Volumen.size()]);
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Volumen.keySet().contains(source))return null;
        return a.divide(Volumen.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Volumen.keySet().contains(target))return null;
        return a.multiply(Volumen.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
        if(Volumen.keySet().size()==0)init();
        if(!Volumen.keySet().contains(source))return a.toString();
        if(!Volumen.keySet().contains(target))return a.toString();
        return baseToTarget(sourceToBase(a,source),target);
     }

     public static void main(String[] a){

        //System.out.println(Arrays.toString(Volumen.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

         System.out.println(convert(new BigDecimal("817.2"),"Kilometer/Stunde","Mach (SI Standard)"));
     }
}
