package com.example.titancalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.ArrayUtils;
import com.example.titancalculator.helper.Math_String.NavigatableString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** The model is the only gateway to the domain layer or business logic.
  */
public class CalcModel {
    //static data
        public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));



    //state
        NavigatableString InputString = new NavigatableString("content");
        String OutputString = "";
        private static String[] MEMORY = new String[6];
        String answer = "";

        public static String[] getMEMORY() {return MEMORY;}
        public static String getMEMORY(int index) {return MEMORY[index];}
        public static void setMEMORY(String[] MEMORY) {CalcModel.MEMORY = MEMORY;}
        public static void setMEMORY(String mem, int index) {CalcModel.MEMORY[index] = mem;}
        public String getAnswer() {return answer;}
        public void setAnswer(String answer) {this.answer = answer;}
        public void setIText(String text) {InputString.setText(text);}
        public String getIText() {return InputString.getDisplayableString();}

        //methods of InputString
        public String inputStringGetDisplayableString(){return InputString.getDisplayableString();}
        public String inputStringGetResult(){return InputString.getResult();}
        public String inputStringNormalToScientific(){return InputString.normalToScientific();}
        public String inputStringScientificToNormal(){return InputString.scientificToNormal();}

    //auxiliary variables
        String current_Callback = "";

    //setting variables
        int base;
        String mode = "BASIC"; String englishMode = "BASIC";
        String language = "";
        boolean solve_inst_pref = false;
        boolean scientificNotation = false;

        public String getMode() {return mode;}
        public void setMode(String mode) {this.mode = mode;}
        public String getEnglishMode() {return englishMode;}
        public void setEnglishMode(String englishMode) {this.englishMode = englishMode;}
        public String getLanguage() {return language;}
        public void setLanguage(String language) {this.language = language;}
        public boolean isSolve_inst_pref() {return solve_inst_pref;}
        public void setSolve_inst_pref(boolean solve_inst_pref) {this.solve_inst_pref = solve_inst_pref;}
        public boolean isScientificNotation() {return scientificNotation;}
        public void setScientificNotation(boolean scientificNotation) {this.scientificNotation = scientificNotation;}


    //methods
    private void setBase(Context context, int Base) {
        if (Base <= 1) {
            return;
        } else {
            base = Base;
        }
    }

    private int getBase(Context context) {
        if (base == 0) {
            String baseString = PreferenceManager.getDefaultSharedPreferences(context).getString("base", "10");
            if (baseString == null) {
                setBase(context, 0);
            } else base = Integer.parseInt(baseString);
        }
        return base;
    }

    public void eingabeAddText(String i, int selectionStart) {
        if(selectionStart < 0)InputString.concatenateText(i);
        else InputString.addText(i,selectionStart);

        /*
        if (solve_inst_pref) {
            if (!noImmidiateOps.contains(InputString.trim())) {
                answer = InputString.getResult();
                if (!answer.equals("Math Error")) ausgabeSetText(answer);
            }
        }
         */
    }

    public void ausgabeSetText(String res) {
        OutputString = res;
    }

    private String transBtnFct(String fct) {
        if (fct.startsWith("btn")) return "";
        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if (fct.equals(">%")) {
            ausgabeSetText(InputString.getPercent());
            return "";
        } else if (fct.equals("A/B")) {
            ausgabeSetText(InputString.getBruch());
            return "";
        } else if (fct.equals("x\u207B\u00B9")) {
            ausgabeSetText(InputString.getReciproke());
            return "";
        } else if (fct.equals("+/-")) {
            ausgabeSetText(InputString.getInvert());
            return "";
        }
        String A = fct;
        A = A.replace("NCR", "C");
        A = A.replace("NCR", "C");
        A = A.replace("!N", "!");
        A = A.replace("x\u207B\u00B9", "C");
        return A;
    }

    public String inputBtn11(){
        switch(englishMode){
            case "BASIC": {return("π"); }
            case "BASIC2": {ausgabeSetText(InputString.getPFZ()); }
            case "TRIGO":  {return("SIN"); }
            case "USER":  {transBtnFct("btn_11"); }
            case "STATISTIC":  {return("Zn()"); }
            case "HYPER":  {return("SINH"); }
            case "LOGIC":  {return("AND(,)"); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }
    
    public String inputBtn12(){
        switch(englishMode){
            case "BASIC": {return("e"); }
            case "BASIC2": {
                if (language.equals("german") || language.equals("deutsch")) {
                    return("GGT(,)");
                } else {
                    return("GCD(,)");
                }   }
            case "TRIGO":  {return("COS"); }
            case "USER":  {transBtnFct("btn_12"); }
            case "STATISTIC":  {return("Zb(,)"); }
            case "HYPER":  {return("COSH"); }
            case "LOGIC":  {return("OR(,)"); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn13(){
        switch(englishMode){
            case "BASIC": {return("^");}
            case "BASIC2": {if (language.equals("german") || language.equals("deutsch")) {
                return("KGV(,)");
            } else {
                return("LCM(,)");
            } }
            case "TRIGO":  {return("TAN"); }
            case "USER":  {transBtnFct("btn_13"); }
            case "STATISTIC":  {return("C"); }
            case "HYPER":  {return("TANH"); }
            case "LOGIC":  {return("XOR(,)"); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn14(){
        switch(englishMode){
            case "BASIC": {return("LOG"); }
            case "BASIC2": {return("∑" + "(,)"); }
            case "TRIGO":  {return("COT"); }
            case "USER":  {transBtnFct("btn_14"); }
            case "STATISTIC":  {return("P"); }
            case "HYPER":  {return("ASINH"); }
            case "LOGIC":  {return("NOT()"); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn15(){
        switch(englishMode){
            case "BASIC": {return("LN"); }
            case "BASIC2": {return("∏"+ "(,)"); }
            case "TRIGO":  {return("ASIN"); }
            case "USER":  {transBtnFct("btn_15"); }
            case "STATISTIC":  {return("MEAN()"); }
            case "HYPER":  {return("ACOSH"); }
            case "LOGIC":  {ausgabeSetText(InputString.getBIN()); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn16(){
        switch(englishMode){
            case "BASIC": {return("LB");}
            case "BASIC2": {}
            case "TRIGO":  {return("ACOS"); }
            case "USER":  {transBtnFct("btn_16"); }
            case "STATISTIC":  {return("VAR()"); }
            case "HYPER":  {return("ATANH"); }
            case "LOGIC":  {ausgabeSetText(InputString.getOCT()); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn21(){
        switch(englishMode){
            case "BASIC": {return("³√"); }
            case "BASIC2": {ausgabeSetText(InputString.getPercent());
                answer = InputString.getPercent();
                ausgabeSetText(answer); }
            case "TRIGO":  {return("ATAN"); }
            case "USER":  {transBtnFct("btn_21"); }
            case "STATISTIC":  {return("E()"); }
            case "HYPER":  {}
            case "LOGIC":  {ausgabeSetText(InputString.getDEC()); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn22(){
        switch(englishMode){
            case "BASIC": {return("√"); }
            case "BASIC2": {ausgabeSetText(InputString.getBruch()); }
            case "TRIGO":  {return("ACOT"); }
            case "USER":  {transBtnFct("btn_22"); }
            case "STATISTIC":  {return("2√(VAR())"); }
            case "HYPER":  {}
            case "LOGIC":  {ausgabeSetText(InputString.getHEX()); }
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn23(){
        switch(englishMode){
            case "BASIC": {return("³"); }
            case "BASIC2": {ausgabeSetText(InputString.getReciproke()); }
            case "TRIGO":  {ausgabeSetText(InputString.getDEG()); }
            case "USER":  {transBtnFct("btn_23"); }
            case "STATISTIC":  { }
            case "HYPER":  {}
            case "LOGIC":  {}
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn24(){
        switch(englishMode){
            case "BASIC": {return("²"); }
            case "BASIC2": {ausgabeSetText(InputString.getInvert()); }
            case "TRIGO":  {ausgabeSetText(InputString.getRAD()); }
            case "USER":  {transBtnFct("btn_24"); }
            case "STATISTIC":  { }
            case "HYPER":  {}
            case "LOGIC":  {}
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn25(){
        switch(englishMode){
            case "BASIC": {return("10^"); }
            case "BASIC2": {return("MIN()"); }
            case "TRIGO":  {return("toPolar(,)"); }
            case "USER":  {transBtnFct("btn_25"); }
            case "STATISTIC":  { }
            case "HYPER":  {}
            case "LOGIC":  {}
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String inputBtn26(){
        switch(englishMode){
            case "BASIC": {return("!"); }
            case "BASIC2": {return("MAX()"); }
            case "TRIGO":  {return("toCart(,)"); }
            case "USER":  {transBtnFct("btn_26"); }
            case "STATISTIC":  { }
            case "HYPER":  {}
            case "LOGIC":  {}
            case "MEMORY":  {}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public void toogleScientificNotation(){scientificNotation = !scientificNotation;}


}
