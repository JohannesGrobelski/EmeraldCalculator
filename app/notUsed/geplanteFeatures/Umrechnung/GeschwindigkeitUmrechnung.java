package com.example.calcitecalculator.geplanteFeatures.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GeschwindigkeitUmrechnung {

    private static HashMap<String, BigDecimal> Geschwindigkeit = new LinkedHashMap<>();

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
        Geschwindigkeit.put("Meter/Sekunde [m/s]", new BigDecimal("1"));
        Geschwindigkeit.put("Meter/Stunde [m/h]", new BigDecimal("3600"));
        Geschwindigkeit.put("Kilometer/Stunde", new BigDecimal("3.6"));
        Geschwindigkeit.put("Foot/Stunde [ft/h]", new BigDecimal("11811.023622047"));
        Geschwindigkeit.put("Yard/Stunde [yd/h]", new BigDecimal("3937.007874016"));
        Geschwindigkeit.put("Mile/Stunde [mi/h]", new BigDecimal("2.236936292"));
        Geschwindigkeit.put("Knoten [kt]", new BigDecimal("1.943844492"));
        Geschwindigkeit.put("Mach (SI Standard)", new BigDecimal("0.003389297"));

        unitsDE = (String[]) Geschwindigkeit.keySet().toArray(new String[Geschwindigkeit.size()]);
        unitsEN = new String[]{"meter/second [m/s]","meter/hour [m/h]","kilometers/hour","foot/hour [ft/h]","yard/Stunde [yd/h]","mile/hour [mi/h]","Knot [kt]","Mach (SI Standard)"};
     }

    public static BigDecimal sourceToBase(BigDecimal a, String source){
        if(!Geschwindigkeit.keySet().contains(source))return null;
        return a.divide(Geschwindigkeit.get(source), MathContext.DECIMAL128);
    }

    public static String baseToTarget(BigDecimal a, String target){
        if(a==null)return "";
        if(!Geschwindigkeit.keySet().contains(target))return null;
        return a.multiply(Geschwindigkeit.get(target), MathContext.DECIMAL128).toString();
    }

     public static String convert(BigDecimal a, String source, String target){
        source = translate(source); target = translate(target);
        if(Geschwindigkeit.keySet().size()==0)init();
        if(!Geschwindigkeit.keySet().contains(source))return a.toString();
        if(!Geschwindigkeit.keySet().contains(target))return a.toString();
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

        //System.out.println(Arrays.toString(Geschwindigkeit.keySet().toArray()));
         //System.out.println(getSystem("Minute","Minute"));
         //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
         //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
         //System.out.println(InitConvMap.getZeit().get("Minute"));

         System.out.println(convert(new BigDecimal("817.2"),"Kilometer/Stunde","Mach (SI Standard)"));
     }
}
