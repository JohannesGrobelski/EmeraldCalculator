package com.example.titancalculator;

import android.content.Context;
import android.util.Log;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NavigatableNumberString;
import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** The model is the only gateway to the domain layer or business logic.
  */
public class CalcModel {
    //debug settings
        public boolean enableLog = true;
    //static data
        public static int precisionDigits = 10;
        public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));

        public static String[] modes = {"basic","basic2","trigo","hyper","logic","statistic","memory","unknown"};
        public static String[] modeBasicFunctionality = {"π","e","^","LOG","LN","LB","³√","√","³","²","10^","!"};
        public static String[] modeBasic2Functionality = {">PFZ","GCD(,)","LCM(,)","∑(,)","∏(,)","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN(,)","MAX(,)"};
        public static String[] modeTrigoFunctionality = {"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
        public static String[] modeHyperFunctionality = {"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
        public static String[] modeLogicFunctionality = {"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};
        public static String[] modeStatisticFunctionality = {"Zn()","Zb(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};
        public static String[] modeMemoryFunctionality = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        public static String[] modeUnknown = {"","","","","","","","","","","",""};
        public static String[][] modesModesFunctionality = {modeBasicFunctionality, modeBasic2Functionality, modeTrigoFunctionality, modeHyperFunctionality, modeLogicFunctionality, modeStatisticFunctionality, modeMemoryFunctionality,modeUnknown};
        public static String[] modeBasicText = {"π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!"};
        public static String[] modeBasic2Text = {"PFZ","GCD","LCM","∑","∏","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX"};
        public static String[] modeTrigoText = {"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
        public static String[] modeHyperText = {"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
        public static String[] modeLogicText = {"AND","OR","XOR","NOT","","","","","","","",""};
        public static String[] modeStatisticText = {"ZN","ZB","NCR","NPR","MEAN","VAR","S","","","","",""};
        public static String[] modeMemoryText = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        public static String[][] modesModesText = {modeBasicText, modeBasic2Text, modeTrigoText, modeHyperText, modeLogicText, modeStatisticText, modeMemoryText,modeUnknown};

    //state variables (incl. getter and setters)
        NavigatableNumberString InputString = new NavigatableNumberString();
        int startSelection, endSelection;
        String OutputString = "";
        private static String[] memory = new String[6];

        public static String[] getMemory() {return memory;}
        public static String getMemory(int index) {if(index<6 && index>=0)return memory[index]; else {System.out.println(index); assert(false); return "";}}
        public static void setMemory(String[] memory) {if(memory.length == CalcModel.memory.length)CalcModel.memory = memory;}
        public static void setMemory(String mem, int index) {if(index<6 && index>=0)CalcModel.memory[index] = mem;}
        public void setOuputString(String res) {OutputString = res;}
        public String getOutputString() {return OutputString;}
        public void setOutputString(String OutputString) {this.OutputString = OutputString;}
        public void setInputText(String text) {InputString.setText(text);}
        public String getInputText() {return InputString.getDisplayableString();}

    //auxiliary variables
        String current_Callback = "";

    //setting variables
        private String last_answer="";
        private String mode = "basic";
        private String language = "";
        private boolean scientificNotation = false;
        public String mean_mode = "AriMit";
        public String var_mode = "AriVar";

        public int dec_places = precisionDigits;
        public int predec_places = 25;

        public String getMode() {return mode;}
        public void setMode(String mode) {this.mode = mode;}
        public String getLanguage() {return language;}
        public void setLanguage(String language) {this.language = language;}
        public boolean isScientificNotation() {return scientificNotation;}
        public void toogleScientificNotation(){scientificNotation = !scientificNotation;}


    //methods
    public void addInputText(String i, int selectionStart) {
        if(selectionStart < 0)InputString.concatenateText(i);
        else InputString.addText(i,selectionStart);
    }

    public boolean isConsistent(){
        //TODO: implement
        return true;
    }

    public String getResult(){
        if(isScientificNotation())return getScientificResult();
        else return calculateResult();
    }

    private String calculateResult(){
        String input = getCalcuableString(InputString.getContent());
        String c = MathEvaluator.evaluate(input,predec_places,dec_places);
        if(!c.equals("Math Error"))last_answer = c;
        return c;
    }

    public String getScientificResult(){
        String input = InputString.getDisplayableString();
        String c = MathEvaluator.evaluate(input,7,7);
        if(!c.equals("Math Error"))last_answer = c;
        return c;
    }

    public void setMeanMode(String mode){
        if(mode.equals("AriMit") || mode.equals("GeoMit") || mode.equals("HarMit") ){
            mean_mode = mode;
        }
    }
    public String getMeanMode(){return mean_mode;}

    public void setVarMode(String mode){
        if(mode.equals("AriVar") || mode.equals("GeoVar") || mode.equals("HarVar") ){
            var_mode = mode;
        }
    }
    public String getVarMode(){return var_mode;}

    public String getDisplayableString(String a){
        return NavigatableNumberString.getDisplayableString(a);
    }

    public String getCalcuableString(String a){
        //language settings
        a = a.replaceAll("LCM","KGV");
        a = a.replaceAll("GCD","GGT");

        a = a.replace("³√","3ROOT");
        for(String r: NumberString.replacements.keySet()){
            a = a.replace(r,NumberString.replacements.get(r));
        }

        //I: fix; sonst: PI -> P(I)
        a = NumberString.paraInComplex(a);

        for(String f: NumberString.functions_paraIn){
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

    /**
     * @param index
     * @return the button text of function buttons (btn_11 .. btn_26) according to functionality
     */
    public String getFunctionButtonText(int index){
        switch(mode){
            case "basic": {
                switch(index){
                    case 11: {return "π";} case 12: {return "e";} case 13: {return "^";} case 14: {return "LOG";} case 15: {return "LN";} case 16: {return "LB";}
                    case 21: {return"³√";} case 22: {return"√";} case 23: {return"x³";} case 24: {return"x²";} case 25: {return"10^x";} case 26: {return"!";}
                }
            }
            case "basic2": {
                switch(index){
                    case 11: {return"PFZ";}
                    case 12: {if (getLanguage().equals("german") || getLanguage().equals("deutsch")){return"GGT";} else {return"GCD";}}
                    case 13: {if (getLanguage().equals("german") || getLanguage().equals("deutsch")){return"KGV";} else {return"LCM";}}
                    case 14: {return"∑";} case 15: {return"∏";} case 16: {return"";}
                    case 21: {return">%";} case 22: {return">A/B";} case 23: {return">x\u207B\u00B9";} case 24: {return">+/-";} case 25: {return"MIN";} case 26: {return"MAX";}
                }
            }
            case "trigo": {
                switch(index){
                    case 11: {return"SIN";} case 12: {return"COS";} case 13: {return"TAN";} case 14: {return"COT";} case 15: {return"SEC";} case 16: {return"CSC";}
                    case 21: {return"ASIN";} case 22: {return"ACOS";} case 23: {return"ATAN";} case 24: {return"ACOT";} case 25: {return"ASEC";} case 26: {return"ACSC";}
                }
           }
            case "hyper": {
                switch(index){
                    case 11: {return"SINH";} case 12: {return"COSH";} case 13: {return"TANH";} case 14: {return"ASINH";} case 15: {return"ACOSH";} case 16: {return"ATANH";}
                    case 21: {return">DEG";} case 22: {return">RAD";} case 23: {return"";} case 24: {return"";}  case 25: {return"";}  case 26: {return"";}
                }
            }
            case "statistic": {
                switch(index){
                    case 11: {return"ZN";} case 12: {return"ZB";} case 13: {return"NCR";} case 14: {return"NPR";} case 15: {return"MEAN";} case 16: {return"VAR";}
                    case 21: {return"S";} case 22: {return"";} case 23: {return"";} case 24: {return"";} case 25: {return"";} case 26: {return"";}
                }
            }
            case "logic": {
                switch(index){
                    case 11: {return"AND";} case 12: {return"OR";} case 13: {return"XOR";} case 14: {return"NOT";} case 15: {return"";} case 16: {return"";}
                    case 21: {return"";} case 22: {return"";} case 23: {return"";} case 24: {return"";} case 25: {return"";} case 26: {return"";}
                }
            }
            case "memory": {
                switch (index) {
                    case 11: {return"M1";} case 12: {return"M2";} case 13: {return"M3";} case 14: {return"M4";} case 15: {return"M5";} case 16: {return"M6";}
                    case 21: {return">M1";} case 22: {return">M2";} case 23: {return">M3";} case 24: {return">M4";} case 25: {return">M5";} case 26: {return">M6";}
                }
            }
        }
        return "";
    }


        /**
         * returns output text of buttons btn_11 .. btn_26 dependend of the mode
         * @param index
         * @return
         */
    public String getFunctionButtonFunctionality(int index){
                switch(index) {
                    case 11: {
                        switch(mode){
                            case "basic": {return("π"); }
                            case "basic2": {
                                setOuputString(MathEvaluator.toPFZ(InputString.getContent())); return ">PFZ";}
                            case "trigo":  {return("SIN"); }
                            case "statistic":  {return("Zn()"); }
                            case "hyper":  {return("SINH"); }
                            case "logic":  {return("AND(,)"); }
                            case "memory":  {return "M1";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 12: {
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
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 13: {
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
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 14: {
                        switch(mode){
                            case "basic": {return("LOG"); }
                            case "basic2": {return("∑(,)"); }
                            case "trigo":  {return("COT"); }
                            case "statistic":  {return("P"); }
                            case "hyper":  {return("ASINH"); }
                            case "logic":  {return("NOT()"); }
                            case "memory":  {return "M4";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 15: {
                        switch(mode){
                            case "basic": {return("LN"); }
                            case "basic2": {return("∏(,)"); }
                            case "trigo":  {return("SEC"); }
                            case "statistic":  {return("MEAN()"); }
                            case "hyper":  {return("ACOSH"); }
                            case "logic":  {return "";} //setOuputString(InputString.getBIN()); return ">BIN";}
                            case "memory":  {return "M5";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 16: {
                        switch(mode){
                            case "basic": {return("LB");}
                            case "basic2": {return "";}
                            case "trigo":  {return("CSC"); }
                            case "statistic":  {return("VAR()"); }
                            case "hyper":  {return("ATANH"); }
                            case "logic":  {return "";} //setOuputString(InputString.getOCT()); return ">OCT";}
                            case "memory":  {return "M6";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 21: {
                        switch(mode){
                            case "basic": {return("³√"); }
                            case "basic2": {
                                setOuputString(MathEvaluator.toPercent(InputString.getContent()));
                                return ">%";}
                            case "trigo":  {return("ASIN"); }
                            case "statistic":  {return("√(VAR())"); }
                            case "hyper":  {
                                setOuputString(MathEvaluator.toDEG(InputString.getContent())); return ">DEG";}
                            case "logic":  {return "";} //setOuputString(InputString.getDEC()); return ">DEC";}
                            case "memory":  {return ">M1";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 22: {
                        switch(mode){
                            case "basic": {return("√"); }
                            case "basic2": {
                                setOuputString(MathEvaluator.toFraction(InputString.getContent())); return ">A/B";}
                            case "trigo":  {return("ACOS"); }
                            case "statistic":  {return(""); }
                            case "hyper":  {
                                setOuputString(MathEvaluator.toRAD(InputString.getContent())); return ">RAD";}
                            case "logic":  {return "";} //setOuputString(InputString.getHEX()); return ">HEX";}
                            case "memory":  {return ">M2";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 23: {
                        switch(mode){
                            case "basic": {return("³"); }
                            case "basic2": {
                                setOuputString(MathEvaluator.toReciproke(InputString.getContent())); return ">x\u207B\u00B9";}
                            case "trigo":  {return("ATAN");}
                            case "statistic":  {return "";}
                            case "hyper":  {return("");}
                            case "logic":  {return "";}
                            case "memory":  {return ">M3";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 24: {
                        switch(mode){
                            case "basic": {return("²"); }
                            case "basic2": {
                                setOuputString(MathEvaluator.toInvert(InputString.getContent())); return ">+/-";}
                            case "trigo":  {return("ACOT");}
                            case "statistic":  {return "";}
                            case "hyper":  {return(""); }
                            case "logic":  {return "";}
                            case "memory":  {return ">M4";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 25: {
                        switch(mode){
                            case "basic": {return("10^"); }
                            case "basic2": {return("MIN(,)"); }
                            case "trigo":  {return("ASEC"); }
                            case "statistic":  {return "";}
                            case "hyper":  {return "";}
                            case "logic":  {return "";}
                            case "memory":  {return ">M5";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    case 26: {
                        switch(mode){
                            case "basic": {return("!"); }
                            case "basic2": {return("MAX(,)"); }
                            case "trigo":  {return "ACSC";}
                            case "statistic":  {return "";}
                            case "hyper":  {return "";}
                            case "logic":  {return "";}
                            case "memory":  {return ">M6";}
                            default:  {if(enableLog)Log.e("CalcMode","unknown Mode: "+mode); return "";}
                        }
                    }
                    default:  {if(enableLog)Log.e("CalcMode","unknown Button: "+index); return "";}
                }
        }

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
            setOuputString(InputString.getPercent());
            return ">%";
        } else if (fct.equals("A/B")) {
            setOuputString(InputString.toFraction());
            return "A/B";
        } else if (fct.equals("x\u207B\u00B9")) {
            setOuputString(InputString.getReciproke());
            return "x\u207B\u00B9";
        } else if (fct.equals("+/-")) {
            setOuputString(InputString.getInvert());
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
