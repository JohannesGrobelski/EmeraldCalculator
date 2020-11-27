package com.example.titancalculator.geplanteFeatures.MathString;

import android.util.Log;

import com.example.titancalculator.geplanteFeatures.notUsedHelper.ArrayUtils;
import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.HashMap;


/**
 * Verhalten:
 * Zeichen werden auf EingabeString konkateniert
 *
 * bei calc() wird Ergebnis der EingabeString berechnet
 */
public class SequentialInfixEvaluator {
    public static String[] constants = {"π", "e"};

    public static String[] unaryOpsAfter = {"!", "³", "²"};
    public static String[] unaryOpsBefore = {"-", "LOG", "LN", "LB", "√", "³√", "10^",
            "SIN", "COS", "TAN", "COT", "ASIN", "ACOS", "ATAN", "ACOT", "SINH", "COSH", "TANH", "COTH", "ASINH", "ACOSH", "ATANH", "ACOTH",
            "NOT"};
    public static String[] NaryOps = {"+", "-", "*", "/", "^", "LOG", "LN", "LB", "√", "GGT", "KGV", "MIN", "MAX", "AND", "OR", "XOR", "C", "P", "MEAN", "VAR", "E"};

    public static HashMap<String, String> equi = new HashMap<String, String>() {{
        put("∑", "+");
        put("∏", "*");
    }};


    private static String input = "";
    private static String currentInput = "";
    private static String lastOperator = "";
    private static String result = "";


    private static boolean init = false;
    private static void init() {

    }

    private static boolean pushDigitComConst(String d){
            if(input.isEmpty() && !result.isEmpty()){
                result = "";
            }
            if (ArrayUtils.array_contains(unaryOpsBefore,currentInput)
                    || ArrayUtils.array_contains(unaryOpsAfter,currentInput)
                    || ArrayUtils.array_contains(NaryOps,currentInput)) {
                currentInput = "";
            }
            if(!currentInput.isEmpty() && ArrayUtils.array_contains(constants,currentInput)){
                return false;
            }
            if (ArrayUtils.array_contains(unaryOpsAfter,lastOperator)){
                return false;
            }
            if(currentInput.contains(".") && d.equals(".")){
                return false;
            }
            else{
                currentInput+=d;
                input += d;
                return true;
            }
    }



    private static boolean pushOperator(String p){
        if (ArrayUtils.array_contains(unaryOpsBefore,currentInput)
            || ArrayUtils.array_contains(unaryOpsAfter,currentInput)
            || ArrayUtils.array_contains(NaryOps,currentInput)) {
                return false;
        }
        else{
            //weiterrechnen
            if(input.isEmpty() && !result.isEmpty()){
                input = result;
            }
            if (!ArrayUtils.array_contains(unaryOpsAfter,p)  && ArrayUtils.array_contains(unaryOpsBefore,p)){
                currentInput=p;
                lastOperator=p;
                input = p;
            }
            else if (ArrayUtils.array_contains(unaryOpsAfter,p) || ArrayUtils.array_contains(NaryOps,p)){
                currentInput=p;
                lastOperator=p;
                input += p;
            }
            return true;
        }
    }

    public static boolean push(String p) {
        Log.v("input","input");
        if(p.equals(".") || p.matches("[0-9]")){
            return pushDigitComConst(p);
        }

        if (ArrayUtils.array_contains(unaryOpsBefore,p)
            || ArrayUtils.array_contains(unaryOpsAfter,p)
            || ArrayUtils.array_contains(NaryOps,p)) {
                return pushOperator(p);
        }


        return false;
    }

    public static String getInput(){
        return input;
    }

    public static String getLast(){
        return currentInput;
    }

    public static String getLastOperator(){
        return lastOperator;
    }

    public static void erase(){
        currentInput = "";
        input = "";
        lastOperator="";
        result = "";
    }

    public static String calc(){
        String res = MathEvaluator.evaluate(NumberString.getCalcuableString(input));
        input = currentInput = lastOperator = "";
        result = res;
        return res;
    }

    public static void main(String[] a){
        SequentialInfixEvaluator.push("SIN");
        System.out.println(SequentialInfixEvaluator.getLast());
        SequentialInfixEvaluator.push("4");
        System.out.println(SequentialInfixEvaluator.getLast());
        System.out.println(SequentialInfixEvaluator.calc());
        System.out.println(SequentialInfixEvaluator.getLast());

    }
}






