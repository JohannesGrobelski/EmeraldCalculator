package com.example.titancalculator.helper.Math_String;

import android.content.Context;

import com.example.titancalculator.evalex.Expression;
import com.example.titancalculator.helper.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static HashMap<String,Integer> int_base_digits = init_intBase_digits();
    public static HashMap<Integer,String> int_base_digits_rl = init_intBase_digits_rl();

    public static HashMap<BigDecimal,String> base_digits = initBase_digits();
    public static HashMap<String,BigDecimal> base_digits_rl = initBase_digits_rl();

    //Math Settings
    public static int pre_decimal_places_pref = 5;
    public static int decimal_places_pref = 5;

	String contentString = "123";
	int marker = 1;

    public static String toRAD(String dec) {
		dec = dec.replace(',','.');
		return String.valueOf(Math.toRadians(Double.valueOf(dec)));
	}

	public static String toDEG(String dec) {
		dec = dec.replace(',','.');
		return String.valueOf(Math.toDegrees(Double.valueOf(dec)));
	}




    /**
     * @param dec
     * @return a fraction representing the input
     */
    public static String toFraction(String dec) {
        System.out.println("fraction input: "+dec);
		dec = dec.replace(',','.');
		if(!dec.contains("."))return dec;

		//convert dec to fraction (abc.def to abcdef/1000000)
        int postdecimalPlaces = dec.substring(dec.indexOf('.')+1).length();
        BigInteger counter = new BigInteger("10").pow(postdecimalPlaces);
        BigInteger denominator = new BigInteger(dec.replace(".","")) ;
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
		return fraction = A+"/"+B;
	}
	
	private static int GGT(double a, double b) {
	    if(b == 0)return (int) Math.abs(a);
		int A = (int) a;
		int B = (int) b;

		int GGT = 1;
		for(int i=1;i<=Math.max(A, B)/2;i++) {
			if((A % i == 0) && (B % i == 0)) {
				GGT = i;
			}
		}
		
		return GGT;
	}

    private static BigInteger GGT(BigInteger a, BigInteger b) {
        return a.gcd(b);
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
                    newnumber += i +"*";
                }
                newnumber = newnumber.substring(0,newnumber.length()-1);
                Integer i =(int) Math.floor(number);
                input = input.replace(i +"!",newnumber);
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

    /**
     wandelt log in LOGB um
     */
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

    public static String toPFZ(Integer number){
        List<Integer> pfz = PFZ(number);
        String result = "(";
        for(int i:pfz){
            result += i+",";
        }
        result = result.substring(0,result.length()-1)+")";

        return result;
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
                scientific_notation = "E+"+ (pre_decimal_places.length() - pre_decimal_places_pref);
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
