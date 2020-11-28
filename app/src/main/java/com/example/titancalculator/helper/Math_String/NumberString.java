package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.geplanteFeatures.MathString.ContentString;
import com.example.titancalculator.helper.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberString extends ContentString {
    private static HashMap<String, String> replacements = new HashMap<String, String>() {{
        put("²","^2");  put("√","ROOT"); put("³","^3"); put("∏","MULP"); put("∑","SUME"); put("π","PI"); put("PI",String.valueOf(Math.PI));
    }};
    public static String[] functions_parentIn = {"ASINH","ACOSH","ATANH","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC","SINH","COSH","TANH","SIN","COS","TAN","COT","MEAN","ROOT","LN","LB","LOG","P","R","C"};
    public static String[] functions_paraIn = {"ROOT","LOG","P","C","R"};

    public static String mean_mode = "AriMit";
    public static String var_mode = "AriVar";

    public static int dec_places = 5;
    public static int predec_places = 25;

    static String last_answer="";
    String content="";

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

    public static String getCalcuableString(String a){
        //language settings
        a = a.replaceAll("LCM","KGV");
        a = a.replaceAll("GCD","GGT");

        a = a.replace("³√","3ROOT");
        for(String r: replacements.keySet()){
            a = a.replace(r,replacements.get(r));
        }

        //I: fix; sonst: PI -> P(I)
        a = NumberString.paraInComplex(a);

        for(String f: functions_paraIn){
            a = a.replace(f.toLowerCase(),f);
        }

        a = NumberString.parenthesise(a);

        //after paraIn (because of AT(ANS)INH)
        Matcher matcherANS = Pattern.compile("ANS").matcher(a);
        while(matcherANS.find()){
            if(matcherANS.group().matches("[^A-Z]*ANS[^A-Z]*")){ //excludes inputs like "ATAN(ASINH(57.860802) = atANSinh57.860802"
                a = a.replace("ANS",last_answer);
            }
            else {
                //System.out.println(a);
            }
        }


        //settings
        a = a.replaceAll("MEAN",mean_mode);
        a = a.replaceAll("VAR",var_mode);

        return a;
    }

    String getResult(){
        String i = getCalcuableString(content);
        String c = MathEvaluator.evaluate(i,predec_places,dec_places);
        if(!c.equals("Math Error"))last_answer = c;
        return c;
    }

    public String normalToScientific(){
        String i = getCalcuableString(content);
        String c = MathEvaluator.evaluate(i,7,7);
        if(!c.equals("Math Error"))last_answer = c;
        return c;
    }

    public String scientificToNormal(){
        String i = getCalcuableString(content);
        String c = MathEvaluator.evaluate(i,predec_places,1000);
        if(!c.equals("Math Error"))last_answer = c;
        return c;
    }

    public String getPercent(){
        return MathEvaluator.evaluate("("+getCalcuableString(content)+")*100",predec_places,7);
    }

    public static String toPercent(String input){
        return MathEvaluator.evaluate("("+input+")*100",predec_places,20);
    }

    public String getInvert(){
        return MathEvaluator.evaluate("-("+getCalcuableString(content)+")",predec_places,7);
    }

    public static String toInvert(String input){
        return MathEvaluator.evaluate("-("+input+")",predec_places,20);
    }

    public String getReciproke() {
        return MathEvaluator.evaluate("1/(" + getCalcuableString(content)+")", predec_places, 7);
    }

    public static String toReciproke(String input) {
        return MathEvaluator.evaluate("1/(" + input+")", predec_places, 20);
    }

    public String toFraction(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toFraction(res));
    }

    public String getRAD(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toRAD(res));
    }

    public String getDEG(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toDEG(res));
    }

    public void setMean_mode(String mode){
        if(mode.equals("AriMit") || mode.equals("GeoMit") || mode.equals("HarMit") ){
            mean_mode = mode;
        }
    }

    public void setVar_mode(String mode){
        if(mode.equals("AriVar") || mode.equals("GeoVar") || mode.equals("HarVar") ){
            var_mode = mode;
        }
    }

    public String getDisplayableString(String a) {
        a = a.replace("ROOT","√");
        return a;
    }

    public String getPFZ(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        try{
            Double a = Double.parseDouble(res);
            if((a == Math.floor(a)) && !Double.isInfinite(a)) {
                Integer r = (int) Math.floor(a);
                return Arrays.deepToString(MathEvaluator.PFZ(r).toArray()).replace("[","(").replace("]",")").replace(" ","");
            }else return res;
        } catch (Exception ex){
            return res;
        }
    }

    @Override public String getContent(){
        return getDisplayableString(content);
    }
    @Override public boolean setContent(String a){content = a; return true;}

    public static boolean sameOpeningClosingBrackets(String input){
        int closing = 0; int opening = 0;
        for(int i = 0; i<input.length(); i++){
            if(input.charAt(i) == '(' || input.charAt(i) == '[' || input.charAt(i) == '{')++opening;
            if(input.charAt(i) == ')' || input.charAt(i) == ']' || input.charAt(i) == '}')++closing;
        }
        return opening == closing;
    }

}