package com.example.titancalculator;

import android.util.Log;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.titancalculator.helper.Math_String.StringUtils.insertString;

/** The model is the only gateway to the domain layer or business logic.
  */
public class CalcModel {
    //debug settings
        public boolean enableLog = true;
    //static data
        public static int precisionDigits = 10;
        public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));
        public static String[] modes = {"basic","advanced","trigo","hyper","logic","statistic","memory","unknown"};

        public static String[] modeBasicText                 = {"π","SIN","COS","TAN","LOG","LN","e","√","x²","^","!",">A/B"};
        public static String[] modeBasicFunctionality        = {"π","SIN","COS","TAN","LOG","LN","e","√","²","^","!",">A/B"};

        public static String[] modeBasic2Text = {"PFZ","GCD","LCM","∑","∏","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX"};
        public static String[] modeBasic2Functionality = {">PFZ","GCD(,)","LCM(,)","∑(,)","∏(,)","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN(,)","MAX(,)"};

        public static String[] modeTrigoText = {"SIN","COS","TAN","COT","SEC","CSC","SINH","COSH","TANH","COTH","SECH","CSCH"};
        public static String[] modeTrigoFunctionality = {"SIN","COS","TAN","COT","SEC","CSC","SINH","COSH","TANH","COTH","SECH","CSCH"};

        public static String[] modeLogicText = {"AND","OR","XOR","NOT","","","","","","","",""};
        public static String[] modeLogicFunctionality = {"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};

        public static String[] modeStatisticText = {"ZN","ZB","NCR","NPR","MEAN","VAR","S","","","","",""};
        public static String[] modeStatisticFunctionality = {"Zn()","Zb(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};

        public static String[] modeMemoryText = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        public static String[] modeMemoryFunctionality = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};

        public static String[] modeUnknown = {"","","","","","","","","","","",""};
        public static String[][] modesModesText = {modeBasicText, modeBasic2Text, modeTrigoText, modeLogicText, modeStatisticText, modeMemoryText,modeUnknown};
        public static String[][] modesModesFunctionality = {modeBasicFunctionality, modeBasic2Functionality, modeTrigoFunctionality, modeLogicFunctionality, modeStatisticFunctionality, modeMemoryFunctionality,modeUnknown};

        public static HashMap<String,String> inverseFunction=  new HashMap<String, String>() {{
            put("SIN", "ASIN"); put("COS", "ACOS"); put("TAN", "ATAN"); put("COT", "ACOT"); put("SEC", "ASEC"); put("CSC", "ACSC");
            put("SINH", "ASINH"); put("COSH", "ACOSH"); put("TANH", "ATANH"); put("COTH", "ACOTH"); put("SECH", "ASECH"); put("CSCH", "ACSCH");
            put("²","√"); put("√","²");put("²","√"); put("³","3√"); put("LN","e^"); put("LOG","10^"); put("AND","NAND"); put("OR","NOR");
        }};
    //state variables (incl. getter and setters)
        String InputString = "";
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
            case "advanced": {return modeBasic2Text[i];}
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
            case "advanced": {output =  modeBasic2Functionality[i]; break;}
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
            case "STANDART2": {return "advanced";}
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
