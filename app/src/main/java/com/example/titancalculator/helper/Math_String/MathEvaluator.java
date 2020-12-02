package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.CalcModel;
import com.example.titancalculator.evalex.Expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Evaluates Expressions with doubles ([0-9]+,?[0-9]*and Operators /,*,+,-
 * @author Johannes
 *
 */
public class MathEvaluator {
    //Math Settings
    private static double toleranceDigits = CalcModel.precisionDigits;
    public static int pre_decimal_places_pref = 5;
    public static int decimal_places_pref = 10;

    public static String toRAD(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        input = input.replace(',','.');
        return String.valueOf(Math.toRadians(Double.valueOf(input)));
    }

    public static String toDEG(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        input = input.replace(',','.');
        return String.valueOf(Math.toDegrees(Double.valueOf(input)));
    }

    public static String toPFZ(String input){
        if(input.isEmpty() || input.equals("Math Error"))return input;
        try{
            Double a = Double.parseDouble(input);
            if((a == Math.floor(a)) && !Double.isInfinite(a)) {
                Integer r = (int) Math.floor(a);
                return Arrays.deepToString(MathEvaluator.PFZ(r).toArray()).replace("[","(").replace("]",")").replace(" ","");
            }else return input;
        } catch (Exception ex){ return input; }
    }

    public static String toPercent(String input){
        if(input.isEmpty() || input.equals("Math Error"))return input;
        return MathEvaluator.evaluate("("+input+")*100",5,15);
    }

    public static String toInvert(String input){
        if(input.isEmpty() || input.equals("Math Error"))return input;
        return MathEvaluator.evaluate("-("+input+")",5,15);
    }

    public static String toReciproke(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        return MathEvaluator.evaluate("1/(" + input+")", 5, 15);
    }

    public static String toFraction(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        input = input.replace(',','.');
        if(!input.contains("."))return input;

        //convert input to fraction (abc.def to abcdef/1000000)
        int postdecimalPlaces = input.substring(input.indexOf('.')+1).length();
        BigInteger counter = new BigInteger("10").pow(postdecimalPlaces);
        BigInteger denominator = new BigInteger(input.replace(".","")) ;
        String fraction = denominator+"/"+counter;
        String shortenedFraction = shortenFraction(fraction);
        return shortenedFraction;
    }

    public static String shortenFraction(String fraction) {
        BigInteger A = new BigInteger(fraction.substring(0,fraction.indexOf('/')));
        BigInteger B = new BigInteger(fraction.substring(fraction.indexOf('/')+1));

        BigInteger GGT = new BigInteger("-1");
        while(!GGT.equals(BigInteger.ONE)) {
            GGT = GGT(A,B);
            A = A.divide(GGT);
            B = B.divide(GGT);
        }
        return A+"/"+B;
    }

    public static String evaluate(String input) {
        input = rootToSqrt(input);
        input = logToLogb(input);

        if(input.contains("!"))input = factorialCorrection(input);
        Expression expression = new Expression(input);
        try {
            System.out.println(expression.toString());
            System.out.println("1. "+expression.eval().toString());
            expression.setPrecision(decimal_places_pref + 1);
            String res = format(expression.eval()).toString();
            return res;
        }
        catch (Exception e) {return "Math Error";}
    }

    public static String evaluate(String input,int predec_places, int dec_places) {
        decimal_places_pref = dec_places; //TODO: ineffizient
        pre_decimal_places_pref = predec_places;

        input = rootToSqrt(input);
        input = logToLogb(input);
        if(input.contains("!"))input = factorialCorrection(input);
        Expression expression = new Expression(input);

        try {
            expression.setPrecision(100);
            String res = format(expression.eval()).toString();
            return res;
        }
        catch (Exception e) {
            return "Math Error";
        }
    }

    public static List<Integer> PFZ(Integer number){
        ArrayList<Integer> pfzList = new ArrayList<>();
        for(int i = 2; i< number; i++) {
            while(number%i == 0) {
                pfzList.add(i);
                number = number/i;
            }
        }
        pfzList.add(number);
        pfzList.remove(Integer.valueOf(1));
        return pfzList;
    }

    public static String toPFZ(Integer number){
        if(number < 2)return String.valueOf(number);
        List<Integer> pfz = PFZ(number);
        String result = "(";
        for(int i:pfz){
            result += i+",";
        }
        result = result.substring(0,result.length()-1)+")";
        return result;
    }

    private static String format(BigDecimal input){
        int realDecPlaces = input.toString().substring(input.toPlainString().indexOf(".")+1).length();
        input = input.setScale(Math.min(realDecPlaces,decimal_places_pref),BigDecimal.ROUND_HALF_UP);

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');

        if(input.toPlainString().length() <= pre_decimal_places_pref + decimal_places_pref){
            String format = "#.#";
            NumberFormat formatter = new DecimalFormat(format,otherSymbols);
            formatter.setMaximumFractionDigits(decimal_places_pref);
            return formatter.format(input).replaceAll("E0","");
        } else {
            String format = "#.#E0";
            NumberFormat formatter = new DecimalFormat(format,otherSymbols);
            formatter.setMaximumFractionDigits(decimal_places_pref);
            return formatter.format(input).replaceAll("E0","");
        }
    }

    private static BigInteger GGT(BigInteger a, BigInteger b) {
        return a.gcd(b);
    }

    /**
     * @param input
     * @return converts a! to !a (correction for Expression)
     */
    private static String factorialCorrection(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9]+\\!").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(int i=0; i<allMatches.size(); i++) {
            String newEl = "!"+allMatches.get(i).replace("!", "");
            input = input.replaceAll(allMatches.get(i),newEl);
        }
        return input;
    }

    /**
     * @param input
     * @return transforms root to sqrt
     */
    private static String rootToSqrt(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("ROOT\\(.+\\)").matcher(input);
        while (m.find()) {
            String s = m.group();
            if(StringUtils.getParameterNumber(s,0) == 1){
                allMatches.add(s);
            }
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String match = s.replace("ROOT","SQRT");
            input = input.replace(s,match);
        }
        return input;
    }

    /**
     * @param input
     * @return transforms log to LogB
     */
    private static String logToLogb(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("LOG\\(.+\\)").matcher(input); //TODO: problem mit verschachtelung?
        while (m.find()) {
            String s = m.group();
            if(StringUtils.getParameterNumber(s,0) == 1){
                allMatches.add(s);
            }
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String match = s.replace("LOG","LOG10");
            input = input.replace(s,match);
        }
        return input;
    }

    /**
     * @param output
     * @param expectedResult
     * @return checks whether the deviation from output and expectedResult is within the tolerance range (toleranceDigits)
     */
    public static boolean resembles(double expectedResult, double output){
        if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < 0.0000000001){return true;
        } else{
            System.out.println("resembles false: "+output+" "+expectedResult);
            return false;
        }
    }

    /**
     * @param output
     * @param expectedResult
     * @return checks whether the deviation from output and expectedResult is within the tolerance range (toleranceDigits)
     */
    public static boolean resembles(String expectedResult, String output,int toleranceDigits){
        double tolerance = 1/Math.pow(10,toleranceDigits);

        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);
        if(expectedResult.startsWith(".") || expectedResult.startsWith("-."))expectedResult=expectedResult.replace(".","0."); //TODO: solve better
        if(output.startsWith(expectedResult.substring(0,Math.min(minlength, CalcModel.precisionDigits)))){return true;}
        else{
            if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < tolerance){return true;
            } else{
                System.out.println("resembleS false: "+output+" "+expectedResult+" dif:"+Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)));
                return false;
            }
        }
    }

    /**
     * @param output
     * @param expectedResult
     * @return checks whether the deviation from output and expectedResult is within the tolerance range (toleranceDigits)
     */
    public static boolean resembles(String output, String expectedResult){
        double tolerance = 1/Math.pow(10, toleranceDigits);
        if(expectedResult.equals("Math Error") || output.equals("Math Error")){
            //System.out.println("resembleS Math Error: "+output+" "+expectedResult);
            return expectedResult.equals(output);
        }
        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);
        if(expectedResult.startsWith(".") || expectedResult.startsWith("-."))expectedResult=expectedResult.replace(".","0."); //TODO: solve better
        if(output.startsWith(expectedResult.substring(0,Math.min(minlength, CalcModel.precisionDigits)))){return true;}
        else{
            if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < tolerance){return true;
            } else{
                System.out.println("resembleS false: "+output+" "+expectedResult);
                return false;
            }
        }
    }
}
