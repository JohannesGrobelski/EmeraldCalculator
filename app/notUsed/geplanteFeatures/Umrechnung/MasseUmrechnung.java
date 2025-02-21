package com.example.titancalculator.geplanteFeatures.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MasseUmrechnung {

    private static HashMap<String, BigDecimal> Masse = new LinkedHashMap<>();

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
        Masse.put("Kilogramm", new BigDecimal("1"));
        Masse.put("Gramm", new BigDecimal("1000"));
        Masse.put("Milligramm", new BigDecimal("1000000"));
        Masse.put("Tonne", new BigDecimal("0.001"));
        Masse.put("Grain", new BigDecimal("15432.35835294143065060816"));
        Masse.put("Karat", new BigDecimal("5000"));
        Masse.put("Unze", new BigDecimal("35.27396194958041291567"));
        Masse.put("Feinunze", new BigDecimal("32.1507465686279805221"));
        Masse.put("Pfund", new BigDecimal("2"));
        Masse.put("Pound", new BigDecimal("2.20462262184877580722"));
        Masse.put("Troypound", new BigDecimal("2.67922888071899837684"));
        Masse.put("Stone", new BigDecimal("0.15747304441776970051"));

        unitsDE = (String[]) Masse.keySet().toArray(new String[Masse.size()]);
        unitsEN = new String[]{"kilogram", "gram", "milligram", "ton", "grain", "carat", "ounce", "fine ounce", "pound", "pound", "troypound", "stone"};
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Masse.keySet().contains(source))return null;
        return a.divide(Masse.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Masse.keySet().contains(target))return null;
        return a.multiply(Masse.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
         source = translate(source); target = translate(target);
         if(Masse.keySet().size()==0)init();
        if(!Masse.keySet().contains(source))return a.toString();
        if(!Masse.keySet().contains(target))return a.toString();
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

        //System.out.println(Arrays.toString(Masse.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

         System.out.println(convert(new BigDecimal("817.2"),"Kilometer/Stunde","Mach (SI Standard)"));
     }
}
