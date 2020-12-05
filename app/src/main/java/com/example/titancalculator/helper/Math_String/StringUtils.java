package com.example.titancalculator.helper.Math_String;

import android.net.http.SslCertificate;

import com.example.titancalculator.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static HashMap<String, String> replacements = new HashMap<String, String>() {{
        put("²","^2");  put("√","ROOT"); put("³","^3"); put("∏","MULP"); put("∑","SUME"); put("π","PI"); put("PI",String.valueOf(Math.PI));
    }};
    public static String[] functions_parentIn = {"ASINH","ACOSH","ATANH","ACOTH","ACSCH","ASECH","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC","SINH","COSH","TANH","COTH","SECH","CSCH","SIN","COS","TAN","COT","MEAN","ROOT","LN","LB","LOG","P","R","C"};
    public static String[] functions_paraIn = {"ROOT","LOG","P","C","R"};
    static String[] mathTokens= new String[]{
            "ASINH","ACOSH","ATANH","ACOTH","ACSCH","ASECH",
            "MEAN",">A/B",">x\u207B\u00B9",">+/-","SINH","COSH","TANH",">DEG",">RAD",">BIN",">OCT",">DEC",">HEX","10^x",
            "ASIN","ACOS","ATAN","ACOT","ASEC","ACSC","SINH","COSH","TANH","COTH","SECH","CSCH",
            "MIN","MAX","NCR","NPR","VAR",">M1",">M2",">M3",">M4",">M5",">M6","ANS","SIN","COS","TAN","COT","SEC","CSC","AND","OR","XOR","NOT","LOG","PFZ","GCD","LCM",
            ">%","LN","LB","Zn","Zb",
            "∑","∏","1","2","3","4","5","6","7","8","9","0",".",",","*","/","+","-","(",")","π","e","^","³√","√","³","²","!","S","M1","M2","M3","M4","M5","M6",
    };

    static String[] allTokens = StringUtils.concatenate(mathTokens,new String[]{"L","R"});


    /**
     * @param a
     * @param b
     * @param <T>
     * @return concatenate of array a and b (a+b)
     */
    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    /**
     * @param regex
     * @param s
     * @return finds longest match of regex in inputstring
     */
    public static String findLongestMatch(String regex, String s) {
        Pattern pattern = Pattern.compile("(" + regex + ")$");
        Matcher matcher = pattern.matcher(s);
        String longest = "";
        int longestLength = -1;
        for (int i = s.length(); i > longestLength; i--) {
            matcher.region(0, i);
            if (matcher.find() && longestLength < matcher.end() - matcher.start()) {
                longest = matcher.group();
                longestLength = longest.length();
            }
        }
        return longest;
    }

    /**
     * @param input
     * @param sub
     * @return #occurences of sub in input
     */
    public static int occurences(String input, String sub){
        String temp = input.replace(sub, "");
        return (input.length() - temp.length()) / sub.length();
    }

    /**
     * @param originalString
     * @param stringToBeInserted
     * @param index
     * @return inserts string at index
     */
    public static String insertString(String originalString,String stringToBeInserted, int index) {
        if(stringToBeInserted == null || stringToBeInserted.isEmpty())return originalString;
        if (index < 0 || index > originalString.length()) return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.insert(index, stringToBeInserted);
        return newString.toString();
    }

    public static String getDisplayableString(String input) {input = input.replace("ROOT","√"); return input;}

    /**
     * @param input
     * @param replacement
     * @param start
     * @param end
     * @return replaces input[start..end] with replacement
     */
    public static String replace(String input, String replacement , int start, int end){
        if(start > end)return input;
        if(input == null || replacement == null)return input;
        if(replacement.isEmpty() || start < 0)return input;
        return input.substring(0,start)+replacement+input.substring(end);
    }

    /**
     * @param input
     * @param k
     * @return inputinput...input (k-times)
     */
    public static String repeat(String input, int k){
        if(k<0)return input;
        if(k==0)return "";
        if(k==1)return input;
        StringBuilder res = new StringBuilder("");
        for(int i=0; i<k; i++){
            res.append(input);
        }
        return res.toString();
    }


    /**
     * @param length
     * @return a random String with length
     */
    public static String randomString(int length){
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = -1;
            if((length - salt.length()) < 5){
                int dif = length - salt.length();
                while(index == -1 || mathTokens[index].length() != dif){
                    index = (int) (rnd.nextFloat() * mathTokens.length);
                }
            } else {
               index = (int) (rnd.nextFloat() * mathTokens.length);
            }
            salt.append(mathTokens[index]);
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String[] splitMathTokens(String input) {
        return split(input,mathTokens);
    }
    public static String[] splitTokens(String input) {
        return split(input,allTokens);
    }

     /**Splits the inputstring by an array of delimiters
     * @param input
     * @return
      * */
    private static String[] split(String input, String[] delimiters) {
        if (input.length() == 0) return new String[]{""};
        if(delimiters.length == 0) return new String[]{input};
        //System.out.println("split: \""+input+"\"");
        LinkedList<String> output = new LinkedList<>();


        while (input.length() > 0) {
            //System.out.println("   splitInput: "+input);
            //System.out.println("   splitArray: "+Arrays.toString(output.toArray(new String[output.size()])));
            for(String delimiter : delimiters) {
                //System.out.println("    "+input+" startsWith "+delimiter+": "+input.startsWith(delimiter));
                if (input.startsWith(delimiter)) {
                    output.add(delimiter);
                    input = input.substring(delimiter.length());
                    break;
                }
            }
        }
        return output.toArray(new String[output.size()]);
    }

    /**
     * @param input
     * @param start
     * @param end
     * @return input[0..start]+input[end,input.length]
     */
    public static String deleteSpan(String input, int start, int end){
        if(start < 0 || start > input.length() || end < 0 || end > input.length() || end < start){
            return input;
        }
        else{
            input = input.substring(0,start) + input.substring(end);
            return input;
        }
    }

    /**
     * Zähle kommas in richtiger Ebene
     * richtige Ebene klammer_offen - klammer_zu <= 1
     * @param eval: Input
     * @param posAnfang: star
     * @return
     */
	public static int getParameterNumber(String eval, int posAnfang){
	    if(!eval.contains("(") || !eval.contains(")"))return 0;
	    if(occurences(eval,"(") != occurences(eval,")"))return -1;
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

    /**
     * transform String like numberX1numberX2...XNnumber
     * to X1(number,X2(number,...(XN(number,number)...)
     * with Xi element of functions_parentIn (ROOT, LOG etc.)
     * @param input
     * @return
     */
    public static String paraInComplex(String input){
        String patternFct = "("; for(String s: functions_parentIn){patternFct+=s+"|";} patternFct = patternFct.substring(0,patternFct.length()-1); patternFct += ")";
        Matcher matcherFct = Pattern.compile(patternFct).matcher(input);
        String patternNumber = "[0-9]*(\\.)?[0-9]+";
        String patternAtomic = patternNumber+patternFct+"\\(?"+patternNumber+"\\)?";
        String patternComplex = "("+patternNumber+patternFct+"\\(?"+")+"+patternNumber+"\\)*";

        if(input.matches(patternAtomic)){
            return paraInAtomic(input,patternFct);
        } else {
            Matcher matcherComplex = Pattern.compile(patternComplex).matcher(input);
            while(matcherComplex.find()){
                String originalComplex = matcherComplex.group();
                List<String> allMatches = new ArrayList<String>();
                Matcher matcherAtomic = Pattern.compile("(?=(" + patternAtomic + ")).").matcher(input);
                int lastOperator = 0;
                while (matcherAtomic.find(lastOperator)) {
                    String atomic = matcherAtomic.group(1);
                    if(matcherFct.find(lastOperator)){
                        lastOperator = matcherFct.start() + 1;
                    }
                    allMatches.add(atomic);
                }
                //for(String s: allMatches)System.out.println("allmatches: "+s);
                String last = paraInAtomic(allMatches.get(allMatches.size()-1),patternFct);
                //System.out.println("last: "+last);
                allMatches.remove(allMatches.size()-1); allMatches.add(last);
                for(int i=allMatches.size()-2; i>=0; i--){
                    String original = allMatches.get(i);
                    Matcher prefixNumberMatcher = Pattern.compile(patternNumber).matcher(original);
                    prefixNumberMatcher.find(); String prefixNumber = prefixNumberMatcher.group();
                    Matcher fctMatcher = Pattern.compile(patternFct).matcher(original);
                    fctMatcher.find(); String FCT = fctMatcher.group();
                    String transformed = FCT+"("+prefixNumber+","+allMatches.get(i+1)+")";
                    allMatches.remove(i);
                    allMatches.add(i,transformed);
                    //System.out.println("transformed: "+allMatches.get(i));
                    //System.out.println("--------------------------------");
                }
                input = input.replace(originalComplex,allMatches.get(0));
            }
            return input;
        }
    }

    /**
     * Transforms numberXnumber to X(number,number)
     * with X element of functions_parentIn (ROOT, LOG etc.)
     * @param input Inputstring with form like numberXnumber
     * @param fct regex presenting an element of function_parentIn
     * @return Outputstring with form like X(number,number)
     */
    public static String paraInAtomic(String input, String fct){
        Matcher matcherFct = Pattern.compile(fct).matcher(input);
        while(matcherFct.find()){
            String PatternNumber = "[0-9]*(\\.)?[0-9]+";
            Matcher matcherInput = Pattern.compile(PatternNumber+fct+"\\(?"+PatternNumber+"\\)?").matcher(input);
            while (matcherInput.find()){
                String instance = matcherInput.group();
                ArrayList<String> numbers = new ArrayList<>();
                Matcher matcherInstance = Pattern.compile(PatternNumber).matcher(instance);
                Matcher fctMatcher = Pattern.compile(fct).matcher(instance);
                fctMatcher.find(); String FCT = fctMatcher.group();
                while(matcherInstance.find()){
                    numbers.add(matcherInstance.group());
                }
                if(numbers.size() == 2){
                    input = input.replace(matcherInput.group(),FCT+"("+numbers.get(0)+","+numbers.get(1)+")");
                }
            }
        }
        input = input.replace(fct,fct.toLowerCase());
        return input;
    }

    /**
     * replaces all X1X2...number with X1(X2(...number))
     * @param input
     * @return
     */
    public static String parenthesise(String input){
        String match = "";
        do{
            match = findLongestParenthesisable(input);
            input = input.replace(match,parenthesiseSub(match));
        } while(!match.isEmpty());

        for(String s: functions_parentIn){
            input = input.replaceAll(s.toLowerCase(), s);
        }
        return input;
    }


    /**
     * @param input
     * @return transform functionParenthesisable into function(Parenthesisable)
     * with Parenthesisable beeing a number or another function(Parenthesisable)
     */
    private static String parenthesiseSub(String input) {
        if (input.isEmpty()) return input;
        LinkedList<String> functionList = new LinkedList<>();
        //1. find function
        boolean foundSomething = true;
        while(foundSomething){
            foundSomething = false;
            for(String candidateFunction: functions_parentIn){
                if(input.startsWith(candidateFunction)){
                    functionList.add(candidateFunction);
                    input = input.replaceFirst(candidateFunction,"");
                    foundSomething = true;
                    break;
                }
            }
        }

        assert(!functionList.isEmpty());
        //2. build String
        StringBuilder result = new StringBuilder("");
        for(String function: functionList){
            result.append(function+"(");
        }
        result.append(input);
        for(int i=0;i<functionList.size();i++)result.append(")");
        //return function(Parenthesisable)
        return result.toString();
    }



    /**
     * @param input
     * @return finds longest Parenthesisable with Parenthesisable beeing a number or another function(Parenthesisable)
     */
    public static String findLongestParenthesisable(String input){
        String subpattern = "";
        for(String s: functions_parentIn){subpattern+=s+"|";} subpattern = subpattern.substring(0,subpattern.length()-1);
        String pattern1 = "("+subpattern+")+"+"-?[0-9]*\\.[0-9]+";
        String pattern2 = "("+subpattern+")+"+"-?[0-9]+\\.[0-9]*";
        String pattern3 = "("+subpattern+")+"+"-?[0-9]+\\.[0-9]+";
        String pattern4 = "("+subpattern+")+"+"-?[0-9]+";
        String match = findLongestMatch(pattern1,input);
        if(match.length() == 0){
            match = findLongestMatch(pattern2,input);
        }
        if(match.length() == 0){
            match = findLongestMatch(pattern3,input);
        }
        if(match.length() == 0){
            match = findLongestMatch(pattern4,input);
        }
        return match;
    }

    /**
     * @param input
     * @return number of opening brackets match number of closing bracket
     */
    public static boolean sameOpeningClosingBrackets(String input){
        int closing = 0; int opening = 0;
        for(int i = 0; i<input.length(); i++){
            if(input.charAt(i) == '(' || input.charAt(i) == '[' || input.charAt(i) == '{')++opening;
            if(input.charAt(i) == ')' || input.charAt(i) == ']' || input.charAt(i) == '}')++closing;
        }
        return opening == closing;
    }
}
