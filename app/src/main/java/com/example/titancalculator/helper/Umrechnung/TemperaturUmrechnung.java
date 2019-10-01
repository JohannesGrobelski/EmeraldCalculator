package com.example.titancalculator.helper.Umrechnung;

import com.example.titancalculator.helper.Math_String.MathEvaluator;

import java.math.BigDecimal;
import java.util.Arrays;

public class TemperaturUmrechnung {


    private static String[] units = {"Celsius","Fahrenheit","Kelvin"};


    public static String[] getUnits(){
        return units;
    }


     public static String convert(BigDecimal a, String source, String target){
        if(!Arrays.asList(units).contains(source))return a.toString();
        if(!Arrays.asList(units).contains(target))return a.toString();

        if(source.equals("Celsius") && target.equals("Fahrenheit")){
            return MathEvaluator.evaluate("("+a.toString()+"*(9/5)) + 32",10);
        } else if(source.equals("Fahrenheit") && target.equals("Celsius")){
             return MathEvaluator.evaluate("("+a.toString()+" - 32) * 5/9",10);
        } else if(source.equals("Celsius") && target.equals("Kelvin")){
             return MathEvaluator.evaluate(a.toString()+" + 273.15",10);
        } else if(source.equals("Kelvin") && target.equals("Celsius")){
             return MathEvaluator.evaluate(a.toString()+" - 273.15",10);
        } else if(source.equals("Fahrenheit") && target.equals("Kelvin")){
            return MathEvaluator.evaluate("(("+a.toString()+" - 32) * 5/9) + 273.15 ",10);
        } else if(source.equals("Kelvin") && target.equals("Fahrenheit")){
            return MathEvaluator.evaluate("(("+a.toString()+" - 273.15) * 9/5) + 32",10);
        } else return "ConversionActivity Error";

     }

     public static void main(String[] a){
         //System.out.println(convert(new BigDecimal("0"),"Fahrenheit","Kelvin"));
         System.out.println(convert(new BigDecimal("0"),"Kelvin","Fahrenheit"));
     }
}
