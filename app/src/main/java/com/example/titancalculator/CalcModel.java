package com.example.titancalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
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
        public static int precisionDigits = 10;
        public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));

    //state
        Context context;
        NavigatableString InputString = new NavigatableString("content");
        int startSelection, endSelection;
        String OutputString = "";
        private static String[] memory = new String[6];

        public static String[] getMemory() {return memory;}
        public static String getMemory(int index) {return memory[index];}
        public static void setMemory(String[] memory) {CalcModel.memory = memory;}
        public static void setMemory(String mem, int index) {CalcModel.memory[index] = mem;}
        public String getOutputString() {return OutputString;}
        public void setOutputString(String OutputString) {this.OutputString = OutputString;}
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
        private int base;
        private String mode = "basic";
        private String language = "";
        private boolean solve_inst_pref = false;
        private boolean scientificNotation = false;

        public String getMode() {return mode;}
        public void setMode(String mode) {this.mode = mode;}
        public String getLanguage() {return language;}
        public void setLanguage(String language) {this.language = language;}
        public boolean isSolve_inst_pref() {return solve_inst_pref;}
        public void setSolve_inst_pref(boolean solve_inst_pref) {this.solve_inst_pref = solve_inst_pref;}
        public boolean isScientificNotation() {return scientificNotation;}
        public void setScientificNotation(boolean scientificNotation) {this.scientificNotation = scientificNotation;}

    public CalcModel(Context c){
            this.context = c;
    }

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

    private static String returnmode(String mode){
        switch(mode){
            case "STANDART": {return "basic";}
            case "STANDART2": {return "basic2";}
            case "STATISTIK": {return "statistic";}
            case "NUTZER": {return "user";}
            case "LOGISCH": {return "logic";}
            case "SPEICHER": {return "memory";}
            default: return mode;
        }
    }

    public void ausgabeSetText(String res) {
        OutputString = res;
    }

    private String transUserInputBtnFct(String fct) {
        if (fct.startsWith("btn")) return "";
        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if (fct.equals(">%")) {
            ausgabeSetText(InputString.getPercent());
            return ">%";
        } else if (fct.equals("A/B")) {
            ausgabeSetText(InputString.getBruch());
            return "A/B";
        } else if (fct.equals("x\u207B\u00B9")) {
            ausgabeSetText(InputString.getReciproke());
            return "x\u207B\u00B9";
        } else if (fct.equals("+/-")) {
            ausgabeSetText(InputString.getInvert());
            return "+/-";
        }
        String A = fct;
        A = A.replace("NCR", "C");
        A = A.replace("NCR", "C");
        A = A.replace("!N", "!");
        A = A.replace("x\u207B\u00B9", "C");
        return A;
    }

    public String translInputBtn11(){
        switch(mode){
            case "basic": {return("π"); }
            case "basic2": {ausgabeSetText(InputString.getPFZ()); return ">PFZ";}
            case "trigo":  {return("SIN"); }
            case "user":  {transUserInputBtnFct("btn_11"); }
            case "statistic":  {return("Zn()"); }
            case "hyper":  {return("SINH"); }
            case "logic":  {return("AND(,)"); }
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }
    
    public String translInputBtn12(){
        switch(mode){
            case "basic": {return("e"); }
            case "basic2": {
                if (language.equals("german") || language.equals("deutsch")) {
                    return("GGT(,)");
                } else {
                    return("GCD(,)");
                }   }
            case "trigo":  {return("COS"); }
            case "user":  {transUserInputBtnFct("btn_12"); }
            case "statistic":  {return("Zb(,)"); }
            case "hyper":  {return("COSH"); }
            case "logic":  {return("OR(,)"); }
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn13(){
        switch(mode){
            case "basic": {return("^");}
            case "basic2": {if (language.equals("german") || language.equals("deutsch")) {
                return("KGV(,)");
            } else {
                return("LCM(,)");
            } }
            case "trigo":  {return("TAN"); }
            case "user":  {transUserInputBtnFct("btn_13"); }
            case "statistic":  {return("C"); }
            case "hyper":  {return("TANH"); }
            case "logic":  {return("XOR(,)"); }
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn14(){
        switch(mode){
            case "basic": {return("LOG"); }
            case "basic2": {return("∑" + "(,)"); }
            case "trigo":  {return("COT"); }
            case "user":  {transUserInputBtnFct("btn_14"); }
            case "statistic":  {return("P"); }
            case "hyper":  {return("ASINH"); }
            case "logic":  {return("NOT()"); }
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn15(){
        switch(mode){
            case "basic": {return("LN"); }
            case "basic2": {return("∏"+ "(,)"); }
            case "trigo":  {return("ASIN"); }
            case "user":  {transUserInputBtnFct("btn_15"); }
            case "statistic":  {return("MEAN()"); }
            case "hyper":  {return("ACOSH"); }
            case "logic":  {ausgabeSetText(InputString.getBIN()); return ">BIN";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn16(){
        switch(mode){
            case "basic": {return("LB");}
            case "basic2": {return "";}
            case "trigo":  {return("ACOS"); }
            case "user":  {transUserInputBtnFct("btn_16"); }
            case "statistic":  {return("VAR()"); }
            case "hyper":  {return("ATANH"); }
            case "logic":  {ausgabeSetText(InputString.getOCT()); return ">OCT";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn21(){
        switch(mode){
            case "basic": {return("³√"); }
            case "basic2": {ausgabeSetText(InputString.getPercent());
                OutputString = InputString.getPercent();
                ausgabeSetText(OutputString); return ">%";}
            case "trigo":  {return("ATAN"); }
            case "user":  {transUserInputBtnFct("btn_21"); }
            case "statistic":  {return("E()"); }
            case "hyper":  {return "";}
            case "logic":  {ausgabeSetText(InputString.getDEC()); return ">DEC";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn22(){
        switch(mode){
            case "basic": {return("√"); }
            case "basic2": {ausgabeSetText(InputString.getBruch()); return ">A/B";}
            case "trigo":  {return("ACOT"); }
            case "user":  {transUserInputBtnFct("btn_22"); }
            case "statistic":  {return("2√(VAR())"); }
            case "hyper":  {return "";}
            case "logic":  {ausgabeSetText(InputString.getHEX()); return ">HEX";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn23(){
        switch(mode){
            case "basic": {return("³"); }
            case "basic2": {ausgabeSetText(InputString.getReciproke()); return ">x\u207B\u00B9";}
            case "trigo":  {ausgabeSetText(InputString.getDEG()); return ">DEG";}
            case "user":  {transUserInputBtnFct("btn_23"); }
            case "statistic":  {return "";}
            case "hyper":  {return "";}
            case "logic":  {return "";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn24(){
        switch(mode){
            case "basic": {return("²"); }
            case "basic2": {ausgabeSetText(InputString.getInvert()); return ">+/-";}
            case "trigo":  {ausgabeSetText(InputString.getRAD()); return ">RAD";}
            case "user":  {transUserInputBtnFct("btn_24"); return "";}
            case "statistic":  {return "";}
            case "hyper":  {return "";}
            case "logic":  {return "";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn25(){
        switch(mode){
            case "basic": {return("10^"); }
            case "basic2": {return("MIN(,)"); }
            case "trigo":  {return("toPolar(,)"); }
            case "user":  {transUserInputBtnFct("btn_25"); }
            case "statistic":  {return "";}
            case "hyper":  {return "";}
            case "logic":  {return "";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public String translInputBtn26(){
        switch(mode){
            case "basic": {return("!"); }
            case "basic2": {return("MAX(,)"); }
            case "trigo":  {return("toCart(,)"); }
            case "user":  {transUserInputBtnFct("btn_26"); return "";}
            case "statistic":  {return "";}
            case "hyper":  {return "";}
            case "logic":  {return "";}
            case "memory":  {return "";}
            default:  {Log.e("CalcMode","unknown Mode"); }
        }
        return "";
    }

    public void toogleScientificNotation(){scientificNotation = !scientificNotation;}


    //persistency

    public void saveMemory(String[] Memory) {
        String MEMS = ArrayUtils.arrayToString(Memory);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MEMORY", MEMS);
        editor.commit();
    }

    public String[] loadMemory() {
        String MEMS = PreferenceManager.getDefaultSharedPreferences(this.context).getString("MEMORY", "");
        String[] memarray = ArrayUtils.stringToArray(MEMS);
        Log.e("array mem", Arrays.toString(memarray));
        String[] res = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < memarray.length) res[i] = memarray[i];
            else res[i] = "";
        }
        return res;
    }

    private static boolean checkPermissionForReadExtertalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private static void requestPermissionForReadExtertalStorage(Context context) throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Integer.parseInt(Manifest.permission.READ_EXTERNAL_STORAGE));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
