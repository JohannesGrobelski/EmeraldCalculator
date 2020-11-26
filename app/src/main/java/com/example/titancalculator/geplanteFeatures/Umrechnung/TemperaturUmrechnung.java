package com.example.titancalculator.geplanteFeatures.Umrechnung;

import com.example.titancalculator.helper.Math_String.MathEvaluator;

import java.math.BigDecimal;
import java.util.Arrays;

public class TemperaturUmrechnung {


    private static String[] unitsDE = {"Celsius","Fahrenheit","Kelvin"};
    private static String[] unitsEN = {"celsius","fahrenheit","celvin"};


    public static String[] getunitsDE(){
        return unitsDE;
    }

    public static String[] getunitsEN(){
        return unitsEN;
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

    public static String convert(BigDecimal a, String source, String target){
        source = translate(source); target = translate(target);
        if(!Arrays.asList(unitsDE).contains(source))return a.toString();
        if(!Arrays.asList(unitsDE).contains(target))return a.toString();

        if(source.equals("Celsius") && target.equals("Fahrenheit")){
            return MathEvaluator.evaluate("("+a.toString()+"*(9/5)) + 32");
        } else if(source.equals("Fahrenheit") && target.equals("Celsius")){
             return MathEvaluator.evaluate("("+a.toString()+" - 32) * 5/9");
        } else if(source.equals("Celsius") && target.equals("Kelvin")){
             return MathEvaluator.evaluate(a.toString()+" + 273.15");
        } else if(source.equals("Kelvin") && target.equals("Celsius")){
             return MathEvaluator.evaluate(a.toString()+" - 273.15");
        } else if(source.equals("Fahrenheit") && target.equals("Kelvin")){
            return MathEvaluator.evaluate("(("+a.toString()+" - 32) * 5/9) + 273.15 ");
        } else if(source.equals("Kelvin") && target.equals("Fahrenheit")){
            return MathEvaluator.evaluate("(("+a.toString()+" - 273.15) * 9/5) + 32");
        } else return "ConversionActivity Error";

     }

     public static void main(String[] a){
         //System.out.println(convert(new BigDecimal("0"),"Fahrenheit","Kelvin"));
         System.out.println(convert(new BigDecimal("0"),"Kelvin","Fahrenheit"));
     }
}
