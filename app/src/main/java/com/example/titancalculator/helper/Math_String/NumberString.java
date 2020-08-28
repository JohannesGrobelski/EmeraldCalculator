package com.example.titancalculator.helper.Math_String;

import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.CalcActivity_science;
import com.example.titancalculator.helper.StringUtils;

import java.math.BigDecimal;
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
    public static String[] functions_parentIn = {"ASINH","ACOSH","ATANH","ASIN","ACOS","ATAN","ACOT","SINH","COSH","TANH","SIN","COS","TAN","COT","MEAN","ROOT","LN","LB","LOG","P","R","C"};
    public static String[] functions_paraIn = {"ROOT","LOG","P","C","R"};

    public static String mean_mode = "AriMit";
    public static String var_mode = "AriVar";

    public static int dec_places = 5;
    public static int predec_places = 5;

    static String last_answer="";
    static String angle_unit = "";


    int base=10;
    String content="";

    String A="a = 0";
    String B="b = 0";
    String C="c = 0";
    String D="d = 0";
    String E="e = 0";

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

    String getDisplayableString(String a) {
        a = a.replace("ROOT","√");

        return a;
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
        String patternNumber = "[0-9]*(\\.)?[0-9]+";
        String patternAtomic = patternNumber+patternFct+patternNumber;
        String patternComplex = "("+patternNumber+patternFct+")+"+patternNumber;

        if(input.matches(patternAtomic)){
            return patternAtomic;
        } else {
            Matcher matcherComplex = Pattern.compile(patternComplex).matcher(input);
            while(matcherComplex.find()){
                String originalComplex = matcherComplex.group();
                List<String> allMatches = new ArrayList<String>();
                Matcher matcherAtomic = Pattern.compile("(?=(" + patternAtomic + ")).").matcher(input);
                while (matcherAtomic.find()) {
                    allMatches.add(matcherAtomic.group(1));
                }
                //for(String s: allMatches)System.out.println(s);
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
            Matcher matcherInput = Pattern.compile(PatternNumber+fct+PatternNumber).matcher(input);
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

    public static String parenthesise(String input, String fct){
        for(int i=0; i<input.length(); i++) {
            for(int j=i; j<i+fct.length() && j < input.length(); j++) {
                if(input.substring(i,j+1).equals(fct)) {
                    int k;
                    for(k=j; k<input.length()-1; k++) {
                        if(input.substring(j+1, k+2).matches("[0-9]+(\\.)"))continue;
                        if(!(input.substring(j+1, k+2).matches("[0-9]+") || input.substring(j+1, k+2).matches("[E|P|PI]") || input.substring(j+1, k+2).matches("[0-9]+(\\.)[0-9]+")))break;
                    }
                    if(k!=j) {
                        String a = input.substring(0,j+1)+"(";
                        a += input.substring(j+1,k+1)+")";
                        a += input.substring(k+1);
                        input = a;
                    }
                }
            }
        }
        return input;
    }

    /**
     * replaces all X1X2...number with X1(X2(...number))
     * @param input
     * @return
     */
    public static String parenthesise(String input){
        //check
        if(input.isEmpty() || input.charAt(0) == '.' || input.charAt(input.length()-1) == '.'){return input;}
        for(int i=1; i<input.length()-2; i++){
            if(input.charAt(i) == '.'){
                if(!String.valueOf(input.charAt(i-1)).matches("[0-9]") &&
                   !String.valueOf(input.charAt(i+1)).matches("[0-9]")){
                    return input;
                }
            }
        }

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

    public static String findLongestParenthesisable(String input){
        String subpattern = "";
        for(String s: functions_parentIn){subpattern+=s+"|";} subpattern = subpattern.substring(0,subpattern.length()-1);
        String pattern1 = "("+subpattern+")+"+"[0-9]+\\.[0-9]+";
        String pattern2 = "("+subpattern+")+"+"[0-9]+";
        String match = StringUtils.findLongestMatch(pattern1,input);
        if(match.length() == 0){
            match = StringUtils.findLongestMatch(pattern2,input);
        }
        return match;
    }




    private static String parenthesiseSub(String input){
        if(input.isEmpty())return input;
        int para = 0;
        StringBuilder save =  new StringBuilder("");
        boolean isnumber = false;

        for(String s: functions_parentIn){
            input = input.replace(s,s+"(");
            input = input.replace("((","(");
            input = input.replaceAll(s, s.toLowerCase());
        }

        for(int i=0; i<input.length(); i++){
            if(input.charAt(i) == '('){
                ++para; continue;
            }

            if(String.valueOf(input.charAt(i)).matches("[0-9]") || input.charAt(i) == '.'){
                save.append(input.charAt(i)); isnumber = true; continue;
            }
            else { //Ende der Kette
                if(isnumber){
                    String insert = StringUtils.repeat(")",para);
                    input = StringUtils.insertString(input,insert,i-1);
                    save = new StringBuilder(); para = 0;
                    isnumber = false;
                }
            }
        }
        if(para > 0){
            String insert = StringUtils.repeat(")",para);
            input = StringUtils.insertString(input,insert,input.length()-1);
        }

        return input;
    }


    public static String getCalcuableString(String a){

        //language settings
        a = a.replaceAll("LCM","KGV");
        a = a.replaceAll("GCD","GGT");

        Matcher matcherANS = Pattern.compile("ANS^((A-Z)*)").matcher(a);
        while(matcherANS.find()){
            if(matcherANS.group().matches("[^A-Z]*ANS[^A-Z]*")){ //excludes inputs like "atANSinh57.860802"
                if(last_answer.equals("Math Error")){
                    a = a.replace("ANS","");
                    last_answer = "";
                } else{
                    a = a.replace("ANS",last_answer);
                }
            }
            else {
                System.out.println(a);
            }
        }

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

        //.e("calcString: ",a);
        if(!last_answer.equals("Math Error"))last_answer = a;

        //settings
        a = a.replaceAll("MEAN",mean_mode);
        a = a.replaceAll("VAR",var_mode);


        return a;
    }



    private static String scToDec(String scientificNotation) {
        try {
            if(Double.parseDouble(scientificNotation) < 1000000000){
                String res = String.valueOf(Double.parseDouble(scientificNotation));
                if(res.endsWith(".0"))res = res.replace(".0","");
                return res;
            }
            else return scientificNotation;
        } catch (Exception e){
            return  scientificNotation;
        }
    }

    String getResult(int base){
        String i = getCalcuableString(content);
        Log.e("getRES input",i);

        String c = MathEvaluator.evaluate(i,predec_places,dec_places);
        Log.e("getRES output",c);

        return c;
    }


    String getPercent(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(Double.toString(Double.parseDouble(res) * 100));
    }

    String getInvert(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(Double.toString(-Double.parseDouble(res)));
    }

    String getReciproke(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(Double.toString(1 / Double.parseDouble(res)));
    }

    String getBruch(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toBruch(res));
    }

    String getRAD(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toRAD(res));
    }

    String getDEG(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toDEG(res));
    }

    String getBIN(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,2));
    }

    String getOCT(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,8));
    }

    String getDEC(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,10));
    }

    String getHEX(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,16)).toUpperCase();
    }

    String getPFZ(int base){
        String res = getResult(base);
        if(res.isEmpty() || res.equals("Math Error"))return res;
        try{
            Double a = Double.parseDouble(res);
            if((a == Math.floor(a)) && !Double.isInfinite(a)) {
                Integer r = (int) Math.floor(a);
                return Arrays.deepToString(PFZ(r).toArray()).replace("[","(").replace("]",")").replace(" ","");
            }else return res;
        } catch (Exception ex){
            return res;
        }
    }

    private static List<Integer> PFZ(Integer number){
        ArrayList<Integer> pfzList = new ArrayList<>();
        for(int i = 2; i< number; i++) {
            while(number%i == 0) {
                pfzList.add(i);
                number = number/i;
            }
        }
        pfzList.add(number);
        return pfzList;
    }

    @Override
    public String getContent(){
        return getDisplayableString(content);
    }

    @Override
    public boolean setContent(String a){
        content = a;
        return true;
    }

    void setArgument(){
        content = content.replace(',','.');
        if(content.contains("a")){
            A = content;
        } else if(content.contains("b")){
            B = content;
        } else if(content.contains("c")){
            C = content;
        } else if(content.contains("d")){
            D = content;
        } else if(content.contains("e")){
            E = content;
        }
    }

    public static boolean isNumeric(String strNum) {
        try {
            BigDecimal d = new BigDecimal(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}