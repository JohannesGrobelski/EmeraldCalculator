package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.FunctionGroupSettingsActivity;
import com.example.titancalculator.evalex.Expression;
import com.example.titancalculator.helper.ArrayUtils;
import com.example.titancalculator.helper.MainDisplay.DesignApplier;

import java.math.BigDecimal;
import java.util.HashMap;


/**
 * Verhalten:
 * Zeichen werden auf EingabeString konkateniert
 *
 * bei calc() wird Ergebnis der EingabeString berechnet
 */
public class SequentialInfixEvaluator {
    public static String[] unaryOpsAfter = {"!", "³", "²"};
    public static String[] unaryOpsBefore = {"-", "LOG", "LN", "LB", "√", "³√", "10^",
            "SIN", "COS", "TAN", "COT", "ASIN", "ACOS", "ATAN", "ACOT", "SINH", "COSH", "TANH", "COTH", "ASINH", "ACOSH", "ATANH", "ACOTH",
            "NOT"};
    public static String[] NaryOps = {"+", "-", "*", "/", "^", "LOG", "LN", "LB", "√", "ggt", "kgv", "MIN", "MAX", "AND", "OR", "XOR", "C", "P", "MEAN", "VAR", "E"};

    public static HashMap<String, String> equi = new HashMap<String, String>() {{
        put("∑", "+");
        put("∏", "*");
    }};


    private static String input = "";
    private static String lastOperator = "";
    private static String currentInput = "";

    private static boolean init = false;
    private static void init() {

    }

    private static boolean pushDigitCom(String d){
            if (ArrayUtils.array_contains(unaryOpsBefore,currentInput)
                    || ArrayUtils.array_contains(unaryOpsAfter,currentInput)
                    || ArrayUtils.array_contains(NaryOps,currentInput)) {
                currentInput = "";
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
            currentInput=p;
            lastOperator=p;
            input += p;
            return true;
        }
    }

    public static boolean push(String p) {
        if(p.equals(".") || p.matches("[0-9]")){
            return pushDigitCom(p);
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
    }

    public static String calc(){
        String res = MathEvaluator.evaluate(NumberString.getCalcuableString(input),10);
        erase();
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






