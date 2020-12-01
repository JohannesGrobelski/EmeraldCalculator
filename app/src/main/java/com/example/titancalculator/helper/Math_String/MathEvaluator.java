package com.example.titancalculator.helper.Math_String;

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
    public static char[] digit_alphabet = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static String[] digit_alphabet_groups = {"1-9", "a-i", "j-r", "s-z"};
    public static String[] int_digit_alphabet_groups = {"1-9", "10-18", "19-27", "28-36"};

    //Math Settings
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


    /**
     * @param input
     * @return a fraction representing the input
     */
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

        if(input.contains("!"))input = facCor(input);
        //input = baseConversion(input,base,10);
        Expression expression = new Expression(input);

        try {
            //System.out.println("1. "+expression.eval().toString());
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
        if(input.contains("!"))input = facCor(input);
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

    private static String facCor(String input) {
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
     wandelt log in LOGB um
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


}
