package com.example.titancalculator;

import android.content.Context;
import android.util.Log;

import com.example.titancalculator.helper.Math_String.NavigatableString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** The model is the only gateway to the domain layer or business logic.
  */
public class CalcModel {
    public boolean enableLog = true;
    //static data
        public static int precisionDigits = 10;
        public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));

    //state variables (incl. getter and setters)
        Context context;
        NavigatableString InputString = new NavigatableString("content");
        int startSelection, endSelection;
        String OutputString = "";
        private static String[] memory = new String[6];

        public static String[] getMemory() {return memory;}
        public static String getMemory(int index) {if(index<6 && index>=0)return memory[index]; else {System.out.println(index); assert(false); return "";}}
        public static void setMemory(String[] memory) {if(memory.length == CalcModel.memory.length)CalcModel.memory = memory;}
        public static void setMemory(String mem, int index) {if(index<6 && index>=0)CalcModel.memory[index] = mem;}
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
        public boolean isScientificNotation() {return scientificNotation;}

    public CalcModel(Context c){
            this.context = c;
    }

    public String translInputBtn11(){
        switch(mode){
            case "basic": {return("π"); }
            case "basic2": {ausgabeSetText(InputString.getPFZ()); return ">PFZ";}
            case "trigo":  {return("SIN"); }
            case "statistic":  {return("Zn()"); }
            case "hyper":  {return("SINH"); }
            case "logic":  {return("AND(,)"); }
            case "memory":  {return "M1";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }
    
    public String translInputBtn12(){
        switch(mode){
            case "basic": {return("e"); }
            case "basic2": {
                if (language.equals("german") || language.equals("deutsch")) {
                    return("GGT(,)");
                }
                return("GCD(,)");
            }
            case "trigo":  {return("COS"); }
            case "statistic":  {return("Zb(,)"); }
            case "hyper":  {return("COSH"); }
            case "logic":  {return("OR(,)"); }
            case "memory":  {return "M2";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn13(){
        switch(mode){
            case "basic": {return("^");}
            case "basic2": {
                if (language.equals("german") || language.equals("deutsch")) {
                    return ("KGV(,)");
                }
                return("LCM(,)");
            }
            case "trigo":  {return("TAN"); }
            case "statistic":  {return("C"); }
            case "hyper":  {return("TANH"); }
            case "logic":  {return("XOR(,)"); }
            case "memory":  {return "M3";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn14(){
        switch(mode){
            case "basic": {return("LOG"); }
            case "basic2": {return("∑(,)"); }
            case "trigo":  {return("COT"); }
            case "statistic":  {return("P"); }
            case "hyper":  {return("ASINH"); }
            case "logic":  {return("NOT()"); }
            case "memory":  {return "M4";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn15(){
        switch(mode){
            case "basic": {return("LN"); }
            case "basic2": {return("∏(,)"); }
            case "trigo":  {return("SEC"); }
            case "statistic":  {return("MEAN()"); }
            case "hyper":  {return("ACOSH"); }
            case "logic":  {return "";} //ausgabeSetText(InputString.getBIN()); return ">BIN";}
            case "memory":  {return "M5";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn16(){
        switch(mode){
            case "basic": {return("LB");}
            case "basic2": {return "";}
            case "trigo":  {return("CSC"); }
            case "statistic":  {return("VAR()"); }
            case "hyper":  {return("ATANH"); }
            case "logic":  {return "";} //ausgabeSetText(InputString.getOCT()); return ">OCT";}
            case "memory":  {return "M6";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn21(){
        switch(mode){
            case "basic": {return("³√"); }
            case "basic2": {
                ausgabeSetText(InputString.getPercent());
                OutputString = InputString.getPercent();
                ausgabeSetText(OutputString); return ">%";}
            case "trigo":  {return("ASIN"); }
            case "statistic":  {return("√(VAR())"); }
            case "hyper":  {ausgabeSetText(InputString.getDEG()); return ">DEG";}
            case "logic":  {return "";} //ausgabeSetText(InputString.getDEC()); return ">DEC";}
            case "memory":  {return ">M1";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn22(){
        switch(mode){
            case "basic": {return("√"); }
            case "basic2": {ausgabeSetText(InputString.getBruch()); return ">A/B";}
            case "trigo":  {return("ACOS"); }
            case "statistic":  {return(""); }
            case "hyper":  {ausgabeSetText(InputString.getRAD()); return ">RAD";}
            case "logic":  {return "";} //ausgabeSetText(InputString.getHEX()); return ">HEX";}
            case "memory":  {return ">M2";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn23(){
        switch(mode){
            case "basic": {return("³"); }
            case "basic2": {ausgabeSetText(InputString.getReciproke()); return ">x\u207B\u00B9";}
            case "trigo":  {return("ATAN");}
            case "statistic":  {return "";}
            case "hyper":  {return("");}
            case "logic":  {return "";}
            case "memory":  {return ">M3";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn24(){
        switch(mode){
            case "basic": {return("²"); }
            case "basic2": {ausgabeSetText(InputString.getInvert()); return ">+/-";}
            case "trigo":  {return("ACOT");}
            case "statistic":  {return "";}
            case "hyper":  {return(""); }
            case "logic":  {return "";}
            case "memory":  {return ">M4";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn25(){
        switch(mode){
            case "basic": {return("10^"); }
            case "basic2": {return("MIN(,)"); }
            case "trigo":  {return("ASEC"); }
            case "statistic":  {return "";}
            case "hyper":  {return "";}
            case "logic":  {return "";}
            case "memory":  {return ">M5";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    public String translInputBtn26(){
        switch(mode){
            case "basic": {return("!"); }
            case "basic2": {return("MAX(,)"); }
            case "trigo":  {return "ACSC";}
            case "statistic":  {return "";}
            case "hyper":  {return "";}
            case "logic":  {return "";}
            case "memory":  {return ">M6";}
            default:  {if(enableLog)Log.e("CalcMode","unknown Mode"); return "";}
        }
    }

    //methods
    public void eingabeAddText(String i, int selectionStart) {
        if(selectionStart < 0)InputString.concatenateText(i);
        else InputString.addText(i,selectionStart);
    }

    public void ausgabeSetText(String res) {OutputString = res;}

    public void toogleScientificNotation(){scientificNotation = !scientificNotation;}


    /*
    //persistency

    public void saveMemory(String[] Memory) {
        String MEMS = ArrayUtils.arrayToString(Memory);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MEMORY", MEMS);
        editor.commit();
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

    public String[] loadMemory() {
        String MEMS = PreferenceManager.getDefaultSharedPreferences(this.context).getString("MEMORY", "");
        String[] memarray = ArrayUtils.stringToArray(MEMS);
        if(enableLog)Log.e("array mem", Arrays.toString(memarray));
        String[] res = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < memarray.length) res[i] = memarray[i];
            else res[i] = "";
        }
        return res;
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

    private void setBase(int Base) {
        if (Base <= 1) {
            return;
        } else {
            base = Base;
        }
    }

    private int getBase() {
        if (base == 0) {
            String baseString = PreferenceManager.getDefaultSharedPreferences(context).getString("base", "10");
            if (baseString == null) {
                setBase(0);
            } else base = Integer.parseInt(baseString);
        }
        return base;
    }

    private String transUserInputBtnFct(String fct) {
        if (fct.startsWith("btn")) return "";
        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if (fct.equals(">%")) {
            ausgabeSetText(InputString.getPercent());
            return ">%";
        } else if (fct.equals("A/B")) {
            ausgabeSetText(InputString.toFraction());
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
     */

}
