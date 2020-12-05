package com.example.titancalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
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
        public static String[] modes = {"basic","advanced","trigo","logic","statistic","memory",};
        public static String[] theoreticalModes = {"basic","advanced","trigo","logic","statistic","memory","unknown"};

        public static String[] modeBasicText                 = {"π","SIN","COS","TAN","LOG","LN","e","√","x²","^","!",">A/B"};
        public static String[] modeBasicFunctionality        = {"π","SIN","COS","TAN","LOG","LN","e","√","²","^","!",">A/B"};

        public static String[] modeAdvancedText = {"PFZ","GCD","LCM","∑","∏","LB",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX"};
        public static String[] modeAdvancedFunctionality = {">PFZ","GCD(,)","LCM(,)","∑(,)","∏(,)","LB",">%",">A/B",">x\u207B\u00B9",">+/-","MIN(,)","MAX(,)"};

        public static String[] modeTrigoText = {"SIN","COS","TAN","COT","SEC","CSC","SINH","COSH","TANH","COTH","SECH","CSCH"};
        public static String[] modeTrigoFunctionality = {"SIN","COS","TAN","COT","SEC","CSC","SINH","COSH","TANH","COTH","SECH","CSCH"};

        public static String[] modeLogicText = {"AND","OR","XOR","NOT","","","","","","","",""};
        public static String[] modeLogicFunctionality = {"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};

        public static String[] modeStatisticText = {"ZN","ZB","NCR","NPR","MEAN","VAR","S","","","","",""};
        public static String[] modeStatisticFunctionality = {"Zn()","Zb(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};

        public static String[] modeMemoryText = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        public static String[] modeMemoryFunctionality = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};

        public static String[] modeUnknown = {"","","","","","","","","","","",""};
        public static String[][] modesModesText = {modeBasicText, modeAdvancedText, modeTrigoText, modeLogicText, modeStatisticText, modeMemoryText,modeUnknown};
        public static String[][] modesModesFunctionality = {modeBasicFunctionality, modeAdvancedFunctionality, modeTrigoFunctionality, modeLogicFunctionality, modeStatisticFunctionality, modeMemoryFunctionality,modeUnknown};

        public static HashMap<String,String> inverseFunction=  new HashMap<String, String>() {{
            put("SIN", "ASIN"); put("COS", "ACOS"); put("TAN", "ATAN"); put("COT", "ACOT"); put("SEC", "ASEC"); put("CSC", "ACSC");
            put("SINH", "ASINH"); put("COSH", "ACOSH"); put("TANH", "ATANH"); put("COTH", "ACOTH"); put("SECH", "ASECH"); put("CSCH", "ACSCH");
            put("²","√"); put("√","²");put("²","√"); put("³","3√"); put("LN","e^"); put("LOG","10^"); put("AND","NAND"); put("OR","NOR");
        }};
    //state variables (incl. getter and setters)
        PersistentModel persistentModel = new PersistentModel();
        String InputString = "";
        int startSelection, endSelection;
        String OutputString = "";

        public String[] getMemory() {return persistentModel.getMemory();}
        public String getMemory(int index) {return persistentModel.getMemory(index);}
        public void setMemory(String[] memory) {persistentModel.setMemory(memory);}
        public void setMemory(String mem, int index) {persistentModel.setMemory(mem,index);}


        public void setOuputString(String res) {OutputString = res;}
        public String getOutputString() {return OutputString;}
        public void setOutputString(String OutputString) {this.OutputString = OutputString;}
        public void setInputText(String text) {InputString=(text);}
        public String getInputText() {return StringUtils.getDisplayableString(InputString);}

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
        public void nextMode() {mode = modes[(findMode(mode)+1)%modes.length];}
        public void previousMode() {int i = (findMode(mode)-1)%modes.length; if(i==-1)mode = modes[modes.length-1]; else mode = modes[i];}
        private int findMode(String mode){for(int i=0;i<modes.length;i++){if(modes[i].equals(mode))return i;}return 0;}

    //methods
    public void addInputText(String i, int selectionStart) {
        if(selectionStart < 0)InputString = InputString+i;
        else InputString = StringUtils.insertString(InputString,i,selectionStart);
    }

    public String getResult(){
        if(isScientificNotation())return getScientificResult();
        else return calculateResult();
    }

    private String calculateResult(){
        String input = getCalcuableString(InputString);
        String c = MathEvaluator.evaluate(input,predec_places,dec_places);
        if(!c.equals("Math Error"))last_answer = c;
        return c;
    }

    public String getScientificResult(){
        String input = StringUtils.getDisplayableString(InputString);
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

    public String getCalcuableString(String a){
        //language settings
        a = a.replaceAll("LCM","KGV");
        a = a.replaceAll("GCD","GGT");

        a = a.replace("³√","3ROOT");
        for(String r: StringUtils.replacements.keySet()){
            a = a.replace(r, StringUtils.replacements.get(r));
        }

        //I: fix; sonst: PI -> P(I)
        a = StringUtils.paraInComplex(a);

        for(String f: StringUtils.functions_paraIn){
            a = a.replace(f.toLowerCase(),f);
        }

        a = StringUtils.parenthesise(a);

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
        int i = (index%10 + ((index/10)-1)*6)-1;
        switch(mode){
            case "basic": {return modeBasicText[i];}
            case "advanced": {return modeAdvancedText[i];}
            case "trigo": {return modeTrigoText[i];}
            case "statistic": {return modeStatisticText[i];}
            case "logic": {return modeLogicText[i];}
            case "memory": {return modeMemoryText[i];}
        }
        return "";
    }


        /**
         * returns output text of buttons btn_11 .. btn_26 dependend of the mode
         * @param index
         * @return
         */
    public String getFunctionButtonFunctionality(int index){
        String output = "";
        int i = (index%10 + ((index/10)-1)*6)-1;
        switch(mode){
            case "basic": {output =  modeBasicFunctionality[i]; break;}
            case "advanced": {output =  modeAdvancedFunctionality[i]; break;}
            case "trigo": {output =  modeTrigoFunctionality[i]; break;}
            case "statistic": {output =  modeStatisticFunctionality[i]; break;}
            case "logic": {output =  modeLogicFunctionality[i]; break;}
            case "memory": {output =  modeMemoryFunctionality[i]; break;}
        }
        switch(output){
            case ((">PFZ")):{setOuputString(MathEvaluator.toPFZ(InputString));break;}
            case((">%")):{setOuputString(MathEvaluator.toPercent(InputString));break;}
            case((">A/B")):{setOuputString(MathEvaluator.toFraction(InputString));break;}
            case((">+/-")):{setOuputString(MathEvaluator.toInvert(InputString));break;}
            case((">x\u207B\u00B9")):{setOuputString(MathEvaluator.toReciproke(InputString));break;}
            case((">DEG")):{setOuputString(MathEvaluator.toDEG(InputString));break;}
            case((">RAD")):{setOuputString(MathEvaluator.toRAD(InputString));break;}
        }
        return output;
    }

    /**
     * returns output text of buttons btn_11 .. btn_26 dependend of the mode
     * @param index
     * @return
     */
    public String getInverseFunctionButtonFunctionality(int index){
        String function = getFunctionButtonFunctionality(index);
        if(inverseFunction.containsKey(function))return inverseFunction.get(function);
        else return "";
    }



}
