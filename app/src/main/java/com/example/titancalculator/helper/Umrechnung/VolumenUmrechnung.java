package com.example.titancalculator.helper.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class VolumenUmrechnung {

    private static HashMap<String, BigDecimal> Volumen = new LinkedHashMap<>();

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
        Volumen.put("Liter", new BigDecimal("1"));
        Volumen.put("Milliliter", new BigDecimal("1000"));
        Volumen.put("Imperialer gallon", new BigDecimal("0.219969"));
        Volumen.put("Imperialer quart", new BigDecimal("0.879877"));
        Volumen.put("Imperialer pint", new BigDecimal("1.75975"));
        Volumen.put("Imperialer cup", new BigDecimal("3.51951"));
        Volumen.put("Imperialer fluid ounce", new BigDecimal("35.1951"));
        Volumen.put("Imperialer tablespoon", new BigDecimal("56.3121"));
        Volumen.put("Imperialer teaspoon", new BigDecimal("168.936"));
        Volumen.put("kubischer foot", new BigDecimal("0.0353147"));
        Volumen.put("kubischer inch", new BigDecimal("61.0237"));
        Volumen.put("US gallon", new BigDecimal("0.264172"));
        Volumen.put("US quart", new BigDecimal("0.879877"));
        Volumen.put("US pint", new BigDecimal("2.11338"));
        Volumen.put("US cup", new BigDecimal("4.16667"));
        Volumen.put("US fluid ounce", new BigDecimal("33.814"));
        Volumen.put("US tablespoon", new BigDecimal("67.628"));
        Volumen.put("US teaspoon", new BigDecimal("202.884"));

        unitsDE = (String[]) Volumen.keySet().toArray(new String[Volumen.size()]);
        unitsEN = new String[]{"liter", "milliliter", "emperial gallon", "emperial quart,emperial pint", "emperial cup", "emperial fluid ounce", "emperial tablespoon emperial teaspoon", "kubischer foot", "kubischer inch", "US gallon", "US quart", "US pint", "US cup", "US fluid ounce", "US tablespoon", "US teaspoon"};
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
         source = translate(source); target = translate(target);
         if(Volumen.keySet().size()==0)init();
        if(!Volumen.keySet().contains(source))return a.toString();
        if(!Volumen.keySet().contains(target))return a.toString();
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

        //System.out.println(Arrays.toString(Volumen.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

         System.out.println(convert(new BigDecimal("2"),"Imperialer quart","Imperialer cup"));
     }
}
