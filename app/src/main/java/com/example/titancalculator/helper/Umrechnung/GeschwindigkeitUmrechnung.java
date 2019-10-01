package com.example.titancalculator.helper.Umrechnung;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GeschwindigkeitUmrechnung {

    private static HashMap<String, BigDecimal> Geschwindigkeit = new LinkedHashMap<>();

    private static String[] units;


    public static String[] getUnits(){
        if(units == null)init();
        return units;
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

        units = (String[]) Geschwindigkeit.keySet().toArray(new String[Geschwindigkeit.size()]);
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
        if(Geschwindigkeit.keySet().size()==0)init();
        if(!Geschwindigkeit.keySet().contains(source))return a.toString();
        if(!Geschwindigkeit.keySet().contains(target))return a.toString();
        return baseToTarget(sourceToBase(a,source),target);
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
