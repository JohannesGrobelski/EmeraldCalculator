package com.example.titancalculator.helper;

import com.example.titancalculator.model.CalcModel;
import com.example.titancalculator.evalex.Expression;
import com.example.titancalculator.view.MainActivity;

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
 * @author Johannes
 */
public class MathEvaluator {
    //Math Settings
    private static double toleranceDigits = CalcModel.precisionDigits;
    public static int pre_decimal_places_pref = 5;
    public static int decimal_places_pref = 10;

    public static String toRAD(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        input = input.replace(',','.');
        String ret = "";
        try{
            ret = String.valueOf(Math.toRadians(Double.valueOf(input)));
        } catch (Exception e){return "Math Error";}
        return ret;
    }

    public static String toDEG(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        input = input.replace(',','.');
        String ret = "";
        try{
            ret = String.valueOf(Math.toDegrees(Double.valueOf(input)));
        } catch (Exception e){return "Math Error";}
        return ret;
    }

    public static String toPFZ(String input){
        if(input.isEmpty() || input.equals("Math Error"))return input;
        try{
            Double a = Double.parseDouble(input);
            if((a == Math.floor(a)) && !Double.isInfinite(a)) {
                Integer r = (int) Math.floor(a);
                if(r < 1)return "Math Error";
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
        String ret =  MathEvaluator.evaluate("1/(" + input+")", 5, 15);
        return ret;
    }

    /**
     * transform input (x1_x2_x3...xm.xd1_xd2_xd3...xdn) to shortened fraction a/b
     * @param input
     * @return
     */
    public static String toFraction(String input) {
        if(input.isEmpty() || input.equals("Math Error"))return input;
        input = input.replace(',','.');
        if(!input.contains("."))return input;

        //convert input to fraction (x1_x2_x3.xd1_xd2_xd3...xdn to x1_x2_x3xd1_xd2_xd3...xdn/10^n)
        int postdecimalPlaces = input.substring(input.indexOf('.')+1).length();
        BigInteger counter = new BigInteger("10").pow(postdecimalPlaces);
        input = input.replace(".","");
        BigInteger denominator = new BigInteger(input) ;
        String fraction = denominator+"/"+counter;
        String shortenedFraction = shortenFraction(fraction);
        return shortenedFraction;
    }

    /**
     * divide fraction = a/b = x(*cd1*...*cdn)/y(*cd1*...*cdn) to x/y (cdi common denominator i)
     * @param fraction
     * @return
     */
    public static String shortenFraction(String fraction) {
        BigInteger A = new BigInteger(fraction.substring(0,fraction.indexOf('/')));
        BigInteger B = new BigInteger(fraction.substring(fraction.indexOf('/')+1));

        BigInteger GGT = new BigInteger("-1");
        for(int i=0; i<(((int) (Math.log(Math.max(A.doubleValue(),B.doubleValue())) / Math.log(2)))*10); i++){
            if(!GGT.equals(BigInteger.ONE)) {
                GGT = GGT(A,B);
                A = A.divide(GGT);
                B = B.divide(GGT);
            } else break;
        }

        return A+"/"+B;
    }

    public static String evaluate(String input) {
        input = logToLogb(input);

        if(input.contains("!"))input = factorialCorrection(input);
        Expression expression = new Expression(input);
        try {
            expression.setPrecision(decimal_places_pref + 1);
            String res = format(expression.eval()).toString();
            return res;
        }
        catch (Exception e) {return "Math Error";}
    }

    public static String evaluate(String input,int predec_places, int dec_places) {
        decimal_places_pref = dec_places;
        pre_decimal_places_pref = predec_places;

        //input = logToLogb(input);
        if(input.contains("!"))input = factorialCorrection(input);
        final Expression expression = new Expression(input);
        try {
            expression.setPrecision(100);
/*          CalcOperation calcOperation = new CalcOperation(expression);
            calcOperation.start();
            calcOperation.join(1000);*/

            String res = format(expression.eval()).toString();
            return res;
        }
        catch (Exception e) {
            return "Math Error";
        }
    }

    /**
     * decomposition of a integer into a product of prime numbers
     * @param number
     * @return
     */
    public static List<Integer> PFZ(Integer number){
        ArrayList<Integer> pfzList = new ArrayList<>();
        for(int i = 2; i< number; i++) {
            for(int nmi=0;nmi<number;nmi++){
                if(number%i == 0) {
                    pfzList.add(i);
                    number = number/i;
                } else break;
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
     * @return converts -?a! to !-?a (correction for Expression)
     */
    private static String factorialCorrection(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("-?[0-9]+\\!").matcher(input);
        for(int i=0;i<(input.length()*10);i++){
            if (m.find()) {
                allMatches.add(m.group());
            } else break;
        }

        for(int i=0; i<allMatches.size(); i++) {
            String newEl = "!"+allMatches.get(i).replace("!", "");
            input = input.replaceAll(allMatches.get(i),newEl);
        }
        return input;
    }

    /**
     * @param input
     * @return transforms log to LogB
     */
    public static String logToLogb(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("LOG\\(.+\\)").matcher(input);
        for(int i=0;i<input.length();i++){
            if (m.find()) {
                String s = m.group();
                if(StringUtils.getParameterNumber(s,0) == 1){
                    allMatches.add(s);
                }
            }else break;
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
    public static boolean resembles(String expectedResult, String output,int toleranceDigits){
        double tolerance = 1/Math.pow(10,toleranceDigits);
        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);
        if(output.startsWith(expectedResult.substring(0,Math.min(minlength, CalcModel.precisionDigits)))){return true;}
        return false;
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
        if(output.startsWith(expectedResult.substring(0,Math.min(minlength, CalcModel.precisionDigits)))){return true;}
        else{
            if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < tolerance)return true;
            return false;
        }
    }
}
