package com.example.titancalculator.helper.Math_String;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberString extends ContentString {
    public static int precision = 0;

    String last_answer="";
    String angle_unit = "";


    int base=10;
    String content="";

    String A="a = 0";
    String B="b = 0";
    String C="c = 0";
    String D="d = 0";
    String E="e = 0";

    String getDisplayableString(String a) {
        a = a.replace(".",",");
        a = a.replace("ROOT","√");

        return a;
    }

    private String paraIn(String input, String fct){
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

    private String parenthesise(String input, String fct){
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

    private String getCalcuableString(String a){
        if(a.contains("ANS")){
            if(last_answer.equals("Math Error")){
                a = a.replace("ANS","");
                last_answer = "";
            } else{
                a = a.replace("ANS",last_answer);
            }
        }

        //I: fix; sonst: PI -> P(I)
        a = a.replaceAll("PI", MathEvaluator.evaluate("PI",10));

        a = a.replace("∑","SUME");
        a = a.replace("∏","MULP");

        a = a.replace("³√","3ROOT");
        a = a.replace("√","ROOT");


        a = parenthesise(a,"ROOT");
        a = parenthesise(a,"LN");
        a = parenthesise(a,"LB");
        a = parenthesise(a,"LOG");
        a = parenthesise(a,"P");
        a = parenthesise(a,"R");
        a = parenthesise(a,"C");

        a = parenthesise(a,"SIN");
        a = parenthesise(a,"COS");
        a = parenthesise(a,"TAN");
        a = parenthesise(a,"ASIN");
        a = parenthesise(a,"ACOS");
        a = parenthesise(a,"ATAN");
        a = parenthesise(a,"SINH");
        a = parenthesise(a,"COSH");
        a = parenthesise(a,"TANH");
        a = parenthesise(a,"ASINH");
        a = parenthesise(a,"ACOSH");
        a = parenthesise(a,"ATANH");

        a = paraIn(a,"ROOT");
        a = paraIn(a,"LOG");
        a = paraIn(a,"P");
        a = paraIn(a,"C");
        a = paraIn(a,"R");

        a = facToMul(a);

        Log.e("calcString: ",a);
        if(!last_answer.equals("Math Error"))last_answer = a;
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

    String getResult(){
        String i = getCalcuableString(content);
        String c = MathEvaluator.evaluate(i,10);
        Log.e("getRES 3",c);

        return c;
    }


    String getPercent(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(Double.toString(Double.parseDouble(res) * 100));
    }

    String getInvert(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(Double.toString(-Double.parseDouble(res)));
    }

    String getReciproke(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(Double.toString(1 / Double.parseDouble(res)));
    }

    String getBruch(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toBruch(res));
    }

    String getRAD(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toRAD(res));
    }

    String getDEG(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.toDEG(res));
    }

    String getBIN(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,2));
    }

    String getOCT(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,8));
    }

    String getDEC(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,10));
    }

    String getHEX(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        return getDisplayableString(MathEvaluator.baseConversion(res,10,16)).toUpperCase();
    }

    String getPFZ(){
        String res = getResult();
        if(res.isEmpty() || res.equals("Math Error"))return res;
        try{
            Double a = Double.parseDouble(res);
            if((a == Math.floor(a)) && !Double.isInfinite(a)) {
                Integer r = (int) Math.floor(a);
                return Arrays.deepToString(PFZ(r).toArray());
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

}