package com.example.titancalculator.geplanteFeatures.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LeistungUmrechnung {

    private static HashMap<String, BigDecimal> Leistung = new LinkedHashMap<>();

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
        Leistung.put("Watt", new BigDecimal("1"));
        Leistung.put("Kilowatt", new BigDecimal("0.001"));
        Leistung.put("Milliwatt", new BigDecimal("1000"));
        Leistung.put("Pferdestärke", new BigDecimal("0.001359622"));
        Leistung.put("Volt Ampere", new BigDecimal("1"));
        Leistung.put("Joule/Sekunde", new BigDecimal("1"));
        Leistung.put("Kilojoule/Sekunde", new BigDecimal("0.001"));

        unitsDE = (String[]) Leistung.keySet().toArray(new String[Leistung.size()]);
        unitsEN = new String[]{"watt", "kilowatts", "millwatt", "horsepower", "volt ampere", "joule/second", "kilojoules/second"};
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Leistung.keySet().contains(source))return null;
        return a.divide(Leistung.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Leistung.keySet().contains(target))return null;
        return a.multiply(Leistung.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
         source = translate(source); target = translate(target);
         if(Leistung.keySet().size()==0)init();
        if(!Leistung.keySet().contains(source))return a.toString();
        if(!Leistung.keySet().contains(target))return a.toString();
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

        //System.out.println(Arrays.toString(Leistung.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

     }
}
