package com.example.titancalculator.helper.Math_String;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.CalcActivity_science;
import com.example.titancalculator.MainActivity;
import com.example.titancalculator.evalex.Expression;
import com.example.titancalculator.helper.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
    public static char[] digit_alphabet = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static String[] digit_alphabet_groups = {"1-9", "a-i", "j-r", "s-z"};
    public static String[] int_digit_alphabet_groups = {"1-9", "10-18", "19-27", "28-36"};

    public static HashMap<String,Integer> int_base_digits = init_intBase_digits();
    public static HashMap<Integer,String> int_base_digits_rl = init_intBase_digits_rl();

    public static HashMap<BigDecimal,String> base_digits = initBase_digits();
    public static HashMap<String,BigDecimal> base_digits_rl = initBase_digits_rl();

    //Math Settings
    public static int pre_decimal_places_pref = 5;
    public static int decimal_places_pref = 5;

	String contentString = "123";
	int marker = 1;
	
	public static void main(String[] args) {
        //test_format();
        //test(100);
        //System.out.println(toDec(".a", 16 , 20));
        //System.out.println(MathEvaluator.evaluate("LOG(2,AriVAR(2,3))",10));
        //System.out.println(MathEvaluator.evaluate("AriVAR(2,3)",10));

        //System.out.println(MathEvaluator.evaluate("78588558.2121",4,5));
        //System.out.println(MathEvaluator.evaluate("LOG(LOG(2))",4,5));

        //System.out.println(NumberString.findLongestParenthesisable("LOGLOG9"));
        System.out.println(NumberString.parenthesise("LOGLOG9"));
        System.out.println(NumberString.parenthesise("LOG(LOG(9))"));
        System.out.println(NumberString.parenthesise("LOG(LOG(9)) + LOGLN9"));



        //System.out.println(MathEvaluator.evaluate("d^b",16));
        //System.out.println(toBase("1412432",16,20));
    }




    public static void test_format() {
        //Testfälle absolut große/kleine zahlen, ganze/bruchzahlen, negative/positive zahlen
        System.out.println(MathEvaluator.evaluate("15rootToSqrt.25*22! + 7.90^sin(22) + 3.12^23",10));
        System.out.println(MathEvaluator.evaluate("69.2*37122.44",10));
        System.out.println(MathEvaluator.evaluate("89*9*62.2",10));
        System.out.println(MathEvaluator.evaluate("MAX(1,2,3)",10));
        System.out.println("ARI: "+MathEvaluator.evaluate("AriVar(1,2,3,4,5,6)",10));
        System.out.println(MathEvaluator.evaluate("AriMit(1,2,3,4,5,6)",10));


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
        //trans scientific notation

        while(input.contains("E+")){
            int places = Integer.valueOf(input.substring(input.indexOf("E+")+2));
            String search = "E\\+"+String.valueOf(places);
            input = input.replaceFirst(search,StringUtils.repeat("0",places));
            input = input.replace(".","");
            input = StringUtils.insertString(input,".",places);
        }
        while(input.contains("E-")){
            int places = Integer.valueOf(input.substring(input.indexOf("E-")+2));
            String search = "E-"+String.valueOf(places);
            input = input.replaceFirst(search,"");

            input = input.replace(".","");
            input = "0."+StringUtils.repeat("0",places)+input;
        }

        Matcher m1 = Pattern.compile("[0-9a-z]+\\.[0-9a-z]+").matcher(input);
        Matcher m2 = Pattern.compile("[0-9a-z]*\\.[0-9a-z]+").matcher(input);
        Matcher m3 = Pattern.compile("[0-9a-z]+\\.[0-9a-z]*").matcher(input);
        Matcher m4  = Pattern.compile("[0-9a-z]+\\.?[0-9a-z]*").matcher(input);
        LinkedHashSet<String> allMatches = new LinkedHashSet<String>();

        while(m1.find()) {allMatches.add(m1.group());}
        while(m2.find()) {allMatches.add(m2.group());}
        while(m3.find()) {allMatches.add(m3.group());}
        while(m4.find()) {allMatches.add(m4.group());}

        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String number = s;
            if(sBase == 10 && dBase != 10){
                number = toBase(s,dBase,1000);
            } else if(sBase != 10 && dBase == 10){
                number = toDec(s,sBase,1000);
            }
            input = input.replace(s,number);
        }
        return input;
    }




    public static String toDec(String input, int base, int decimal_places_pref){
	    if(base < 0 || base > 36) return "";
        BigDecimal b = new BigDecimal(base);

        String pc = "";
        if(input.contains(".")){
            pc = input.substring(input.indexOf(".")+1);
            input = input.substring(0,input.indexOf("."));
        }

        BigDecimal res = BigDecimal.ZERO;

        if(!input.isEmpty()){
            for(int i=input.length()-1; i>=0; --i){
                String digit = String.valueOf(input.toCharArray()[i]);
                BigDecimal d = base_digits_rl.get(digit);
                int p = input.length()-1-i;
                BigDecimal m = BigDecimal.valueOf(base).pow(p,MathContext.DECIMAL128);
                d = d.multiply(m,MathContext.DECIMAL128);
                res = res.add(d,MathContext.DECIMAL128);
                //System.out.println("digit: "+digit+", d: "+d+", m: "+m+", res"+res);
            }
        }

        if(!pc.isEmpty()){
            BigDecimal pc_res = BigDecimal.ZERO;
            if(!pc.isEmpty()){
                for(int i=pc.length()-1; i>=0; --i){
                    String digit = String.valueOf(pc.toCharArray()[i]);
                    BigDecimal d = base_digits_rl.get(digit);
                    int p = -(i+1);
                    BigDecimal m = BigDecimal.valueOf(base).pow(p,MathContext.DECIMAL128);
                    d = d.multiply(m,MathContext.DECIMAL128);
                    pc_res = pc_res.add(d,MathContext.DECIMAL128);
                   //System.out.println("decdigit: "+digit+", p: "+p+", d: "+d+", m: "+m+", res: "+pc_res);
                }
            }
            res = res.add(pc_res);
        }

        return res.toString();
    }

    public static String toBase(String input, int base, int decimal_places_pref){
        if(base < 2 || base > 36) return "";

        BigDecimal b = new BigDecimal(base);

	    String pc = "";
	    if(input.contains(".")){
	        pc = "0"+input.substring(input.indexOf("."));
	        input = input.substring(0,input.indexOf("."));
        }

        String res = "";
        if(!input.isEmpty()){
            BigDecimal n = new BigDecimal(input,MathContext.DECIMAL128);
            while(n.compareTo(BigDecimal.ZERO)>=0){
                BigDecimal d = n.divide(b,MathContext.DECIMAL128).setScale(0,RoundingMode.FLOOR);
                BigDecimal r = n.remainder(b,MathContext.DECIMAL128).setScale(0,RoundingMode.FLOOR);
                res = base_digits.get(r) + res;

                n = d;
                //System.out.println("n: "+n.toString()+", r: "+r+", res: "+res);

                if(n.compareTo(b)<=0){
                    r = n.remainder(b,MathContext.DECIMAL128).setScale(0,RoundingMode.HALF_DOWN);
                    res = base_digits.get(r) + res;

                    //System.out.println("n: "+n.toString()+", r: "+r+", res: "+res);
                    break;
                }
            }
        }

        String pc_res="";
	    if(!pc.isEmpty()){
            BigDecimal n = new BigDecimal(pc,MathContext.DECIMAL128);
            //System.out.println(n+" "+b+" "+(n.compareTo(b) == 0));

            while(!(n.multiply(b,MathContext.DECIMAL128).compareTo(b) == 0) && !(n.compareTo(BigDecimal.ZERO) == 0)){
                n = n.multiply(b,MathContext.DECIMAL128);
                BigDecimal r = n.setScale(0,RoundingMode.FLOOR).remainder(b,MathContext.DECIMAL128);
                pc_res += base_digits.get(r);

                //System.out.println("n: "+n.toString()+", r: "+r+", pc_res: "+pc_res);
                n = n.remainder(b,MathContext.DECIMAL128);

                --decimal_places_pref;
                if(decimal_places_pref == 0)break;
            }

            res = res+"."+pc_res;
        }
        return res;
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

    /**
     * Zähle kommas in richtiger Ebene
     * richtige Ebene klammer_offen - klammer_zu <= 1
     * @param eval: Input
     * @param posAnfang: star
     * @return
     */
	public static int getParameterNumber(String eval, int posAnfang){
	    if(StringUtils.occurences(eval,"(") != StringUtils.occurences(eval,")"))return -1;
	    int number_comma = 0;
	    int klammern_offen = 0; int klammern_geschlossen = 0;
	    while(posAnfang < eval.length()-2){
	        ++posAnfang;
	        char pos = eval.charAt(posAnfang);
	        if(pos == '(')++klammern_offen;
            if(pos == ')')++klammern_geschlossen;
            if(pos == ',' && klammern_offen - klammern_geschlossen <= 1)++number_comma;
        }
        return number_comma + 1;
    }

	public static String evaluate(String input,int base) {
	    input = rootToSqrt(input);
        input = logToLogb(input);

        if(input.contains("!"))input = facCor(input);
        //input = baseConversion(input,base,10);

        if(base != 10){
            input = baseConversion(input,base,10);
        }

        Expression expression = new Expression(input);

        try {
            //System.out.println("1. "+expression.eval().toString());
            if(base == 10){
                expression.setPrecision(decimal_places_pref + 1);
                String res = format(expression.eval()).toString();
                return res;
            }
            else {
                //BigDecimal a = new BigDecimal(baseConversion(res,10,base));
                //return a.toString();
                expression.setPrecision(Math.max(decimal_places_pref,100));
                String res = expression.eval().toString();
                return formatNumber(baseConversion(res,10,base),base);
            }
        }
        catch (Exception e) {
            return "Math Error";
        }
    }



    public static String evaluate(String input,int predec_places, int dec_places) {
	    decimal_places_pref = dec_places; //TODO: ineffizient
	    pre_decimal_places_pref = predec_places;

        input = rootToSqrt(input);
        input = logToLogb(input);
        if(input.contains("!"))input = facCor(input);
        System.out.println(input);
        Expression expression = new Expression(input);

        try {
            expression.setPrecision(100000);
            String res = format(expression.eval()).toString();
            return res;
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
        Matcher m = Pattern.compile("ROOT\\(.+\\)").matcher(input);
        while (m.find()) {
            String s = m.group();
            if(getParameterNumber(s,0) == 1){
                allMatches.add(s);
            }
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String match = s.replace("ROOT","SQRT");
            input = input.replace(s,match);
        }
        return input;
    }

    private static String logToLogb(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("LOG\\(.+\\)").matcher(input); //TODO: problem mit verschachtelung?
        while (m.find()) {
            String s = m.group();
            if(getParameterNumber(s,0) == 1){
                allMatches.add(s);
            }
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
	
	
	
	public static void applySettings(Context c){
        //Math Settings
        /*
        String pre_decimal_places = PreferenceManager.getDefaultSharedPreferences(c).getString("pre_decimal_places_pref","5");
        String decimal_places = PreferenceManager.getDefaultSharedPreferences(c).getString("decimal_places_pref","5");
        if(isInteger(decimal_places))decimal_places_pref = Integer.parseInt(decimal_places);
        if(isInteger(pre_decimal_places))pre_decimal_places_pref = Integer.parseInt(pre_decimal_places);
        */
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


    private static void test(int durchläufe){
        for(int i=0; i<durchläufe;i++){
            BigDecimal z = BigDecimal.valueOf(Math.random()*100000);
            int bz = (int) Math.round(Math.random()*36)+1;
            BigDecimal res = new BigDecimal(toDec(toBase(String.valueOf(z), bz , 20),bz,20),MathContext.DECIMAL128);
            if(Math.round(z.subtract(res).doubleValue()) > .1){
                System.out.println(z+" != "+ res);
            }
        }
    }

    private static HashMap<Integer,String> init_intBase_digits_rl(){
        HashMap<Integer,String> base_digits = new HashMap<Integer,String>(){{}};
        for(int i=0; i<36; i++){
            base_digits.put(i,String.valueOf(digit_alphabet[i]));
        }
        return base_digits;
    }

    private static HashMap<String,Integer> init_intBase_digits(){
        HashMap<String,Integer> base_digits = new HashMap<String,Integer>(){{}};
        for(int i=0; i<36; i++){
            base_digits.put(String.valueOf(digit_alphabet[i]),i);
        }
        return base_digits;
    }

    private static HashMap<BigDecimal,String> initBase_digits(){
        HashMap<BigDecimal,String> base_digits = new HashMap<BigDecimal,String>(){{}};
        for(int i=0; i<36; i++){
            base_digits.put(BigDecimal.valueOf(i),String.valueOf(digit_alphabet[i]));
        }
        return base_digits;
    }

    private static HashMap<String,BigDecimal> initBase_digits_rl(){
        HashMap<String,BigDecimal> base_digits = new HashMap<String,BigDecimal>(){{}};
        for(int i=0; i<36; i++){
            base_digits.put(String.valueOf(digit_alphabet[i]),BigDecimal.valueOf(i));
        }
        return base_digits;
    }

    private static String formatNumber(String input, int base){
	    //erase all leading zeros
        while(input.startsWith("0") && input.length() > 1){
            if(input.matches("0\\..+"))break;
            input = input.substring(1);
        }

        //deleting all last zeros
        while(input.endsWith("0") && input.length() > 1){
            input = input.substring(0,input.length()-1);
        }

        //cut of .0+
        if(input.contains(".") && input.substring(input.indexOf(".")).matches("\\.0*")){
            input = input.substring(0,input.indexOf("."));
        }

        //put into scientific notation
        String scientific_notation="";

        if(input.length() > pre_decimal_places_pref){
            String pre_decimal_places = "";
            if(input.matches(".*\\..+")) {
                pre_decimal_places = input.substring(0,input.indexOf("."));
            } else {
                pre_decimal_places = input;
            }

            //E+
            pre_decimal_places_pref = 10;
            if(pre_decimal_places.length() > pre_decimal_places_pref){
                scientific_notation = "E+"+String.valueOf(pre_decimal_places.length() - pre_decimal_places_pref);
                //round
                String last_decimal_place = pre_decimal_places.substring(pre_decimal_places_pref-1,pre_decimal_places_pref);
                //lookup
                if(!int_base_digits.containsKey(last_decimal_place))return input;
                else{
                    if(int_base_digits.get(last_decimal_place) * 2 > base){ //round up
                        char[] dp = pre_decimal_places.toCharArray();
                        //System.out.println("TEST: "+int_base_digits_rl.get(int_base_digits.get(last_decimal_place)+1).toCharArray()[0]);
                        dp[pre_decimal_places_pref-1] = int_base_digits_rl.get(int_base_digits.get(last_decimal_place)+1).toCharArray()[0];
                        pre_decimal_places = String.valueOf(dp);
                    }
                    pre_decimal_places = pre_decimal_places.substring(0,pre_decimal_places_pref);
                }

                System.out.println("bef scino: "+input);
                System.out.println("bef scino: "+pre_decimal_places);


                if(input.matches(".*\\..+")) {
                    input = input.replace(input.substring(0,input.indexOf(".")),pre_decimal_places);
                    //input = pre_decimal_places;
                } else {
                    pre_decimal_places = StringUtils.insertString(input,".",Math.min(pre_decimal_places_pref,pre_decimal_places.length()-1));
                    System.out.println("TEST:"+pre_decimal_places);
                    input = pre_decimal_places;
                }
            }

            String decimal_places = "";
            if(input.matches("0\\..+")){
                decimal_places = input.substring(input.indexOf(".")+1);
            }

            //E-
            if(!decimal_places.isEmpty()){
                int len = 0;
                for(int i=0; i<decimal_places.length(); i++){
                    if(decimal_places.charAt(i) == '0')++len;
                    else break;
                }

                if(len > pre_decimal_places_pref){
                    scientific_notation = "E-"+len;
                    input = "0."+input.substring(len+2);
                }
            }
        }

        //round
        if(input.matches(".*\\..+")){
            String decimal_places = input.substring(input.indexOf(".")+1);
            if(decimal_places.length() > decimal_places_pref){
                String last_decimal_place = decimal_places.substring(decimal_places_pref-1,decimal_places_pref);
                //lookup
                if(!int_base_digits.containsKey(last_decimal_place))return input;
                else{
                    if(int_base_digits.get(last_decimal_place) * 2 > base){ //round up
                        char[] dp = decimal_places.toCharArray();
                        //System.out.println("TEST: "+int_base_digits_rl.get(int_base_digits.get(last_decimal_place)+1).toCharArray()[0]);
                        dp[decimal_places_pref-1] = int_base_digits_rl.get(int_base_digits.get(last_decimal_place)+1).toCharArray()[0];
                        decimal_places = String.valueOf(dp);
                    }
                    decimal_places = decimal_places.substring(0,decimal_places_pref);

                }
                input = input.replace(input.substring(input.indexOf(".")+1),decimal_places);
            }
        }



        return input+scientific_notation;
    }

}
