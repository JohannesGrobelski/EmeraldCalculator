package com.example.calcitecalculator.model;

import com.example.calcitecalculator.helper.MathEvaluator;
import com.example.calcitecalculator.helper.StringUtils;

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
        public static String[] modes = new String[]{"BASIC","ADVANCED","TRIGO","STATISTIC","LOGIC","MEMORY"};

        public static String[] modeBasicText                 = {"π","SIN","COS","TAN","LOG","LN","e","√","x²","^","!",">A/B"};
        public static String[] modeBasicFunctionality        = {"π","SIN","COS","TAN","LOG","LN","e","√","²","^","!",">A/B"};

        public static String[] modeAdvancedText = {"PFZ","GCD","LCM","∑","∏","LB",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX"};
        public static String[] modeAdvancedFunctionality = {">PFZ","GCD(,)","LCM(,)","∑(,)","∏(,)","LB",">%",">A/B",">x\u207B\u00B9",">+/-","MIN(,)","MAX(,)"};

        public static String[] modeTrigoText = {"SIN","COS","TAN","COT","SEC","CSC","SINH","COSH","TANH","COTH","SECH","CSCH"};
        public static String[] modeTrigoFunctionalityInverse = {"ASIN","ACOS","ATAN","ACOT","ASEC","ACSC","ASINH","ACOSH","ATANH","ACOTH","ASECH","ACSCH"};
        public static String[] modeTrigoFunctionality = {"SIN","COS","TAN","COT","SEC","CSC","SINH","COSH","TANH","COTH","SECH","CSCH"};

        public static String[] modeLogicText = {"AND","OR","XOR","NOT","","","","","","","",""};
        public static String[] modeLogicFunctionality = {"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};

        public static String[] modeStatisticText = {"RAND","RANDB","nCr","nPr","MEAN","VAR","S","","","","",""};
        public static String[] modeStatisticFunctionality = {"RAND()","RANDB(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};

        public static String[] modeMemoryText = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        public static String[] modeMemoryFunctionality = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};

        public static String[] modeUnknown = {"","","","","","","","","","","",""};
        public static String[][] modesModesText = {modeBasicText, modeAdvancedText, modeTrigoText, modeStatisticText, modeLogicText, modeMemoryText,modeUnknown};
        public static String[][] modesModesFunctionality = {modeBasicFunctionality, modeAdvancedFunctionality, modeTrigoFunctionality, modeStatisticFunctionality, modeLogicFunctionality, modeMemoryFunctionality,modeUnknown};

        public static HashMap<String,String> inverseFunction=  new HashMap<String, String>() {{
            put("SIN", "ASIN"); put("COS", "ACOS"); put("TAN", "ATAN"); put("COT", "ACOT"); put("SEC", "ASEC"); put("CSC", "ACSC");
            put("SINH", "ASINH"); put("COSH", "ACOSH"); put("TANH", "ATANH"); put("COTH", "ACOTH"); put("SECH", "ASECH"); put("CSCH", "ACSCH");
            put("²","√"); put("√","²");put("²","√"); put("³","3√"); put("LN","e^"); put("LOG","10^"); put("AND","NAND"); put("OR","NOR");
        }};
        public static void setModes(String[] Modes){modes = Modes;}
        public static void translatePrimeFactorization(String translation){if(modeAdvancedText[0].equals("PFZ"))modeAdvancedText[0] = translation;}
        public static void translateGreatestCommonDenominator(String translation){if(modeAdvancedText[1].equals("GCD")){modeAdvancedText[1] = translation;modeAdvancedFunctionality[1] = translation+"(,)";}}
        public static void translateLeastCommonMultiply(String translation){if(modeAdvancedText[2].equals("LCM")){modeAdvancedText[2] = translation;modeAdvancedFunctionality[2] = translation+"(,)";}}
        public static void translateRandomNumber(String translation){if(modeStatisticText[0].equals("RAND")){
            modeStatisticText[0] = translation;modeStatisticText[1] = translation;
            modeStatisticFunctionality[0] = translation+"()";modeStatisticFunctionality[1] = translation+"(,)";
        }}

    //state variables (incl. getter and setters)
        String InputString = "";
        int startSelection, endSelection;
        String OutputString = "";

        private String[] memory = new String[6];

        public String[] getMemory() {return memory;}
        public String getMemory(int index) {if(index<6 && index>=0)return memory[index];else return "";}
        public void setMemory(String[] memory) {if(this.memory.length == memory.length){this.memory = memory;}}
        public void setMemory(String mem, int index) {if(index<6 && index>=0){memory[index] = mem;}}

        public void setOuputString(String res) {OutputString = res;}
        public String getOutputString() {return OutputString;}
        public void setOutputString(String OutputString) {this.OutputString = OutputString;}
        public void setInputText(String text) {InputString=(text);}
        public String getInputText() {return StringUtils.getDisplayableString(InputString);}

    //auxiliary variables
        String current_Callback = "";

    //setting variables
        private String last_answer="";
        private int mode = 0;
        private String language = "";
        private boolean scientificNotation = false;
        public String mean_mode = "AriMit";
        public String var_mode = "AriVar";

        public int dec_places = precisionDigits;
        public int predec_places = 25;

        public int getMode() {return mode;}
        public void setMode(int mode) {this.mode = mode;}
        public String getLanguage() {return language;}
        public void setLanguage(String language) {this.language = language;}
        public boolean isScientificNotation() {return scientificNotation;}
        public void toogleScientificNotation(){scientificNotation = !scientificNotation;}
        public void nextMode() {mode = (mode+1)%modes.length;}
        public void previousMode() {int i = mode = (mode+-1)%modes.length; if(i==-1)mode = modes.length-1; else mode = i;}

    //methods
    public void addInputText(String i, int selectionStart) {
        if(selectionStart < 0)InputString = InputString+i;
        else InputString = StringUtils.insertString(InputString,i,selectionStart);
    }



    public void calculateResult(){
        int predec = predec_places;
        int dec = dec_places;
        String input = getCalcuableString(InputString);

        if(isScientificNotation()){
           predec = dec = 7;
        }
        String c = MathEvaluator.evaluate(input,predec,dec);
        setOuputString(c);
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

    /**
     *
     * @param input
     * @return
     */
    public String translateOperator(String input){
       input = input.replace("×","*")
                    .replace("÷","/");
       return input;
    }

    public String getCalcuableString(String a){
        //language settings
        a = a.replaceAll(modeAdvancedText[1],"GCD");
        a = a.replaceAll(modeAdvancedText[2],"LCM");

        a = a.replace("³√","3SQRT");
        for(String r: StringUtils.replacements.keySet()){
            a = a.replace(r, StringUtils.replacements.get(r));
        }

        //I: fix; sonst: PI -> P(I)
        a = StringUtils.paraInComplex(a);

        for(String f: StringUtils.functions_paraIn){
            a = a.replace(f.toLowerCase(),f);
        }
        a = StringUtils.parenthesise(a);
        System.out.println("test3");
        //after paraIn (because of AT(ANS)INH)
        Matcher matcherANS = Pattern.compile("ANS").matcher(a);

        for(int i=0; i<((a.length())*3); i++){
            if(matcherANS.find()){
                System.out.println(a);
                if(matcherANS.group().matches("[^A-Z]*ANS[^A-Z]*")){ //excludes inputs like "ATAN(ASINH(57.860802) = atANSinh57.860802"
                    a = a.replace("ANS",last_answer);
                }
                else {
                    //System.out.println(a);
                }
            } else break;
        }

        //settings
        a = a.replaceAll("MEAN",mean_mode);
        a = a.replaceAll("VAR",var_mode);

        a = translateOperator(a);

        return a;
    }

    /**
     * @param index
     * @return the button text of function buttons (btn_11 .. btn_26) according to functionality
     */
    public String getFunctionButtonText(int index){
        int i = (index%10 + ((index/10)-1)*6)-1;
        switch(mode){
            case 0: {return modeBasicText[i];}
            case 1: {return modeAdvancedText[i];}
            case 2: {return modeTrigoText[i];}
            case 3: {return modeStatisticText[i];}
            case 4: {return modeLogicText[i];}
            case 5: {return modeMemoryText[i];}
            default:{return "";}
        }
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
            case 0: {output =  modeBasicFunctionality[i]; break;}
            case 1: {output =  modeAdvancedFunctionality[i]; break;}
            case 2: {output =  modeTrigoFunctionality[i]; break;}
            case 3: {output =  modeStatisticFunctionality[i]; break;}
            case 4: {output =  modeLogicFunctionality[i]; break;}
            case 5: {output =  modeMemoryFunctionality[i]; break;}
            default: return "";
        }
        switch(output){
            case ((">PFZ")):{setOuputString(MathEvaluator.toPFZ(MathEvaluator.evaluate(InputString)));break;}
            case((">%")):{setOuputString(MathEvaluator.toPercent(MathEvaluator.evaluate(InputString)));break;}
            case((">A/B")):{setOuputString(MathEvaluator.toFraction(MathEvaluator.evaluate(InputString)));break;}
            case((">+/-")):{setOuputString(MathEvaluator.toInvert(MathEvaluator.evaluate(InputString)));break;}
            case((">x\u207B\u00B9")):{setOuputString(MathEvaluator.toReciproke(MathEvaluator.evaluate(InputString)));break;}
            case((">DEG")):{setOuputString(MathEvaluator.toDEG(MathEvaluator.evaluate(InputString)));break;}
            case((">RAD")):{setOuputString(MathEvaluator.toRAD(MathEvaluator.evaluate(InputString)));break;}
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
