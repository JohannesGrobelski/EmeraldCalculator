package com.example.titancalculator.helper.Math_String;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.CalcActivity_science;
import com.example.titancalculator.MainActivity;
import com.example.titancalculator.evalex.Expression;
import com.example.titancalculator.helper.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Evaluates Expressions with doubles ([0-9]+,?[0-9]*and Operators /,*,+,-
 * @author Johannes
 *
 */
public class MathEvaluator {

    //Math Settings
    public static int pre_decimal_places_pref = 10;
    public static int decimal_places_pref = 10;

	String contentString = "123";
	int marker = 1;
	
	public static void main(String[] args) {
	    Expression e = new Expression(("3+5"));
	    System.out.println(e.toRPN());
        System.out.println(e.infixNotation());

        test_format();
	}

    public static void test_format() {
        //Testfälle absolut große/kleine zahlen, ganze/bruchzahlen, negative/positive zahlen
        System.out.println(MathEvaluator.evaluate("3 4 +",10));

        System.out.println(MathEvaluator.evaluate("10000!",10));

        System.out.println(MathEvaluator.evaluate("-8^32",10));
        System.out.println(MathEvaluator.evaluate("8^32",10));

        System.out.println(MathEvaluator.evaluate("-8^32 + 0.123456789",10));
        System.out.println(MathEvaluator.evaluate("8^32 - 0.123456789",10));

        System.out.println(MathEvaluator.evaluate("-150",10));
        System.out.println(MathEvaluator.evaluate("150",10));

        System.out.println(MathEvaluator.evaluate("-150 + 0.123456789",10));
        System.out.println(MathEvaluator.evaluate("150 - 0.123456789",10));
    }

        public static String toRAD(String dec) {
		dec = dec.replace(',','.');
		return String.valueOf(Math.toRadians(Double.valueOf(dec)));
	}

	public static String toDEG(String dec) {
		dec = dec.replace(',','.');
		return String.valueOf(Math.toDegrees(Double.valueOf(dec)));
	}

    // Parse EVERY number with source radix
    // and transform in specified radix(base)
    public static String baseConversion(String input, int sBase, int dBase) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9A-Z]+").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String number = Integer.toString(Integer.parseInt(s, sBase), dBase);
            input = input.replace(s,number);
        }
        return input;
    }


    public static String toBruch(String dec) {
		dec = dec.replace(',','.');

		String gesamtbruch="";
		List<String> brueche = new ArrayList<String>();
		double decD = Double.parseDouble(dec);
		String beforeCom="";
		String afterCom="";
		if(dec.contains(".")) {
			beforeCom = dec.substring(0,dec.indexOf('.'));
			afterCom = dec.substring(dec.indexOf('.')+1);
		}
		else {beforeCom = dec;}
		int exp = beforeCom.length();
		for(char c: beforeCom.toCharArray()) {
			String b = String.valueOf(Character.digit(c, 10)*Math.pow(10,exp-1))+"/"+"1";
			brueche.add(b);
			--exp;
		}
		for(char c: afterCom.toCharArray()) {
			String b = Character.digit(c, 10)+"/"+String.valueOf(Math.pow(10,exp+1));
			brueche.add(b);
			++exp;
		}
		
		double div = Math.pow(10,exp);	
		double n = 0;
		for(String bruch: brueche) {
			n += Double.parseDouble(bruch.substring(0,bruch.indexOf('/')))*div / Double.parseDouble(bruch.substring(bruch.indexOf('/')+1));
		}
		
		gesamtbruch = String.valueOf(n)+"/"+String.valueOf(div);
		gesamtbruch = kuerzen(gesamtbruch);
		return gesamtbruch;
	}
	
	private static String kuerzen(String bruch) {
		while(true) {
			int A = (int) Double.parseDouble(bruch.substring(0,bruch.indexOf('/')));
			int B = (int) Double.parseDouble(bruch.substring(bruch.indexOf('/')+1));
			bruch = A+"/"+B;

			int ggT = ggT(A,B);
			if(ggT==1)break;
			A = A / ggT;
			B = B / ggT;
			///System.out.println("k�rzen: "+A+"/"+B);
			
			bruch = A+"/"+B;
		}
		return bruch;
	}
	
	private static int ggT(double a, double b) {
		int A = (int) a;
		int B = (int) b;

		int ggT = 0;
		for(int i=1;i<=Math.max(A, B)/2;i++) {
			if((A % i == 0) && (B % i == 0)) {
				ggT = i;
			}
		}
		
		return ggT;
	}

	public static String evaluate(String input,int base) {
	    input = rootToSqrt(input);
        input = logToLogb(input);

        if(input.contains("!"))input = facCor(input);
        //input = baseConversion(input,base,10);

        Expression expression = new Expression(input);

        try {
            //System.out.println("1. "+expression.eval().toString());
            expression.setPrecision(decimal_places_pref);

            String res = format(expression.eval()).toString();
            return res; //baseConversion(res,10,base);
        }
        catch (Exception e) {
            return "Math Error";
        }

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

    private static String facToMul(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9]+\\!").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            Double number = Double.valueOf(s.replace("!", ""));

            if (!(number == Math.floor(number)) || Double.isInfinite(number) || number < 0 || number > 1000) {
                return "";
            } else {
                String newnumber = "";
                for(int i=1; i<=Math.floor(number); i++) {
                    newnumber += String.valueOf(i)+"*";
                }
                newnumber = newnumber.substring(0,newnumber.length()-1);
                Integer i =(int) Math.floor(number);
                input = input.replace(String.valueOf(i)+"!",newnumber);
            }

        }
        return input;
    }

    private static String rootToSqrt(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("ROOT\\([^,]+\\)").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String match = s.replace("ROOT","SQRT");
            input = input.replace(s,match);
        }
        return input;
    }

    private static String logToLogb(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("LOG\\([^,]+\\)").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String match = s.replace("LOG","LOG10");
            input = input.replace(s,match);
        }
        return input;
    }


	
	
	void clear(){
        String c = contentString;
        if(c.equals(""))return;
        else{
            if(marker > 0) {

                String s = "";

                if(marker > 1){
                    s+=c.substring(0,marker-1);
                }
                if(marker < c.length()){
                    s+=c.substring(marker);
                }
                
                --marker;

                System.out.println("DEL: "+s);
            }
        }
    }


    public static String format(BigDecimal input){
	    //1. problem: 150: 1.5E+2


        //System.out.println("3.1  "+input);


            if(input.toPlainString().length() <= pre_decimal_places_pref){
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                otherSymbols.setDecimalSeparator('.');
                otherSymbols.setGroupingSeparator(',');
                String format = "#.#";

                NumberFormat formatter = new DecimalFormat(format,otherSymbols);
                formatter.setMaximumFractionDigits(decimal_places_pref);
                return formatter.format(input).replaceAll("E0","");
            }


        return input.toString();
    }
	
	
	
	public static void applySettings(Context c){
        //Math Settings
        String pre_decimal_places = PreferenceManager.getDefaultSharedPreferences(c).getString("pre_decimal_places_pref","5");
        String decimal_places = PreferenceManager.getDefaultSharedPreferences(c).getString("decimal_places_pref","5");
        if(isInteger(decimal_places))decimal_places_pref = Integer.parseInt(decimal_places);
        if(isInteger(pre_decimal_places))pre_decimal_places_pref = Integer.parseInt(pre_decimal_places);
    }

    public static String repeatString(String s, int number){
	    String res = "";
	    for(int i=0; i<number; i++){
	        res += s;
        }
	    return res;
    }

    public static boolean isInteger(String s) {
        return s.matches("-?\\d+");
    }

}
