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
        put("²","^2"); put("³","^3"); put("√","ROOT"); put("³√","3ROOT"); put("∏","MULP"); put("∑","SUME"); put("π","PI"); put("PI",String.valueOf(Math.PI));
    }};
    private static String[] functions_parentIn = {"ROOT","LN","LB","LOG","P","R","C",
            "SIN","COS","TAN","COT","ASIN","ACOS","ATAN","ACOT","SINH","COSH","TANH","ASINH","ACOSH","ATANH",
            "MEAN","ROOT"};
    private static String[] functions_paraIn = {"ROOT","LOG","P","C","R"};

    static String mean_mode = "AriMit";
    static String var_mode = "AriVar";

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

    public static String paraIn(String input, String fct){
        String res="";
        while(input.contains(fct)) {
            int i = input.indexOf(fct);
            int j = i+fct.length()-1;

            int k;
            for(k=i; k>0; ) {
                if(input.substring(k-1,i).matches("(\\.)[0-9]+")) {
                    --k;
                    continue;
                }
                if(!(input.substring(k-1,i).matches("[0-9]+") || input.substring(k-1,i).matches("[E|PI]") || input.substring(k-1,i).matches("[0-9]+(\\.)[0-9]+")))break;
                else {
                    --k;
                }

            }
            if(k!=i) {

                String b = input.substring(i,j+2);
                b += input.substring(Math.max(0,k),i)+",";
                b += input.substring(j+2,input.indexOf(')', j+2)+1);

                input = input.replace(input.substring(Math.max(0,k),input.indexOf(')', i)+1),b);

            }
            input = input.replaceFirst(fct, fct.toLowerCase());
        }
        input = input.replace(fct.toLowerCase(),fct);
        input = input.replace("( ","(");
        input = input.replace("(,","(");

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
        return input;
    }

    public static String findLongestParenthesisable(String input){
        String subpattern = "";
        for(String s: functions_parentIn){subpattern+=s+"|";} subpattern = subpattern.substring(0,subpattern.length()-1);
        String pattern1 = "("+subpattern+")+"+"[0-9]+\\\\.[0-9]+";
        String pattern2 = "("+subpattern+")+"+"[0-9]+";
        String match = StringUtils.findLongestMatch(pattern1,input);
        if(match.isEmpty()){
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

        if(a.contains("ANS")){
            if(last_answer.equals("Math Error")){
                a = a.replace("ANS","");
                last_answer = "";
            } else{
                a = a.replace("ANS",last_answer);
            }
        }

        //I: fix; sonst: PI -> P(I)

        a = NumberString.parenthesise(a);

        /*
        for(String f: functions_parentIn){
            a = (a,f);
        }

         */
        for(String f: functions_paraIn){
            NumberString.paraIn(a,f);
        }
        for(String r: replacements.keySet()){
            a = a.replace(r,replacements.get(r));
        }



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