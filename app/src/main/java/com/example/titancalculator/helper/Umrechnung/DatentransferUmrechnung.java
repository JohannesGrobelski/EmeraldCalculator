package com.example.titancalculator.helper.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DatentransferUmrechnung {

    private static HashMap<String, BigDecimal> Datenspeicher = new LinkedHashMap<>();

    private static String[] units;


    public static String[] getUnits(){
        if(units == null)init();
        return units;
    }

    private static void init(){
        Datenspeicher.put("Bit pro Sekunde", new BigDecimal("1"));
        Datenspeicher.put("Kilobit pro Sekunde", new BigDecimal("0.001"));
        Datenspeicher.put("Kibibit pro Sekunde", new BigDecimal("0.000976563"));
        Datenspeicher.put("Megabit pro Sekunde", new BigDecimal("1e-6"));
        Datenspeicher.put("Mebibit pro Sekunde", new BigDecimal("9.5367e-7"));
        Datenspeicher.put("Gigabit pro Sekunde", new BigDecimal("1e-9"));
        Datenspeicher.put("Gibibit pro Sekunde", new BigDecimal("9.3132e-10"));
        Datenspeicher.put("Terabit pro Sekunde", new BigDecimal("1e-12"));
        Datenspeicher.put("Tebiti pro Sekunde", new BigDecimal("9.0949e-13"));
        Datenspeicher.put("Petabit pro Sekunde", new BigDecimal("1e-15"));
        Datenspeicher.put("Pebibit pro Sekunde", new BigDecimal("8.8818e-16"));
        Datenspeicher.put("Kilobyte pro Sekunde", new BigDecimal("0.000125"));
        Datenspeicher.put("Kibibyte pro Sekunde", new BigDecimal("0.00012207"));
        Datenspeicher.put("Megabyte pro Sekunde", new BigDecimal("1.25e-7"));
        Datenspeicher.put("Mebibyte pro Sekunde", new BigDecimal("1.1921e-7"));
        Datenspeicher.put("Gigabyte pro Sekunde", new BigDecimal("1.25e-10"));
        Datenspeicher.put("Gibibyte pro Sekunde", new BigDecimal("1.1642e-10"));
        Datenspeicher.put("Terabyte pro Sekunde", new BigDecimal("1.25e-13"));
        Datenspeicher.put("Tebibyte pro Sekunde", new BigDecimal("1.1369e-13"));



        units = (String[]) Datenspeicher.keySet().toArray(new String[Datenspeicher.size()]);
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Datenspeicher.keySet().contains(source))return null;
        return a.divide(Datenspeicher.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Datenspeicher.keySet().contains(target))return null;
        return a.multiply(Datenspeicher.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
        if(Datenspeicher.keySet().size()==0)init();
        if(!Datenspeicher.keySet().contains(source))return a.toString();
        if(!Datenspeicher.keySet().contains(target))return a.toString();
        return baseToTarget(sourceToBase(a,source),target);
     }

     public static void main(String[] a){

        //System.out.println(Arrays.toString(Datenspeicher.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

         System.out.println(convert(new BigDecimal("817.2"),"Kilometer/Stunde","Mach (SI Standard)"));
     }
}
