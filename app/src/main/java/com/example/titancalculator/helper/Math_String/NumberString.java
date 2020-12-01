package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.geplanteFeatures.MathString.ContentString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberString extends ContentString {
    public static HashMap<String, String> replacements = new HashMap<String, String>() {{
        put("²","^2");  put("√","ROOT"); put("³","^3"); put("∏","MULP"); put("∑","SUME"); put("π","PI"); put("PI",String.valueOf(Math.PI));
    }};
    public static String[] functions_parentIn = {"ASINH","ACOSH","ATANH","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC","SINH","COSH","TANH","SIN","COS","TAN","COT","MEAN","ROOT","LN","LB","LOG","P","R","C"};
    public static String[] functions_paraIn = {"ROOT","LOG","P","C","R"};

    /**
     * transform String like numberX1numberX2...XNnumber
     * to X1(number,X2(number,...(XN(number,number)...)
     * with Xi element of functions_parentIn (ROOT, LOG etc.)
     * @param input
     * @return
     */
    public static String paraInComplex(String input){
        //TODO: 3ROOT(3ROOT(8 wird auch als richtig erkannt weil schließende klammern nicht gezählt werden. Fehler oder Nutzerfreundlich?
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
        //check for Strings like X1X2...number
        String match = "";
        do{
            match = findLongestParenthesisable(input);
            input = input.replace(match,parenthesiseSub(match));
        }
        while(!match.isEmpty());

        for(String s: functions_parentIn){
            input = input.replaceAll(s.toLowerCase(), s);
        }

        return input;
    }

    private static String parenthesiseSub(String input){
        if(input.isEmpty())return input;
        int para = 0;
        StringBuilder save =  new StringBuilder("");
        //boolean isnumber = false;

        for(String s: functions_parentIn){
            input = input.replace(s,s+"(");
            input = input.replace("((","(");
            input = input.replaceAll(s, s.toLowerCase());
        }

        for(int i=0; i<input.length(); i++){
            if(input.charAt(i) == '('){
                ++para; continue;
            }
            if(String.valueOf(input.charAt(i)).matches("[0-9]") || input.charAt(i) == '.' || input.charAt(i)== '-'){
                save.append(input.charAt(i)); //isnumber = true;
                continue;
            }
            /* TODO: test to verify: not reachable => delete
            else { //Ende der Kette
                if(isnumber){
                    System.out.println("isnumber2: "+input);
                    String insert = StringUtils.repeat(")",para);
                    input = StringUtils.insertString(input,insert,i-1);
                    save = new StringBuilder(); para = 0;
                    isnumber = false;
                }
            }
             */
        }
        if(para > 0){
            String insert = StringUtils.repeat(")",para);
            input = StringUtils.insertString(input,insert,input.length()-1);
        }
        return input;
    }

    public static String findLongestParenthesisable(String input){
        String subpattern = "";
        for(String s: functions_parentIn){subpattern+=s+"|";} subpattern = subpattern.substring(0,subpattern.length()-1);
        String pattern1 = "("+subpattern+")+"+"-?[0-9]*\\.[0-9]+";
        String pattern2 = "("+subpattern+")+"+"-?[0-9]+\\.[0-9]*";
        String pattern3 = "("+subpattern+")+"+"-?[0-9]+\\.[0-9]+";
        String pattern4 = "("+subpattern+")+"+"-?[0-9]+";
        String match = StringUtils.findLongestMatch(pattern1,input);
        if(match.length() == 0){
            match = StringUtils.findLongestMatch(pattern2,input);
        }
        if(match.length() == 0){
            match = StringUtils.findLongestMatch(pattern3,input);
        }
        if(match.length() == 0){
            match = StringUtils.findLongestMatch(pattern4,input);
        }
        return match;
    }


    @Override public String getContent(){
        return NavigatableNumberString.getDisplayableString(content);
    }
    @Override public void setContent(String a){content = a;}

    public static boolean sameOpeningClosingBrackets(String input){
        int closing = 0; int opening = 0;
        for(int i = 0; i<input.length(); i++){
            if(input.charAt(i) == '(' || input.charAt(i) == '[' || input.charAt(i) == '{')++opening;
            if(input.charAt(i) == ')' || input.charAt(i) == ']' || input.charAt(i) == '}')++closing;
        }
        return opening == closing;
    }

}