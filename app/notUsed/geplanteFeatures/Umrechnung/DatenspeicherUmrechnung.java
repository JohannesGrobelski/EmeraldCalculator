package com.example.calcitecalculator.geplanteFeatures.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DatenspeicherUmrechnung {

    private static HashMap<String, BigDecimal> Datenspeicher = new LinkedHashMap<>();

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
        Datenspeicher.put("Bit", new BigDecimal("1"));
        Datenspeicher.put("Kilobit", new BigDecimal("0.001"));
        Datenspeicher.put("Kibibit", new BigDecimal("0.000976563"));
        Datenspeicher.put("Megabit", new BigDecimal("1e-6"));
        Datenspeicher.put("Mebibit", new BigDecimal("9.5367e-7"));
        Datenspeicher.put("Gigabit", new BigDecimal("1e-9"));
        Datenspeicher.put("Gibibit", new BigDecimal("9.3132e-10"));
        Datenspeicher.put("Terabit", new BigDecimal("1e-12"));
        Datenspeicher.put("Tebiti", new BigDecimal("9.0949e-13"));
        Datenspeicher.put("Petabit", new BigDecimal("1e-15"));
        Datenspeicher.put("Pebibit", new BigDecimal("8.8818e-16"));
        Datenspeicher.put("Kilobyte", new BigDecimal("0.000125"));
        Datenspeicher.put("Kibibyte", new BigDecimal("0.00012207"));
        Datenspeicher.put("Megabyte", new BigDecimal("1.25e-7"));
        Datenspeicher.put("Mebibyte", new BigDecimal("1.1921e-7"));
        Datenspeicher.put("Gigabyte", new BigDecimal("1.25e-10"));
        Datenspeicher.put("Gibibyte", new BigDecimal("1.1642e-10"));
        Datenspeicher.put("Terabyte", new BigDecimal("1.25e-13"));
        Datenspeicher.put("Tebibyte", new BigDecimal("1.1369e-13"));

        unitsDE = (String[]) Datenspeicher.keySet().toArray(new String[Datenspeicher.size()]);
        unitsEN = new String[]{"bit", "kilobit", "kibibit", "megabit", "mebibit", "gigabit", "gibibit", "terabit", "tebibit", "petabit", "pebibit", "byte", "kilobyte", "kibibyte", "megabyte", "mebibyte", "gigabyte", "gibibyte", "terabyte", "tebibyte"};
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
         source = translate(source); target = translate(target);
         if(Datenspeicher.keySet().size()==0)init();
        if(!Datenspeicher.keySet().contains(source))return a.toString();
        if(!Datenspeicher.keySet().contains(target))return a.toString();
        return baseToTarget(sourceToBase(a,source),target);
     }

    public static String translate(String measure){
        for(int i=0; i<unitsDE.length; i++){
            if(unitsDE[i].equals(measure))return unitsDE[i];
        }
        for(int i=0; i<unitsEN.length; i++) {
            if (unitsEN[i].equals(measure)) return unitsDE[i];
        }
        return "";
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
